/*
 * Created by ArduinoGetStarted.com
 *
 * This example code is in the public domain
 *
 * Tutorial page: https://arduinogetstarted.com/tutorials/arduino-ultrasonic-sensor-piezo-buzzer
 */

// constants won't change
const int TRIG_PIN   = 6; // Arduino pin connected to Ultrasonic Sensor's TRIG pin
const int ECHO_PIN   = 7; // Arduino pin connected to Ultrasonic Sensor's ECHO pin
const int BUZZER_PIN = 3; // Arduino pin connected to Piezo Buzzer's pin
const int DISTANCE_THRESHOLD = 50; // centimeters
char Incoming_value = 0;                //Variable for storing Incoming_value

// variables will change:
float duration_us, distance_cm;

void setup() {
  Serial.begin (9600);         // initialize serial port
  pinMode(TRIG_PIN, OUTPUT);   // set arduino pin to output mode
  pinMode(ECHO_PIN, INPUT);    // set arduino pin to input mode
  pinMode(BUZZER_PIN, OUTPUT); // set arduino pin to output mode
}

void loop() {
  // generate 10-microsecond pulse to TRIG pin
  digitalWrite(TRIG_PIN, HIGH);
  delayMicroseconds(10);
  digitalWrite(TRIG_PIN, LOW);


  
  // measure duration of pulse from ECHO pin
  duration_us = pulseIn(ECHO_PIN, HIGH);
  // calculate the distance
  distance_cm = 0.017 * duration_us;

  if(distance_cm < DISTANCE_THRESHOLD){
    digitalWrite(BUZZER_PIN, HIGH); // turn on Piezo Buzzer
    Serial.println("Blind person Danger !!!");
    Serial.println(""); 
  }
  else{
     Serial.print("Safe"); 
          Serial.println(""); 

     digitalWrite(BUZZER_PIN, LOW);
    }


  // print the value to Serial Monitor
  Serial.println(""); 
  Serial.print("distance: ");
  Serial.print(distance_cm);
  Serial.println(" cm");

  delay(500);

  //if(Serial.available() > 0)  
//  {
//    Incoming_value = Serial.read();      //Read the incoming data and store it into variable Incoming_value
//    Serial.print(Incoming_value);        //Print Value of Incoming_value in Serial monitor
//    Serial.print("\n");        //New line 
//    if(Incoming_value == '1')            //Checks whether value of Incoming_value is equal to 1 
//      Serial.print("Blind person is near by object");  //If value is 1 then LED turns ON
//    else if(Incoming_value == '0')       //Checks whether value of Incoming_value is equal to 0
//      Serial.print("Blind person is safe"); 
//      digitalWrite(BUZZER_PIN, LOW);  // turn off Piezo Buzzer
//  }
    
}
