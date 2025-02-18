package com.yurani.mifare_mmtz

import android.app.Activity
import android.nfc.NfcAdapter
import android.nfc.tech.MifareClassic
import android.os.Build
import android.util.Log
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
// import io.flutter.plugin.common.PluginRegistry.Registrar
import java.util.*
import kotlin.jvm.internal.Intrinsics

private const val TAG = "MifareMmtzPlugin"

/** MifareMmtzPlugin */
class MifareMmtzPlugin: FlutterPlugin, MethodCallHandler, ActivityAware {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  private lateinit var activity: Activity
  private var mNfcAdapter: NfcAdapter? = null
  private lateinit var mifareClassic: MifareClassic
  private val flag = NfcAdapter.FLAG_READER_NFC_A

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "mifare_mmtz")
    channel.setMethodCallHandler(this)
    mNfcAdapter = NfcAdapter.getDefaultAdapter(flutterPluginBinding.applicationContext)
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    when (call.method) {
      "isNFCEnable" -> {
        isNFCEnabled(result)
      }
      "getId" -> {
        getId(result)
      }
      else -> {
        result.notImplemented()
      }
    }
  }

  private fun isNFCEnabled(result: Result) {
    val message: String = if (mNfcAdapter != null && mNfcAdapter!!.isEnabled) {
      "Enabled"
    } else if (mNfcAdapter != null && !mNfcAdapter!!.isEnabled) {
      "NotEnabled"
    } else {
      "NotSupported"
    }
    result.success(message)
  }

  private fun getId(result: Result) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      mNfcAdapter?.enableReaderMode(activity, { tag ->
        try {

          mifareClassic = MifareClassic.get(tag)
          mifareClassic.connect()
          val id = idToDecimalString(tag.id)
          activity.runOnUiThread {
            result.success(id)
          }

        } catch (e: Exception) {
          Log.e(TAG, "getID: ", e)
          activity.runOnUiThread {
            result.error("404",
            e.localizedMessage, null)
          }

        } finally {
          mifareClassic.close()
          mNfcAdapter?.disableReaderMode(activity)
        }
      }, flag, null)
    }
  }

  private fun idToDecimalString(bArr: ByteArray): String {
    val sb = StringBuilder()
    for (length in bArr.indices.reversed()) {
      val objArr = arrayOf<Any>(java.lang.Byte.valueOf(bArr[length]))
      val format = String.format("%02X", *objArr.copyOf(objArr.size))
      Intrinsics.checkExpressionValueIsNotNull(format, "java.lang.String.format(format, *args)")
      sb.append(format)
    }
    val sb2 = sb.toString()
    Intrinsics.checkExpressionValueIsNotNull(sb2, "sb.toString()")
    if (sb2 != null) {
      val lowerCase = sb2.toLowerCase()
      Intrinsics.checkExpressionValueIsNotNull(lowerCase, "(this as java.lang.String).toLowerCase()")
      return lowerCase.toLong(16).toString()
    }
    throw TypeCastException("null cannot be cast to non-null type java.lang.String")
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity
  }

  override fun onDetachedFromActivityForConfigChanges() {
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    activity = binding.activity
  }

  override fun onDetachedFromActivity() {
  }
}
