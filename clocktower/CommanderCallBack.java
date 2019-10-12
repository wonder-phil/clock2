package clocktower;


import static java.lang.Thread.currentThread;

import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.google.gson.*;

import blocks.Block;
import blocks.MineBlock;
import sharedData.SharedData;

public class CommanderCallBack implements MqttCallback {

    enum MiningENUM { MINING, STOP }
    
    private MiningENUM MiningState = MiningENUM.STOP;
    private Gson gson = new Gson();
    private Block newBlock;
    private MineBlock mineBlock;
    private Thread thread = null;
    private FileWriter writer = null;
    
    @Override
	public void connectionLost(Throwable arg0) {
    		System.out.println("Connection to MQTT broker lost!");
    }

    @Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
	
		String[] topics = arg0.getTopics();
		for (String t : topics) {
		    System.out.println("Delivery Complete: "+t);
		}
    }

    @Override
	public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
	
    	/*
		System.out.println("Message received:\t"+ new String(mqttMessage.getPayload()) );
		System.out.println("mqttMessage topic: " + topic);
		
		System.out.println("Message received !:\t"+ new String(mqttMessage.getPayload()) );
		*/
		String value = new String(mqttMessage.getPayload());		    
		
		switch(topic) {
		
		case "new_block":
		    //newBlock = gson.fromJson(value, Block.class);
		    System.out.println(topic + ": " + value);
		    
		    SharedData.newBlockFound = true;
		    SharedData.lastBlock = value;
		    
		    break;
		       
		        
		default:
		    System.err.println("Error: bad topic");
		    
		}
    }
}

