#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>
#include <ESP8266mDNS.h>
#include <ArduinoJson.h>
#include <Wire.h>
#include "SparkFun_AS7265X.h" 
AS7265X sensor;

// Setup JSON document
StaticJsonDocument<500> doc;
//DynamicJsonDocument doc(500);

//const char *ssid = "Spectrometer WIFI Access Point"; // The name of the Wi-Fi network that will be created
//const char *password = "thecakeisalie";   // The password required to connect to it, leave blank for an open network
const char *ssid = "TP-Link_E66C"; // The name of the Wi-Fi network that will be created
const char *password = "Anhsao123"; // The password required to connect to it, leave blank for an open network
ESP8266WebServer server(80);

float calibrationData[18]; // Creates an array filled with calibration data
boolean spectrometerLightMeasurement = false; // Setting a toggle for spectroscopy light measurements
boolean spectrometerCheck = false; // Spectrometer flag to only run once to stop status LED flashing

void handleRoot() {
  server.send(200, "text/plain", "Connected");
  Serial.println("Connected");
}

void turnLEDOn() {
  server.send(200, "text/plain", "LED turned on");
  Serial.println("LED turned on");
  spectrometerLightMeasurement = true;
}

void turnLEDOff() {
  server.send(200, "text/plain", "LED turned off");
  Serial.println("LED turned off");
  spectrometerLightMeasurement = false;
}

void transmitJSON() {
  doc.clear(); // Clear JSON document
  
  // Create JSON value and array
  doc["type"] = "spectrometer sensor";
  JsonArray data = doc.createNestedArray("data");
  
  // Add data to JSON array
  data.add(calibrationData[0]);
  data.add(calibrationData[1]);
  data.add(calibrationData[2]);
  data.add(calibrationData[3]);
  data.add(calibrationData[4]);
  data.add(calibrationData[5]);
  
  data.add(calibrationData[6]);
  data.add(calibrationData[7]);
  data.add(calibrationData[8]);
  data.add(calibrationData[9]);
  data.add(calibrationData[10]);
  data.add(calibrationData[11]);
  
  data.add(calibrationData[12]);
  data.add(calibrationData[13]);
  data.add(calibrationData[14]);
  data.add(calibrationData[15]);
  data.add(calibrationData[16]);
  data.add(calibrationData[17]);

  serializeJson(doc, server);
}

void setup() {
  Serial.begin(9600);

  // Setup WIFI 
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

  if (MDNS.begin("esp8266")) {
    Serial.println("MDNS responder started");
  }

  server.on("/", handleRoot);
  server.on("/1", turnLEDOn);
  server.on("/2", turnLEDOff);
  server.on("/data", transmitJSON);
}

void loop() {
  server.handleClient();
  checkSpectrometer();
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

// Transmit JSON file
/*void transmitJSON() {  
  doc.clear(); // Clear JSON document
  
  // Create JSON value and array
  doc["type"] = "spectrometer sensor";
  JsonArray data = doc.createNestedArray("data");
  
  // Add data to JSON array
  data.add(calibrationData[0]);
  data.add(calibrationData[1]);
  data.add(calibrationData[2]);
  data.add(calibrationData[3]);
  data.add(calibrationData[4]);
  data.add(calibrationData[5]);
  
  data.add(calibrationData[6]);
  data.add(calibrationData[7]);
  data.add(calibrationData[8]);
  data.add(calibrationData[9]);
  data.add(calibrationData[10]);
  data.add(calibrationData[11]);
  
  data.add(calibrationData[12]);
  data.add(calibrationData[13]);
  data.add(calibrationData[14]);
  data.add(calibrationData[15]);
  data.add(calibrationData[16]);
  data.add(calibrationData[17]);

  // Transmit data (FUNCTION CURRENTLY NOT WORKING)
  /*WiFiClient client = server.available();
  if(client) {
    Serial.println("Transmitting data");
    client.print("<!DOCTYPE HTML>\r\n");
    client.print("<head>\r\n");
    client.print("<link rel='shortcut icon' href='data:image/x-icon;,' type='image/x-icon'> \r\n"); // Shortcut icon to remove favicon.ico issue
    client.print("</head>\r\n");
    client.print("<html>\r\n");
    serializeJson(doc, client);
    client.print("</html>\r\n");
    client.stop();
  } else {
    Serial.println("Could not transmit data");
  }
}*/
