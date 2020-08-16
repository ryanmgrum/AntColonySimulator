package dataStructures;

/**
 *	interface Map
 *
 *	Encapsulates the basic functionality for a data structure which maps items
 *	to keys.  An item can only be added, removed, or retrieved via its key.
 *
 *	Duplicate items are allowed in a Map, but duplicate keys are not allowed.
 *
 *	This interface does not provide iterators; rather, the keys or values in the
 *	Map can be returned in the form of a List, which can be traversed.
 *
 *	Written by Roger West, University of Illinois at Springfield
 */
public interface Map
{
	
	/**
	 *	add the specified value to the Map, and associate it with the
	 *	specified key
	 *	the position of the newly added item is dependent on the type of Map
	 *	the only guarantee is that the number of items in the Map will be
	 *	increased by 1, assuming the add was successful
	 *
	 *	returns true if the add was successful, false otherwise
	 */
	public boolean add(Object key, Object value);


	/**
	 *	remove the item from the Map that corresponds to the specified key
	 *
	 *	returns true if the remove was successful, false otherwise
	 */
	public boolean remove(Object key);


	/**
	 *	empty the Map
	 *	the Map may have all the items physically removed, or the items
	 *	may simply be logically removed, depending on the type of Map
	 */
	public void clear();


	/**
	 *	return the number of items in the Map
	 */
	public int size();


	/**
	 *	return true if the Map contains 0 items, or false if the
	 *	Map contains at least 1 item
	 */
	public boolean isEmpty();


	/**
	 *	return the item from the Map that is associated with the specified key
	 */
	public Object get(Object key);
	
	
	/**
	 *	return whether the Map contains the specified key
	 */
	public boolean containsKey(Object key);
	
	
	/**
	 *	return whether the Map contains the specified value
	 */
	public boolean containsValue(Object value);
	
	
	/**
	 *	return a List of keys in the Map
	 *
	 *	no guarantee is provided as to the ordering of the keys
	 */
	public List keyList();
	
	
	/**
	 *	return a List of values in the Map
	 *
	 *	no guarantee is provided as to the ordering of the values
	 */
	public List valueList();
	
	
	/**
	 *	return an Iterator for the Map that begins at the first entry in the Map
	 *
	 *	which entry is designated as the first entry depends on the specific
	 *	implementation of the Map
	 *
	 *	this iterator only allows the items to be retrieved; keys cannot be
	 *	retrieved using this iterator
	 */
	public Iterator iterator();
	
	
	/**
	 *	return a MapIterator for the Map that begins at the first entry in the
	 *	Map
	 *
	 *	which entry is designated as the first entry depends on the specific
	 *	implementation of the Map
	 *
	 *	this iterator allows the keys associated with items to be retrieved, as
	 *	well as the items themselves
	 */
	public MapIterator mapIterator();

}