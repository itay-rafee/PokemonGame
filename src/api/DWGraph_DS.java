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

    public DWGraph_DS(){
        _graph = new HashMap<>();
        _ni = new HashMap<>();
        _niRevers = new HashMap<>();
        _edgeSize = 0;
        _mc = 0;
    }

    @Override
    public node_data getNode(int key) {
        return _graph.get(key);
    }

    @Override
    public edge_data getEdge(int src, int dest) {
        return _ni.get(src).get(dest);
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
        edge_data e = new EdgeDate(src,dest,w);
        _ni.get(src).put(dest,e);
        _niRevers.get(dest).add(src);
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
        if (!_graph.containsKey(key))return null;

        //delete the edge from key to others
        _edgeSize -= _ni.get(key).size();
        _ni.remove(key);

        //delete the edge from others to key
        Integer[] ed = _niRevers.get(key).toArray(new Integer[0]);
        _edgeSize -= ed.length;
        for (Integer key2 : ed) {
            _ni.get(key2).remove(key);
        }

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
    private class EdgeLocation implements edge_location{
        private edge_data _edge;
        private double _ratio;

        /// ????????? ///

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


    /** not sure it's in this class */
    /////////////////////////////////////////
    //////////////// Node class /////////////
    /////////////////////////////////////////
//    private class NodeData implements node_data{
//
//        private int _key, _tag;
//        private double _weight;
//        private geo_location _location;
//        private String _info;
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


//    /////////////////////////////////////////
//    ////////Node Location class /////////////
//    /////////////////////////////////////////
//    private class GeoLocation implements geo_location{
//        private double _x, _y, _z, _distance;
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
