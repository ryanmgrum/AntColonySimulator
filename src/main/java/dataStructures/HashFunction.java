package dataStructures;

/**
 *	abstract class HashFunction
 *
 *	this class encapsulates the basic functionality required for a hashing
 *	function
 *
 *	this class itself cannot be instantiated, but subclasses of this class can
 *	be created
 *
 *	Written by Roger West, University of Illinois at Springfield
 */
public abstract class HashFunction
{
	
	/***************
	 *	constructors
	 **************/
	
	/**
	 *	default constructor
	 */
	public HashFunction()
	{
	}
	
	/**********
	 *	methods
	 *********/
	
	/**
	 *	return an integer hash value for the specified key
	 */
	public abstract int hashValue(Object key);
}