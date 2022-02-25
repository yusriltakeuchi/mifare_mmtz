import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:mifare_mmtz/mifare_mmtz.dart';

void main() {
  const MethodChannel channel = MethodChannel('mifare_mmtz');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await MifareMmtz.platformVersion, '42');
  });
}
