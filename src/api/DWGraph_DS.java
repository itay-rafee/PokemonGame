package api;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class DWGraph_DS implements directed_weighted_graph{

    //here we put the edges with the direction
    private HashMap<Integer, HashMap<Integer, edge_data>> _ni;

    //here we put the edges against the direction
    private HashMap<Integer, HashSet<Integer>> _niRevers;

    //here we put the node
    private HashMap<Integer,node_data> _graph;

    private int _edgeSize;
    private int _mc;

    public DWGraph_DS(){
        _graph = new HashMap<>();
        _ni = new HashMap<>();
        _niRevers = new HashMap<>();
        _edgeSize = 0;
        _mc = 0;
    }
    
    /* Copy Constructor (Hard Copy) */
	/* This constructor is mainly used by copy() method in DWGraph_Algo class */
    public DWGraph_DS(directed_weighted_graph g){
    	this();
    	Collection<node_data> gNi = g.getV();
    	gNi.forEach(node -> {
        	node_data temp = new NodeData((NodeData) node);
			_graph.put(temp.getKey(), temp);
			_ni.put(temp.getKey(), new HashMap<>());
			_niRevers.put(temp.getKey(), new HashSet<>());
		});
    	gNi.forEach(node -> {
			g.getE(node.getKey()).forEach(e -> {
				this._ni.get(e.getSrc()).put(e.getDest(), new EdgeData(e));
				this._niRevers.get(e.getDest()).add(e.getSrc());
			});
		});
		this._mc = g.nodeSize() + g.edgeSize();
		this._edgeSize = g.edgeSize();
    }

    @Override
    public node_data getNode(int key) {
        return _graph.get(key); // exception is better instead of null if no such node
    }

    @Override
    public edge_data getEdge(int src, int dest) {
    	if (!_graph.containsKey(src)||!_graph.containsKey(dest)||src==dest) return null; // exception is better
        return _ni.get(src).get(dest); // exception is better instead of null if no edge
    }

    @Override
    public void addNode(node_data n) {
    	// if n==null or this graph contains node with such key -> do nothing.
    	if (n==null||this._graph.containsKey(n.getKey())) return;
        int key = n.getKey();
        _graph.put(key,n);
        // define new place to the node in the _ni
        _ni.put(key,new HashMap<>());
        // define new place to the node in the _ni
        _niRevers.put(key,new HashSet<>());
    }

    @Override
    public void connect(int src, int dest, double w) {
    	if (!_graph.containsKey(src)||!_graph.containsKey(dest)||src==dest||w<0) return; // w==0 valid input? read interface
    	HashMap<Integer, edge_data> srcNi = _ni.get(src);
    	edge_data e = new EdgeData(src,dest,w);
    	if (srcNi.containsKey(dest)) { // edge existe, check the weight
    		if (srcNi.get(dest).getWeight()==w) return; // same as input >> do nothing (MC stays the same)
    		srcNi.put(dest,e); // update edge only
    	} else { // there no such edge
    		srcNi.put(dest,e);
    		_niRevers.get(dest).add(src);
    		_edgeSize++; // a new edge added
    	}
        _mc++;
    }

    @Override
    public Collection<node_data> getV() {
        return _graph.values();
    }

    @Override
    public Collection<edge_data> getE(int node_id) {
    	if (!_graph.containsKey(node_id)) return null; // returning null ???
        return _ni.get(node_id).values();
    }
    
    // Solution 1
    // another solution on commit no. 736f2897
 	@Override
    public node_data removeNode(int key) {
        //while the node not exists
        if (!_graph.containsKey(key)) return null;
        //delete the reversed edges (src=key dest=keyNeighbors) from _niRevers
        this._ni.get(key).forEach((k,v) -> {
        	_niRevers.get(k).remove(key);
		});
        _edgeSize -= _ni.get(key).size(); //delete the edge from key to others
        _ni.remove(key);
        //delete the edge from others to key
        _edgeSize -= this._niRevers.get(key).size();
        this._niRevers.get(key).forEach(e -> {
        	_ni.get(e).remove(key);
		});
        // need to check if 2 vertices with a two-way edge from one to the other
        // will count as 2 edges or as one edge (for updating _edgeSize variable)
        return _graph.remove(key); //remove the node
    }

    @Override
    public edge_data removeEdge(int src, int dest) {
        //while the edge not exists
        if (!_graph.containsKey(src) || !_graph.containsKey(dest)
                || !_ni.get(src).containsKey(dest)) return null;
        //remove the edge
        this._edgeSize--;
        _niRevers.get(dest).remove(src);
        return _ni.get(src).remove(dest);
    }

    @Override
    public int nodeSize() {
        return _graph.size();
    }

    @Override
    public int edgeSize() {
        return _edgeSize;
    }

    @Override
    public int getMC() {
        return _mc;
    }
    
    @Override
	public boolean equals(Object g) {
    	if (g == this) { return true; } 
    	if (g instanceof directed_weighted_graph) {
			return this.equals((directed_weighted_graph) g);
		}
		return false;
	}

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
			if (nodeE.size() != _ni.get(node.getKey()).size()) {return false;}
			for (edge_data e : nodeE) {
				tempTwo = _ni.get(tempOne.getKey()).get(e.getDest());
				if (tempTwo == null || !e.equals(tempTwo))
					return false;
			}
		}
		return true;
	}
	
	/* toString() method */
	/* The implementation of this method is mainly for testing purposes */
	/* The method is overridden so that the interface does not need to be changed */
	@Override
	public String toString() { // Print by keys printGraph
		System.out.println(" ______________________________________________");
		// System.out.println("| #############################################");
		System.out.println("| ############### Print By Keys ###############");
		_graph.forEach((k, v) -> {
			System.out.print("| NodeKey = " + v.getKey() + ". NeighborsKeys = ");
			this.getE(k).forEach(node -> {
				System.out.print(node.getDest()+", ");
				//System.out.print(" (weight:" + edges.get(v).get(node) + "), ");
			});
			System.out.println();
		});
		System.out.println("| ############### Print By Node ###############");
		_graph.forEach((k, v) -> {
			System.out.print("| NodeKey = " + v + ". NeighborsKeys = ");
			this.getE(k).forEach(node -> {
				System.out.print(node+", ");
				//System.out.print(" (weight:" + edges.get(v).get(node) + "), ");
			});
			System.out.println();
		});
		System.out.println("|______________________________________________");
		return "";
	}

    /////////////////////////////////////////
    //////// Edge Location class ////////////
    /////////////////////////////////////////
    public class EdgeLocation implements edge_location{
    	// This class should be accessible from outside 
    	// (the reason is because there are no setters in the inner class and also in the outer class)
    	// and hence this class is public
        private edge_data _edge;
        private double _ratio;
        
        public EdgeLocation(){
        	_edge = new EdgeData(); // EdgeData should be public to implement this
        	_ratio = 0;
        }
        
        public EdgeLocation(edge_data e, double r){
        	_edge = e;
        	_ratio = r;
        }
        
        /* Copy Constructor */
		/* This constructor is mainly used by deep copy methods */
        public EdgeLocation(EdgeLocation el){
        	_edge = new EdgeData(el.getEdge());
        	_ratio = el.getRatio();
        }
        
        @Override
        public edge_data getEdge() {
            return _edge;
        }

        @Override
        public double getRatio() {
            return _ratio;
        }
    }

    /////////////////////////////////////////
    //////////////// Edge class /////////////
    /////////////////////////////////////////
    public class EdgeData implements edge_data{
    	// This class should be accessible from outside
    	// (the reason is that when we approach the neighbors of an vertex from outside this class
    	// we get a list of edges. The only option to get the vertices is through this class [getDest() method]).
    	// and hence this class is public
        private int _src, _dest, _tag;
        private double _weight;
        private String _info;
        
        public EdgeData(){
            _src = 0;
            _dest = 0;
            _weight = 0;
            _tag = 0;
            _info = "";
        }
        
        public EdgeData(int src, int dest, double weight){
            _src = src;
            _dest = dest;
            _weight = weight;
            _tag = 0;
            _info = "";
        }
        
        /* Copy Constructor */
		/* This constructor is mainly used by deep copy methods */
        public EdgeData(edge_data e){
        	this();
        	if (e==null) return;
        	_src = e.getSrc();
            _dest = e.getDest();
            _weight = e.getWeight();
            _tag = e.getTag();
            _info = new String(e.getInfo());
        }

        @Override
        public int getSrc() {
            return _src;
        }

        @Override
        public int getDest() {
            return _dest;
        }

        @Override
        public double getWeight() {
            return _weight;
        }

        @Override
        public String getInfo() {
            return _info;
        }

        @Override
        public void setInfo(String s) {
        	// if (s==null) return; // risky on tests if accessible from outside
            _info = s;
        }

        @Override
        public int getTag() {
            return _tag;
        }

        @Override
        public void setTag(int t) {
            _tag = t;
        }
        
        @Override
    	public boolean equals(Object e) {
        	if (e instanceof EdgeData) {return this.equals((EdgeData) e);} 
    		return false;
    	}

        public boolean equals(EdgeData e) {
    		//System.out.println("this.src = "+this._src+"."+" e.src = "+e._src);
    		//System.out.println("this.dest = "+this._dest+"."+" e.dest = "+e._dest);
    		//System.out.println("this.tag = "+this._tag+"."+" e.tag = "+e._tag);
    		//System.out.println("this.weight = "+this._weight+"."+" e.weight = "+e._weight);
    		if (this._src==e._src&&this._dest==e._dest&&this._tag==e._tag
    				&&this._weight==e._weight&&this._info.equals(e._info))
    			return true;
    		return false;
    	}
    }
}