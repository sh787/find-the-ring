package ourA4;

import static org.junit.jupiter.api.Assertions.*;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

import junit.framework.AssertionFailedError;

public class Heap<E, P> implements PriorityQueue<E, P>{
	
	//store the data of the heap in an ArrayList
	private ArrayList<E> data;
	
	//the comparator for the priority of the elements
	private Comparator<P> comparator;
	
	//the HashMap storing the location of each element
	private HashMap<E, Integer> locationMap;
	
	//the HashMap storing the priority of the element at given index
	private HashMap<Integer, P> priorityMap;

	public Heap(Comparator<P> comparator) {
		this.comparator = comparator;
		this.data = new ArrayList<E>();
		locationMap = new HashMap<E, Integer>();
		priorityMap = new HashMap<Integer, P>();
	}
	
	/**
	 * Added this method to Group 5's Heap implementation. 
	 * Returns whether the Heap contains Element e.
	 */
	public boolean contains(E e) {
		return locationMap.containsKey(e);
	}
	
	/**
	 * Added this method to Group 5's Heap implementation. 
	 * Returns the Priority of Element e.
	 */
	public P getPriority (E e) {
		if(!(this.data.contains(e)))
			throw new NoSuchElementException("Heap does not contain e.");
		return priorityMap.get(locationMap.get(e));
	}
	
	/** Added this method to Group 5's Heap implementation.
	 * Returns string of Heap in the format of (Element, Priority)
	 */
	public String toString() {
		String prt = "";
		for (E e: data) {
			prt += "(" + e + ", " + priorityMap.get(locationMap.get(e)) + ")";
		}
		
		return prt;
	}
	
	@Override
	/**
	 * @return the comparator of the PriorityHeap
	 */
	public Comparator<P> comparator() {
		return comparator;
	}

	/**
	 * @return the size of the heap
	 */
	@Override
	public int size() {
		return data.size();
	}
	
	/** remove and return the largest element of this.
	 * @throws NoSuchElementException if this is empty.
	 */
	@Override
	public E poll() throws NoSuchElementException {
		E maxEle = peek();
		swap(0, size() - 1);
		data.remove(size() - 1);
		locationMap.remove(maxEle);
		priorityMap.remove(size());
		bubbleDown(0);
		return maxEle;
	}
	
	/**
	 * return the largest element of this, according to comparator().
	 * @throws NoSuchElementException if this is empty.
	 */
	@Override
	public E peek() throws NoSuchElementException {
		if(size() == 0)
			throw new NoSuchElementException();
		
		return data.get(0);
	}
	
	@Override
	public void add(E e, P p) throws IllegalArgumentException {
		//throw if e is already contained.
		if(locationMap.containsKey(e))
			throw new IllegalArgumentException();

		//int index = data.size();
		data.add(e);
		locationMap.put(e, data.indexOf(e));
		priorityMap.put(data.indexOf(e), p);
		bubbleUp(data.indexOf(e));
	}
	
	/**
	 * Note: Changed this method when debugging the Heap.
	 * 
	 * Change the priority associated with e to p.
	 *
	 * @throws NoSuchElementException if this does not contain e.
	 */
	@Override
	public void changePriority(E e, P p) throws NoSuchElementException {
		if(!(this.data.contains(e)))
			throw new NoSuchElementException("ArrayList does not contain e.");
		int index = locationMap.get(e);
		//P prevPrio = priorityMap.get(index);
		priorityMap.replace(index, p);
		//if(comparator.compare(prevPrio, p)>0) {
			bubbleUp(index);
		//}
		//else if(comparator.compare(prevPrio, p)<0) {
			bubbleDown(index);
		//}
		
	}
	
	/**
	 * make sure that the element at the given index satisfies the heap invariant
	 * and check whether the element can rise to a lower level
	 * @param index
	 */
	private void bubbleDown(int index) {
		while(left(index) != -1 || right(index) != -1) {
			int indexLeft = left(index);
			int indexRight = right(index);
			P curEle = priorityMap.get(index);
			P eleLeft = priorityMap.get(indexLeft);
			P eleRight = priorityMap.get(indexRight);
			if((eleLeft != null && comparator.compare(curEle, eleLeft) < 0)
					|| (eleRight != null && comparator.compare(curEle, eleRight) < 0)) {
				if(eleLeft != null && (eleRight == null || comparator.compare(eleLeft, eleRight) > 0)) {
					swap(index, indexLeft);
					index = indexLeft;
				}
				else {
					swap(index, indexRight);
					index = indexRight;
				}
			}
			else {
				break;
			}
		}
	}
	
	/**
	 * Note: Changed this method to debug Heap priority ordering.
	 * 
	 * make sure that the element at the given index satisfies the heap invariant
	 * and check whether the element can rise to an upper level
	 * @param index
	 */
	private void bubbleUp(int index) {
	
		if (parent(index)!= -1) {
			if(comparator().compare(priorityMap.get(index), priorityMap.get(parent(index))) > 0) {
				swap(index, parent(index));
				bubbleUp(parent(index));
			}
		}
	}
	
	/** returns depth of item at index i
	 * 
	 * @param i < data.size();
	 * @return integer
	 * @throws IndexOutOfBoundsException
	 */
	private int depth(int index) throws IndexOutOfBoundsException{
		if(index > size())
			throw new IndexOutOfBoundsException();
		
		return Integer.toBinaryString(index + 1).length();
	}
	
	/**
	 * swap the elements at given indexes
	 * @param a the index of the first element
	 * @param b the index of the other element
	 */
	private void swap(int a, int b) {
		E tempA = data.get(a);
		E tempB = data.get(b);
		P tempAPrior = priorityMap.get(a);
		P tempBPrior = priorityMap.get(b);
		//System.out.println("swap " + tempA + " with " + data.get(b));
		priorityMap.replace(a, tempBPrior);
		priorityMap.replace(b, tempAPrior);
		locationMap.replace(tempA, b);
		locationMap.replace(tempB, a);
		data.set(a, tempB);
		data.set(b, tempA);
	}
	
	/** returns number of element at depth of item at index i
	 * 
	 * @param i < data.size()
	 * @return -1 if the index is invalid
	 */
	private int noOfElements(int index){
		return (int)(Math.pow(2, depth(index) - 1));
	}
	
	/** returns index of left child of item at index i
	 * 
	 * @param i < data.size()
	 * @return -1 if the index is invalid
	 */
	private int left(int index){
		if(index < 0 || index >= size())
			return -1;
		
		int temp = 2 * index + 1;
		return (temp < size()) ? temp : -1;
	}
	
	/** 
	 * Note: Changed this method's structure when debugging the Heap.
	 * 
	 * returns index of right child of item at index i
	 * 
	 * @param i < data.size()
	 * @return -1 if the index is invalid
	 */ 
	private int right(int index){
		/*int temp = left(index);
		if(temp == -1)
			return temp;
		
		return (temp + 1 < size()) ? temp + 1 : -1;*/
		if(index < 0 || index >= size())
			return -1;
		int temp = 2 * index + 2;
		return (temp < size()) ? temp : -1;
	}
	
	/** 
	 * Note: Changed this method's structure when debugging the Heap.
	 * 
	 * returns the index of the parent of item at index i
	 * 
	 * @param i < data.size()
	 * @return integer
	 */
	private int parent(int index) {
		/*if(index >= 0 && index < size())
			return (index - 1) / 2;
		else
			return -1;*/
		if (index%2 != 0) {
			if ((index - 1)/2 >= 0) {
				return (index - 1)/2;
			} else return -1;
		} else if (index%2 == 0) {
			if ((index - 2)/2 >= 0) {
				return (index - 2)/2;
			} else return -1;
		} else return -1;
	}
}