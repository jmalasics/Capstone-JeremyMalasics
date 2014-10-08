int ledPin = 13;
int inputPin = 2;
const int RFID_ENABLE = 12;
const int CODE_LEN = 10;
const int VALIDATE_TAG = 1;
const int VALIDATE_LENGTH = 200;
const int ITERATION_LENGTH = 2000;
const byte START_BYTE = 0x0A;
const byte STOP_BYTE = 0x0D;

char tag[CODE_LEN];

void setup() {
  Serial.begin(2400);
  pinMode(ledPin, OUTPUT);
  pinMode(inputPin, INPUT);
  pinMode(RFID_ENABLE, OUTPUT);
  digitalWrite(ledPin, LOW);
}

void loop() {
  enableRFID();
  getRFIDTag();
  if(isCodeValid()) {
    sendCode();
    delay(ITERATION_LENGTH);
  } else {
    Serial.println("Got some noise");
  }
  Serial.flush();
  clearCode();
  int value = digitalRead(inputPin);
  if(value == HIGH) {
    digitalWrite(ledPin, HIGH);
    delay(3000);
    digitalWrite(ledPin, LOW);
  } else {
    digitalWrite(ledPin, LOW);
  }
}

void clearCode() {
  for(int i = 0; i < CODE_LEN; i++) {
    tag[i] = 0;
  }
}

void sendCode() {
  Serial.print("TAG:");
  char full_tag[10];
  for(int i = 0; i < CODE_LEN; i++) {
    if(i == 9) {
      Serial.println(tag[i]);
    } else {
      Serial.print(tag[i]);
    }
  }
}

/* RFID FUNCTIONS */

void enableRFID() {
  digitalWrite(RFID_ENABLE, LOW);
}

void disableRFID() {
  digitalWrite(RFID_ENABLE, HIGH);
}

void getRFIDTag() {
  byte next_byte = 0;
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
  return true;
}


