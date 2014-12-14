#define RFID_ENABLE 12
#define CODE_LEN 10
#define VALIDATE_TAG 1
#define VALIDATE_LENGTH 200
#define ITERATION_LENGTH 2000
#define START_BYTE 0x0A
#define STOP_BYTE 0x0D
#define MOTION_SENSOR_DELAY 30000

char messageArray[15];
char tag[CODE_LEN];
boolean rfidOn = false;
int ledPin = 13;
int motionPin = 2;
int secondLedPin = 11;
int secondMotionPin = 3;

void setup() {
  Serial.begin(2400);
  delay(100);
  pinMode(ledPin, OUTPUT);
  pinMode(motionPin, INPUT);
  pinMode(RFID_ENABLE, OUTPUT);
  pinMode(secondLedPin, OUTPUT);
  pinMode(secondMotionPin, INPUT);
  digitalWrite(ledPin, LOW);
  digitalWrite(secondLedPin, LOW);
  while(true) {
    if(Serial.available() > 0) {
      break;
    }
  }
}

void loop() {
  readRFID();
  detectMotion();
}

void clearCode() {
  for(int i = 0; i < CODE_LEN; i++) {
    tag[i] = 0;
  }
}

void sendCode() {
  Serial.print("TAG: ");
  for(int i = 0; i < CODE_LEN; i++) {
    Serial.print(tag[i]);
  }
  Serial.println("                        ");
}

void detectMotion() {
  /* Motion Sensor turns light on */
  int secondValue = digitalRead(secondMotionPin);
  if(secondValue == HIGH) {
      Serial.println("LOG: Motion detector #2 turned lights on. ");
      Serial.flush();
      delay(10);
      digitalWrite(secondLedPin, HIGH);
      motionDetectorDelay(2);
      if(!rfidOn) {
        secondValue = digitalRead(secondMotionPin);
        if(secondValue == LOW) {
          digitalWrite(secondLedPin, LOW);
          Serial.println("LOG: Motion detector #2 turned lights off.");
          Serial.flush();
          delay(10);
        }
      }
    } 
    else {
      digitalWrite(secondLedPin, LOW);
    }
  if(!rfidOn) {
    int value = digitalRead(motionPin);
    if(value == HIGH) {
      Serial.println("LOG: Motion detector #1 turned lights on. ");
      Serial.flush();
      delay(10);
      digitalWrite(ledPin, HIGH);
      motionDetectorDelay(1);
      if(!rfidOn) {
        value = digitalRead(motionPin);
        if(value == LOW) {
          digitalWrite(ledPin, LOW);
          Serial.println("LOG: Motion detector #1 turned lights off.");
          Serial.flush();
          delay(10);
        }
      }
    } 
    else {
      digitalWrite(ledPin, LOW);
    }
  }  
}

void motionDetectorDelay(int motionSensorNumber) {
  for(int i = 0; i < MOTION_SENSOR_DELAY; i++) {
    readRFID();
    if(!rfidOn) {
      delay(1);
      if(i == MOTION_SENSOR_DELAY) {
        if(motionSensorNumber == 1) {
           Serial.println("LOG: Motion detector #1 delay finished.   "); 
           Serial.flush();
           delay(10);
        } else if(motionSensorNumber == 2) {
           Serial.println("LOG: Motion detector #2 delay finished.   ");
           Serial.flush();
           delay(10); 
        }
      }
    } 
    else {
      break;
    }
  }
}

void readRFID() {
  /* RFID turns light on */
  enableRFID();
  getRFIDTag();
  if(isCodeValid()) {
    disableRFID();
    sendCode();
    byte next_byte;
    if(Serial.available() > 0) {
      if((next_byte = Serial.read()) == START_BYTE) {      
        byte bytesread = 0; 
        while(bytesread < 12) {
          if(Serial.available() > 0) { 
            if((next_byte = Serial.read()) == STOP_BYTE) break;       
            messageArray[bytesread++] = next_byte;                   
          }
        }                
      }
    }
    String message(messageArray);
    delay(10);
    if(message.equals("TAG VALID.  ")) {
      if(rfidOn) {
        //delay(3000);
        Serial.println("LOG: RFID turned lights off.           ");
        Serial.flush();
        delay(10);
        digitalWrite(ledPin, LOW);
        rfidOn = false;
      } 
      else {
        Serial.println("LOG: RFID turned lights on.            ");
        Serial.flush();
        delay(10);
        digitalWrite(ledPin, HIGH);
        rfidOn = true;
      }
    }
  }
  clearCode();
}

/* RFID FUNCTIONS */

void enableRFID() {
  digitalWrite(RFID_ENABLE, LOW);
}

void disableRFID() {
  digitalWrite(RFID_ENABLE, HIGH);
}

void getRFIDTag() {
  byte next_byte;
  if(Serial.available() > 0) {
    if((next_byte = Serial.read()) == START_BYTE) {      
      byte bytesread = 0; 
      while(bytesread < CODE_LEN) {
        if(Serial.available() > 0) { 
          if((next_byte = Serial.read()) == STOP_BYTE) break;       
          tag[bytesread++] = next_byte;                   
        }
      }                
    }
  }
}

boolean isCodeValid() {
  byte next_byte;
  int count = 0;
  if(isCodeAllZero()) {
    return false;
  }
  while(Serial.available() < 2) {
    delay(1);
    if(count++ > VALIDATE_LENGTH)
      return false;
  }
  Serial.read();

  if((next_byte = Serial.read()) == START_BYTE) {
    byte bytes_read = 0;
    while(bytes_read < CODE_LEN) {
      if(Serial.available() > 0) {
        if((next_byte = Serial.read()) == STOP_BYTE) break;
        if(tag[bytes_read++] != next_byte) return false;
      }
    }
  }
  delay(ITERATION_LENGTH);
  return true;
}

boolean isCodeAllZero() {
  for(byte b = 0; b < CODE_LEN; b++) {
    if(tag[b] != 0) { 
      return false; 
    }
  }
  return true;
}











