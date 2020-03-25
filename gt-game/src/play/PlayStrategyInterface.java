package play;

import java.util.Iterator;

import gametree.GameTree;

/**
 * This class represents a game strategy for a single round.
 * It is used to pass information between the game engine and 
 * the strategy developed to play that game.
 * 
 */
public interface PlayStrategyInterface {

	/**
	 * Obtains the approximate amount of milliseconds until 
	 * the timeout of the current round.
	 * @return the approximate amount of milliseconds
	 * until the player has to provide the next play. Notice that
	 * consulting this method will make you lose some time, and also
	 * that the strategy must be delivered in time so that it can
	 * be transmitted to the server within this bound (which takes
	 * at most a few hundreds of milliseconds). 
	 */
	public long timeToExpire();
	
	/**
	 * Returns the probability to continue to the next
	 * iteration (assuming next iteration is below @getMaximumNumberOfIterations()).
	 * @return the probability to continue
	 */
	public double probabilityForNextIteration();
	
	/**
	 * Returns the maximum number of iterations of the
	 * current game.
	 * @return the maximum number of iterations
	 */
	public int getMaximumNumberOfIterations();

	/**
	 * Reports if the current round is the first round of a game.
	 * @return true if the current round is the first round, false
	 * otherwise.
	 */
	public boolean isFirstRound();

	/**
	 * This method should be executed to notify the engine of the
	 * game that the player is quitting the game (this will lead
	 * the player to lose the game). After executing this method
	 * the instance of PlayStraegy must be delivered to the game
	 * engine.
	 */
	public void quitGame();

	/**
	 * Updates the probability value associated with following the
	 * game branch associated with the information set denoted by
	 * the @key to @value.
	 * @param key The label for the information set of the node.
	 * @param value Probability, must be a value between 0 and 1.
	 * @return
	 */
	public Double put(String key, Double value);

	/**
	 * Returns an ordered iterator that enables iteration over
	 * the labels for the information sets associated with the
	 * current game.
	 * @return an iterator of type String with the labels
	 * associated with the information sets of this game.
	 */
	public Iterator<String> keyIterator();

	/**
	 * Exposes the (unique) index of the terminal node in the 
	 * game tree where the latest round of the game terminated
	 * when this player was acting as player 1 (i.e, challenger)
	 * @return unique positive index of the terminal node, -1
	 * if there was no round in the game before.
	 */
	public int getFinalP1Node();

	/**
	 * Exposes the (unique) index of the terminal node in the 
	 * game tree where the latest round of the game terminated
	 * when this player was acting as player 2 (i.e, challenged)
	 * @return unique positive index of the terminal node, -1
	 * if there was no round in the game before.
	 */
	public int getFinalP2Node();
	
	/**
	 * Exposes the score of the current player in the latest round
	 * when playing as P1. The return of this method in the first
	 * round is not specified.
	 * @param t the GameTree for the current game
	 * @return current player current score in the previous round
	 * as player 1.
	 */
	public int getLastRoundSelfScoreAsP1(GameTree t);
	
	/**
	 * Exposes the score of the current player in the latest round
	 * when playing as P2. The return of this method in the first
	 * round is not specified.\
	 * @param t the GameTree for the current game
	 * @return current player current score in the previous round
	 * as player 2.
	 */
	public int getLastRoundSelfScoreAsP2(GameTree t);
	
	/**
	 * Exposes the score of the opponent in the latest round
	 * when the current player was playing as P1. The return 
	 * of this method in the first round is not specified.
	 * @param t the GameTree for the current game
	 * @return opponent score in the previous round when the
	 * the current player was playing as player 1.
	 */
	public int getLastRoundOpponentScoreAsP1(GameTree t);
	
	/**
	 * Exposes the score of the opponent in the latest round
	 * when the current player was playing as P2. The return 
	 * of this method in the first round is not specified.
	 * @param t the GameTree for the current game
	 * @return opponent score in the previous round when the
	 * the current player was playing as player 2.
	 */
	public int getLastRoundOpponentScoreAsP2(GameTree t);

}