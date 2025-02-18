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
  NFCStatus? nfcStatus;
  String? mifareID;
  void checkSupport() async {
    final result = await MifareMmtz.isNFCEnable();
    setState(() {
      nfcStatus = result;
    });
  }

  void readMifareId() async {
    final result = await MifareMmtz.getId();
    if (result.isNotEmpty) {
      setState(() {
        mifareID = result;
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
          title: const Text('Mifare MMTZ Plugin'),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text(
                nfcStatus != null
                  ? 'NFC Status: ${nfcStatus == NFCStatus.Enabled ? 'ON' : 'OFF'}'
                  : 'NFC Status: OFF',
                style: TextStyle(
                  fontSize: 20
                ),
              ),
              Text(
               "NFC Mifare ID: $mifareID",
                style: TextStyle(
                  fontSize: 20
                ),
              ),

              SizedBox(height: 20,),
              ElevatedButton(
                onPressed: () => readMifareId(),
                style: ButtonStyle(
                  backgroundColor: WidgetStateProperty.all(Colors.blue)
                ),
                child: Text(
                  "Read Mifare ID",
                  style: TextStyle(
                    color: Colors.white
                  ),
                ),
              )
            ],
          ),
        ),
      ),
    );
  }
}
