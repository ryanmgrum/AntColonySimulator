package dataStructures;

/**
 *	class DefaultHashFunction
 *
 *	implementation of the default hashing function used by the Hashtable class
 *
 *	this class uses the hashcode the Java Virtual Machine generates for every
 *	Object that gets instantiated
 *
 *	Written by Roger West, University of Illinois at Springfield
 */
public class DefaultHashFunction extends HashFunction
{
	/***************
	 *	constructors
	 **************/
	
	/**
	 *	create a new DefaultHashFunction
	 */
	public DefaultHashFunction()
	{
		super();
	}
	
	
	/**********
	 *	methods
	 *********/
	
	/**
	 *	return the hash value for the specified key
	 *	the hash value returned is guaranteed to be > 0
	 */
	public int hashValue(Object key)
	{
		// logically AND key's hashcode with 0x7FFFFFFF to guarantee result is > 0
		return (key.hashCode() & 0x7FFFFFFF);
	}
}