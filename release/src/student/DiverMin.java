package student;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import a5.GraphAlgorithms;
import game.FindState;
import game.FleeState;
import game.NodeStatus;
import game.SewerDiver;
import graph.LabeledEdge;
import game.Node;

import common.NotImplementedError;

public class DiverMin implements SewerDiver {
	
	

	/** Get to the ring in as few steps as possible. Once you get there, <br>
	 * you must return from this function in order to pick<br>
	 * it up. If you continue to move after finding the ring rather <br>
	 * than returning, it will not count.<br>
	 * If you return from this function while not standing on top of the ring, <br>
	 * it will count as a failure.
	 *
	 * There is no limit to how many steps you can take, but you will receive<br>
	 * a score bonus multiplier for finding the ring in fewer steps.
	 *
	 * At every step, you know only your current tile's ID and the ID of all<br>
	 * open neighbor tiles, as well as the distance to the ring at each of <br>
	 * these tiles (ignoring walls and obstacles).
	 *
	 * In order to get information about the current state, use functions<br>
	 * currentLocation(), neighbors(), and distanceToRing() in state.<br>
	 * You know you are standing on the ring when distanceToRing() is 0.
	 *
	 * Use function moveTo(long id) in state to move to a neighboring<br>
	 * tile by its ID. Doing this will change state to reflect your new position.
	 *
	 * A suggested first implementation that will always find the ring, but <br>
	 * likely won't receive a large bonus multiplier, is a depth-first walk. <br>
	 * Some modification is necessary to make the search better, in general. */
	@Override
	public void find(FindState state) {
		List<Long> visited = new ArrayList<Long>();
		findRing(state, visited);
			
	}
	
	public static boolean findRing(FindState state, List<Long> visited) {
		if (state.distanceToRing() == 0) {
			return true;
		} else {
			Long now = state.currentLocation();
			visited.add(now);
			for (NodeStatus n: state.neighbors()) {
				if (!(visited.contains(n.getId()))) {
					int nNow = n.getDistanceToTarget();
					NodeStatus nodeNow = n;
					for (NodeStatus n2: state.neighbors()) {
						if ((n2.getDistanceToTarget() < nNow) && (!(visited.contains(n2.getId())))) {
							nNow = n2.getDistanceToTarget();
							nodeNow = n2;
						}
					}
					if ((!(visited.contains(nodeNow.getId())))) {
						state.moveTo(nodeNow.getId());
						if (state.distanceToRing() != 0) {
							findRing(state, visited);
						}
						if (state.distanceToRing() != 0) {
							state.moveTo(now);
						}
						if (state.distanceToRing() == 0) {
							return true;
						}
					}	
				}
			} 
			return false;
		}
	}
	
	/** This is a previous implementation of find (to be called as a helper method), 
	 * which is less efficient. It uses depth-first search to find the ring.
	 * 
	 * @param state
	 * @param visited
	 * @return
	 */
	public static boolean dfsWalk(FindState state, List<Long> visited) {
		if (state.distanceToRing() == 0) {
			return true;
		} else {
			Long now = state.currentLocation();
			visited.add(now);
			for (NodeStatus n: state.neighbors()) {
					if (!(visited.contains(n.getId()))) {
						state.moveTo(n.getId());
						if (state.distanceToRing() != 0) {
							dfsWalk(state, visited);
							if (state.distanceToRing() != 0) {
								state.moveTo(now);
							}
							if (state.distanceToRing() == 0) {
								return true;
							}
						}
					}
					if (state.distanceToRing() == 0) {
						return true;
					}
		
			}
			return false;
		}
	}
		
	
	/** Flee the sewer system before the steps are all used, trying to <br>
	 * collect as many coins as possible along the way. Your solution must ALWAYS <br>
	 * get out before the steps are all used, and this should be prioritized above<br>
	 * collecting coins.
	 *
	 * You now have access to the entire underlying graph, which can be accessed<br>
	 * through FleeState. currentNode() and getExit() will return Node objects<br>
	 * of interest, and getNodes() will return a collection of all nodes on the graph.
	 *
	 * You have to get out of the sewer system in the number of steps given by<br>
	 * getStepsRemaining(); for each move along an edge, this number is <br>
	 * decremented by the weight of the edge taken.
	 *
	 * Use moveTo(n) to move to a node n that is adjacent to the current node.<br>
	 * When n is moved-to, coins on node n are automatically picked up.
	 *
	 * You must return from this function while standing at the exit. Failing <br>
	 * to do so before steps run out or returning from the wrong node will be<br>
	 * considered a failed run.
	 *
	 * Initially, there are enough steps to get from the starting point to the<br>
	 * exit using the shortest path, although this will not collect many coins.<br>
	 * For this reason, a good starting solution is to use the shortest path to<br>
	 * the exit. */

	/*public void flee(FleeState state) {
		List<Node> sPath = a5.GraphAlgorithms.shortestPath(state.currentNode(), state.getExit());
		boolean flag =false; 
		for (Node n: sPath) {
			if (flag == true ) {
				state.moveTo(n);
			} else {
				flag = true;
			}
		}
	}
	*/
	
	@Override
	public void flee(FleeState state) {
		//List<Node> dfsPath = a5.GraphAlgorithms.dfs(state.currentNode());
		//int counter = 1;
		List <Node> visited = new ArrayList<Node> ();
		dfs2(state, visited);
		
	}
	
	/*
	public void bestPath(FleeState state, List<Node> dfs, int counter) {
		int sPathSize = a5.GraphAlgorithms.shortestPath(state.currentNode(), state.getExit()).size();
		Node n = dfs.get(counter);
		if ( (state.stepsLeft() > sPathSize) && (counter < dfs.size())) {
			state.moveTo(n);
			bestPath(state, dfs, counter + 1);
			//sPathSize = a5.GraphAlgorithms.shortestPath(state.currentNode(), state.getExit()).size();
		} 
		
	}
	*/
	
	public static void dfs2(FleeState state, List<Node> visited) {
		int sPathSize = a5.GraphAlgorithms.shortestPath(state.currentNode(), state.getExit()).size();
		Node now = state.currentNode();
		visited.add(now);
		while (state.stepsLeft() > sPathSize) {
			for (Node n: now.getNeighbors()) {
					if (!(visited.contains(n))) {
						state.moveTo(n);
						sPathSize = a5.GraphAlgorithms.shortestPath(state.currentNode(), state.getExit()).size();
						if (!state.currentNode().equals(state.getExit())) {
							dfs2(state, visited);
							if (!state.currentNode().equals(state.getExit())) {
								state.moveTo(now);
								sPathSize = a5.GraphAlgorithms.shortestPath(state.currentNode(), state.getExit()).size();
							}
						}
						
					}
			}
		
			List<Node> sPath = a5.GraphAlgorithms.shortestPath(state.currentNode(), state.getExit());
			boolean flag =false; 
			for (Node n: sPath) {
				if (flag == true ) {
					state.moveTo(n);
				} else {
					flag = true;
				}
			}
		}
	}
}