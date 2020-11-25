package api;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.*;
import java.util.*;

public class DWGraph_Algo implements dw_graph_algorithms{
    directed_weighted_graph _graph;

    public DWGraph_Algo(){
        _graph = new DWGraph_DS();
    }

    public DWGraph_Algo(directed_weighted_graph graph){
        _graph = graph;
    }

    @Override
    public void init(directed_weighted_graph g) {
        _graph = g;
    }

    @Override
    public directed_weighted_graph getGraph() {
        return _graph;
    }

    @Override
    public directed_weighted_graph copy() {
        return null;
    }
    
    
    // Check if a given directed graph is strongly connected (Kosaraju using BFS).
    // The directed graph will be considered connected if the original graph is connected and
    // also if after reversing the graph is still connected!
    // (reversing the graph by rotatating the edges in the opposite direction)
    // check out: https://www.geeksforgeeks.org/check-given-directed-graph-strongly-connected-set-2-kosaraju-using-bfs/
    @Override
    public boolean isConnected() {
    	directed_weighted_graph Original = _graph; // point to original graph to init it again after all.
    	directed_weighted_graph Fliped = flipedGraph(); // for checking if the reversed graph connected.
    	if (!this._graph.getV().iterator().hasNext()) // check if there is at least one node.
    		return true;
    	// the node to start impliment BFS on original and also on the reversed graph (must be the same node).
    	int startNode = this._graph.getV().iterator().next().getKey();
    	boolean original = isConnectedBFS(startNode); // original graph connected result.
    	this.init(Fliped); // init temporarily to reversed graph (so we can call methods).
    	boolean fliped = isConnectedBFS(startNode); // reversed graph connected result.
    	this.init(Original); // init to original graph again.
    	return original&&fliped; // return true only if original&&fliped connected.
    }
    
    // No comments. same BFS algo as on Ex1.
    public boolean isConnectedBFS(int startNode) { 
        HashMap<node_data, Boolean> vis = new HashMap<>();
        Queue<node_data> q = new LinkedList<node_data>();
        if (!this._graph.getV().iterator().hasNext())
            return vis.size() == this._graph.getV().size();
        node_data first = this._graph.getNode(startNode);
        node_data current = first;
        q.add(current);
        vis.put(current, true);
        while (!q.isEmpty()) {
            current = (node_data) q.remove();
            for (edge_data node : this._graph.getE(current.getKey())) {
                if (!vis.containsKey(_graph.getNode(node.getDest()))) {
                    q.add(_graph.getNode(node.getDest()));
                    vis.put(_graph.getNode(node.getDest()), true);
                }
            }
        }
        return vis.size() == this._graph.getV().size();
    }
    
    // copy to a new reversed graph by rotatating the edges in the opposite direction
    public directed_weighted_graph flipedGraph() {
    	directed_weighted_graph temp = new DWGraph_DS();
    	for (node_data node : this._graph.getV())
    		temp.addNode(node);
    	for (node_data node : this._graph.getV())
    		for (edge_data e : this._graph.getE(node.getKey()))
    			temp.connect(e.getDest(), e.getSrc(), e.getWeight());
    	return temp;
    }

    /**
     * returns the length of the shortest path between src to dest
     * if no such path --> returns -1
     * @param src - start node
     * @param dest - end (target) node
     * @return distance of the shortest path between them
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        if (_graph.getNode(src) == null || _graph.getNode(dest) == null){
            return -1;//while one or two of the keys not exists
        }
        if(src == dest)return 0;
        //keep all the node that we path
        HashMap<node_data,HashMap<node_data,node_data>> thePath = new HashMap<>();
        //call the function that return the weight
        double w = shortestPathDist(src,dest,thePath);
        resetTag(thePath.keySet());
        return 1;//w;
    }

    /**
     * return the shortest path between src to dest
     * by using the algorithm Dijkstra
     * @param src  - start node
     * @param dest - end (target) node
     * @return List with node of the shortest path between src and dest
     */
    @Override
    public List<node_data> shortestPath(int src, int dest) {
        if (_graph.getNode(src) == null || _graph.getNode(dest) == null){
            return null;//while one or two of the keys not exists
        }
        List<node_data> theList = new LinkedList<>();
        if (src == dest) {
            theList.add(_graph.getNode(src));
            return theList;
        }
        HashMap<node_data,HashMap<node_data,node_data>> thePath = new HashMap<>();
        double w = shortestPathDist(dest,src,thePath);
        //if there is no path between them
        if (w == -1) {
            resetTag(thePath.keySet());
            return null;
        }
        //reset the tag of the node that we path
        resetTag(thePath.keySet());
        //the list of the path that return
        node_data nodeSrc = _graph.getNode(src);
        node_data nodeDest = _graph.getNode(dest);
        node_data n = nodeSrc;

        while (thePath.get(nodeDest).get(n) != nodeDest){
            theList.add(n);
            n = thePath.get(nodeDest).get(n);
        }
        theList.add(n);
        theList.add(nodeDest);
        return theList;
    }

    //// saving json by java-json ///

    @Override
    public boolean save(String file) {
        if (_graph == null || file == null)return false;
        int counter = 0;

        try{
            PrintWriter pw = new PrintWriter(new File(file));
            //
            JSONObject g = new JSONObject();
            JSONArray Edges = new JSONArray();
            Collection<node_data> nodes = _graph.getV();

            // add the edges
            for (node_data n: nodes) {
                int src = n.getKey();
                Collection<edge_data> edge = _graph.getE(src);
                for (edge_data ed: edge) {
                    JSONObject e1 = new JSONObject();
                    e1.put("src",src);
                    e1.put("w", ed.getWeight());
                    e1.put("dest",ed.getWeight());
                    Edges.put(e1);
                }
            }

            //add the nodes
            JSONArray Nodes1 = new JSONArray();
            for (node_data n :nodes) {
                JSONObject e1 = new JSONObject();
                geo_location pos = n.getLocation();
                String s = pos.x()+","+pos.y()+","+pos.z();//the x, y, z.
                e1.put("pos", s);
                e1.put("id", n.getKey());
                Nodes1.put(e1);
            }
            g.put("Edge",Edges);
            g.put("Node",Nodes1);

            //
            pw.write(g.toString());
            pw.close();
            return true;
        }
        catch (FileNotFoundException | JSONException e){
            e.printStackTrace();
        }
        return false;
    }

    ////////// json by google ///////

//    public boolean save(String file) {
//        if (_graph == null || file == null)return false;
//        int counter = 0;
//
//        try{
//            PrintWriter pw = new PrintWriter(new File(file));
//            //
//            Gson gson = new Gson();
//            JsonObject g = new JsonObject();
//            JsonArray Edges = new JsonArray();
//            Collection<node_data> nodes = _graph.getV();
//            for (node_data n: nodes) {
//                int src = n.getKey();
//                Collection<edge_data> edge = _graph.getE(src);
//                for (edge_data ed: edge) {
//                    JsonObject e = new JsonObject();
//                    e.add("src",gson.toJsonTree(src));
//                    e.add("w",gson.toJsonTree(ed.getWeight()));
//                    e.add("dest", gson.toJsonTree(ed.getDest()));
//                    Edges.add(e);
//                }
//            }
//            JsonArray Nodes = new JsonArray();
//            for (node_data n :nodes) {
//                JsonObject e = new JsonObject();
//                geo_location pos = n.getLocation();
//                String s = pos.x()+","+pos.y()+","+pos.z();
//                e.add("pos",gson.toJsonTree(s));
//                e.add("id",gson.toJsonTree(n.getKey()));
//                Nodes.add(e);
//            }
//            g.add("Edge",Edges);
//            g.add("Nodes",Nodes);
//            //
//            pw.write(g.toString());
//            pw.close();
//            return true;
//        }
//        catch (FileNotFoundException | JSONException e){
//            e.printStackTrace();
//        }
//        return false;
//    }


    @Override
    public boolean load(String file) {
        if(file == null) return false;
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = "", s1 = "";
            while ((s1 = br.readLine()) != null)s = s + s1;
            directed_weighted_graph g = new DWGraph_DS();
            JSONObject graphFile = new JSONObject(s);
            JSONArray nodes = graphFile.getJSONArray("Nodes");

            int t = nodes.length();

            // add the nodes
            for (int i = 0; i < t; i++) {
                JSONObject node = nodes.getJSONObject(i);
                String pos = node.getString("pos");
                String[] xyz = pos.split(",");
                double x = Double.parseDouble(xyz[0]), y = Double.parseDouble(xyz[1]), z = Double.parseDouble(xyz[2]);
                geo_location geo = new GeoLocation(x,y,z);
                int id = (int)node.get("id");
                node_data n1 = new NodeData(id,geo);
                g.addNode(n1);
            }

            JSONArray edges = graphFile.getJSONArray("Edges");
            t = edges.length();

            //add the edges
            for (int i = 0; i < t; i++) {
                JSONObject ed = edges.getJSONObject(i);
                int src = ed.getInt("src"), dest = ed.getInt("dest");
                double w = ed.getDouble("w");
                g.connect(src,dest,w);
            }

            _graph = g;
            return true;
        }
        catch (IOException | JSONException e){
            System.out.println("as is");
            return false;
        }
    }



    ////////// private function //////////////

    /**
     * return the shortest path distance between
     * src to dest by using the algorithm dijkstra
     * we using with a PriorityQueue
     * first we put there the src and then we put all the
     * neighbor and the queue poll them by the weight
     * the weight we define with the 'tag'
     * Which is updated each time on the shortest weight
     * @param src - the starting point
     * @param dest - the eng point
     * @param thePath - the node that we path
     * @return the weight of the path between src to dest
     */
    private double shortestPathDist(int src, int dest, HashMap<node_data,HashMap<node_data,node_data>> thePath){//Complexity: O(n)
        PriorityQueue<node_data> pq = new PriorityQueue<>(new node());
        boolean flag = false;
        node_data nodeSrc = _graph.getNode(src);
        nodeSrc.setTag(1);
        thePath.put(nodeSrc,new HashMap<>());//
        pq.add(nodeSrc);
        while (!pq.isEmpty() && !flag){
            node_data node = pq.remove();
            int key1 = node.getKey();
            //when we find the dest and we break the loop
            if (key1 == dest){
                flag = true;
            }
//            else {
//                Collection<node_data> ni = _graph.getV(key1);
//                for (node_data n: ni){    //take the neighbors
//                    int key2 = n.getKey();
//                    double w = _graph.getEdge(key1,key2);
//                    if (n.getTag() == 0 || n.getTag() > node.getTag()+w){
//                        n.setTag(node.getTag()+w);    //set the weight of the node
//                        thePath.put(n,null);   //the node that his tag change
//                        thePath.get(nodeSrc).put(n,node);//order the path
//                        pq.add(n);
//                    }
//                }
//            }
        }
        if (flag) return _graph.getNode(dest).getTag()-1;
        return -1;
    }

    private void resetTag(Collection<node_data> node){ //reset the tag: tag = 0
        for (node_data n: node){
            n.setTag(0);
        }
    }

    /**
     * in this class we define the Comparator that compare between the nodes
     */
    private static class node implements Comparator<node_data> {//define comparator for node_info
        @Override
        public int compare(node_data o1, node_data o2) {
            return Double.compare(o1.getTag(), o2.getTag());
        }
    }
}
