package dataStructures;

/**
 *	Class ShortestPath
 *
 *	Represents the shortest path between two vertices in a Graph.
 *	The locations along the path can be retrieved in a format that is traversable.
 *
 *	Written by Roger West, University of Illinois at Springfield
 */
public class ShortestPath
{
	/*************
	 *	attributes
	 ************/
	
	/** the vertices on the shortest path */
	private List thePath;
	
	/** the cost of the path */
	private double pathCost;
	
	
	/**************
	 *	constructor
	 *************/
	
	/**
	 *	Create a new, ShortestPath with the specified List of vertices that
	 *	comprise the path, and the specified path cost
	 */
	public ShortestPath(List path, double cost)
	{
		thePath = path;
		pathCost = cost;
	}
	
	
	/**********
	 *	methods
	 *********/
	
	/**
	 *	Return the path in List form
	 */
	public List getPath()
	{
		return thePath;
	}
	
	
	/**
	 *	Return the path cost
	 */
	public double getCost()
	{
		return pathCost;
	}
	
	
	/**
	 *	Return the origin of the path
	 *
	 *	Returns null if path is empty
	 */
	public Object getOrigin()
	{
		return thePath.isEmpty() ? null : thePath.get(0);
	}
	
	
	/**
	 *	Return the destination of the path
	 *
	 *	Returns null if path is empty
	 */
	public Object getDestination()
	{
		return thePath.isEmpty() ? null : thePath.get(thePath.size() - 1);
	}
}