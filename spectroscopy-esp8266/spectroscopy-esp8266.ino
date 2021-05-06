#include "SparkFun_AS7265X.h" 
AS7265X sensor;

#include <Wire.h>

float calibrationData[18]; // Creates an array filled with calibration data

void setup() {
  Serial.begin(9600);
  Serial.read();
  // Checks if the spectrometer sensor is connected
  if(sensor.begin() == false) { 
    Serial.println("Spectrometer sensor is not connected, please check connection and try again");
    while(1);
  }
  Serial.println("Spectrometer sensor connected");
  sensor.disableIndicator(); // Turn off the blue status LED to not interfere with readings
}

void loop() {
  getCalibrationData();
  Serial.println(calibrationData[0]);
  delay(1000);
}

void getCalibrationData(){
  sensor.takeMeasurements();
  //sensor.takeMeasurementsWithBulb();
  
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

  Serial.println(sensor.getCalibratedA());
}
