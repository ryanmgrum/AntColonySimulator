package dataStructures;

/**
 *	class BinaryMinHeap
 *
 *	Implementation of a binary min heap.  Access to items is restricted to the
 *	item with the minimum value.  Whenever an item is added or removed, it is
 *	guaranteed that upon completion of the operation the items will be ordered
 *	such that the next item that can be retrieved or removed will be the item
 *	that has the minimum value.  Comparisons are done via the compareTo method.
 *
 *	Items are positioned such that when the heap is represented as a complete
 *	binary tree, for any given node, X, in the tree:
 *		1)	the item in X is <= the item in X's left child
 *		2)	the item in X is <= the item in X's right child
 *
 *	When the heap is represented as an array, A, for any item in array index, i,
 *	where i != 0:
 *		1)	the item in A[i] is <= the item in A[i * 2]
 *		2)	the item in A[i] is <= the item in A[i * 2 + 1]
 *
 *	Written by Roger West, University of Illinois at Springfield
 *	Adapted from code written by Mark Weiss in Data Structures and Problem
 *	Solving Using Java, 2nd edition, 2002
 */
public class BinaryMinHeap extends BinaryHeap
{
	
	/*************
	 *	constants
	 ************/
	
	/** position of min item */
	private final static int MIN_ITEM = 1;
	
	
	/**************
	 *	constructors
	 *************/
	 
	/**
	 *	create a new, empty BinaryMinHeap
	 */
	public BinaryMinHeap()
	{
		// invoke constructor of superclass, BinaryHeap
		super();
	}
	
	
	/**********
	 *	methods
	 *********/
	
	/**
	 *	called following removal of an item to ensure ordering of remaining
	 *	items is consistent with min heap rules
	 *
	 *	beginning at the specified index, shift item at index down to its
	 *	proper position in the heap
	 */
	private void downShift(int index)
	{
		// index of "child", which will be either index * 2 or index * 2 + 1
		int childIndex;
		
		// temp storage for item at index where shifting begins
		Comparable temp = theItems[index];
		
		// shift items, as needed
		while (index * 2 <= theSize)
		{
			// set childIndex to "left" child
			childIndex = index * 2;
			
			// move to "right" child if "right" child < "left" child
			if (childIndex != theSize && theItems[childIndex + 1].compareTo(theItems[childIndex]) < 0)
				childIndex++;
			
			if (theItems[childIndex].compareTo(temp) < 0)
			{
				// shift "child" down if child < temp
				theItems[index] = theItems[childIndex];
			}
			else
			{
				// shifting complete
				break;
			}
			
			// increment index
			index = childIndex;
		}
		
		// position item that was originally at index where shifting began
		theItems[index] = temp;
	}
	
	
	/******************************************
	 *	methods inherited from class BinaryHeap
	 *****************************************/
	
	/**
	 *	add the specified item to this BinaryMinHeap
	 *	
	 *	The item will be inserted into the proper position to ensure that the
	 *	rules for a min heap are maintained.
	 *
	 *	returns true if the add was successful, false otherwise
	 */
	public boolean add(Comparable obj)
	{
		// resize array, if necessary
		if (theSize + 1 == theItems.length)
			doubleArray();
		
		// increment size, and position index at bottom of heap
		int index = ++theSize;
		
		// temporarily store new item at index 0 (not normally accessed by any
		// of the standard operations)
		theItems[0] = obj;
		
		// begin shifting items down, as needed, starting at bottom of heap
		while (obj.compareTo(theItems[index / 2]) < 0)
		{
			// new item < item at current position, so shift current item down
			theItems[index] = theItems[index / 2];
			index /= 2;
		}
		
		// position new item
		theItems[index] = obj;
		
		// add successful
		return true;
	}


	/**
	 *	remove the item from this BinaryMinHeap that has the minimum value
	 *
	 *	Following the removal, it is guaranteed the rules for a min heap are
	 *	maintained.
	 *
	 *	If two or more items have the same value, no guarantee is made as to
	 *	which item will be removed.
	 *
	 *	returns true if the remove was successful, false otherwise
	 */
	public boolean remove()
	{
		// return false if this BinaryMinHeap is empty
		if (isEmpty())
			return false;
		
		// logically remove min item and decrement size
		theItems[MIN_ITEM] = theItems[theSize--];
		
		// shift items down as needed
		downShift(1);	
		
		// remove successful
		return true;
	}
	
	
	/**************************************
	 *	methods inherited from class Object
	 *************************************/
	
	/**
	 *	return a String representation of this BinaryMinHeap
	 */
	public String toString()
	{
		String result = "";
		
		for (int i = MIN_ITEM; i <= theSize; i++)
		{
			if (i == theSize)
				result += theItems[i].toString();
			else
				result += theItems[i].toString() + " ";
		}
			
		return result;
	}
}