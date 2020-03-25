package play;

import java.security.SecureRandom;
import java.util.Iterator;

import gametree.GameNode;
import play.exception.InvalidStrategyException;

public class RandomStrategy extends Strategy {

	@Override
	public void execute() throws InterruptedException {
		
		SecureRandom random = new SecureRandom();
		
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
				
				Iterator<Integer> iterator = tree.getValidationSet().iterator();
				Iterator<String> keys = myStrategy.keyIterator();
				
				while(iterator.hasNext()) {
					double[] moves = new double[iterator.next()];
					double sum = 0;
					for(int i = 0; i < moves.length - 1; i++) {
						moves[i] = random.nextDouble();
						while(sum + moves[i] >= 1) moves[i] = random.nextDouble();
						sum = sum + moves[i];
					}
					moves[moves.length-1] = ((double) 1) - sum;
					
					for(int i = 0; i < moves.length; i++) {
						if(!keys.hasNext()) {
							System.err.println("PANIC: Strategy structure does not match the game.");
							return;
						}
						myStrategy.put(keys.next(), moves[i]);
					}
				}
				
				try{
					this.provideStrategy(myStrategy);
					playComplete = true;
				} catch (InvalidStrategyException e) {
					System.err.println("Invalid strategy: " + e.getMessage());;
					e.printStackTrace(System.err);
				} 
			}
		}
		
	}

}
