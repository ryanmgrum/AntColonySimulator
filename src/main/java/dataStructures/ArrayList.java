package dataStructures;

/**
 *	ArrayList class
 *
 *	implementation of an array-based list
 *
 *	Written by Roger West, University of Illinois at Springfield
 *	Adapted from code written by Mark Weiss in Data Structures and Problem
 *	Solving Using Java, 2nd edition, 2002
 */
public class ArrayList implements List
{

	/************
	 *	constants
	 ***********/
	
	/** default array capacity */
	private static final int DEFAULT_CAPACITY = 10;
	
	/** return value for unsuccessful ai.searches */
	private static final int NOT_FOUND = -1;


	/*************
	 *	attributes
	 ************/
	
	/** array to store items */
	private Object[] theItems;
	
	/** current number of items in list */
	private int theSize;
	
	/** current number of modifications to list */
	private int modCount;


	/***************
	 *	constructors
	 **************/
	

	/**
	 *	return a new, empty ArrayList with the default capacity
	 */
	public ArrayList()
	{
		// empty this ArrayList
		clear();
	}
	
	
	/**********
	 *	methods
	 *********/
	
	
	/**
	 *	double the size of the underlying array
	 */
	private void doubleArray()
	{
		// backup original array
		Object[] old = theItems;
		
		// create new array
		theItems = new Object[theItems.length * 2 + 1];
		
		// copy items from old array to new array
		for(int i = 0; i < theSize; i++)
			theItems[i] = old[i];
	}
	
	
	/**************************************
	 *	methods inherited from class Object
	 *************************************/
	

	/**
	 *	return a String representation of the ArrayList
	 *
	 *	items are listed in order from beginning to end in comma-delimited fashion
	 */
	public String toString()
	{
		String s = "";
		
		for (int i = 0; i < theSize; i++)
		{
			if (i == theSize - 1)
				s += theItems[i].toString();
			else
				s += theItems[i].toString() + ",";
		}
		
		return s;
	}
	
	
	/****************************************
	 *	methods inherited from interface List
	 ***************************************/
	
	/**
	 *	return the item at the specified position
	 *
	 *	throws ArrayIndexOutOfBoundsException if position is invalid
	 */
	public Object get(int index)
	{
		// throw exception if invalid index
		if(index < 0 || index >= theSize)
			throw new ArrayIndexOutOfBoundsException("Index " + index + "; size " + theSize);

		// return the item at index
		return theItems[index];
	}


	/**
	 *	return the position of the specified item, if it is in the list
	 *
	 *	returns -1 if item is not in list
	 */
	public int indexOf(Object obj)
	{
		// do linear search from beginning of array
		for(int i = 0; i < theSize; i++)
		{
			if(obj == null)
			{
				// null items have to be compared with ==
				if(theItems[i] == null)
					return i;
			}
			else if(obj.equals(theItems[i]))
			{
				// obj found; return index
				return i;
			}
		}

		// obj not found
		return NOT_FOUND;
	}


	/**
	 *	add the specified item to the ArrayList at the specified position
	 *
	 *	throws ArrayIndexOutOfBoundsException if position is invalid
	 */
	public boolean add(int index, Object obj)
	{
		// throw exception if invalid index
		if (index < 0 || index > theSize)
    		throw new ArrayIndexOutOfBoundsException("Index " + index + "; size " + theSize);

		// resize array if full
		if(theItems.length == theSize)
			doubleArray();
	
		// shift items from position i to end down
		for (int i = theSize; i > index; i--)
			theItems[i] = theItems[i - 1];
        
        // insert new item
		theItems[index] = obj;

		// add 1 to size of this ArrayList
		theSize++;
		
		// indicate a modification has been made
		modCount++;
		
		// add successful
		return true;
	}


	/**
	 *	replace the item at the specified position
	 *
	 *	throws ArrayIndexOutOfBoundsException if position is invalid
	 */
	public Object set(int index, Object obj)
	{
		// throw exception if invalid index
		if(index < 0 || index >= theSize)
			throw new ArrayIndexOutOfBoundsException("Index " + index + "; size " + theSize);

		// temp storage for old item
		Object old = theItems[index];
		
		// replace old item with obj
		theItems[index] = obj;

		// return old item
		return old;
	}

	
	/**
	 *	remove the first occurrence of the specified item
	 *
	 *	returns true if removal was successful
	 *	returns false if item is not in list
	 */
	public boolean remove(Object obj)
    {
    	// get position of obj
		int pos = indexOf(obj);

		if(pos == NOT_FOUND)
		{
			// item not in this ArrayList
			return false;
		}
		else
		{
			// remove obj
			remove(pos);
			
			// remove successful
			return true;
		}
    }


	/**
	 *	remove the item at the specified position
	 *
	 *	throws ArrayIndexOutOfBoundsException if position is invalid
	 */
	public boolean remove(int index)
	{
		// throw exception if invalid index
		if(index < 0 || index >= theSize)
			throw new ArrayIndexOutOfBoundsException("Index " + index + "; size " + theSize);

		// logically remove item at index by shifting items from index + 1 to left
		for(int i = index; i < theSize - 1; i++)
			theItems[i] = theItems[i + 1];

		// subtract 1 from size of this ArrayList
		theSize--;
		
		// indicate a modification has been made
		modCount++;
		
		// remove successful
		return true;
	}


	/**
	 *	return a ListIterator that begins at the specified position
	 */
	public ListIterator listIterator(int index)
	{
		return new ArrayListIterator(index);
	}


	/**********************************************
	 *	methods inherited from interface Collection
	 *********************************************/
	
	
	/**
	 *	add the specified item to the end of the ArrayList
	 */
	public boolean add(Object obj)
	{
		// resize array if full
		if(theItems.length == size())
			doubleArray();
        
        // add 1 to size of this ArrayList and add obj at end of array
		theItems[theSize++] = obj; 
	
		// indicate a modification has been made
		modCount++;

		// add successful
		return true;
	}


	/**
	 *	remove the last item in the ArrayList
	 */
	public boolean remove()
	{
		// throw exception if this ArrayList is empty
		if(theSize - 1 < 0)
			throw new ArrayIndexOutOfBoundsException("Index " + (theSize - 1) + "; size " + theSize);
		
		// remove item at end of array
		return remove(theSize - 1);
	}


	/**
	 *	empty the ArrayList
	 *
	 *	array will be reset to the default capacity
	 *	size will be set to zero
	 *	clearing the ArrayList counts as a modification of the list
	 */
	public void clear()
	{
		// set size to 0
		theSize = 0;
		
		// create new underlying array with default capacity
		theItems = new Object[DEFAULT_CAPACITY];
		
		// clearing the list counts as a modification
		modCount++;
	}


	/**
	 *	return the number of items in the ArrayList
	 */
	public int size()
	{
		return theSize;
	}


	/**
	 *	return true if the ArrayList is empty
	 */
	public boolean isEmpty()
	{
		return theSize == 0;
	}


	/**
	 *	return the item at the end of the ArrayList
	 */
	public Object get()
	{
		return get(theSize - 1);
	}
	

	/*********************************************************
	 *	methods inherited from interface TraversableCollection
	 ********************************************************/
	
	
	/**
	 *	return an Iterator beginning at position 0
	 */
	public Iterator iterator()
	{
		return new ArrayListIterator(0);
	}
	
	
	/**
	 *	return true if the specified item is in the list
	 */
	public boolean contains(Object obj)
	{
		// if obj is in this ArrayList, it will have a valid index
		return indexOf(obj) != NOT_FOUND;
	}


	/****************
	 *	inner classes
	 ***************/
	

	/**
	 *	inner class ArrayListIterator
	 *
	 *	traverses an ArrayList in both forward and backward directions
	 *	maintains the current position in the traversal
	 *
	 *	instances of ArrayListIterator will be invalidated when ArrayList methods
	 *	are used to modify the list
	 */
	private class ArrayListIterator implements ListIterator
	{
		
		/*************
		 *	attributes
		 ************/

		/**	current position */
		private int current;
		
		/** number of list modifications the iterator is aware of */
		private int expectedModCount;


		/***************
		 *	constructors
		 **************/
		

		/**
		 *	return an ArrayListIterator that begins at the specified position
		 *
		 *	throws ArrayIndexOutOfBoundsException if position is invalid
		 */
		public ArrayListIterator(int index)
		{
			// throw exception if invalid index
			if(index < 0 || index >= theSize)
				throw new ArrayIndexOutOfBoundsException();

			// set current position of this ArrayListIterator to index
			current = index;
			
			// sync mod count of this ArrayListIterator with mod count of the ArrayList
			expectedModCount = modCount;
		}


		/********************************************
		 *	methods inherited from interface Iterator
		 *******************************************/
				
		
		/**
		 *	return the item at the iterator's current position
		 *
		 *	throws ArrayIndexOutOfBoundsException if position is invalid
		 */
		public Object getCurrent()
		{
			// throw exception if invalid index
			if(current < 0 || current >= theSize)
				throw new ArrayIndexOutOfBoundsException();
			
			// return item at current position of this ArrayListIterator
			return theItems[current];
		}


		/**
		 *	return true if there is a next item
		 *	there is a next item as long as the iterator's current position < theSize
		 *
		 *	throws ConcurrentModificationException if iterator has been invalidated
		 */
		public boolean hasNext()
        {
        	// throw exception if ArrayList has been modified outside of this ArrayListIterator
			if(expectedModCount != modCount)
				throw new ConcurrentModificationException();

			// if more items, current < theSize will be true
			return current < theSize;
        }


		/**
		 *	advance forward one position
		 *
		 *	throws NoSuchElementException if there is no next item
		 */
		public void next()
		{
			// throw exception if no more items to traverse
			if(!hasNext())
				throw new NoSuchElementException();

			// update current position of this ArrayListIterator
			current++;
		}


		/**
		 *	add the specified object to the ArrayList
		 *	object is inserted at the iterator's current position
		 *
		 *	throws ConcurrentModificationException if iterator has been invalidated
		 */
		public boolean add(Object obj)
		{
			// throw exception if ArrayList has been modified outside of this ArrayListIterator
			if(expectedModCount != modCount)
				throw new ConcurrentModificationException();
			
			// add obj at current position by invoking the ArrayList add method
			ArrayList.this.add(current, obj);
			
			// sync mod count of this ArrayListIterator with the ArrayList
			expectedModCount++;
			
			// add successful
			return true;
		}
		

		/**
		 *	remove the item at the iterator's current position
		 *
		 *	throws ConcurrentModificationException if iterator has been invalidated
		 */
		public boolean remove()
		{
			// throw exception if ArrayList has been modified outside of this ArrayListIterator
			if(expectedModCount != modCount)
				throw new ConcurrentModificationException();

			// remove current item by invoking the ArrayList remove method
			ArrayList.this.remove(current);

			// sync mod count of this ArrayListIterator with the ArrayList
			expectedModCount++;
			
			// remove successful
			return true;
		}


		/************************************************
		 *	methods inherited from interface ListIterator
		 ***********************************************/
		

		/**
		 *	return true if there is a previous item
		 *	there is a previous item as long as the iterator's current position >= 0
		 *
		 *	throws ConcurrentModificationException if iterator has been invalidated
		 */
        public boolean hasPrevious()
        {
        	// throw exception if ArrayList has been modified outside of this ArrayListIterator
			if(expectedModCount != modCount)
				throw new ConcurrentModificationException();

			// if previous item exists, current >= 0 will be true
			return current >= 0;
        }


		/**
		 *	move backward one position
		 *
		 *	throws NoSuchElementException if there is no previous item
		 */
        public void previous()
        {
        	// throw exception if no previous item in traversal
			if(!hasPrevious())
				throw new NoSuchElementException();
			
			// set current to previous position
			current--;
        }

	}

}