package play;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

import gametree.GameNode;
import play.exception.InvalidStrategyException;
import reader.NonBlockingReader;

public class InteractiveStrategy extends Strategy {

	@Override
	public void execute() throws InterruptedException {
		System.err.println("Interactive Strategy is now active...");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		NonBlockingReader r = new NonBlockingReader();
		while(!this.isTreeKnown()) {
			System.err.println("Waiting for game tree to become available.");
			Thread.sleep(1000);
		}
	
		while(true) {
		
			PlayStrategy myStrategy = this.getStrategyRequest();
			if(myStrategy == null) //Game was terminated by an outside event
				break;
			boolean playComplete = false;
			while(! playComplete ) {
			
				if(myStrategy.getFinalP1Node() != -1) {
					GameNode finalP1 = this.tree.getNodeByIndex(myStrategy.getFinalP1Node());
					if(finalP1 != null)
						System.out.println("Terminal node in last round as P1: " + finalP1);
				}
				
				if(myStrategy.getFinalP2Node() != -1) {
					GameNode finalP2 = this.tree.getNodeByIndex(myStrategy.getFinalP2Node());
					if(finalP2 != null)
						System.out.println("Terminal node in last round as P2: " + finalP2);
				}
				
				System.out.println("Continue playing? [y/n: default is y]");
				try{
					if(r.nonBlockingRead(br).equalsIgnoreCase("n")){
						this.quitGame(myStrategy);
						//return;
					}		
				} catch (IOException e) {
					;//Nothing to be done here
				}
				
				System.out.println("Provide your strategy:");
				
				Iterator<String> iterator = myStrategy.keyIterator();
				
				Double d = null;
				
				while(iterator.hasNext()) {
					String key = iterator.next();
					while(true) {
						System.out.println("Provide value (0-1) for the information set: " + key);
						try {
							d = Double.valueOf(r.nonBlockingRead(br));
							myStrategy.put(key, d);
							break;
						} catch (NumberFormatException e) {
							System.err.println("Invalid number.");
						} catch (IOException e) {
							System.err.println("Invalid input.");
						}
					}	
				}
				
				try {
					this.provideStrategy(myStrategy);
					playComplete = true;
				} catch (InvalidStrategyException e) {
					System.err.println("Strategy refused: " + e.getMessage());
				}
			}
		}
		
	}

}
