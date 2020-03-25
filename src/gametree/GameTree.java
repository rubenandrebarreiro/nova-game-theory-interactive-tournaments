package gametree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import tree.GameTreeClass;
import tree.Node;

public class GameTree implements GameTreeInterface {
	
	private GameNode root;
	private GameNode current;
	
	private Map<Integer, GameNode> nodeIndex;
	private final List<Integer> validationSet;
	
	private GameTree(List<List<Integer>> validationSet) { 
		this.nodeIndex = new HashMap<Integer, GameNode>();
		this.validationSet = new ArrayList<Integer>();
		for(int i = 0; i < validationSet.size(); i++) {
			for(int j = 0; j < validationSet.get(i).size(); j++) 
				this.validationSet.add(validationSet.get(i).get(j));
		}
	}
	
	/* (non-Javadoc)
	 * @see gametree.GameTreeInterface#getRootNode()
	 */
	public GameNode getRootNode() {
		return this.root;
	}
	
	/* (non-Javadoc)
	 * @see gametree.GameTreeInterface#getCurrentNode()
	 */
	public GameNode getCurrentNode() {
		return this.current;
	}
	
	/* (non-Javadoc)
	 * @see gametree.GameTreeInterface#reset()
	 */
	public void reset() {
		this.current = root;
	}
	
	/* (non-Javadoc)
	 * @see gametree.GameTreeInterface#goUp()
	 */
	public boolean goUp() {
		GameNode temp = null;
		try {
			temp = this.current.getAncestor();
		} catch (GameNodeDoesNotExistException e) {
			return false;
		}
		this.current = temp;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see gametree.GameTreeInterface#numberOfChildren()
	 */
	public int numberOfChildren() {
		return this.current.numberOfChildren();
	}
	
	/* (non-Javadoc)
	 * @see gametree.GameTreeInterface#goDown(java.lang.String)
	 */
	public boolean goDown(String label) {
		GameNode temp = null;
		try {
			temp = this.current.getDescendant(label);
		} catch (GameNodeDoesNotExistException e) {
			return false;
		}
		this.current = temp;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see gametree.GameTreeInterface#setCurrentNode(int)
	 */
	public boolean setCurrentNode(int index) {
		if(this.nodeIndex.containsKey(index)) {
			this.current = this.nodeIndex.get(index);
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see gametree.GameTreeInterface#getNodeByIndex(int)
	 */
	public GameNode getNodeByIndex(int index) {
		if(this.nodeIndex.containsKey(index)) {
			return this.nodeIndex.get(index);
		}
		return null;
	}
	
	private void build(GameTreeClass gtc) {
		Queue<ProcessUnit> queue = new LinkedList<ProcessUnit>();
		
		Node root = gtc.getCurrent();
		this.root = new GameNode(root.getLabel(), 0 , root.getValue(), root.getType(), "");
		queue.add(new ProcessUnit(root, this.root));
		//System.out.println("Adding processing of node queue. ROOT");
		this.nodeIndex.put(root.getValue(), this.root);
		
		while(!queue.isEmpty()) {
			ProcessUnit pu = queue.remove();
			Node[] descendants = pu.node.getChildren();
			double[] descendantsWeight = pu.node.getChildrenWeight();
			String[] descendantsOutcome = pu.node.getOutcome();
			if(descendants.length == 0) { //Is a terminal node
				pu.n.setPayoffP1(Integer.parseInt(descendantsOutcome[0]));
				pu.n.setPayoffP2(Integer.parseInt(descendantsOutcome[1]));
			}
			for(int i = 0; i < descendants.length; i++) {
				//System.out.println("Adding processing of node queue. Outcome: " + descendantsOutcome[i] + " label: " + descendants[i].getLabel());
				GameNode child = new GameNode(pu.n, descendantsOutcome[i], descendantsWeight[i], descendants[i].getValue(), descendants[i].getType(), descendants[i].getLabel());
				pu.n.addChild(child);
				this.nodeIndex.put(child.getValue(), child);
				queue.add(new ProcessUnit(descendants[i], child));
			}
		}
		
	}
	
	
	public static GameTree fromGameTreeClass(GameTreeClass gtc) {
		GameTree t = new GameTree(gtc.getPosibilities());
		t.build(gtc);	
		return t;
	}

	/* (non-Javadoc)
	 * @see gametree.GameTreeInterface#getValidationSet()
	 */
	public List<Integer> getValidationSet() {
		return this.validationSet;
	}
	
	public class ProcessUnit {
		Node node;
		GameNode n;
		
		public ProcessUnit(Node node, GameNode n) {
			this.node = node;
			this.n = n;
		}
	}

}
