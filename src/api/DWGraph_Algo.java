package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.Map.Entry;

public class DWGraph_Algo implements dw_graph_algorithms{
    directed_weighted_graph _graph;

    public DWGraph_Algo() {
        _graph = new DWGraph_DS();
    }

    public DWGraph_Algo(directed_weighted_graph graph) {
    	if (graph==null) _graph = new DWGraph_DS(); // better avoid null on testers
    	else _graph = graph;
    }

    @Override
    public void init(directed_weighted_graph g) {
    	if (g==null) _graph = new DWGraph_DS(); // better avoid null on testers
    	else _graph = g;
    }

    @Override
    public directed_weighted_graph getGraph() {
        return _graph;
    }

    @Override
    public directed_weighted_graph copy() {
    	return new DWGraph_DS(this._graph);
    }
    
    
    // Check if a given directed graph is strongly connected (Kosaraju using BFS).
    // The directed graph will be considered connected if the original graph is connected and
    // also if after reversing the graph is still connected!
    // (reversing the graph by rotatating the edges in the opposite direction)
    // check out: https://www.geeksforgeeks.org/check-given-directed-graph-strongly-connected-set-2-kosaraju-using-bfs/
    @Override
    public boolean isConnected() {
    	directed_weighted_graph Original = _graph; // point to original graph to init it again after all.
    	if (!this._graph.getV().iterator().hasNext()) // check if there is at least one node.
    		return true;
    	// the node to start impliment BFS on original and also on the reversed graph (must be the same node).
    	int startNode = this._graph.getV().iterator().next().getKey();
    	boolean original = isConnectedBFS(startNode); // original graph connected result.
    	if (original==false) return false;
    	directed_weighted_graph Fliped = flipedGraph(); // for checking if the reversed graph connected.
    	this.init(Fliped); // init the reversed graph temporarily (so we can call methods).
    	boolean fliped = isConnectedBFS(startNode); // reversed graph connected result.
    	this.init(Original); // init to original graph again.
    	return fliped; // return true only if original&&fliped connected.
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
    
    // copy to a new reversed graph by rotatating the edges to the opposite direction
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
        if (_graph.getNode(src) == null || _graph.getNode(dest) == null)
            return -1;//while one or two of the keys not exists
        if(src == dest)return 0;
        //used in shortestPath()
        HashMap<node_data,node_data> thePath = new HashMap<>();
        //call the function that return the weight
        double w = shortestPathDist(src,dest,thePath);
        return w;
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
        if (_graph.getNode(src) == null || _graph.getNode(dest) == null)
            return null;//while one or two of the keys not exists
        List<node_data> theList = new LinkedList<>();
        if (src == dest) {
            theList.add(_graph.getNode(src));
            return theList;
        }
        HashMap<node_data,node_data> p = new HashMap<>();
        double w = shortestPathDist(src,dest,p);
        //if there is no path between them
        if (w == -1) return null;
        
        //the list of the path that return
        node_data nodeSrc = _graph.getNode(src);
        node_data nodeDest = _graph.getNode(dest);
        node_data n = nodeDest;
        Stack<node_data> opList = new Stack<>();
        while (p.get(n) != nodeSrc) {
            opList.push(n);
            n = p.get(n);
        }
        opList.push(n);
        opList.push(nodeSrc);
        while (!opList.isEmpty()) theList.add(opList.pop());
        return theList;
    }
    
    
    //// saving json by java-json ///
    @Override
    public boolean save(String file) {
        if (_graph == null || file == null)return false;
        int counter = 0;
        try {
            PrintWriter pw = new PrintWriter(new File(file));
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
                    e1.put("dest",ed.getDest());
                    Edges.put(e1);
                }
            }
            //add the nodes
            JSONArray Nodes1 = new JSONArray();
            for (node_data n :nodes) {
                JSONObject e1 = new JSONObject();
                geo_location pos = n.getLocation();
                String s = pos.x()+","+pos.y()+","+pos.z(); //the x, y, z.
                e1.put("pos", s);
                e1.put("id", n.getKey());
                Nodes1.put(e1);
            	}
            g.put("Edges",Edges);
            g.put("Nodes",Nodes1);
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
    

    /*
    @Override
    public boolean save(String file) {
    	Gson gson = new GsonBuilder().setPrettyPrinting().create();
    	String json = gson.toJson(this._graph);
    	
    	try {
    		PrintWriter pw = new PrintWriter(new File(file));
    		pw.write(json);
    		pw.close();
    		return true;
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    		return false;
    	}
    }
    
    @Override
    public boolean load(String file) {
    	try {
    		GsonBuilder builder = new GsonBuilder();
        	builder.registerTypeAdapter(DWGraph_DS.class, new GraphJsonDeserializer());
        	Gson gson = builder.create();
        	FileReader reader = new FileReader(file);
        	DWGraph_DS graph = gson.fromJson(reader, DWGraph_DS.class);
        	this._graph = graph;
        	return true;
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    		return false;
    	}
    }
    */

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
    private double shortestPathDist(int src, int dest,
        HashMap<node_data,node_data> thePath){//Complexity: O(n)
        PriorityQueue<node_w> pq = new PriorityQueue<>(new nodeComp());
        // node that we already check
        HashMap<Integer, node_w> ch = new HashMap<>();
        boolean flag = false;
        node_data nodeSrc = _graph.getNode(src);
        node_w n = new node_w(nodeSrc,1);
        ch.put(src,n);
        pq.add(n);
        while (!pq.isEmpty() && !flag){
            node_w n_w = pq.remove();
            node_data n1 = n_w.get_n();
            int key1 = n1.getKey();
            //when we find the dest and we break the loop
            if (key1 == dest){
                flag = true;
            }
            else {
                Collection<edge_data> ed = _graph.getE(key1);
                for (edge_data e: ed){
                    double w = e.getWeight();
                    int key2 = e.getDest();
                    node_data n2 = _graph.getNode(key2);
                    double wKey1 = ch.get(key1).get_w();
                    if (!ch.containsKey(key2) || ch.get(key2).get_w() > wKey1 + w){
                        n_w = new node_w(n2,wKey1 + w);
                        ch.put(key2,n_w);
                        thePath.put(n2,n1);
                        pq.add(n_w);
                    }
                }
            }
        }
        if (flag) return ch.get(dest).get_w()-1;
        return -1;
    }

    /**
     * in this class we define the Comparator that compare between the nodes
     */
    private static class nodeComp implements Comparator<node_w> {//define comparator
        @Override
        public int compare(node_w o1, node_w o2) {
            return Double.compare(o1.get_w(), o2.get_w());
        }
    }

    private static class node_w {
        private double _w;
        private node_data _n;
        public node_w(node_data n, double w){
            _n = n;
            _w = w;
        }
        public node_data get_n(){return _n;}
        public double get_w(){return _w;}
    }
    
    /*
    private static class GraphJsonDeserializer implements JsonDeserializer<DWGraph_DS>{
		@Override
		public DWGraph_DS deserialize(JsonElement json, Type arg1, JsonDeserializationContext arg2)
				throws JsonParseException {
			JsonObject jsonObject = json.getAsJsonObject();
			directed_weighted_graph graph = new DWGraph_DS();
			// add all the nodes from "_graph" hashmap (with GeoLocation objects).
			JsonObject _graph = jsonObject.get("_graph").getAsJsonObject();
			for (Map.Entry<String, JsonElement> set : _graph.entrySet()) {
				JsonElement nodes = set.getValue(); // the value of the hashmap as json element
				// take node variables
				int key = nodes.getAsJsonObject().get("_key").getAsInt();
				int tag = nodes.getAsJsonObject().get("_tag").getAsInt();
				double weight = nodes.getAsJsonObject().get("_weight").getAsDouble();
				String info = nodes.getAsJsonObject().get("_info").getAsString();
				// take geo_location object variables (related to this node)
				JsonElement geo_location = nodes.getAsJsonObject().get("_location").getAsJsonObject();
				double gl_x = geo_location.getAsJsonObject().get("_x").getAsDouble();
				double gl_y = geo_location.getAsJsonObject().get("_y").getAsDouble();
				double gl_z = geo_location.getAsJsonObject().get("_z").getAsDouble();
				//double gl_distance = locationElement.getAsJsonObject().get("_z").getAsDouble();
				geo_location gl = new GeoLocation(gl_x, gl_y, gl_z); // distance ???
				node_data node = new NodeData(key,tag,weight,info,gl);
				graph.addNode(node);
			}
			// add all the edges from "_ni" hashmap.
			JsonObject _ni = jsonObject.get("_ni").getAsJsonObject();
			for (Entry<String, JsonElement> niHashMap :  _ni.entrySet()) {
				JsonObject InnerHashMap = niHashMap.getValue().getAsJsonObject();
				for (Entry<String, JsonElement> innerHashMap : InnerHashMap.entrySet()) {
					JsonObject edges = innerHashMap.getValue().getAsJsonObject();  // the value of the inner hashmap as json element
					// take edge variables
					int eSrc = edges.getAsJsonObject().get("_src").getAsInt();
					int eDst = edges.getAsJsonObject().get("_dest").getAsInt();
					double weight = edges.getAsJsonObject().get("_weight").getAsDouble();
					int tag = edges.getAsJsonObject().get("_tag").getAsInt();
					String info = edges.getAsJsonObject().get("_info").getAsString();
					// create the edge using the graph
					graph.connect(eSrc, eDst, weight);
					// return the edge created and set tag&info variables.
					edge_data e = graph.getEdge(eSrc, eDst);
					e.setTag(tag);
					e.setInfo(info);
				}
			}
			return (DWGraph_DS) graph;
		}
    }
    */
}