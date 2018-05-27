#include <Arduino.h>

#include <ESP8266WiFi.h>
#include <ESP8266WiFiMulti.h>

#include <ESP8266HTTPClient.h>

#define USE_SERIAL Serial

ESP8266WiFiMulti WiFiMulti;
int application1 = 0;
int application2 = 1;
int application3 = 2;
int application4 = 3;

void setup() {

  pinMode(LED_BUILTIN, OUTPUT);     // Initialize the LED_BUILTIN pin as an output
  pinMode(application1, OUTPUT);     // Initialize the application1 pin as an output
  pinMode(application2, OUTPUT);     // Initialize the application2 pin as an output
  pinMode(application3, OUTPUT);     // Initialize the application3 pin as an output
  pinMode(application4,OUTPUT);     // Initialize the application4 pin as an output

  WiFiMulti.addAP("your WiFi ssid", "password");

}

void loop() {
    // wait for WiFi connection

    if(!(WiFiMulti.run() == WL_CONNECTED)){
        //analogWrite(0, rvalue);
        digitalWrite(LED_BUILTIN, HIGH);  // Turn the LED off by making the voltage HIGH
        delay(500);
        digitalWrite(LED_BUILTIN, LOW);  // Turn the LED off by making the voltage HIGH
        delay(500);
        return;
      }
    if((WiFiMulti.run() == WL_CONNECTED)) {

        HTTPClient http;
        http.begin("your server url"); //HTTP GET
        int httpCode = http.GET();
        if(httpCode > 0) {
            if(httpCode == HTTP_CODE_OK) {
                String payload = http.getString();
                if(payload=="your application id and statue")
                    digitalWrite(application1,HIGH);
                if(payload=="your application id and statue")
                    digitalWrite(application1,LOW);
                if(payload=="your application id and statue")
                    digitalWrite(application2,HIGH);
                if(payload=="your application id and statue")
                    digitalWrite(application2,LOW);
                if(payload=="your application id and statue")
                    digitalWrite(application3,HIGH);
                if(payload=="your application id and statue")
                    digitalWrite(application3,LOW);
                if(payload=="your application id and statue")
                    digitalWrite(application4,LOW);
                if(payload=="your application id and statue")
                    digitalWrite(application4,HIGH);
            }
        }
        http.end();
    }

    delay(1000);
}
