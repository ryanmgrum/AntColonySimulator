package dataStructures;

/**
 *	class PriorityQueue
 *
 *	Implementation of a priority queue.  Items are ordered according to their
 *	assigned priorities.  The next item that can be removed or retrieved is
 *	always the item with the highest priority.
 *
 *	Written by Roger West, University of Illinois at Springfield
 */
public class PriorityQueue implements OrderedCollection
{
	
	/************
	 *	constants
	 ***********/
	
	/** indicates item priorities are in ascending order */
	public final static int PRIORITY_ASCENDING = 0;
	
	/** indicates item priorities are in descending order */
	public final static int PRIORITY_DESCENDING = 1;
	
	
	/*************
	 *	attributes
	 ************/
	
	/** BinaryHeap used as underlying structure */
	private BinaryHeap theQueue;
	 
	 
	/**************
	 *	constructors
	 *************/
	 
	/**
	 *	create a new, empty PriorityQueue
	 *
	 *	By default, item priorities are in descending order
	 */
	public PriorityQueue()
	{
		// to order items in descending priority order, use max heap as
		// underlying implementation
		theQueue = new BinaryMaxHeap();
	}
	
	
	/**
	 *	create a new, empty PriorityQueue with the specified order for item
	 *	priorities
	 *
	 *	@param priorityOrder		either PriorityQueue.ASCENDING or
	 *									   PriorityQueue.DESCENDING
	 */
	public PriorityQueue(int priorityOrder)
	{
		if (priorityOrder == PRIORITY_ASCENDING)
		{
			// priorities will be in ascending order
			theQueue = new BinaryMinHeap();
		}
		else
		{
			//  priorities will be in descending order
			theQueue = new BinaryMaxHeap();
		}
	}
	
	
	/**********
	 *	methods
	 *********/
	
	
	
	/*****************************************************
	 *	methods inherited from interface OrderedCollection
	 ****************************************************/
	
	/**
	 *	add the specified item to this PriorityQueue
	 *	
	 *	The item will be inserted into the proper position based on its priority
	 *	relative to the priorities of the existing items.
	 *
	 *	returns true if the add was successful, false otherwise
	 */
	public boolean add(Comparable obj)
	{
		return theQueue.add(obj);
	}


	/**
	 *	remove the item from this PriorityQueue that has the highest priority
	 *
	 *	If two or more items have the same priority, no guarantee is made as to
	 *	which item will be removed.
	 *
	 *	returns true if the remove was successful, false otherwise
	 */
	public boolean remove()
	{
		return theQueue.remove();
	}


	/**
	 *	empty this PriorityQueue
	 */
	public void clear()
	{
		theQueue.clear();
	}


	/**
	 *	return the number of items in this PriorityQueue
	 */
	public int size()
	{
		return theQueue.size();
	}


	/**
	 *	return true if this PriorityQueue contains 0 items, or false if this
	 *	PriorityQueue contains at least 1 item
	 */
	public boolean isEmpty()
	{
		return theQueue.isEmpty();
	}


	/**
	 *	return the item from this PriorityQueue that has the highest priority
	 *	
	 *	If two or more items have the same priority, no guarantee is made as to
	 *	which item will be returned.
	 */
	public Comparable get()
	{
		return theQueue.get();
	}
	
	
	/**************************************
	 *	methods inherited from class Object
	 *************************************/
	
	/**
	 *	return a String representation of this PriorityQueue
	 */
	public String toString()
	{
		return theQueue.toString();
	}
}