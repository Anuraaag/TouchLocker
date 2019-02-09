#include <SoftwareSerial.h>
#define in1 3
#define in2 4
int rotDirection = 0;

SoftwareSerial mySerial(10, 11); // RX, TX

void setup() {
  
Serial.begin(9600);
mySerial.begin(9600);
  pinMode(in1, OUTPUT);
  pinMode(in2, OUTPUT);

  // Set initial rotation direction
  digitalWrite(in1, LOW);
  digitalWrite(in2, LOW);
}
char key;
void loop() {
    if (mySerial.available()) 
    {
       key = (char)mySerial.read();
       Serial.print(key);
    }
   if ( key == '1' )
   {
      digitalWrite(in1, LOW);
      digitalWrite(in2, HIGH);
      
      delay(3000);
      key = '3';
    }   
   if ( key == '0' ) 
   {
      digitalWrite(in1, HIGH);
      digitalWrite(in2, LOW);
      
      delay(3000);
      key = '3';
   }
   if( key == '3')
  {
      digitalWrite(in1, LOW);
      digitalWrite(in2, LOW);
    
  }
  
  
}
