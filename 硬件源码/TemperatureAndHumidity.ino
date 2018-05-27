#include <Arduino.h>
#include "DHT.h"
#include <ESP8266WiFi.h>
#include <ESP8266WiFiMulti.h>

#include <ESP8266HTTPClient.h>

#define USE_SERIAL Serial

ESP8266WiFiMulti WiFiMulti;
#define DHTPIN 0     // what digital pin we're connected to
#define DHTTYPE DHT11   // DHT 11
DHT dht(DHTPIN, DHTTYPE);
char tempturestr[] = "";
char humiditystr[] = "";

void setup() {

  pinMode(LED_BUILTIN,OUTPUT);
  WiFiMulti.addAP("your WiFi ssid", "password");
  dht.begin();
}

void loop() {

    if(!(WiFiMulti.run() == WL_CONNECTED)){
        digitalWrite(LED_BUILTIN, HIGH);  // Turn the LED off by making the voltage HIGH
        delay(500);
        digitalWrite(LED_BUILTIN, LOW);  // Turn the LED off by making the voltage HIGH
        delay(500);
        return;
      }
    if((WiFiMulti.run() == WL_CONNECTED)) {
        digitalWrite(LED_BUILTIN, HIGH);
        delay(1000);
        float h = dht.readHumidity();
        float t = dht.readTemperature();
        if (isnan(h) || isnan(t)) {
          return;
     }
        HTTPClient http;
        dtostrf(t,3,1,tempturestr);

        String url = "your server url";
        url = url + tempturestr;
        delay(200);
        dtostrf(h,3,1,humiditystr);
        String humidityurl = "&humidity=";
        url = url + humidityurl + humiditystr;
        http.begin(url); //HTTP
        int httpCode = http.GET();
        if(httpCode > 0) {
            if(httpCode == HTTP_CODE_OK) {
               // String payload = http.getString();
                digitalWrite(LED_BUILTIN,LOW);
                delay(100);
                digitalWrite(LED_BUILTIN,HIGH);
            }
        }

        http.end();
    }

    delay(1000);
}
