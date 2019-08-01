package a5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import a4.Heap;
import common.NotImplementedError;
import graph.Edge;
import graph.Node;
import graph.LabeledEdge;


/** We've provided depth-first search as an example; you need to implement Dijkstra's algorithm.
 */
public class GraphAlgorithms  {
	/** Return the Nodes reachable from start in depth-first-search order */
	public static <N extends Node<N,E>, E extends Edge<N,E>>
	List<N> dfs(N start) {
		
		Stack<N> worklist = new Stack<N>();
		worklist.add(start);
		
		Set<N>   visited  = new HashSet<N>();
		List<N>  result   = new ArrayList<N>();
		while (!worklist.isEmpty()) {
			// invariants:
			//    - everything in visited has a path from start to it
			//    - everything in worklist has a path from start to it
			//      that only traverses visited nodes
			//    - nothing in the worklist is visited
			N next = worklist.pop();
			visited.add(next);
			result.add(next);
			for (N neighbor : next.outgoing().keySet())
				if (!visited.contains(neighbor))
					worklist.add(neighbor);
		}
		return result;
	}
	
	/**
	 * Return a minimal path from start to end.  This method should return as
	 * soon as the shortest path to end is known; it should not continue to search
	 * the graph after that. 
	 * 
	 * @param <N> The type of nodes in the graph
	 * @param <E> The type of edges in the graph; the weights are given by e.label()
	 * @param start The node to search from
	 * @param end   The node to find
	 */
	public static <N extends Node<N,E>, E extends LabeledEdge<N,E,Integer>>
	List<N> shortestPath(N start, N end) {
		
		Heap<N,Integer> worklist = new Heap<N,Integer>(Collections.reverseOrder());
		worklist.add(start, 0);
		Map <N, Integer> visitedEdges = new HashMap<N, Integer>();
		Map <N, N> backPointers = new HashMap<N, N>();
		List<N>  result   = new ArrayList<N>();
		N trace = end;
		
		while (worklist.size() > 0) {
			visitedEdges.put(worklist.peek(), worklist.getPriority(worklist.peek()));
			N next = worklist.poll();
			
			if (next.equals(end)) {
				result.add(trace);
				
				while (backPointers.get(trace) != null) {
					result.add(0, backPointers.get(trace));
					trace = backPointers.get(trace);
				}
				
				if ((!start.equals(end)) && (!visitedEdges.containsKey(end))) {
					return new ArrayList<N>();
				}
				
				return result;
			}
			
			for (N neighbor : next.outgoing().keySet()) {
				int p = visitedEdges.get(next) + next.outgoing().get(neighbor).label();
				if (!visitedEdges.containsKey(neighbor)) {
					if (worklist.contains(neighbor)) {
						if (Collections.reverseOrder().compare(p, worklist.getPriority(neighbor)) > 0) {
							worklist.changePriority(neighbor, p);
							backPointers.put(neighbor, next);
							
						}
					} else {
						worklist.add(neighbor, p);
						backPointers.put(neighbor, next);
						
					}
				}
			}
			
		}
		
		return result;
		
	}
	
}
