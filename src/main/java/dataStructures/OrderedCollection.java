package dataStructures;

/**
 *	OrderedCollection interface
 *	Encapsulates the basic functionality for an ordered collection of objects
 *	Only objects that implement the Comparable interface may be added to an
 *	OrderedCollection
 *
 *	Written by Roger West, University of Illinois at Springfield
 */
public interface OrderedCollection
{

	/**
	 *	add the specified object to the Collection
	 *	the ordering of the items in the Collection will be preserved
	 *
	 *	returns true if the add was successful, false otherwise
	 */
	public boolean add(Comparable obj);


	/**
	 *	remove an item from the Collection
	 *	which item is removed depends on the type of Collection
	 *	the only guarantee is that if the Collection contains 1 or more items,
	 *	the number of items in the Collection will be decreased by 1
	 *
	 *	returns true if the remove was successful, false otherwise
	 */
	public boolean remove();


	/**
	 *	empty the Collection
	 *	the Collection may have all the items physically removed, or the items
	 *	may simply be logically removed, depending on the type of Collection
	 */
	public void clear();


	/**
	 *	return the number of items in the Collection
	 */
	public int size();


	/**
	 *	return true if the Collection contains 0 items, or false if the
	 *	Collection contains at least 1 item
	 */
	public boolean isEmpty();


	/**
	 *	return the next available item from the Collection
	 *	which item is returned depends on the type of Collection
	 */
	public Comparable get();

}