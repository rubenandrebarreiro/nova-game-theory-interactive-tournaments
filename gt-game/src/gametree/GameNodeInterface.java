package gametree;

import java.util.Iterator;

public interface GameNodeInterface {

	/**
	 * This method is used to check if the current node is the root of the tree.
	 * @return true if the node is root, false otherwise.
	 */
	boolean isRoot();

	/**
	 * This method is used to check if the current node is a terminal node of
	 * the game tree.
	 * @return true if this is a terminal node, false otherwise.
	 */
	boolean isTerminal();

	/**
	 * This method is used to check if the current node is a nature node.
	 * @return true if the current node is nature, false otherwise.
	 */
	boolean isNature();

	/**
	 * This method is used to check if this node belong to player1, meaning
	 * that it represents a choice for the first player playing the game.
	 * Note that the labels of the descendants of this node should be used
	 * by player2.
	 * @return true if the node belong to player1, false otherwise
	 */
	boolean isPlayer1();

	/**
	 * This method is used to check if this node belong to player2, meaning
	 * that it represents a choice for the second player playing the game.
	 * Note that the labels of the descendants of this node should be used
	 * by player1.
	 * @return true if the node belong to player2, false otherwise
	 */
	boolean isPlayer2();

	/**
	 * If this is a terminal node, this returns a String representation of
	 * the outcome represented by this node. If the node is not terminal
	 * the behavior of this method is not specified.
	 * @return String representation of the outcome represented by this node (if the node is terminal).
	 */
	String getOutcome();

	/**
	 * This method returns the ancestor of the current node if one exists
	 * and generates an exception when executed over a node without an 
	 * ancestor (the root node).
	 * @return the GameNode that is directly above the current one in the tree
	 * @throws GameNodeDoesNotExistException if the current node is the root
	 * of a game tree.
	 */
	GameNode getAncestor() throws GameNodeDoesNotExistException;

	/**
	 * This method returns a descendant of the current node identified by
	 * label if that node exists.
	 * @param label The label of the target descendant node.
	 * @return an instance of GameNode representing the descendant node if it exists
	 * @throws GameNodeDoesNotExistException if the requested node is not 
	 * a descendant of the current node or does not exists.
	 */
	GameNode getDescendant(String label) throws GameNodeDoesNotExistException;

	/**
	 * This method exposes an iterator over the labels of all direct descendants of
	 * the current node.
	 * @return an Iterator over String, that contains the labels of direct 
	 * descendant nodes.
	 */
	Iterator<String> getChildrenLabels();

	/**
	 * This method exposes an iterator over all direct descendants of
	 * the current node.
	 * @return an Iterator over GameNode, that contains the direct 
	 * descendant nodes.
	 */
	Iterator<GameNode> getChildren();

	/**
	 * This method returns the number of direct descendant nodes of the current
	 * node
	 * @return number of direct descendants
	 */
	int numberOfChildren();

	/**
	 * Returns the label of the edge that allows to navigate from the 
	 * ancestor node to the current one.
	 * @return a String containing the label of the direct edge that allows
	 * to reach the current node in the tree.
	 */
	String getLabel();

	/**
	 * If the ancestor to the current node is nature, this represents
	 * the probability of reaching the current node.
	 * @return the probability of reaching the current node if the ancestor
	 * is nature. The return value is unspecified for nodes whose ancestor
	 * is not nature.
	 */
	double getWeight();

	/**
	 * Returns a string representation of the current node.
	 * @return String with a non-unique String representation of
	 * the current node.
	 */
	String toString();
	
	/**
	 * Returns the payoff of a terminal node for player 1.
	 * If the node is not terminal returns zero.
	 * @return payoff
	 */
	int getPayoffP1();
	
	/**
	 * Returns the payoff of a terminal node for player 2.
	 * If the node is not terminal returns zero.
	 * @return payoff
	 */
	int getPayoffP2();

}