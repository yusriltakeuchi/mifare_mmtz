package com.yurani.mifare_mmtz

import android.app.Activity
import android.nfc.NfcAdapter
import android.nfc.tech.MifareClassic
import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

/** MifareMmtzPlugin */
class MifareMmtzPlugin: FlutterPlugin, MethodCallHandler {
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

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}
