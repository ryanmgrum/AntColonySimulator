package dataStructures;

/**
 *	class HashMap
 *
 *	Implementation of a hash table.
 *
 *	Duplicate keys are not maintained - if an attempt is made to add a duplicate
 *	key, the existing item with that key is replaced with the new item.
 *
 *	This HashMap uses chaining hashing to handle collisions.
 *
 *	This class is customizable, in that the hashing function, the load factor,
 *	and the set of table sizes to use can be specified by the user.
 *
 *	Written by Roger West, University of Illinois at Springfield
 */
public class HashMap implements Map
{
	
	/************
	 *	constants
	 ***********/
	
	/** default value for load factor */
	private final double DEFAULT_LOAD_FACTOR = 0.75;
	
	
	/*************
	 *	attributes
	 ************/
	
	/** number of items currently in this HashMap */
	private int theSize;
	
	/** percent capacity that determines when this HashMap is resized */
	private double loadFactor;
	
	/** threshold capacity for resizing this HashMap */
	private int threshold;
	
	/** the hashing function used to generate hashcodes */
	private HashFunction hashFunction;
	
	/** default table sizes (all prime numbers close to a power of 2) */
	private int[] tableSizes = {13,
								31,
								61,
								127,
								251,
								509,
								1021,
								2039,
								4093,
								8191,
								16381,
								32749,
								65521,
								131071,
								262139,
								524287,
								1048573,
								2097143,
								4194301,
								8388593,
								16777213,
								33554393,
								67108859,
								134217689,
								268435399,
								536870909,
								1073741789,
								2147483647
					 			};
	
	/** the table of entries */
	private HashMapEntry[] table;
	
	/** number of structural modifications made to this HashMap */
	private int modCount;
	
	
	/***************
	 *	constructors
	 **************/
	
	/**
	 *	create a new, empty HashMap:
	 *		1)	the default set of prime table sizes is used
	 *		2)	uses DefaultHashFunction for computing hashes
	 *		3)	default load factor is used
	 */
	public HashMap()
	{
		// set load factor to default
		loadFactor = DEFAULT_LOAD_FACTOR;
		
		// empty this HashMap
		clear();
		
		// set hashing function to default
		hashFunction = new DefaultHashFunction();
	}
	
	
	/**
	 *	create a new, empty HashMap:
	 *		1)	the default set of prime table sizes is used
	 *		2)	uses the specified hash function for computing hashes
	 *		3)	default load factor is used
	 *
	 *	@param hf - the hashing function used for generating hashcodes
	 */
	public HashMap(HashFunction hf)
	{
		// set load factor to default
		loadFactor = DEFAULT_LOAD_FACTOR;
		
		// empty this HashMap
		clear();
		
		// set hashing function to specified hashing function
		hashFunction = hf;
	}
	
	
	/**
	 *	create a new, empty HashMap:
	 *		1)	the specified set of prime table sizes is used
	 *		2)	uses the specified hash function for computing hashes
	 *		3)	uses the specified load factor
	 *
	 *	@param hf - the hashing function used for generating hashcodes
	 *	@param tableSizes - user-specified list of table sizes
	 *	@param loadFactor - user-specified load factor
	 */
	public HashMap(HashFunction hf, int[] tableSizes, double loadFactor)
	{
		// set table size array
		this.tableSizes = tableSizes;
		
		// set load factor to specified load factor
		this.loadFactor = loadFactor;
		
		// empty this HashMap
		clear();
		
		// set hashing function to specified hashing function
		hashFunction = hf;
	}
	
	
	/**********
	 *	methods
	 *********/
	
	/**
	 *	resize this HashMap, and compute new indices for all items
	 */
	private void rehash()
	{
		// index for table
		int index;
		
		// storage for rehashed entries
		HashMapEntry newEntry;
		
		// determine new table size
		int newSize;
		int i = 1;
		
		// skip sizes already used
		while ((newSize = tableSizes[i]) <= table.length)
			i++;
		
		if (i > tableSizes.length)
		{
			// storage capacity exceeded
			throw new CapacityExceededException("Capacity of hashtable has been exceeded!");
		}
		
		// store old table
		HashMapEntry[] oldTable = table;
		
		// create new table
		table = new HashMapEntry[newSize];
		
		// rehash and copy items into new table
		for (i = 0; i < oldTable.length; i++)
		{
			for (HashMapEntry he = oldTable[i]; he != null; he = he.nextEntry)
			{
				// compute new index
				index = he.hashValue % table.length;
				
				// create new HashMapEntry object - using 'he' won't work
				newEntry = new HashMapEntry(he.key, he.value, he.hashValue);
				
				// add to chain
				newEntry.nextEntry = table[index];
				table[index] = newEntry;
			}
		}
		
		// recompute threshold
		threshold = (int)(loadFactor * newSize);
		
		// indicate a modification has been made
		modCount++;
	}
	
	
	/***************************************
	 *	methods inherited from interface Map
	 **************************************/
	
	/**
	 *	add the specified value to this HashMap, and associate it with the
	 *	specified key
	 *
	 *	If the specified key has already been associated with an item in this
	 *	HashMap, the value of the existing item is replaced with the specified
	 *	value.
	 *
	 *	returns true if the add was successful, false otherwise
	 */
	public boolean add(Object key, Object value)
	{
		// rehash, if necessary
		if (theSize + 1 >= threshold)
			rehash();
		
		// get the hash value for the key
		int hash = hashFunction.hashValue(key);
		
		// determine the table index
		int index = hash % table.length;

		// first determine if key has already been mapped to existing item
		for (HashMapEntry he = table[index]; he != null; he = he.nextEntry)
		{
			if (he.hashValue == hash && he.key.equals(key))
			{
				// key has already been mapped; replace existing item
				he.value = value;
				
				// replacement successful
				return true;
			}
		}
		
		// key is not mapped to existing item; create new entry
		HashMapEntry newEntry = new HashMapEntry(key, value, hash);
			
		// add new entry to chain
		newEntry.nextEntry = table[index];
		
		// store new entry
		table[index] = newEntry;
		
		// add 1 to the size of this HashMap
		theSize++;
		
		// indicate a modification has been made
		modCount++;
		
		// add successful
		return true;
	}


	/**
	 *	remove entry from this HashMap that corresponds to the specified key
	 *
	 *	returns true if the remove was successful
	 *	returns false for unsuccessful removal, including case where key has not
	 *	been mapped to an existing item
	 */
	public boolean remove(Object key)
	{
		// stores previous entry in chain
		HashMapEntry previous = null;
		
		// get the hash value for the key
		int hash = hashFunction.hashValue(key);
		
		// determine table index
		int index = hash % table.length;

		// get the table entry
		HashMapEntry he = table[index];
		
		// check for entry with specified key
		while (he != null)
		{
			if (he.key.equals(key) && he.hashValue == hash)
			{
				// item found
				if (previous == null)
				{
					// item was first item in chain
					table[index] = he.nextEntry;
				}
				else
				{
					// item not first item in chain, so reconnect chain
					previous.nextEntry = he.nextEntry;
				}
				
				// subtract 1 from size of this HashMap
				theSize--;
				
				// indicate a modification has been made
				modCount++;
				
				// remove successful
				return true;
			}
			
			// go to next entry
			previous = he;
			he = he.nextEntry;
		}
		
		// key not mapped to existing item
		return false;
	}


	/**
	 *	empty this HashMap
	 *	the empty HashMap will be reset to its minimum capacity
	 */
	public void clear()
	{
		// reset table of items
		table = new HashMapEntry[tableSizes[0]];
		
		// rset size to 0
		theSize = 0;
		
		// reset threshold
		threshold = (int)(loadFactor * tableSizes[0]);
		
		// emptying counts as a modification
		modCount++;
	}


	/**
	 *	return the number of items in the HashMap
	 */
	public int size()
	{
		return theSize;
	}


	/**
	 *	return true if this HashMap contains 0 items, or false if the
	 *	HashMap contains at least 1 item
	 */
	public boolean isEmpty()
	{
		return theSize == 0;
	}


	/**
	 *	return the item from this HashMap that is associated with the specified
	 *	key
	 */
	public Object get(Object key)
	{
		// get the hash value for the key
		int hash = hashFunction.hashValue(key);
		
		// determine the table index
		int index = hash % table.length;

		// check for entry with specified key
		for (HashMapEntry he = table[index]; he != null; he = he.nextEntry)
		{
			if (he.key.equals(key) && he.hashValue == hash)
			{
				// entry found
				return he.value;
			}
		}
		
		// entry doesn't exist		
		return null;
	}
	
	
	/**
	 *	return whether this HashMap contains the specified key
	 *
	 *	@param key - key to search for
	 */
	public boolean containsKey(Object key)
	{
		// get the hash value for the key
		int hash = hashFunction.hashValue(key);
		
		// determine table index
		int index = hash % table.length;
		
		// check for entry with specified key
		for (HashMapEntry he = table[index]; he != null; he = he.nextEntry)
		{
			if (he.key.equals(key) && he.hashValue == hash)
			{
				// key exists in this HashMap
				return true;
			}
		}
		
		// key does not exist in this HashMap
		return false;
	}
	
	
	/**
	 *	return whether this HashMap contains the specified value
	 *
	 *	@param value - value to search for
	 */
	public boolean containsValue(Object value)
	{
		// check for entry with specified value
		for (int i = 0; i < table.length; i++)
		{
			for (HashMapEntry he = table[i]; he != null; he = he.nextEntry)
			{
				if (he.value.equals(value))
				{
					// value exists in this HashMap
					return true;
				}
			}
		}
		
		// value does not exist in this HashMap
		return false;
	}
	
	
	/**
	 *	return a List of keys in the Map
	 *
	 *	no guarantee is provided as to the ordering of the keys
	 */
	public List keyList()
	{
		// store keys in a LinkedList
		LinkedList keys = new LinkedList();
		
		// add keys to list
		for (int i = 0; i < table.length; i++)
		{
			for (HashMapEntry he = table[i]; he != null; he = he.nextEntry)
			{
				keys.add(he.key);
			}
		}
		
		return keys;
	}
	
	
	/**
	 *	return a List of values in the Map
	 *
	 *	no guarantee is provided as to the ordering of the values
	 */
	public List valueList()
	{
		// store values in a LinkedList
		LinkedList values = new LinkedList();
		
		// add values to list
		for (int i = 0; i < table.length; i++)
		{
			for (HashMapEntry he = table[i]; he != null; he = he.nextEntry)
			{
				values.add(he.value);
			}
		}
		
		return values;
	}
	
	
	/**
	 *	return an Iterator for this HashMap that begins at the first entry in
	 *	the HashMap
	 *
	 *	this iterator only allows the items to be retrieved; keys cannot be
	 *	retrieved using this iterator
	 */
	public Iterator iterator()
	{
		return new HashMapIterator();
	}
	
	
	/**
	 *	return a MapIterator for this HashMap that begins at the first entry
	 *	in the HashMap
	 *
	 *	this iterator allows the keys associated with items to be retrieved, as
	 *	well as the items themselves
	 */
	public MapIterator mapIterator()
	{
		return new HashMapIterator();
	}
	
	
	/**************************************
	 *	methods inherited from class Object
	 *************************************/
	
	/**
	 *	return a String representation of this HashMap
	 *
	 *	the entry values, not the keys, are returned
	 */
	public String toString()
	{
		String result = "";
		
		for (int i = 0; i < table.length; i++)
		{
			result += i + ": ";
			
			for (HashMapEntry he = table[i]; he != null; he = he.nextEntry)
			{
				if (he.nextEntry == null)
					result += he.value.toString();
				else
					result += he.value.toString() + " --> ";
			}
			
			result += "\n";
		}
		
		return result;
	}
	
	
	/**
	 *	inner class HashMapIterator
	 *
	 *	Implementation of an iterator for a HashMap.
	 *
	 *	This class allows one-way traversal of the entries in a HashMap, but
	 *	does not provide a mechanism for adding or removing entries
	 */
	protected class HashMapIterator implements MapIterator
	{
		
		/*************
		 *	attributes
		 ************/
		
		/** current table index */
		int currentIndex;
		
		/** current table entry */
		HashMapEntry current;
		
		/** number of structural modifications of which this Iterator is aware */
		int expectedModCount;
		
		
		/***************
		 *	constructors
		 **************/
		
		/**
		 *	create a new HashMapIterator that begins at the first entry in the
		 *	HashMap
		 *
		 *	if HashMap is empty, this HashMapIterator will still be created,
		 *	but calls to hasNext() will return false
		 */
		public HashMapIterator()
		{
			// start at first table cell
			currentIndex = 0;
			
			// go to first cell with an entry, if it exists
			while (currentIndex < table.length && table[currentIndex] == null)
				currentIndex++;
			
			if (currentIndex >= table.length)
			{
				// HashMap is empty
				current = null;
			}
			else
			{
				// set current to first entry in HashMap
				current = table[currentIndex];
			}
			
			// sync mod counts of HashMap and this HashMapIterator
			expectedModCount = modCount;
		}
		
		
		/***********************************************
		 *	methods inherited from interface MapIterator
		 **********************************************/
		
		/**
	 	 *	return the current key in the traversal
	 	 */
		public Object getCurrentKey()
		{
			// return null if at end of traversal, otherwise return key of current node
			return current == null ? null : current.key;
		}
		

		/********************************************
		 *	methods inherited from interface Iterator
		 *******************************************/
		
		/**
	 	 *	return the current item (value) in the traversal
	 	 */
		public Object getCurrent()
		{
			// return null if at end of traversal, otherwise return value of current node
			return current == null ? null : current.value;
		}


		/**
	 	 *	return whether there is a next item to traverse
	 	 *	return true if yes, false otherwise
	 	 */
		public boolean hasNext()
		{
			// check for concurrent modification
			if(expectedModCount != modCount)
				throw new ConcurrentModificationException();
			
			// if next item exists current will not be null
			return current != null;
		}


		/**
	 	 *	advance to the next item
	 	 */
		public void next()
		{
			// check for concurrent modification
			if(expectedModCount != modCount)
				throw new ConcurrentModificationException();
			
			// go to next entry in chain, if it exists
			current = current.nextEntry;
			
			if (current == null)
			{
				// at end of chain; go to next cell containing an entry
				while (++currentIndex < table.length && table[currentIndex] == null)
					;
				
				if (currentIndex >= table.length)
				{
					// past end of table
					current = null;
				}
				else
				{
					// set next entry
					current = table[currentIndex];
				}
			}
		}
		
	}
	
	
	/**
	 *	inner class HashMapEntry
	 *
	 *	implementation of a single entry in a hashtable
	 */
	protected class HashMapEntry
	{
		
		/*************
		 *	attributes
		 ************/
		
		/** key mapped to data item */
		Object key;
		
		/** the data item */
		Object value;
		
		/** hashcode for key */
		int hashValue;
		
		/** link to next HashMapEntry in chain (in case of collision) */
		HashMapEntry nextEntry;
		
		
		/***************
		 *	constructors
		 **************/
		
		/**
		 *	create a new HashMapEntry
		 *
		 *	@param itemKey - key for data item
		 *	@param itemValue - the data item being stored
		 *	@param hash - hashcode for key
		 */
		HashMapEntry(Object itemKey, Object itemValue, int hash)
		{
			key = itemKey;
			value = itemValue;
			hashValue = hash;
			nextEntry = null;
		}
		
		
		/**********
		 *	methods
		 *********/
		
		
		/**
		 *	return whether the specified Object equals this HashMapEntry
		 */
		public boolean equals(Object o)
		{
			// o is wrong reference type
			if (!(o instanceof HashMapEntry))
				return false;
			
			// create new HashMapEntry
			HashMapEntry he = (HashMapEntry)o;
			
			// 2 HashMapEntry objects are equal iff keys of both entries are
			// equal AND values of both entries are equal
			return ((key == null ? he.key == null : key.equals(he.key)) &&
					(value == null ? he.value == null : value.equals(he.value)));
		}

	}
	
}