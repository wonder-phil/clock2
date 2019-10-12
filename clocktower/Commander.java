package clocktower;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import clockchain.SubscriberCallback;
import sharedData.SharedData;

public class Commander {
	
	public static void main(String[] args) throws MqttException {

	    String messageString = "Hello World from Commander";

	    if (args.length == 2 ) {
	      messageString = args[1];
	    }

	    System.out.println("== START Commander PUBLISHER ==");
	    
	    MqttClient client = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
	    client.connect();
	    
	    /*
	     * Listen on new_block topic for any announcements about miners
	     * finding the next block!
	     */
	    CommanderCallBack commanderCallBack = new CommanderCallBack();
		client.setCallback( commanderCallBack ); // new SimpleMqttCallBack()
	    client.subscribe("new_block");

	    /*
	     * Starting difficulty
	     */
	    int difficulty = 4;
	    
	    boolean New_Block_Found = false;
	    int chainLength = 10;
	    int i = 0;
	    
	    while (i < chainLength) {
	    
	    		MqttMessage message = new MqttMessage();
		    message.setPayload("MINE".getBytes());
		    client.publish("mine_state", message);
		    System.out.println("\tMessage '"+ messageString +"' to 'mine_state'");
	    	
		    while (!New_Block_Found) {
		    	
		    		if (SharedData.newBlockFound) {
		    			New_Block_Found = true;
		    			SharedData.newBlockFound = false;
		    		}
		    }
		    chainLength++;
		    
		    MqttMessage stopMessage = new MqttMessage();
		    stopMessage.setPayload("STOP".getBytes());
		    client.publish("mine_state", stopMessage);
		    System.out.println("\tMessage '"+ messageString +"' to 'mine_state'");
	    }

	    client.disconnect();

	    System.out.println("== END PUBLISHER ==");
	  }
	

}
