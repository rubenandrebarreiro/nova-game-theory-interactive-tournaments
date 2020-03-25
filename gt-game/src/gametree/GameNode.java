package gametree;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GameNode implements GameNodeInterface {

	private GameNode ancestor;
	private String label;
	private Map<String,GameNode> children;
	private double weight;
	private int value;
	private String type;
	private String outcome;
	private int terminalOutcomeP1;
	private int terminalOutcomeP2;
	
	public GameNode(String label, double weight, int value, String type, String outcome) {
		this.ancestor = null;
		this.label = label;
		this.weight = weight;
		this.value = value;
		this.type = type;
		this.outcome = outcome;
		this.children = new HashMap<String,GameNode>();
		this.terminalOutcomeP1 = 0;
		this.terminalOutcomeP2 = 0;
	}
	
	public GameNode(GameNode ancestor, String label, double weight, int value, String type, String outcome) {
		this.ancestor = ancestor;
		this.label = label;
		this.weight = weight;
		this.value = value;
		this.type = type;
		this.outcome = outcome;
		this.children = new HashMap<String,GameNode>();
		this.terminalOutcomeP1 = 0;
		this.terminalOutcomeP2 = 0;
	}
	
	/* (non-Javadoc)
	 * @see gametree.GameNodeInterface#isRoot()
	 */
	public boolean isRoot() {
		return this.ancestor == null;
	}
	
	/* (non-Javadoc)
	 * @see gametree.GameNodeInterface#isTerminal()
	 */
	public boolean isTerminal() {
		return this.type.compareToIgnoreCase("t") == 0;
	}
	
	/* (non-Javadoc)
	 * @see gametree.GameNodeInterface#isNature()
	 */
	public boolean isNature() {
		return this.type.compareToIgnoreCase("c") == 0;
	}
	
	/* (non-Javadoc)
	 * @see gametree.GameNodeInterface#isPlayer1()
	 */
	public boolean isPlayer1() {
		return this.type.compareToIgnoreCase("P1") == 0;
	}
	
	/* (non-Javadoc)
	 * @see gametree.GameNodeInterface#isPlayer2()
	 */
	public boolean isPlayer2() {
		return this.type.compareToIgnoreCase("P2") == 0;
	}
	
	/* (non-Javadoc)
	 * @see gametree.GameNodeInterface#getOutcome()
	 */
	public String getOutcome() {
		return this.outcome;
	}
	
	/* (non-Javadoc)
	 * @see gametree.GameNodeInterface#getAncestor()
	 */
	public GameNode getAncestor() throws GameNodeDoesNotExistException {
		if(ancestor != null)
			return this.ancestor;
		else 
			throw new GameNodeDoesNotExistException("Root node does not have an ancestor");
	}
	
	/* (non-Javadoc)
	 * @see gametree.GameNodeInterface#getDescendant(java.lang.String)
	 */
	public GameNode getDescendant(String label) throws GameNodeDoesNotExistException {
		if(this.children.containsKey(label)) 
			return this.children.get(label);
		else
			if(this.children.keySet().size() > 0)
				throw new GameNodeDoesNotExistException("Node with label '" + this.label + "' does not have a descendeant with label + '" + label + "'.");
			else 
				throw new GameNodeDoesNotExistException("node with label '" + this.label + "' is terminal.");
	}
	
	/* (non-Javadoc)
	 * @see gametree.GameNodeInterface#getChildrenLabels()
	 */
	public Iterator<String> getChildrenLabels() {
		return this.children.keySet().iterator();
	}
	
	/* (non-Javadoc)
	 * @see gametree.GameNodeInterface#getChildren()
	 */
	public Iterator<GameNode> getChildren() {
		return this.children.values().iterator();
	}

	/* (non-Javadoc)
	 * @see gametree.GameNodeInterface#numberOfChildren()
	 */
	public int numberOfChildren() {
		return this.children.size();
	}

	/* (non-Javadoc)
	 * @see gametree.GameNodeInterface#addChild(gametree.GameNode)
	 */
	public void addChild(GameNode child) {
		this.children.put(child.getLabel(), child);
	}

	/* (non-Javadoc)
	 * @see gametree.GameNodeInterface#getLabel()
	 */
	public String getLabel() {
		return label;
	}

	/* (non-Javadoc)
	 * @see gametree.GameNodeInterface#getWeight()
	 */
	public double getWeight() {
		return weight;
	}

	/* (non-Javadoc)
	 * @see gametree.GameNodeInterface#getValue()
	 */
	public int getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see gametree.GameNodeInterface#toString()
	 */
	public String toString() {
		return this.label + " (" + value + ")" ;
	}

	public void setPayoffP1(int payoff) {
		this.terminalOutcomeP1 = payoff;
	}
	
	public void setPayoffP2(int payoff) {
		this.terminalOutcomeP2 = payoff;
	}
	
	@Override
	public int getPayoffP1() {
		return terminalOutcomeP1;
	}

	@Override
	public int getPayoffP2() {
		return terminalOutcomeP2;
	}
	
	
	
	
	
	
}
