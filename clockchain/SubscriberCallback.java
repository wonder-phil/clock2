package clockchain;

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

public class SubscriberCallback implements MqttCallback {

    enum MiningENUM { MINING, STOP }
    
    private MiningENUM MiningState = MiningENUM.STOP;
    private Gson gson = new Gson();
    private Block block;
    private MineBlock mineBlock;
    private Thread thread = null;
    private FileWriter writer = null;
    
    public SubscriberCallback() {
	
	mineBlock = new MineBlock();
	
	thread = new Thread(mineBlock,"Block Miner");
    }
    
    @Override
	public void connectionLost(Throwable arg0) {
    		System.out.println("Connection to MQTT broker lost!");
    }

    @Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
	
		String[] topics = arg0.getTopics();
		for (String t : topics) {
		    System.out.println(t);
		}
    }

    @Override
	public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
	
		System.out.println("Message received:\t"+ new String(mqttMessage.getPayload()) );
		System.out.println("mqttMessage topic: " + topic);
		
		System.out.println("Message received !:\t"+ new String(mqttMessage.getPayload()) );
		String value = new String(mqttMessage.getPayload());		    
		Block b = new Block("Foo", "Bar");

		SharedData.difficulty = 3;
		
		switch(topic) {
		
		case "difficulty":
		    block = gson.fromJson(value, Block.class);
		    System.out.println(value);
		    
		    SharedData.difficulty = Integer.parseInt(value);
		    
		    break;
		        
		case "last_block":
		    block = gson.fromJson(value, Block.class);
		    System.out.println(value);
		    
		    SharedData.lastBlock = value;
		    
		    break;
		        
		case "mine_state":
		    if (value.equals(new String("START"))) {
			
		    		System.out.println("--->START MINING");
			
		    		if (!thread.isAlive()) {
		    			thread.start();
		    		}
			
			
		    } else { // STOP
			
		    		System.out.println("--->STOP");
		    		mineBlock.stop();
		    		thread.interrupt();
		    }
		    
		    break;
		        
		default:
		    System.out.println("DEFAULT");
		    
		}
    }
}
