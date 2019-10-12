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
    
	String host = "192.168.0.100";

	MqttClient client=new MqttClient("tcp://" + host + ":1883", MqttClient.generateClientId());
	client.setTimeToWait(100000000);
	
	MqttConnectOptions connOpts = new MqttConnectOptions();
    	connOpts.setKeepAliveInterval(3000000);
    	client.setTimeToWait(1000*6000);
	
	SubscriberCallback subscriberCallBack = new SubscriberCallback();
	client.setCallback( subscriberCallBack ); // new SimpleMqttCallBack()
	client.connect(connOpts);

	client.subscribe("difficulty");
	client.subscribe("mine_state");
	client.subscribe("last_block");
    }

}
