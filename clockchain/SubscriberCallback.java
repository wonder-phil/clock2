package clockchain;

import static java.lang.Thread.currentThread;

import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

import com.google.gson.*;

import blocks.Block;
import blocks.MineBlock;
import clockchain.SimpleMqttCallBack;
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
		
		switch(topic) {
		
		case "difficulty":
	
		    System.out.println("difficulty: " + value);
		    
		    SharedData.difficulty = Integer.parseInt(value);
		    
		    break;
		        
		case "last_block":
		    block = gson.fromJson(value, Block.class);
		    System.out.println(value);
		    
		    SharedData.lastBlock = value;
		    
		    break;
		        
		case "mine_state":
		    if (value.equals(new String("MINE"))) {
			
			System.out.println("--->START MINING");

			if (!thread.isAlive()) {
				thread.start();
			}

			String newHash = SharedData.newHash;
			String previousHash = SharedData.previousHash;

			Block newBlock = (Block) new Block(newHash,previousHash);

			SharedData.newBlockString = gson.toJson(newBlock, Block.class);

			System.out.println("Before connection to new_block");
		    		
		    MqttClient client = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
		    MqttConnectOptions connOpts = new MqttConnectOptions();
		    connOpts.setKeepAliveInterval(3000000);
		    client.setTimeToWait(1000*6000);
		    SimpleMqttCallBack simpleMqttCallBack = new SimpleMqttCallBack();
			client.setCallbac	k( simpleMqttCallBack ); // new SimpleMqttCallBack()

		    client.connect(connOpts);

			MqttMessage newBlockMessage = new MqttMessage();
		    newBlockMessage.setPayload(SharedData.newBlockString.getBytes());
		    client.publish("new_block", newBlockMessage);

		    System.out.println("After connection to new_block");

		    client.disconnect();

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
