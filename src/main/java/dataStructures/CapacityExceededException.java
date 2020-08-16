package dataStructures;

/**
 *	Class CapacityExceededException
 *
 *	Generic class to represent the exception that occurs when a data structure's
 *	capacity is exceeded.  Used by the HashMap class.
 *
 *	Written by Roger West, University of Illinois at Springfield
 */
public class CapacityExceededException extends RuntimeException
{
	
	/***************
	 *	constructors
	 **************/

	/**
	 *	create a new CapacityExceededException
	 */
	public CapacityExceededException()
	{
		
	}


	/**
	 *	create a new CapacityExceededException that will display the specified
	 *	message if thrown
	 *
	 *	@param msg - the message to be displayed
	 */
	public CapacityExceededException(String msg)
	{
		super(msg);
	}
}