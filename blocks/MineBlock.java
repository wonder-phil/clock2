package blocks;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import com.google.gson.Gson;

import sharedData.SharedData;

public class MineBlock implements Runnable {

    private volatile boolean exit = false;
    private String fileNPath = "block.json";
    
    @Override
	public void run() {
	
        Gson gson = new Gson();
        String jsonString = SharedData.lastBlock;
        int difficulty = SharedData.difficulty;
        
        Block lastBlock = gson.fromJson(jsonString, Block.class);
        
        System.out.println("Mine Block");
        
        lastBlock.mineBlock(difficulty);
    }
    
    public void stop(){
	exit = true;
    }
}
