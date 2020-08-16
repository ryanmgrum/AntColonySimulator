package dataStructures;

/**
 *	ListGraph class
 *	
 *	implementation of a graph that uses a LinkedList to implement adjacency
 *	lists
 *
 *	Duplicate items are not permitted in this implementation.
 *
 *	Written by Roger West, University of Illinois at Springfield
 *	Adapted from code written by Mark Weiss in Data Structures and Problem
 *	Solving Using Java, 2nd edition, 2002
 */
public class ListGraph implements Graph
{
	
	/************
	 *	constants
	 ***********/
	
	/** maximum value for path length between 2 Vertices */
	private static final double INFINITY = Double.MAX_VALUE;
	
	
	/*************
	 *	attributes
	 ************/
	
	/** HashMap for underlying storage of items */
	private HashMap theItems;
	
	
	/***************
	 *	constructors
	 **************/
	
	public ListGraph()
	{
		// empty this ListGraph
		clear();
	}

	
	/**********
	 *	methods
	 *********/
	
	/**
	 *	internal method for retrieving the Vertex containing the specified item,
	 *	if it exists, or adding a new Vertex containing the specified item if it
	 *	does not exist
	 */
	private Vertex getOrCreateVertex(Object obj)
	{
		// retrieve Vertex, if it exists
		Vertex v = (Vertex)theItems.get(obj);
		
		if (v == null)
		{
			// Vertex does not exist, so create it and add it to this ListGraph
			v = new Vertex(obj);
			theItems.add(obj, v);
		}
		
		return v;
	}
	
	
	/**
	 *	Internal method for returning the Vertex containing the specified item,
	 *	if it exists
	 *
	 *	@param obj - the item being sought
	 *
	 *	Returns the Vertex containing obj, if it exists
	 *	Returns null if obj does not exist in this ListGraph
	 */
	private Vertex getVertex(Object obj)
	{
		// retrieve Vertex, if it exists
		return (Vertex)theItems.get(obj);
	}
	
	
	/**
	 *	add an Edge to this ListGraph connecting the Vertex containing the
	 *	specified origin item to the Vertex containing the specified destination
	 *	item, with the specified edge cost
	 *
	 *	The new Edge will be unidirectional from the origin Vertex to the
	 *	destination Vertex.
	 *
	 *	If either the origin or destination vertices do not exist, they will be
	 *	created and added to this ListGraph.
	 *
	 *	It is permissible to have multiple edges from any given origin Vertex to
	 *	any given destination Vertex.
	 *
	 *	returns true if the add was successful, false if the Edge could not be
	 *	added
	 */
	public boolean addEdge(Object origin, Object destination, double cost)
	{
		// retrieve origin and destination vertices (create them if necessary)
		Vertex o = getOrCreateVertex(origin);
		Vertex d = getOrCreateVertex(destination);
		
		// add new Edge to origin's adjacency list
		o.addDestination(new Edge(d, cost));
		
		// edge added successfully
		return true;
	}
	
	
	/**
	 *	Add a bidirectional Edge to this ListGraph connecting the Vertex
	 *	containing the specified origin item and the Vertex containing the
	 *	specified destination item, with the specified edge cost.
	 *
	 *	In effect, two edges are added, one from the origin to the destination
	 *	and one from the destination to the origin.  The costs of both edges are
	 *	equal.
	 *
	 *	If either the origin or destination vertices do not exist, they will be
	 *	created and added to this ListGraph.
	 *
	 *	It is permissible to have multiple bidirectional edges between any given
	 *	origin Vertex and any given destination Vertex.
	 *
	 *	returns true if both edges were added successfully, false otherwise
	 */
	public boolean addBidirectionalEdge(Object origin, Object destination, double cost)
	{
		// retrieve origin and destination vertices (create them if necessary)
		Vertex o = getOrCreateVertex(origin);
		Vertex d = getOrCreateVertex(destination);
		
		// add new Edge to origin's adjacency list
		o.addDestination(new Edge(d, cost));
		
		// add new Edge to destination's adjacency list
		d.addDestination(new Edge(o, cost));
	
		// edges added sucessfully	
		return true;
	}
	
	
	/**
	 *	Remove the Edge having the specified origin and destination vertices,
	 *	and the specified cost.
	 *
	 *	If two or more edges with the same cost value exist for the origin and
	 *	destination vertices, only one of the edges will be removed.
	 *
	 *	Returns true if the removal was successful, false if the Edge could not
	 *	be removed, or if the Edge does not exist
	 */
	public boolean removeEdge(Object origin, Object destination, double cost)
	{
		// retrieve the origin and destination vertices
		Vertex o = (Vertex)theItems.get(origin);
		Vertex d = (Vertex)theItems.get(destination);
		
		if (o == null || d == null)
		{
			// either origin or destination Vertex does not exist
			return false;
		}
		
		// remove the Edge from the origin Vertex
		return o.removeDestination(new Edge(d, cost));
	}
	
	
	/**
	 *	Remove all edges between the specified origin and destination vertices.
	 *
	 *	Returns true if the removal was successful, false otherwise
	 */
	public boolean removeAllEdges(Object origin, Object destination)
	{
		// retrieve the origin and destination vertices
		Vertex o = (Vertex)theItems.get(origin);
		Vertex d = (Vertex)theItems.get(destination);
		
		if (o == null || d == null)
		{
			// one of the vertices does not exist
			return false;
		}
		
		// remove all edges from the origin to the destination
		if (!o.adjacencyList.isEmpty())
		{
			for (ListIterator itr = o.adjacencyList.listIterator(0); itr.hasNext(); )
			{
				Edge e = (Edge)itr.getCurrent();
			
				// if Edge to destination exists remove it; otherwise advance
				if (e.destination.equals(d))
					itr.remove();
				else
					itr.next();
			}
		}
		
		// edges successfully removed
		return true;
	}
	
	
	/**
	 *	Return whether two items in this ListGraph are adjacent
	 *
	 *	item2 is adjacent to item1 if and only if item2 is contained in item1's
	 *	adjacency list; it is possible for item1 to be adjacent to item2 without
	 *	item2 being adjacent to item1
	 *
	 *	Returns true if item2 is adjacent to item1
	 *	Returns false if item2 does not exist in this ListGraph, or if item1
	 *	either does not exist in this ListGraph or item2 is not adjacent to item1
	 */
	public boolean areAdjacentItems(Object item1, Object item2)
	{
		// Attempt to retrieve vertices containing item1 and item2
		Vertex v1 = getVertex(item1);
		Vertex v2 = getVertex(item2);
	
		if (v1 == null || v2 == null)
		{
			// can be no adjacency if item1 or item2 is not in this ListGraph
			return false;
		}
		
		return v1.isAdjacentTo(item2);
	}
	
	/**
	 *	Return a List of all items that are adjacent to the specified item
	 *
	 *	Returns an empty List if there are no items adjacent to the specified
	 *	item or if the specified item is not in this ListGraph
	 */
	public List getAdjacentItems(Object obj)
	{
		// retrieve the Vertex containing obj
		Vertex v = getVertex(obj);
		
		if (v == null)
		{
			// obj is not in this ListGraph
			return new LinkedList();
		}
		
		return v.getAdjacentItems();
	}
	 
	 
	/**
	 *	Internal method for resetting all vertices prior to performing a
	 *	shortest path computation.
	 */
	private void resetVertices()
	{
		for (Iterator itr = theItems.iterator(); itr.hasNext(); )
		{
			((Vertex)itr.getCurrent()).reset();
			itr.next();
		}
	}
	
	
	/**
	 *	Return the unweighted shortest path from the specified origin to the
	 *	specified destination.
	 */
	public ShortestPath unweightedShortestPath(Object origin, Object destination)
	{
		// the unweighted shortest path from origin to destination
		LinkedList path = new LinkedList();
		
		// origin Vertex
		Vertex orig = (Vertex)theItems.get(origin);
		
		// destination Vertex
		Vertex dest = (Vertex)theItems.get(destination);
		
		// path cost
		double cost;
		
		if (orig == null || dest == null)
			throw new NoSuchElementException();
		
		// compute shortest path
		computeUnweightedShortestPath(orig);
		
		// store cost to destination
		cost = dest.distance;
		
		// store shortest path from origin to destination
		while (dest != null)
		{
			path.addFirst(dest.theItem);
			dest = dest.previousVertex;
		}
		
		// return shortest path as a ShortestPath instance
		return new ShortestPath(path, cost);
	}
	
	
	/**
	 *	For a graph containing only non-negative Edge costs, return the weighted
	 *	shortest path from the specified origin to the specified destination, in
	 *	List form.  The items in the vertices, not the vertices themselves, are
	 *	contained in the List.  Traversing the List in the forward direction
	 *	will go from the origin to the destination.
	 */
	public ShortestPath weightedShortestPath(Object origin, Object destination)
	{
		// the weighted shortest path from origin to destination
		LinkedList path = new LinkedList();
		
		// origin Vertex
		Vertex orig = (Vertex)theItems.get(origin);
		
		// destination Vertex
		Vertex dest = (Vertex)theItems.get(destination);
		
		// path cost
		double cost;
		
		if (orig == null || dest == null)
			throw new NoSuchElementException();
		
		// compute shortest path
		computeWeightedShortestPath(orig);
		
		// store cost to destination
		cost = dest.distance;
		
		// store shortest path from origin to destination
		while (dest != null)
		{
			path.addFirst(dest.theItem);
			dest = dest.previousVertex;
		}
		
		// return shortest path as a ShortestPath instance
		return new ShortestPath(path, cost);
	}
	
	
	/**
	 *	For a graph that may contain negative Edge costs, return the weighted
	 *	shortest path from the specified origin to the specified destination, in
	 *	List form.  The items in the vertices, not the vertices themselves, are
	 *	contained in the List.  Traversing the List in the forward direction
	 *	will go from the origin to the destination.
	 */
	public ShortestPath negativeWeightedShortestPath(Object origin, Object destination)
	{
		// the weighted shortest path from origin to destination
		LinkedList path = new LinkedList();
		
		// origin Vertex
		Vertex orig = (Vertex)theItems.get(origin);
		
		// destination Vertex
		Vertex dest = (Vertex)theItems.get(destination);
		
		// path cost
		double cost;
		
		if (orig == null || dest == null)
			throw new NoSuchElementException();
		
		// compute shortest path
		computeNegativeWeightedShortestPath(orig);
		
		// store cost to destination
		cost = dest.distance;
		
		// store shortest path from origin to destination
		while (dest != null)
		{
			path.addFirst(dest.theItem);
			dest = dest.previousVertex;
		}
		
		// return shortest path as a ShortestPath instance
		return new ShortestPath(path, cost);
	}
	
	
	/**
	 *	For an acyclic graph, perform a topological sort and return the shortest
	 *	weighted path from the origin to the destination, in List form.  The
	 *	items in the vertices, not the vertices themselves, are	contained in the
	 *	List.  Traversing the List in the forward direction will go from the
	 *	origin to the destination.
	 */
	public List topologicalSort(Object origin, Object destination)
	{
		// the weighted shortest path from origin to destination
		LinkedList path = new LinkedList();
		
		// origin Vertex
		Vertex orig = (Vertex)theItems.get(origin);
		
		// destination Vertex
		Vertex dest = (Vertex)theItems.get(destination);
		
		if (orig == null || dest == null)
			throw new NoSuchElementException();
		
		// compute shortest path
		computeTopologicalSort(orig);
		
		// store shortest path from origin to destination
		while (dest != null)
		{
			path.addFirst(dest.theItem);
			dest = dest.previousVertex;
		}
		
		// return shortest path as a ShortestPath instance
		return path;
	}
	
	
	/**
	 *	Internal method to compute the shortest unweighted path to all vertices
	 *	from the specified origin Vertex.
	 *
	 *	@param origin		first Vertex in shortest path; must be non-null
	 */
	private void computeUnweightedShortestPath(Vertex origin)
	{
		// queue for storing vertices not yet visited
		LinkedQueue q = new LinkedQueue();
		
		// current Vertex in traversal
		Vertex v;
		
		// Vertex adjacent to v
		Vertex w;
		
		// Edge connecting v to w
		Edge e;
		
		// reset shortest path info for vertices
		resetVertices();
		
		// start with origin Vertex
		q.enqueue(origin);
		
		// distance from origin to itself is 0
		origin.distance = 0.0;
		
		// find the shortest path
		while(!q.isEmpty())
		{
			// get next Vertex to visit
			v = (Vertex)q.dequeue();
			
			// check all adjacent vertices
			for (Iterator itr = v.getAdjacentVertices().iterator(); itr.hasNext(); )
			{ 
				// retrieve current Edge
				e = (Edge)itr.getCurrent();
				
				// retrieve destination Vertex from Edge
				w = e.destination;
				
				if (w.distance == INFINITY)
				{
					// destination not yet reached
					
					// update distance to adjacent Vertex
					w.distance = v.distance + 1.0;
					
					// update link to previous Vertex in shortest path
					w.previousVertex = v;
					
					// add destination to queue of unvisited vertices
					q.enqueue(w);
				}
				
				// advance to next adjacent Vertex
				itr.next();
			}
		}
	}
	
	
	/**
	 *	Internal method to compute the shortest weighted path to all vertices
	 *	from the specified origin Vertex.
	 *
	 *	This method uses Dijkstra's algorithm to compute the shortest path.
	 *	Negative Edge costs are not permitted.
	 *
	 *	@param origin		first Vertex in shortest path; must be non-null
	 */
	private void computeWeightedShortestPath(Vertex origin)
	{
		// priority queue to store paths that need to be visited
		PriorityQueue pq = new PriorityQueue(PriorityQueue.PRIORITY_ASCENDING);
		
		// number of vertices visited
		int visited;
		
		// current shortest path
		Path shortestPath;
		
		// Vertex in current shortest path
		Vertex v;
		
		// Vertex adjacent to v
		Vertex w;
		
		// Edge connecting v to w
		Edge e;
		
		// cost of Edge from v to w
		double vwCost;
		
		// reset shortest path info for vertices
		resetVertices();
		
		// start with origin Vertex
		pq.add(new Path(origin, 0.0));
		
		// distance from origin to itself is 0
		origin.distance = 0.0;

		// no vertices have been visited yet
		visited = 0;
		
		// find the shortest path
		while (!pq.isEmpty() && visited < theItems.size())
		{
			// retrieve and remove current path from priority queue
			shortestPath = (Path)pq.get();
			pq.remove();
			
			// retrieve destination Vertex of current Path
			v = shortestPath.destination;
			
			if (v.scratch != 0)
			{
				// already processed v; get next Path from priority queue
				continue;
			}
			
			// indicate Vertex v has been visited
			v.scratch = 1;
			
			// increment total number of vertices visited
			visited++;
			
			// search v's adjacent vertices
			for (Iterator itr = v.getAdjacentVertices().iterator(); itr.hasNext(); )
			{
				// retrieve current Edge
				e = (Edge)itr.getCurrent();
				
				// retrieve destination Vertex from Edge
				w = e.destination;
				
				// retrieve edge cost
				vwCost = e.cost;
				
				if (vwCost < 0.0)
				{
					// graph has 1 or more negative edge costs
					throw new GraphException("Graph has negative edges");
				}
				
				if (w.distance > v.distance + vwCost)
				{
					// new shortest path from v to w found
					
					// update distance to w
					w.distance = v.distance + vwCost;
					
					// update link to previous Vertex in shortest path
					w.previousVertex = v;
					
					// add new shortest path to priority queue
					pq.add(new Path(w, w.distance));
				}
				
				// advance to next adjacent Vertex
				itr.next();
			}
		}
	}
	
	
	/**
	 *	Internal method to compute the shortest weighted path to all vertices
	 *	from the specified origin Vertex.
	 *
	 *	This method uses the Bellman-Ford algorithm to compute the shortest path.
	 *	Negative Edge costs are permitted.
	 *
	 *	@param origin		first Vertex in shortest path; must be non-null
	 */
	private void computeNegativeWeightedShortestPath(Vertex origin)
	{	
		// queue for storing vertices not yet visited
		LinkedQueue q = new LinkedQueue();
	
		// Vertex in current shortest path
		Vertex v;
		
		// Vertex adjacent to v
		Vertex w;
		
		// Edge connecting v to w
		Edge e;
		
		// cost of Edge from v to w
		double vwCost;
		
		// reset shortest path info for vertices
		resetVertices();
		
		// start with origin Vertex
		q.enqueue(origin);
		
		// distance from origin to itself is 0
		origin.distance = 0.0;
		
		// indicate origin is in queue
		origin.scratch++;
		
		// find the shortest path
		while (!q.isEmpty())
		{
			// retrieve next Vertex to visit
			v = (Vertex)q.dequeue();
			
			if (v.scratch > 2 * theItems.size())
			{
				// negative cycle exists
				throw new GraphException("Negative cycle detected");
			}
			
			// indicate v has been dequeued
			v.scratch++;
			
			// search v's adjacent vertices
			for (Iterator itr = v.getAdjacentVertices().iterator(); itr.hasNext();)
			{
				// retrieve current Edge
				e = (Edge)itr.getCurrent();
				
				// retrieve destination Vertex from Edge
				w = e.destination;
				
				// retrieve edge cost
				vwCost = e.cost;
				
				if (w.distance > v.distance + vwCost)
				{
					// new shortest path from v to w found
					
					// update distance to w
					w.distance = v.distance + vwCost;
					
					// update link to previous Vertex in shortest path
					w.previousVertex = v;
					
					// enqueue w only if not already in queue
					if (w.scratch % 2 == 0)
					{
						// indicate w has been enqueued
						w.scratch++;
						
						// place w back in queue
						q.enqueue(w);
					}
				}
				
				// advance to next adjacent Vertex
				itr.next();
			}
		}
	}
	
	
	/**
	 *	Internal method to perform a topological sort on this Graph, and compute
	 *	the shortest weighted path to all vertices from the specified origin
	 *	Vertex.
	 *
	 *	This Graph must be an acyclic graph in order for a topological sort to
	 *	be performed.  If this Graph contains a cycle, an exception will be
	 *	thrown.
	 *
	 *	@param origin		Vertex where topological sort begins; must be non-null
	 */
	private void computeTopologicalSort(Vertex origin)
	{
		// queue for storing vertices not yet visited
		LinkedQueue q = new LinkedQueue();
		
		// current Vertex
		Vertex v;
		
		// Vertex adjacent to v
		Vertex w;
		
		// Edge connecting v to w
		Edge e;
		
		// cost of Edge from v to w
		double vwCost;
		
		// iterations through graph
		int iterations;
		
		// reset shortest path info for vertices
		resetVertices();
		
		// start with origin Vertex
		origin.distance = 0.0;
		
		// compute indegrees for all vertices
		for (Iterator itemsItr = theItems.iterator(); itemsItr.hasNext();)
		{
			// retrieve current Vertex in traversal
			v = (Vertex)itemsItr.getCurrent();
			
			if (!v.getAdjacentVertices().isEmpty())
			{
				// increment indegree value for all vertices adjacent to v
				for (Iterator itr = v.getAdjacentVertices().iterator(); itr.hasNext(); )
				{
					// increment indegree
					((Edge)itr.getCurrent()).destination.scratch++;
					
					// advance to next adjacent Vertex
					itr.next();
				}
			}
			
			// advance to next Vertex in Graph
			itemsItr.next();
		}
		
		// enqueue vertices of indegree 0
		for (Iterator itr = theItems.iterator(); itr.hasNext(); )
		{
			// retrieve current Vertex in traversal
			v = (Vertex)itr.getCurrent();
			
			if (v.scratch == 0)
			{
				// v has indegree of 0, so add it to queue
				q.enqueue(v);
			}
			
			// advance to next Vertex in graph
			itr.next();
		}
		
		// find the shortest path
		for (iterations = 0; !q.isEmpty(); iterations++)
		{
			// retrieve next Vertex in queue
			v = (Vertex)q.dequeue();

			if (!v.getAdjacentVertices().isEmpty())
			{
				// check adjacent vertices
				for (Iterator itr = v.getAdjacentVertices().iterator(); itr.hasNext();)
				{
					// retrieve Edge to next adjacent Vertex, w
					e = (Edge)itr.getCurrent();
					
					// retrieve destination Vertex, w
					w = e.destination;
					
					// retrieve edge cost
					vwCost = e.cost;
					
					// remove indegree from w
					w.scratch--;
					
					if (w.scratch == 0)
					{
						// indegree of w is 0; w can now be visited
						q.enqueue(w);
					}
				
					if (v.distance == INFINITY)
					{
						// v appears prior to origin in topological sort, so is
						// unreachable from origin; don't calculate distance
						itr.next();
						continue;
					}
				
					if (w.distance > v.distance + vwCost)
					{
						// new shortest path from v to w found
						w.distance = v.distance + vwCost;
						
						// update link to previous Vertex in shortest path
						w.previousVertex = v;
					}
					
					// advance to next adjacent Vertex
					itr.next();
				}
			}
		}
		
		if (iterations != theItems.size())
		{
			// this Graph has a cycle
			throw new GraphException("Graph has a cycle!");
		}
	}
	
	
	/*****************************************
	 *	methods inherited from interface Graph
	 ****************************************/
	
	/**
	 *	return the specified item
	 *
	 *	returns null if specified item doesn't exist
	 */
	public Object get(Object obj)
	{
		// retrieve Vertex containing specified item, if it exists
		Vertex v = (Vertex)theItems.get(obj);
		
		// return null if item does not exist; otherwise return item
		return v == null ? null : v.theItem;
	}
	
	
	/**
	 *	remove the specified item
	 *
	 *	returns true if remove was successful, false if the item could not be
	 *	removed, or if the item does not exist
	 */
	public boolean remove(Object obj)
	{
		// retrieve Vertex containing the specified item, if it exists
		Vertex v = (Vertex)theItems.get(obj);
		
		if (v == null)
		{
			// Vertex does not exist
			return false;
		}
		
		// first remove any Edges from all other vertices to v
		for (Iterator itr = theItems.iterator(); itr.hasNext(); )
		{
			// remove all edges from current Vertex to v
			removeAllEdges(((Vertex)itr.getCurrent()).theItem, v.theItem);
			
			// advance to next Vertex in this ListGraph
			itr.next();
		}
		
		// remove the Vertex, v
		theItems.remove(v.theItem);
		
		// remove successful
		return true;
	}
	
	
	/*********************************************************
	 *	methods inherited from interface TraversableCollection
	 ********************************************************/
	
	/**
	 *	return an Iterator for this ListGraph
	 *
	 *	no guarantee is made as to the order in which the items will be returned
	 */
	public Iterator iterator()
	{
		return theItems.iterator();
	}
	
	
	/**
	 *	return whether this ListGraph contains the specified item
	 */
	public boolean contains(Object obj)
	{
		return theItems.containsKey(obj);
	}
	
	
	/**********************************************
	 *	methods inherited from interface Collection
	 *********************************************/
	
	/**
	 *	add a new Vertex containing the specified item to this ListGraph
	 *
	 *	the Vertex containing the new item will not have connections to any other
	 *	vertices in this ListGraph
	 *
	 *	returns true if the add was successful, false if the item could not be
	 *	added or if it is already present
	 */
	public boolean add(Object obj)
	{
		// retrieve Vertex containing specified item, if it exists
		Vertex v = new Vertex(obj);
		
		if (theItems.containsKey(obj))
		{
			// item already exists; add fails
			return false;
		}
		
		// create Vertex with new item and add to this ListGraph
		theItems.add(obj, v);
		
		// add successful
		return true;
	}


	/**
	 *	remove an item from this ListGraph
	 *
	 *	the item that will be removed will be the first item returned by a
	 *	traversal of this ListGraph using the iterator returned by the
	 *	iterator() method
	 *
	 *	returns true if the remove was successful, false otherwise
	 */
	public boolean remove()
	{
		// the first Vertex in the traversal
		Vertex v;
		
		// Iterator for traversing this ListGraph
		Iterator graphItr = theItems.iterator();
		
		if (graphItr.hasNext())
		{
			// retrieve first Vertex in traversal
			v = (Vertex)graphItr.getCurrent();
			
			// first remove any Edges from all other vertices to v
			for (Iterator itr = theItems.iterator(); itr.hasNext(); )
			{
				// remove all edges from current Vertex to v
				removeAllEdges(((Vertex)itr.getCurrent()).theItem, v.theItem);
				
				// advance to next Vertex in this ListGraph
				itr.next();
			}
			
			// remove the Vertex, v
			return theItems.remove(v.theItem);
		}
		
		// this ListGraph is empty
		return false;
	}


	/**
	 *	empty this ListGraph
	 */
	public void clear()
	{
		// reset underlying HashMap
		theItems = new HashMap();
	}


	/**
	 *	return the number of items in this ListGraph
	 */
	public int size()
	{
		return theItems.size();
	}


	/**
	 *	return true if this ListGraph contains 0 items, or false if the
	 *	ListGraph contains at least 1 item
	 */
	public boolean isEmpty()
	{
		return theItems.size() == 0;
	}


	/**
	 *	return the next available item from this ListGraph
	 *
	 *	the item that will be returned will be the first item returned by a
	 *	traversal of this ListGraph using the iterator returned by the
	 *	iterator() method
	 */
	public Object get()
	{
		// stores current Vertex
		Vertex v;
		
		// default iterator for this ListGraph
		Iterator itr = theItems.iterator();
		
		if (itr.hasNext())
		{
			// retrieve first Vertex in traversal
			v = (Vertex)itr.getCurrent();
			
			// return item in first Vertex
			return theItems.get(v.theItem);
		}
		
		// this ListGraph is empty
		return null;
	}
	
	
	/**************************************
	 *	methods inherited from class Object
	 *************************************/
	
	/**
	 *	Return a String representation of this ListGraph
	 */
	public String toString()
	{
		Vertex v;
		Edge e;
		String result = "";
		
		for (Iterator itr = iterator(); itr.hasNext(); )
		{
			v = (Vertex)itr.getCurrent();
			result += v.toString() + "\n";
			itr.next();
		}
		
		return result;
	}
	
	
	/**
	 *	inner class Vertex
	 *
	 *	represents a node, or vertex, in a graph
	 */
	private class Vertex
	{
		/*************
		 *	attributes
		 ************/
		
		/** the data item */
		Object theItem;
		
		/** list of adjacent vertices */
		LinkedList adjacencyList;
		
		/** distance to this Vertex */
		double distance;
		
		/** Vertex that comes before this Vertex on shortest path to this Vertex */
		Vertex previousVertex;

		/** temp storage for various info */
		int scratch;
		
		
		/***************
		 *	constructors
		 **************/
		
		/**
		 *	create a new Vertex containing the specified item
		 *	the newly created Vertex will have no adjacent vertices
		 *	
		 *	@param	item		the data item contained in this Vertex
		 */
		public Vertex(Object item)
		{
			// store item
			theItem = item;
			
			// create adjacency list
			adjacencyList = new LinkedList();
			
			// reset the state of this Vertex
			reset();
		}
		
		
		/**********
		 *	methods
		 *********/
		
		
		/**
		 *	reset the state of this Vertex to default state:
		 *		previousVertex = null;
		 *		distance = INFINITY
		 *		scratch = 0
		 */
		public void reset()
		{
			previousVertex = null;
			distance = INFINITY;
			scratch = 0;
		}
		
		
		/**
		 *	Return this Vertex's list of adjacent vertices.
		 *
		 *	The adjacent vertices are encapsulated in Edge objects.
		 */
		public List getAdjacentVertices()
		{
			return adjacencyList;
		}
		
		
		/**
		 *	Return this Vertex's list of adjacent items.
		 *
		 *	Returns empty List if this Vertex has no adjacent vertices
		 */
		public List getAdjacentItems()
		{
			// List for storing adjacent items
			LinkedList adjacentItems = new LinkedList();
			
			// traverse adjacency list of this Vertex and place items of all
			// adjacent vertices into adjacent items list
			if (!adjacencyList.isEmpty())
			{
				for (Iterator itr = adjacencyList.iterator(); itr.hasNext(); itr.next())
				{
					// get current Edge from adjacency list
					Edge e = (Edge)itr.getCurrent();
				
					// add current item to adjacent items list
					adjacentItems.add(e.destination.theItem);
				}
			}
			
			return adjacentItems;
		}
		
		
		/**
		 *	Add the specified Edge to this Vertex's adjacency list.
		 *
		 *	Returns true if Edge was successfully added, false otherwise.
		 */
		public boolean addDestination(Edge e)
		{
			adjacencyList.add(e);
			
			return true;
		}
		
		
		/**
		 *	Remove the specified Edge from this Vertex's adjacency list.
		 */
		public boolean removeDestination(Edge e)
		{
			return adjacencyList.remove(e);
		}
		
		
		/**
		 *	Return whether the specified item is adjacent to the item in this
		 *	Vertex
		 */
		public boolean isAdjacentTo(Object obj)
		{
			// obj is adjacent to this Vertex if an Edge containing a Vertex
			// containing obj is in the adjacency list for this Vertex
			return getEdge(obj) != null;
		}
		
		
		/**
		 *	internal method for retrieving from the adjacency list the Edge with
		 *	the specified item 
		 *
		 *	@param obj - the item whose Edge is needed
		 *
		 *	Returns the Edge containing obj, if it is in the adjacency list
		 *	Returns null if no Edge in the adjacency list contains obj
		 */
		private Edge getEdge(Object obj)
		{
			// search adjacency list for an Edge containing a Vertex containing obj
			for (Iterator itr = adjacencyList.iterator(); itr.hasNext(); itr.next())
			{
				Edge e = (Edge)itr.getCurrent();
				
				if (e.destination.theItem.equals(obj))
				{
					// Edge found
					return e;
				}
			}
			
			// Edge not found
			return null;
		}
		
		
		/**************************************
		 *	methods inherited from class Object
		 *************************************/
		
		/**
		 *	return whether the specified Object, which must be of type Vertex,
		 *	equals this Vertex
		 *
		 *	Two vertices are considered equal if they contain the same item.
		 */
		public boolean equals(Object o)
		{
			Vertex v = (Vertex)o;
			
			return this.theItem.equals(v.theItem);
		}
		
		
		/**
		 *	return a String representation of this Vertex
		 */
		public String toString()
		{
			Edge e;
			
			String result = (theItem == null ? "null" : theItem.toString() + ":\n");
			
			if (!adjacencyList.isEmpty())
			{
				result += "   Destinations:\n";
				
				for (Iterator itr = adjacencyList.iterator(); itr.hasNext(); )
				{
					result += "      " + ((Edge)itr.getCurrent()).toString() + "\n";
					itr.next();
				}
			}
			
			return result;
		}
	}
	
	
	/**
	 *	inner class Edge
	 *
	 *	represents an edge connecting two vertices in a graph
	 *
	 *	contains a reference to a destination Vertex, and a numeric edge cost
	 */
	private class Edge
	{
		
		/*************
		 *	attributes
		 ************/
		
		/** this Edge's destination Vertex */
		Vertex destination;
		
		/** cost associated with this Edge */
		double cost;
		
		
		/***************
		 *	constructors
		 **************/
		
		/**
		 *	Create a new Edge with the specified destination Vertex and the
		 *	specified cost.
		 */
		public Edge(Vertex dest, double edgeCost)
		{
			destination = dest;
			cost = edgeCost;
		}
		
		
		/**************************************
		 *	methods inherited from class Object
		 *************************************/
		
		/**
		 *	Return a String representation of this Edge.
		 */
		public String toString()
		{
			return (destination == null ? "null" : destination.theItem + " (cost = " + cost + ")");
		}
		
		
		/**
		 *	Return whether the specified Edge equals this Edge
		 *
		 *	Two edges are considered equal if they have the same destination
		 *	and the same cost.
		 */
		public boolean equals(Object o)
		{
			Edge e = (Edge)o;
			
			return (cost == e.cost) && destination.equals(e.destination);
		}
	}
	
	
	/**
	 *	Inner class Path
 	 *
 	 *	Represents an entry in the priority queue for weighted shortest path
 	 *	(Djikstra's) algorithm.
 	 */
	public class Path implements Comparable
	{
		
		/*************
		 *	attributes
		 ************/
		
		/** destination Vertex */
		private Vertex destination;
		
		/** cost to destination */
		private double cost;
	
	
		/***************
		 *	constructors
		 **************/
	
		/**
	 	 * Create a new Path with destination Vertex d and cost c.
		 */
		public Path(Vertex d, double c)
		{
			destination = d;
			cost = c;
		}
		
		
		/**********************************************
		 *	methods inherited from interface Comparable
		 *********************************************/
	
		/**
	 	 * Implementation for comparing two Path objects.
	 	 *
	 	 * Returns:
	 	 *		-1 if cost for this Path < cost for otherPath
	 	 *		 0 if cost for this Path == cost for otherPath
	 	 *		 1 if cost for this Path > cost for otherPath
	 	 */
		public int compareTo(Object otherPath)
		{
			double otherCost = ((Path)otherPath).cost;
		
			return cost < otherCost ? -1 : cost > otherCost ? 1 : 0;
		}
	}

}