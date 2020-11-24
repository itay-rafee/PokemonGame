package api;

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
	private static int keys; // static key for initialize nodes

    public DWGraph_DS(){
        _graph = new HashMap<>();
        _ni = new HashMap<>();
        _niRevers = new HashMap<>();
        _edgeSize = 0;
        _mc = 0;
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
        return _ni.get(node_id).values();
    }

    @Override
    public node_data removeNode(int key) {

        //while the node not exists
        if (!_graph.containsKey(key)) return null;

        //delete the edge from key to others
        _edgeSize -= _ni.get(key).size();
        _ni.remove(key);
        
        //delete the edge from others to key
        Integer[] ed = _niRevers.get(key).toArray(new Integer[0]);
        _edgeSize -= ed.length;
        for (Integer key2 : ed) {
            _ni.get(key2).remove(key);
        }
        // need to check if 2 vertices with a two-way edge from one to the other
        // will count as 2 edges or as one edge (for updating _edgeSize variable)
        
        //remove the node
        return _graph.remove(key);
    }

    @Override
    public edge_data removeEdge(int src, int dest) {
        //while the edge not exists
        if (!_graph.containsKey(src) || !_graph.containsKey(dest)
                || !_ni.get(src).containsKey(dest)) return null;

        //remove the edge
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



    /////////////////////////////////////////
    //////// Edge Location class ////////////
    /////////////////////////////////////////
    public class EdgeLocation implements edge_location{
    	// This class should be accessible from outside 
    	// (the reason is because there are no setters in the inner class and also in the outer class)
    	// and hence this class is public
        private edge_data _edge;
        private double _ratio;
        
        /*
        public EdgeLocation(){
        	_edge = new EdgeData(); // EdgeData should be public to implement this
        	_ratio = 0;
        }
        */
        
        public EdgeLocation(edge_data e, double r){
        	_edge = e;
        	_ratio = r;
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
    private class EdgeData implements edge_data{
    	// This class should be accessible from outside
    	// (the reason is that when we approach the neighbors of an vertex from outside this class
    	// we get a list of edges. The only option to get the vertices is through this class [getDest() method]).
    	// and hence this class is public
        private int _src, _dest, _tag;
        private double _weight;
        private String _info;

        public EdgeData(int src, int dest, double weight){
            _src = src;
            _dest = dest;
            _weight = weight;
            _tag = 0;
            _info = "";
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
    }

//
//    /** not sure it's in this class */
//    /////////////////////////////////////////
//    //////////////// Node class /////////////
//    /////////////////////////////////////////
//    public class NodeData implements node_data{
//    	// This class should be accessible from outside
//    	// (the reason is because we initialize a vertex before adding it to the graph)
//    	// and hence this class is public
//        private int _key, _tag;
//        private double _weight;
//        private geo_location _location;
//        private String _info;
//
//        public NodeData(){
//        	_key = keys++;
//        	_location = new GeoLocation();
//            _weight = 0;
//            _tag = 0;
//            _info = "";
//        }
//
//        @Override
//        public int getKey() {
//            return _key;
//        }
//
//        @Override
//        public geo_location getLocation() {
//            return _location;
//        }
//
//        @Override
//        public void setLocation(geo_location p) {
//
//        }
//
//        @Override
//        public double getWeight() {
//            return _weight;
//        }
//
//        @Override
//        public void setWeight(double w) {
//
//        }
//
//        @Override
//        public String getInfo() {
//            return _info;
//        }
//
//        @Override
//        public void setInfo(String s) {
//
//        }
//
//        @Override
//        public int getTag() {
//            return _tag;
//        }
//
//        @Override
//        public void setTag(int t) {
//            _tag = t;
//        }
//    }
//
//
//
//    /////////////////////////////////////////
//    ////////Node Location class /////////////
//    /////////////////////////////////////////
//    public class GeoLocation implements geo_location{
//    	// This class should be accessible from outside
//    	// (the reason is because we initialize a GeoLocation before adding it to an NodeData)
//    	// and hence this class is public
//        private double _x, _y, _z, _distance;
//
//        public GeoLocation(){
//            _distance = 0;
//            _x = 0;
//            _y = 0;
//            _z = 0;
//        }
//
//        public GeoLocation(double x, double y, double z){
//            //// _distance = ?? ////
//            _x = x;
//            _y = y;
//            _z = z;
//        }
//
//        @Override
//        public double x() {
//            return _x;
//        }
//
//        @Override
//        public double y() {
//            return _y;
//        }
//
//        @Override
//        public double z() {
//            return _z;
//        }
//
//        @Override
//        public double distance(geo_location g) {
//            /////// ?? ////////
//            return _distance;
//        }
//    }

}
