package dataStructures;

/**
 *	Class GraphException
 *
 *	Generic class to represent any exception that occurs when using a Graph
 *	data structure.
 *
 *	Written by Roger West, University of Illinois at Springfield
 */
public class GraphException extends RuntimeException
{

	/***************
	 *	constructors
	 **************/

	/**
	 *	create a new GraphException
	 */
	public GraphException()
	{
		
	}


	/**
	 *	create a new GraphException that will display the specified
	 *	message if thrown
	 *
	 *	@param msg - the message to be displayed
	 */
	public GraphException(String msg)
	{
		super(msg);
	}
}