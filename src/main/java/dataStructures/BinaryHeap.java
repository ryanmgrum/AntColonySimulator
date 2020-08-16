package dataStructures;

/**
 *	class BinaryHeap
 *
 *	Abstraction of a binary heap.
 *
 *	Written by Roger West, University of Illinois at Springfield
 */
public abstract class BinaryHeap implements OrderedCollection
{
	
	/*************
	 *	constants
	 ************/
	
	/** default capacity for underlying array */
	protected final static int DEFAULT_CAPACITY = 10;
	
	/** position of next item that can be retrieved */
	protected final static int NEXT_ITEM = 1;
	
	
	/*************
	 *	attributes
	 ************/
	
	/** array for storing items */
	protected Comparable[] theItems;
	
	/** current number of items in heap */
	protected int theSize;
	 
	 
	/**************
	 *	constructors
	 *************/
	 
	/**
	 *	create a new, empty BinaryHeap
	 */
	public BinaryHeap()
	{
		// empty this BinaryHeap
		clear();
	}
	
	
	/**********
	 *	methods
	 *********/
	
	/**
	 *	double the size of the array
	 */
	protected void doubleArray()
	{
		// backup original array
		Comparable[] old = theItems;
		
		// create new array
		theItems = new Comparable[theItems.length * 2 + 1];
		
		// copy items from old array to new array
		for(int i = NEXT_ITEM; i <= theSize; i++)
			theItems[i] = old[i];
	}
	
	
	/*****************************************************
	 *	methods inherited from interface OrderedCollection
	 ****************************************************/
	
	/**
	 *	add the specified item to this BinaryHeap
	 *	
	 *	The item will be inserted into the proper position depending on whether
	 *	this heap is a min heap or a max heap.
	 *
	 *	returns true if the add was successful, false otherwise
	 */
	public abstract boolean add(Comparable obj);


	/**
	 *	remove the item from this BinaryHeap that has either the minimum or
	 *	maximum value, depending on whether this heap is a min heap or max heap
	 *
	 *	If two or more items have the same value, no guarantee is made as to
	 *	which item will be removed.
	 *
	 *	returns true if the remove was successful, false otherwise
	 */
	public abstract boolean remove();


	/**
	 *	empty this BinaryHeap
	 */
	public void clear()
	{
		// create new array with default capacity
		theItems = new Comparable[DEFAULT_CAPACITY];
		
		// set size to 0
		theSize = 0;
	}


	/**
	 *	return the number of items in this BinaryHeap
	 */
	public int size()
	{
		return theSize;
	}


	/**
	 *	return true if this BinaryHeap contains 0 items, or false if this
	 *	BinaryHeap contains at least 1 item
	 */
	public boolean isEmpty()
	{
		return theSize == 0;
	}


	/**
	 *	return the item from this BinaryHeap that has either the minimum or
	 *	maximum value, depending on whether this heap is a min heap or max heap
	 *
	 *	If two or more items have the same value, no guarantee is made as to
	 *	which item will be returned.
	 *
	 *	Returns null if this BinaryHeap is empty
	 */
	public Comparable get()
	{
		// return null if heap is empty
		if (isEmpty())
			return null;
		
		// min item (for min heap) or max item (for max heap) will always be in
		// same position
		return theItems[NEXT_ITEM];
	}

}