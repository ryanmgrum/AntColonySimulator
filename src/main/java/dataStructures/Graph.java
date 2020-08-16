package dataStructures;

/**
 *	Graph interface
 *
 *	Encapsulates the basic functionality for a graph data structure.
 *
 *	Written by Roger West, University of Illinois at Springfield
 */
public interface Graph extends TraversableCollection
{

	/**
	 *	return the specified item
	 *
	 *	returns null if specified item doesn't exist
	 */
	public Object get(Object obj);
	
	
	/**
	 *	remove the specified item
	 *
	 *	returns true if remove was successful, false if the item could not be
	 *	removed, or if the item does not exist
	 */
	public boolean remove(Object obj);

}