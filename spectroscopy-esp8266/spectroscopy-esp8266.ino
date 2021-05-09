#include <ESP8266WiFi.h>
#include "SparkFun_AS7265X.h" 
AS7265X sensor;

#include <Wire.h>

//const char *ssid = "Spectrometer WIFI Access Point"; // The name of the Wi-Fi network that will be created
//const char *password = "thecakeisalie";   // The password required to connect to it, leave blank for an open network
const char *ssid = "TP-Link_E66C"; // The name of the Wi-Fi network that will be created
const char *password = "Anhsao123";   // The password required to connect to it, leave blank for an open network
WiFiServer server(80);

float calibrationData[18]; // Creates an array filled with calibration data
boolean spectrometerLightMeasurement = false; // Setting a toggle for spectroscopy light measurements
boolean spectrometerCheck = false; // Spectrometer flag to only run once to stop status LED flashing

void setup() {
  Serial.begin(9600);
  
  // Set up WIFI 
  WiFi.begin(ssid, password);
  // Makes sure the ESP8266 is connected to WIFI
  while (WiFi.waitForConnectResult() != WL_CONNECTED) {
    Serial.println(WiFi.status()); // Prints WL error number 
    /*
     * WL codes
     * WL_NO_SHIELD        = 255   // For compatibility with WiFi Shield library
     * WL_IDLE_STATUS      = 0
     * WL_NO_SSID_AVAIL    = 1
     * WL_SCAN_COMPLETED   = 2
     * WL_CONNECTED        = 3
     * WL_CONNECT_FAILED   = 4
     * WL_CONNECTION_LOST  = 5
     * WL_DISCONNECTED     = 6
    */
  }
  server.begin(); // Tells the server to begin listening for incoming connections
  Serial.println("WIFI connected");
  Serial.print("IP Address: "); 
  Serial.println(WiFi.localIP()); // Prints IP address
}

void loop() {
  checkSpectrometer();
  checkWIFI();
  getCalibrationData();
}

// Checks if the spectrometer is connected
void checkSpectrometer() {
  if(!spectrometerCheck) { 
    if(!sensor.begin()) {
      Serial.println("Spectrometer sensor is not connected, please check connection and try again");
      // Should print error to app
      while(1);
    } else {
      sensor.disableIndicator(); // Turn off the blue status LED to not interfere with readings
      spectrometerCheck = true;
    }
  }
}

// Checks if the app is connected to the IP address
void checkWIFI() {
  WiFiClient client = server.available();
  if(client) {
    String request = client.readStringUntil('\r'); // HTTP request
    client.flush(); // Waits until all of buffer have been sent
    Serial.println("Connected");
    // Checks if the suffix of the IP address
    if(request.indexOf(" /1 ") != -1) {
      spectrometerLightMeasurement = true;
      Serial.println("Light measurement on");
    }
    else if(request.indexOf(" /2 ") !=  -1) {
      spectrometerLightMeasurement = false;
      Serial.println("Light measurement off");
    }
  }
}

// Retrieves the spectrometer data
void getCalibrationData() {
  spectrometerLightMeasurement ? sensor.takeMeasurementsWithBulb() : sensor.takeMeasurements(); // Takes measurements based on light toggle
  
  calibrationData[0] = sensor.getCalibratedA();
  calibrationData[1] = sensor.getCalibratedB();
  calibrationData[2] = sensor.getCalibratedC();
  calibrationData[3] = sensor.getCalibratedD();
  calibrationData[4] = sensor.getCalibratedE();
  calibrationData[5] = sensor.getCalibratedF();
  
  calibrationData[6] = sensor.getCalibratedG();
  calibrationData[7] = sensor.getCalibratedH();
  calibrationData[8] = sensor.getCalibratedI();
  calibrationData[9] = sensor.getCalibratedJ();
  calibrationData[10] = sensor.getCalibratedK();
  calibrationData[11] = sensor.getCalibratedL();
  
  calibrationData[12] = sensor.getCalibratedR();
  calibrationData[13] = sensor.getCalibratedS();
  calibrationData[14] = sensor.getCalibratedT();
  calibrationData[15] = sensor.getCalibratedU();
  calibrationData[16] = sensor.getCalibratedV();
  calibrationData[17] = sensor.getCalibratedW();
}
