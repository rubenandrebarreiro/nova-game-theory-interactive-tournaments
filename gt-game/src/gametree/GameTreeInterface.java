package gametree;

import java.util.List;

public interface GameTreeInterface {

	/**
	 * Return the GameNode that represents the root of the current game tree.
	 * @return an instance of GameNode that is the root of the current tree.
	 */
	GameNode getRootNode();

	/**
	 * Returns the GameNode that represents the current node when using 
	 * the current instance to navigate the tree.
	 * @return an instance of GameNode that represents the current node.
	 */
	GameNode getCurrentNode();

	/**
	 * This method resets the tree ensuring that the current node becomes the 
	 * root node of the tree.
	 * Subsequent call to getCurrentNode() will return the same node as the
	 * one returned by method getRootNode().
	 */
	void reset();

	/**
	 * This method changes the current node to the ancestor of the current
	 * node if one exists.
	 * @return true if the current node had an ancestor, false otherwise.
	 */
	boolean goUp();

	/**
	 * This method reports the number of descendant nodes that the current 
	 * node has.
	 * @return number of descendants for the current node.
	 */
	int numberOfChildren();

	/**
	 * This method changes the current node to the descendant of the current
	 * node identified by label, if one exists.
	 * @param label the label of the target descendant node.
	 * @return true if the current node has an descendant identified by label,
	 * false otherwise.
	 */
	boolean goDown(String label);

	/**
	 * This method sets the current node to be the node in the game
	 * tree identified by an unique index.
	 * @param index an integer that unequivocally identifies a node
	 * in the game tree.
	 * @return true if the tree had a node identified by index, false 
	 * otherwise.
	 */
	boolean setCurrentNode(int index);

	/**
	 * This method returns an instance of GameNode in the game
	 * tree identified by an unique index.
	 * @param index an integer that unequivocally identifies a node
	 * in the game tree.
	 * @return the appropriate GameNode instance if the tree had a 
	 * node identified by index, null if the node does not exists. 
	 */
	GameNode getNodeByIndex(int index);

	/**
	 * This method returns an ordered list of Integers. In this
	 * list each position represents the number of different possible
	 * moves for each depth of the game. Position 0 represents 
	 * the number of moves possible from the root of the tree and so 
	 * forth. There is no integer in this list for terminal nodes.
	 * @return ordered List of Integer.
	 */
	List<Integer> getValidationSet();

}