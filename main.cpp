#include "mbed.h"
#include "SPI.h"
#include "MQTTEthernet.h"
#include "MQTTClient.h"

#define ECHO_SERVER_PORT   7
 
Serial pc(USBTX, USBRX);
Serial serial(D1,D0); // Enabling Serial transmission betwenn Wiz750SR and W7500. 

//DigitalOut myled(LED1);


void messageArrived(MQTT::MessageData& md)
{
    char arr[100];
    int i = 0;
    MQTT::Message &message = md.message;
    
    pc.printf("Payload %.*s\r\n", message.payloadlen, (char*)message.payload);
    serial.printf("%.*s", message.payloadlen, (char*)message.payload);       
}

int main(void) {
    printf("Wait a second...\r\n");
    char* topic = "FingerPrints";                    //if we are subscribing the acknowledgement.
    MQTTEthernet ipstack = MQTTEthernet();
    
    MQTT::Client<MQTTEthernet, Countdown> client = MQTT::Client<MQTTEthernet, Countdown>(ipstack);
    
    char* hostname = "m11.cloudmqtt.com";   //Give the IP Address of the MQTT Broker.
    int port = 11093;                  // Port number of the MQTT broker.  
    
 
    int rc = ipstack.connect(hostname, port);
    if (rc != 0)
    printf("rc from TCP connect is %d\n", rc);
    printf("Topic: %s\r\n",topic);
    
    MQTTPacket_connectData data = MQTTPacket_connectData_initializer;       
    data.MQTTVersion = 3;
    data.clientID.cstring = "parents";
    data.username.cstring = "dpuhuqmn";
    data.password.cstring = "J6rvrL_4Ggq_";
    data.cleansession = 0;

    if ((rc = client.connect(data)) == 0)
    printf("rc from MQTT connect is %d\n", rc);
    
    if ((rc = client.subscribe(topic, MQTT::QOS2, messageArrived)) != 0)
    printf("rc from MQTT subscribe is %d\r\n", rc);  
    while(1)
    client.yield(10000);
}
