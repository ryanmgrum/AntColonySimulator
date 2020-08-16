package dataStructures;


/**
 *	interface MapIterator
 *
 *	expands upon the functionality of Iterator to allow retrieval of the keys
 *	associated with the items in a Map
 *
 *	Written by Roger West, University of Illinois at Springfield
 */
public interface MapIterator extends Iterator
{

	/**
	 *	return the current item key in the traversal
	 */
	public Object getCurrentKey();

}