package bcs.service;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import com.google.gson.Gson;





public final class Blockchain implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 696491248001555039L;
	private ArrayList<Block> blocks = new ArrayList<Block>();
	private static Blockchain instance = null;
	
	
	
	private Blockchain() {}
	
	public static Blockchain getInstance() {
		if ( instance == null) {
			instance = new Blockchain();
			instance.blocks.add(instance.createGenesisBlock());
			
		}
		return instance;
	}
	
	private Block createGenesisBlock() {
		return new Block(new Timestamp(System.currentTimeMillis()), new ArrayList<Transaction>(), SHA.sha256(new Timestamp(System.currentTimeMillis()).toString()).toString());
		
	}
	
	public Block getLastBlock() {
		return this.blocks.get(this.blocks.size()-1);
	}

	public void addBlock(Block block) {
		this.blocks.add(block);
	}
	
	
	
	
	public ArrayList<Block> getBlocks() {
		return blocks;
	}
	
	
	public File sendBlockchainFile() {
		String path = System.getProperty("user.dir");
		return new File(path + "/blockchain.json");
		
	}
	

	public void toJSONFile() {
		String path = System.getProperty("user.dir");
		Gson gson = new Gson();
		try(FileWriter fileWriter = new FileWriter(path + "/blockchain.json")){
			gson.toJson(this,fileWriter);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
