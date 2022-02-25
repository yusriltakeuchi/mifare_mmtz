import 'package:flutter/material.dart';
import 'package:mifare_mmtz/mifare_mmtz.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  NFCStatus nfcStatus;
  void checkSupport() async {
    final result = await MifareMmtz.isNFCEnable();
    if (result != null) {
      setState(() {
        nfcStatus = result;
      });
    }
  }
  @override
  void initState() {
    super.initState();
    this.checkSupport();
  }
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Text(
            nfcStatus != null
              ? 'NFC Status: ${nfcStatus == NFCStatus.Enabled ? 'ON' : 'OFF'}'
              : 'NFC Status: OFF'
          ),
        ),
      ),
    );
  }
}
