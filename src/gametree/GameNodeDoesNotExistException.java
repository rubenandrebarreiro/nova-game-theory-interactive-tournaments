package gametree;

public class GameNodeDoesNotExistException extends Exception {

	/**
	 * This class signals that some Node that was attempted to be
	 * accessed in the game tree does not exists.
	 */
	
	public GameNodeDoesNotExistException() {
		super();
	}

	public GameNodeDoesNotExistException(String message) {
		super(message);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
