
import 'dart:async';

import 'package:flutter/services.dart';

enum NFCStatus {
  Enabled,
  NotEnabled,
  NotSupported
}
class MifareMmtz {
  
  /// Current channel
  static const MethodChannel _channel = const MethodChannel('mifare_mmtz');

  /// Check current availability of NFC
  static Future<NFCStatus> isNFCEnable() async {
    final response = await _channel.invokeMethod("isNFCEnable") as String;
    return _mapEnableStats(response);
  }

  /// Get current mifare id
  static Future<String> getId() async {
    final response = await _channel.invokeMethod("getId") as String;
    return response;
  }

  static NFCStatus _mapEnableStats(String status) {
    switch (status) {
      case "Enabled":
        return NFCStatus.Enabled;
      case "NotEnabled":
        return NFCStatus.NotEnabled;
      case "NotSupported":
        return NFCStatus.NotSupported;
      default:
        return NFCStatus.NotSupported;
    }
  }
}
