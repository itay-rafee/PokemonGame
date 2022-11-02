package api;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class DWGraph_DS implements directed_weighted_graph{
    private HashMap<Integer,node_data> _graph; // nodes of this graph
    private HashMap<Integer, HashMap<Integer, edge_data>> _edges; // edges of this graph
    private HashMap<Integer, HashSet<Integer>> _reversEdges; // edges against the direction
    private int _edgeSize;
    private int _mc;
    
    /* Copy Constructor */
    public DWGraph_DS(){
        _graph = new HashMap<>();
        _edges = new HashMap<>();
        _reversEdges = new HashMap<>();
        _edgeSize = 0;
        _mc = 0;
    }
    
    	/* Copy Constructor (Hard Copy) */
	/** This constructor is mainly used by copy() method in WGraph_Algo class
	 * The implement this method is by Hashtables represents nodes, edges and reversed edges.
	 * In the first loop initialize the Hashtables:
	 * 		first add each vertex to each Hashtable.
	 *  then perform iterations on the neighbors of the vertices of the old graph
	 *  and isomorphically add to the new graph the same list of edges by the edge_data
	 *  copy constructor.
	 *  Finally copy the 'MC', 'degree' variables.
	 *  (MC = The number of actions required to create the graph [g.nodeSize() + g.edgeSize()]).
	 */
    public DWGraph_DS(directed_weighted_graph g){
    	this();
    	Collection<node_data> gNi = g.getV();
    	gNi.forEach(node -> {
        	node_data temp = new NodeData((NodeData) node);
		_graph.put(temp.getKey(), temp);
		_edges.put(temp.getKey(), new HashMap<>());
		_reversEdges.put(temp.getKey(), new HashSet<>());
		});
    	gNi.forEach(node -> {
		g.getE(node.getKey()).forEach(e -> {
			this._edges.get(e.getSrc()).put(e.getDest(), new EdgeData(e));
			this._reversEdges.get(e.getDest()).add(e.getSrc());
			});
		});
		this._mc = g.nodeSize() + g.edgeSize();
		this._edgeSize = g.edgeSize();
    }
    
        /**
	 * About getNode(int key) method: this method return the node_data by the
	 * node_id. the method return null if none as requested.
	 * @param key - the node_id
     * @return the node_data by the node_id, null if none.
	 */
    @Override
    public node_data getNode(int key) {
        if (_graph.containsKey(key))
        	return _graph.get(key);
        return null;
    }
    
        /**
	 * About getEdge(int src, int dest) method: 
	 * this method return the edge_data object.
	 * the method return -1 if node1 or node2 aren't in the graph.
	 * the method return -1 if node1==node2 (There is no edge from a Node to itself).
	 * the method return null in case there is no such edge.
	 * the implementation of the method is simply by 'get' method in '_graph' Hashmap
	 * [O(1)].
	 * @param src
     * @param dest
     * @return
	 */
    @Override
    public edge_data getEdge(int src, int dest) {
    	if (!_graph.containsKey(src)||!_graph.containsKey(dest)||src==dest) return null; // exception is better
        return _edges.get(src).get(dest); // exception is better instead of null if no edge
    }
    
        /**
	 * About addNode(node_data n) method:
	 * this method add a new node to the graph with the given node_data object.
	 * if n==null or there is already a node with such a key no action performed.
	 * the method is implemented by adding the node to each Hashmaps in this class.
	 * if the method is implemented we update the MC (Mode Count).
	 * @param n
	 */
    @Override
    public void addNode(node_data n) {
    	// if n==null or this graph contains node with such key -> do nothing.
    	if (n==null||this._graph.containsKey(n.getKey())) return;
        int key = n.getKey();
        _graph.put(key,n);
        // define new place to the node in the _ni
        _edges.put(key,new HashMap<>());
        // define new place to the node in the _ni
        _reversEdges.put(key,new HashSet<>());
        _mc++;
    }
    
        /**
	 * About connect(int node1, int node2, double w) method:
	 * this method connect an edge between node1 and node2, with an edge with weight>=0.
	 * the method implemented only if node1 or node2 in the graph, node1!=node2 and w>=0. 
	 * if the edge node1-node2 already exists the method updates the weight of the edge.
	 * (in case the existing edge weight == w no action performed).
	 * if there is no such edge the method adds each Node to the list of neighbors
	 * [Hashmap] of the other Node [O(1)] and updates 'degree' variable.
	 * if any action performed the method updates 'MC' variable.
	 */
    @Override
    public void connect(int src, int dest, double w) {
    	if (!_graph.containsKey(src)||!_graph.containsKey(dest)||src==dest||w<0) return; // w==0 valid input? read interface
    	HashMap<Integer, edge_data> srcNi = _edges.get(src);
    	edge_data e = new EdgeData(src,dest,w);
    	if (srcNi.containsKey(dest)) { // edge existe, check the weight
    		if (srcNi.get(dest).getWeight()==w) return; // same as input >> do nothing (MC stays the same)
    		srcNi.put(dest,e); // update edge only
    	} else { // there no such edge
    		srcNi.put(dest,e);
    		_reversEdges.get(dest).add(src);
    		_edgeSize++; // a new edge added
    	}
        _mc++;
    }
    
        /**
	 * About getV() method: This method return a pointer (shallow copy) for the
	 * collection representing all the nodes in the graph. the method is implemented
	 * by returning '_graph' HashMap values.
	 * @return Collection<node_data>
	 */
    @Override
    public Collection<node_data> getV() {
        return _graph.values();
    }
    
        /**
	 * About getE(int node_id) method: This method returns a collection containing
	 * all the edges connected to node_id. the method returning null if
	 * node_id isn't in the graph. The implementation of the method is simply by
	 * returning the HashMap values of node_id neighbors.
	 * @return Collection<node_data>
	 */
    @Override
    public Collection<edge_data> getE(int node_id) {
    	if (!_graph.containsKey(node_id)) return null; // returning null ???
        return _edges.get(node_id).values();
    }
    
        /**
	 * About removeNode(int key) method: this method delete the node (with the given
	 * ID) from the graph - and removes all edges which starts or ends at this node.
	 * the method return null if the Node isn't in the graph as requested. otherwise
	 * the method performs an iteration on all the neighbors of the Node [O(n)]
	 * and removes his connected edges one by one
	 * while each step updates the MC (Mode Count). 
	 * afterwards the method removes the Node itself [O(1)] while returning
	 * pointer to the node and update the MC (Mode Count) once again.
	 * (another solution on commit no. 736f2897)
	 * @return the data of the removed node (null if none).
     * @param key
	 */
 	@Override
    public node_data removeNode(int key) {
        // while the node not exists
        if (!_graph.containsKey(key)) return null;
        // delete the reversed edges to other nodes marked by _niRevers
        this._edges.get(key).forEach((k, v) -> {
        	_reversEdges.get(k).remove(key);
        	// (no need to update mc\edgeSize because _niRevers used for mark only)
		});
        //delete the edge from key to others
        int niSize = _edges.get(key).size();
        _edges.remove(key);
        _edgeSize -= niSize; 
        _mc += niSize;
        //delete the edge from others to key
        _edgeSize -= this._reversEdges.get(key).size();
        this._reversEdges.get(key).forEach(e -> {
        	_edges.get(e).remove(key);
        	_mc++;
		});
        _mc++;
        // remove the node
        return _graph.remove(key);
    }
 	
        /**
	 * About removeEdge(int src, int dest) method: This method delete the edge
	 * from the graph. the method returns null if src node or dest node aren't in
	 * the graph. if src node and dest node are neighbors the method removes the edge
	 * between them by removing each Node from the list of neighbors [Hashmap] of
	 * the other one [O(1)]. afterwards the method update Degree variable used for
	 * counting the edges and also update the MC (Mode Count).
	 * @param src
     * @param dest
	 */
    @Override
    public edge_data removeEdge(int src, int dest) {
        //while the edge not exists
        if (!_graph.containsKey(src) || !_graph.containsKey(dest)
                || !_edges.get(src).containsKey(dest)) return null;
        //remove the edge
        this._edgeSize--;
        _reversEdges.get(dest).remove(src);
        _mc++;
        return _edges.get(src).remove(dest);
    }

        /**
	 * About nodeSize() method: this method return the number of vertices (nodes) in
	 * the graph. The implementation of the method is simply by returning '_graph'
	 * size [O(1)].
	 * @return number of node in graph
	 */
    @Override
    public int nodeSize() {
        return _graph.size();
    }

        /**
	 * About edgeSize() method: this method return the number of edges
	 * (directional graph). The implementation of the method is simply by
	 * returning node neighbors size [Hashmap > O(1)].
	 * @return
	 */
    @Override
    public int edgeSize() {
        return _edgeSize;
    }

        /**
	 * About getMC() method: this method return the Mode Count - for testing changes
	 * in the graph. The implementation of the method is simply by returning 'MC'
	 * variable.
	 * @return
	 */
    @Override
    public int getMC() {
        return _mc;
    }
    
        /**
	 * About equals(Object g) method: this is an auxiliary method mainly for testing
	 * copy()/save()/load() methods. the method returns false if the input object
	 * isn't instanceof directed_weighted_graph.
	 * otherwise calls equals(directed_weighted_graph g) method [below].
	 * @param g
	 * @return boolean
	 */
    @Override
	public boolean equals(Object g) {
    	if (g == this) { return true; } 
    	if (g instanceof directed_weighted_graph) {
			return this.equals((directed_weighted_graph) g);
		}
		return false;
	}

        /**
	 * About equals(directed_weighted_graph g) method:
	 * this method compares this graph with the input graph.
	 * if this nodes size != g nodes size or this edges size != g edges size - return false
	 * (the graphs obviously not equals).
	 * otherwise the method performs iteration on all g vertices:
	 * 		for each vertex - the method check if this graph contains vertex with same key
	 * 		and checks equality using node 'equals' method.
	 * 		if g vertex neighbors size != this vertex neighbors size - return false
	 * 		(the graphs obviously not equals).
	 * 		otherwise, the method perform iteration on the vertex neighbors and checks existence
	 * 		and equality of each neighbor using edge 'equals' method.
	 * 	# There is no need to check equation between MC variables because the purpose of the test
	 * 	# is equality between the graphs and not for the number of operations performed on them.
	 * @param g
	 * @return boolean
	 */
	public boolean equals(directed_weighted_graph g) {
		Collection<node_data> gNi = g.getV();
		if (this.edgeSize() != g.edgeSize() || this._graph.size() != gNi.size())
			return false;
		node_data tempOne;
		edge_data tempTwo;
		for (node_data node : gNi) {
			tempOne = this._graph.get(node.getKey());
			if (tempOne == null || !tempOne.equals(node))
				return false;
			Collection<edge_data> nodeE = g.getE(node.getKey());
			if (nodeE.size() != _edges.get(node.getKey()).size()) {return false;}
			for (edge_data e : nodeE) {
				tempTwo = _edges.get(tempOne.getKey()).get(e.getDest());
				if (tempTwo == null || !e.equals(tempTwo))
					return false;
			}
		}
		return true;
	}
	
	/*
	 toString() method 
	 The implementation of this method is mainly for testing purposes 
	 The method is overridden so that the interface does not need to be changed 
	@Override
	public String toString() { // Print by keys printGraph
		System.out.println(" ______________________________________________");
		// System.out.println("| #############################################");
		System.out.println("| ############### Print By Keys ###############");
		_graph.forEach((k, v) -> {
			System.out.print("| NodeKey = " + v.getKey() + 
					" (GL:"+ v.getLocation() + ") " + ". NeighborsKeys = ");
			this.getE(k).forEach(node -> {
				System.out.print(node.getDest()+", ");
				//System.out.print(" (weight:" + edges.get(v).get(node) + "), ");
			});
			System.out.println();
		});
		System.out.println("| ############### Print By Node ###############");
		_graph.forEach((k, v) -> {
			System.out.print("| NodeKey = " + v + 
					" (GL:"+ v.getLocation() + ") " + ". NeighborsKeys = ");
			this.getE(k).forEach(node -> {
				System.out.print(node+", ");
				//System.out.print(" (weight:" + edges.get(v).get(node) + "), ");
			});
			System.out.println();
		});
		System.out.println("|______________________________________________");
		return "";
	}
	*/
}
