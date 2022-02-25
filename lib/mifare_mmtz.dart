
import 'dart:async';

import 'package:flutter/services.dart';

class MifareMmtz {
  static const MethodChannel _channel =
      const MethodChannel('mifare_mmtz');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
