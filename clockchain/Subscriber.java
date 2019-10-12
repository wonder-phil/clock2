package clockchain;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

/*
 *
 *
 * From: http://tugdualgrall.blogspot.com/2017/01/getting-started-with-mqtt-and-java.html 
 *
 */


public class Subscriber {

    public static void main(String[] args) throws MqttException {

	System.out.println("=== START SUBSCRIBER ===");
    
	String host = "";

	MqttClient client=new MqttClient("tcp://" + host + ":1883", MqttClient.generateClientId());
	client.setTimeToWait(100000000);
	
	SubscriberCallback subscriberCallBack = new SubscriberCallback();
	client.setCallback( subscriberCallBack ); // new SimpleMqttCallBack()
	client.connect();

	client.subscribe("difficulty");
	client.subscribe("mine_state");
	//client.subscribe("new_block"); // ***
	client.subscribe("last_block");
    }

}
