#include <SoftwareSerial.h>

SoftwareSerial mySerial(5, 6); // RX, TX

void setup()
{
  // Open serial communications and wait for port to open:
  Serial.begin(9600);
  while (!Serial) {
    ; // wait for serial port to connect. Needed for Leonardo only
  }



  // set the data rate for the SoftwareSerial port
  mySerial.begin(9600);

}

void loop() // run over and over
{
  if (mySerial.available())
    Serial.write(mySerial.read());
  if (Serial.available())
    mySerial.write(Serial.read());
  mySerial.write("*6");
  int rand[5];
  rand[0] = random(0,9)+48;
  rand[1] = random(0,9)+48;
  rand[2] = random(0,9)+48;
  rand[3] = random(0,9)+48;
  rand[4] = random(1,4)+48;
  
  mySerial.write(rand[0]);
  mySerial.write(rand[1]);
  mySerial.write(rand[2]);
  mySerial.write(rand[3]);
  mySerial.write(rand[4]);
  
  Serial.write(rand[0]);
  Serial.write(rand[1]);
  Serial.write(rand[2]);
  Serial.write(rand[3]);
//  Serial.write(rand[4]);
  Serial.println();
  
  delay(100);
}

