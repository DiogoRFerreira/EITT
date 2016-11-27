//MQ-3 Alcohol Sensor
#define SensorAout 0
#define SensorDout 3

//Memory
int prev_value = 9999;
char inbyte = 0;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600); //baud rate
  pinMode(SensorDout, INPUT);
}

void loop() {
  // put your main code here, to run repeatedly:
   if (Serial.available() > 0){
    inbyte = Serial.read();
    if (inbyte == '1'){
      int i = 0;
      int value2 = 0;
      while(i<20){
        int limit = analogRead(SensorAout);
        int value = digitalRead(SensorDout);
        if(value != prev_value){
          if(value >= value2){
            value2 = value;
          }
          prev_value = value;
        }
        i++;
      }
      Serial.print(value2);
    }
  }
}
