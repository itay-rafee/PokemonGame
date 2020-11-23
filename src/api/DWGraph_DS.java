package api;

import java.util.Collection;
import java.util.HashMap;

public class DWGraph_DS implements directed_weighted_graph {
    //here we put the edges
    private HashMap<Integer, HashMap<Integer, edge_data>> _ni;
    //here we put the node
    private HashMap<Integer,node_data> _graph;
    // private int _nodeSize; // simply _graph.size() ? maybe usless integer
    private int _edgeSize; // consider calculate degree beside edge
    private int _mc;

    public DWGraph_DS(){
        _graph = new HashMap<>();
        _ni = new HashMap<>();
        //_nodeSize = 0; // simply _graph.size() ? maybe usless integer
        _edgeSize = 0; // consider calculate degree beside edge
        _mc = 0;
    }

    @Override
    public node_data getNode(int key) {
        return _graph.get(key);
    }

    @Override
    public edge_data getEdge(int src, int dest) {
        return _ni.get(src).get(dest);
    } // Does it matter that there is a edge from src to dest and not from dest to src?
    
    @Override
    public void addNode(node_data n) {
    	///// i don't know if it's true that n is node_data /////
    	/// ---> seems like that.. ///
    	if (n==null) return;
    	_graph.put(n.getKey(), n);
    	_ni.put(n.getKey(), new HashMap<>());
    }

    // note, added one edge from src to dest but not from dest to src (seems to be usless at this point)
    @Override
    public void connect(int src, int dest, double w) {
    	if (!_graph.containsKey(src)||!_graph.containsKey(dest)||w<0) return; // w==0 valid input? read interface
    	if (_ni.get(src).containsKey(dest)) { // edge existe, check the weight
    		if (_ni.get(src).get(dest).getWeight()==w) return; // same as input > do nothing (MC stays the same)
    		else _ni.get(src).put(dest, new EdgeDate(src,dest,w)); // update edge only
    	} else { // there no such edge
    		_ni.get(src).put(dest, new EdgeDate(src,dest,w));
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
        return null;
    }

    @Override
    public edge_data removeEdge(int src, int dest) {
        if (!_graph.containsKey(src) || !_graph.containsKey(dest)
                || !_ni.get(src).containsKey(dest)) return null;
        return _ni.get(src).remove(dest);
    }

    @Override
    public int nodeSize() {
        //return _nodeSize;
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

    /** not sure it's in this class */
    /////////////////////////////////////////
    //////////////// Node class /////////////
    /////////////////////////////////////////
    
    // Splitted the NodeData class due to an static 'key' variable


    /////////////////////////////////////////
    //////// Edge Location class ////////////
    /////////////////////////////////////////
    private class EdgeLocation implements edge_location{
        private edge_data _edge;
        private double _ratio;



        @Override
        public edge_data getEdge() {
            return null;
        }

        @Override
        public double getRatio() {
            return 0;
        }
    }


    /////////////////////////////////////////
    //////////////// Edge class /////////////
    /////////////////////////////////////////
    private class EdgeDate implements edge_data{
        private int _src, _dest, _tag;
        private double _weight;
        private String _info;

        public EdgeDate(int src, int dest, double weight){
            _src = src;
            _dest = dest;
            _weight = weight;
            _tag = 0;
            _info = null;
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


    /////////////////////////////////////////
    ////////Node Location class /////////////
    /////////////////////////////////////////
    private class GeoLocation implements geo_location{
        private double _x, _y, _z, _distance;

        public GeoLocation(double x, double y, double z){
            //// _distance = ?? ////
            _x = x;
            _y = y;
            _z = z;
        }

        @Override
        public double x() {
            return _x;
        }

        @Override
        public double y() {
            return _y;
        }

        @Override
        public double z() {
            return _z;
        }

        @Override
        public double distance(geo_location g) {
            /////// ?? ////////
            return _distance;
        }
    }

}
