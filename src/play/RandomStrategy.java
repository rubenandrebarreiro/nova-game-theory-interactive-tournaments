package play;

import java.security.SecureRandom;
import java.util.Iterator;

import play.exception.InvalidStrategyException;

/**
 *
 * Computational Game Theory - 2019/2020
 * Faculty of Sciences and Technology of
 * New University of Lisbon (FCT NOVA)
 *
 * 1st Tournament - Prisoner's Dilemma,
 * using NOVA GTI (Game Theory Interactive) Platform
 *
 * Random Strategy
 *
 * Authors:
 * - Pedro Lamarao Pais (Student no. 48247)
 *   - pgp@campus.fct.unl.pt
 * - Ruben Andre Barreiro (Student no. 42648)
 *   - r.barreiro@campus.fct.unl.pt
 *
 */

public class RandomStrategy extends Strategy {

	@Override
	public void execute() throws InterruptedException {

		// Creating a Random Seed
		SecureRandom random = new SecureRandom();

		// Waits until the Game Tree become known and available
		while(!this.isTreeKnown()) {

			System.err.println("Waiting for Game Tree to become Available...");

			//noinspection BusyWait
			Thread.sleep(1000);

		}

		// Infinite Loop
		while(true) {
		
			PlayStrategy myStrategy = this.getStrategyRequest();

			// The Strategy chosen by me become NULL,
			// what means (probably) that the Game
			// was terminated by an outside event
			if(myStrategy == null) {

				// Breaks the Infinite Loop
				break;

			}

			// Prints the Basic Information about this Strategy
			System.out.println("Start playing with the Random Strategy...");

			// My Play wasn't completed yet
			boolean playComplete = false;

			// While My Play isn't complete yet
			while(! playComplete ) {

				// Verify if the current Game Nodes for the Plays as both,
				// Player #1 and Player #2, are Terminal Game Nodes or not
				StrategyCommonUtils.isATerminalGameNode
						(
								myStrategy,
								this.tree.getNodeByIndex(myStrategy.getFinalP1Node()),
								this.tree.getNodeByIndex(myStrategy.getFinalP2Node())
						);

				// The Current Validation Set for the Available Moves
				Iterator<Integer> currentValidationSetIterator = tree.getValidationSet().iterator();

				// The Labels for All the Available Moves
				Iterator<String> availableMovesLabels = myStrategy.keyIterator();

				// Loop to check all the Entries of the Validation Set
				// (i.e., a Decision Node of the Tree representing the current Game)
				while(currentValidationSetIterator.hasNext()) {

					// Creates the array to keep the information for
					// the Probabilities for all the Available Moves
					double[] availableMovesProbabilities = new double[ currentValidationSetIterator.next() ];

					// The Sum of the Probabilities for all the Available Moves
					double sumAvailableMovesProbabilities = 0;

					// Sets the Probabilities for all the Available Moves, excluding the last one Move
					for(int i = 0; i < ( availableMovesProbabilities.length - 1 ); i++) {

						// Sets the Probability for the current Move
						//noinspection ConstantConditions
						availableMovesProbabilities[i] = random.nextDouble();

						// While the current Sum of Probabilities set for all the Available Moves it's
						// greater or equal than one, sets a new Probability for the current Move
						while( ( sumAvailableMovesProbabilities + availableMovesProbabilities[i] ) >= 1 ) {

							availableMovesProbabilities[i] = random.nextDouble();

						}

						// Sums the Probability for the current Move to
						// the Sum of Probabilities set for all the Available Moves
						sumAvailableMovesProbabilities += availableMovesProbabilities[i];

					}

					// Sets the Remaining Available Probability for the Remaining Available Move
					availableMovesProbabilities[availableMovesProbabilities.length - 1] =
												( ( (double) 1 ) - sumAvailableMovesProbabilities );


					// Assigns all the current Available Probabilities to all the current Available Moves
					for(double availableMoveProbability : availableMovesProbabilities) {

						// The Strategy's Structure doesn't match the current Game
						if(!availableMovesLabels.hasNext()) {

							System.err.println("PANIC: Strategy's Structure doesn't match the current Game!!!");

							return;

						}

						// Assigns the current Available Probability to the current
						myStrategy.put( availableMovesLabels.next(), availableMoveProbability );

					}

				}
				
				try {

					// Sets and provides the final Strategy
					this.provideStrategy(myStrategy);

					// Sets My Play as completed, as long I'm finished playing
					playComplete = true;

				}
				catch (InvalidStrategyException invalidStrategyException) {

					System.err.println("Invalid Strategy: " + invalidStrategyException.getMessage());
					invalidStrategyException.printStackTrace(System.err);

				}

			}

		}
		
	}

}