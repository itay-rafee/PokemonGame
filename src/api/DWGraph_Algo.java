package api;

import kotlin.Pair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

public class DWGraph_Algo implements dw_graph_algorithms{
    directed_weighted_graph _graph;
    
    /* Constructor */
    public DWGraph_Algo() {
        _graph = new DWGraph_DS();
    }
    
    /* Constructor */
    public DWGraph_Algo(directed_weighted_graph graph) {
        if (graph==null) _graph = new DWGraph_DS();
        else _graph = graph;
    }

	/**
	 * About init(directed_weighted_graph g) method:
	 * this method init the graph on which this set of algorithms operates on.
	 * if the given graph=null the method init an empty graph.
	 * @param g
	 */
    @Override
    public void init(directed_weighted_graph g) {
        // better avoid null on testers
        _graph = Objects.requireNonNullElseGet(g, DWGraph_DS::new);
    }
    
	/**
	 * About getGraph() method:
	 * this method return the underlying graph of which this class works.
	 * @return
	 */
    @Override
    public directed_weighted_graph getGraph() {
        return _graph;
    }
    
	/**
	 * About copy() method: this method compute a deep copy of this weighted graph.
	 * the method uses DWGraph_DS copy constructor to implement deep copy.
	 * @return
	 */
    @Override
    public directed_weighted_graph copy() {
        return new DWGraph_DS(this._graph);
    }
	/**
	 * About isConnected() method:
	 * this method returns true if there is a valid path from EVERY node
	 * to each (strongly connected) by Kosaraju algorithm (using BFS).
	 * The directed graph will be considered connected if the original graph
	 * is connected and also if after reversing the graph it still connected!
	 * (reversing the graph by rotatating the edges in the opposite direction)
	 * Source: https://www.geeksforgeeks.org/check-given-directed-graph-strongly-
	 * connected-set-2-kosaraju-using-bfs/
	 * if the graph is empty the function returns true.
	 * Otherwise the function selects a random vertex from which it checks whether
	 * the graph is connected using isConnectedBFS(int startNode) method [below].
	 * if true - the method calls flipedGraph() method [below] and check
	 * if the reversed graph is also connected from the same vertex.
	 * If indeed the 2 conditions are true then the graph is strongly connected.
	 * @return boolean
	 */
    @Override
    public boolean isConnected() {
        directed_weighted_graph Original = _graph; // point to original graph to init it again after all.
        if (!this._graph.getV().iterator().hasNext()) // check if there is at least one node.
            return true;
        // the node to start impliment BFS on original and also on the reversed graph (must be the same node).
        int startNode = this._graph.getV().iterator().next().getKey();
        boolean original = isConnectedBFS(startNode); // original graph connected result.
        if (!original) return false;
        directed_weighted_graph Fliped = flipedGraph(); // for checking if the reversed graph connected.
        this.init(Fliped); // init the reversed graph temporarily (so we can call methods).
        boolean fliped = isConnectedBFS(startNode); // reversed graph connected result.
        this.init(Original); // init to original graph again.
        return fliped; // return true only if original&&fliped connected.
    }

	/**
	 * About isConnectedBFS(int startNode) method:
	 * this method returns true if there is a valid path from startNode node to others.
	 * The method is based on the BFS algorithm.
	 * In this algorithm we use a data structure of a queue to store the vertices we "visit".
	 * In addition, we use a data structure of a HashMap to store the Nodes we "visited" before.
	 * so that we don't loop this Nodes again.
	 * Initially we use an iterator on a random Node in the graph and adding the Node to the queue.
	 * using the BFS algorithm we progress through the neighbor list of the first Node in the queue:
	 * 	mark the first Node on the queue as current.
	 * 	delete the current Node from the queue. 
	 * 	mark the current Node as visited (by adding the current Node to the HashMap).
	 * 	We loop on the neighbors of the current Node as long as the queue isn't empty:
	 * 		if we found a node that marked as visited => do nothing.
	 * 		else => we add the Node to the queue and mark the Node as visited.
	 * The method will stop when the loop "visits" all the Nodes that
	 * were connected to the first Node (where we started).
	 * If we visited the same number of Nodes as the total number of
	 * Nodes in the graph then indeed there is a valid path
	 * from startNode node to each.
	 * @return boolean
	 */
    private boolean isConnectedBFS(int startNode) {
        HashMap<node_data, Boolean> vis = new HashMap<>();
        Queue<node_data> q = new LinkedList<node_data>();
        if (!this._graph.getV().iterator().hasNext())
            return vis.size() == this._graph.getV().size();
        node_data current = this._graph.getNode(startNode);
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

    /**
     * About isConnectedBFS method:
     *   this method we using the same BFS algo as on DWGraph_Algo.
     * @param startNode - the game
     * @param g - the game
     * @return vis - the group of connect
     */
    public HashSet<node_data> isConnectedBFS(int startNode, directed_weighted_graph g) {
        HashSet<node_data> vis = new HashSet<>();
        Queue<node_data> q = new LinkedList<node_data>();
        node_data current = g.getNode(startNode);
        q.add(current);
        vis.add(current);
        while (!q.isEmpty()) {
            current = q.remove();
            for (edge_data node : g.getE(current.getKey())) {
                node_data dest = g.getNode(node.getDest());
                if (!vis.contains(dest)) {
                    q.add(dest);
                    vis.add(dest);
                }
            }
        }
        return vis;
    }
    
	/**
	 * About flipedGraph() method:
	 * this method returns a copy of this graph with reverse edges
	 * the implementation of this method is by copying the nodes to a new graph
	 * and then iterate on each node and copy it edges while rotating
	 * the edges to the opposite direction (switching between source and destination).
	 * @return directed_weighted_graph
	 */
    public directed_weighted_graph flipedGraph() {
        directed_weighted_graph temp = new DWGraph_DS();
        for (node_data node : this._graph.getV())
            temp.addNode(node);
        for (node_data node : this._graph.getV())
            for (edge_data e : this._graph.getE(node.getKey()))
                temp.connect(e.getDest(), e.getSrc(), e.getWeight());
        return temp;
    }

    public Collection<node_data> connected_component(int id1){
        if (_graph.getNode(id1) == null)return null;
        return findGroup(id1,new HashSet<>());
    }

    public HashSet<Collection<node_data>> connected_components(){
        HashSet<Collection<node_data>> allGroup = new HashSet<>();
        HashSet<node_data> vis = new HashSet<>();
        Collection<node_data> nodes = _graph.getV();
        //here we fine all the graphing component of a graph
        for (node_data n : nodes) {
            if (!vis.contains(n)){
                Collection<node_data> group = findGroup(n.getKey(),vis);
                allGroup.add(group);
            }
        }
        return allGroup;
    }

    /**
     * About findGroup() method:
     *   this method find the graphing component
     *   of the key.
     *   working by two BFS algo:
     *     one for the right graph.
     *     second for the fliped graph .
     *   then all the element that in the two
     *   group is the graphing component of key
     * @param key - the pokemons that in the group
     * @param vis - the node that have a graphing component
     * @return group - the graphing component
     */
    private Collection<node_data> findGroup (int key, HashSet<node_data> vis){
        Collection<node_data> group = new ArrayList<>();
        if (_graph.getE(key).isEmpty()){
            group.add(_graph.getNode(key));
            return group;
        }
        HashSet<node_data> ni1 = isConnectedBFS(key, _graph);
        directed_weighted_graph g2 = flipedGraph();
        HashSet<node_data> ni2 = isConnectedBFS(key, g2);
        for (node_data n1 : ni1) {
            if (ni2.contains(n1)) {
                group.add(n1);
                vis.add(n1);
            }
        }
        return group;
    }

    /**
     * About shortestPathDist() method:
     * in this method we using the algo dijkstra()
     * that return the shortest path distance
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
        double w = dijkstra(src,dest,thePath);
        return w;
    }

    /**
     * About shortestPath() method:
     * in this method we want to return all the nodes
     * one by one in order that gets the shortest path
     * in other words we return the shortest path between src to dest
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
        double w = dijkstra(src,dest,p);
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

    /**
     * About save(String file) method: this method saves this weighted directed
     * graph to the given file name in json format. this method returns true - iff the file was
     * successfully saved.
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) {
        if (_graph == null || file == null)return false;
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

    /**
     * About load(String file) method: this method load a graph to this graph
     * algorithm. if the file was successfully loaded - the underlying graph of this
     * class will be changed (to the loaded one), in case the graph was not loaded
     * the original graph remain "as is". this method returns true - iff the graph
     * was successfully loaded.
     * @param file - file name
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {
        if(file == null) return false;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = "", s1 = "";
            while ((s1 = br.readLine()) != null) s = s + s1;
            directed_weighted_graph g = new DWGraph_DS();
            JSONObject graphFile = new JSONObject(s);
            JSONArray nodes = graphFile.getJSONArray("Nodes");
            
            int t = nodes.length();
            
            // add the nodes
            for (int i = 0; i < t; i++) {
                JSONObject node = nodes.getJSONObject(i);
                geo_location geo = new GeoLocation();
                if (node.has("pos")){
                    String pos = node.getString("pos");
                    String[] xyz = pos.split(",");
                    double x = Double.parseDouble(xyz[0]), y = Double.parseDouble(xyz[1]), z = Double.parseDouble(xyz[2]);
                    geo = new GeoLocation(x,y,z);
                }
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
            br.close();
            return true;
        } catch (IOException | JSONException e) {
            System.out.println("as is");
            return false;
        }
    }

    ////////// private function //////////////

    /**
     * About dijkstra(int src, int dest) method:
     * This is an auxiliary method method for two methods: (We implemented both of the methods in the same way)
     * 	  an method to returns the length of the shortest path between src to dest.
     * 	  an method to returns a List of the shortest path between src to dest.
     * The method is based on dijkstra's algorithm.
     * In this algorithm we use a couple of data structures:
     * 	  an HashMap to store the vertices we "visit".
     * 	  an PriorityQueue to store the Nodes sorted by their weight value to source Node.
     * 	  an PriorityQueue to store the the neighbors of the Node on which iterations are performed (sorted).
     * 	  an HashMap to store the Nodes for building the output path - each node with his parent.
     * 	  an List for returning the output in the same data structure as requested.
     * Initially we add all the Nodes to the PriorityQueue with weight = Infinity, and src weight = 0.
     * using the dijkstra's algorithm we progress through the graph:
     * while the PriorityQueue isn't empty:
     * 	 mark the first Node on the queue (lowest weight value to source Node) as current.
     * 	 delete the current Node from the PriorityQueue.
     * 	 mark the current Node as visited (by adding the current Node to the HashMap).
     * 		We loop on the neighbors of the current Node (by their weight) as long as the queue isn't empty:
     * 		if a node that marked as visited founded  => remove from the neighbors PriorityQueue.
     * 		else =>
     * 			remove from the neighbors PriorityQueue.
     * 			Setting 't' variable = the weight from the current Node to the source Node.
     * 			update the weight of the current Node in case 't' is lower.
     * 			add the current Node to the HashMap that stores the Nodes for the output path.
     * 			// we store the Node as a key while his parent is the value. //
     * 			// that's means that through a Node we can return to his parent (progress one level) //
     * 			// (we need to store the shortest path = shortest levels to destination) //
     * 			mark the Node as visited.
     * The method will stop when the PriorityQueue is empty.
     * At the end of the loop:
     * 	 if the tag value of src Node = Infinity => return null (-1 for length); // can't reach destination.
     * 	 else => do nothing. // we reach'd destination Node.
     * Finally we build the path using the HashMap that stores the Nodes for the output path
     * // each Node is a key while his parent is the value. (so we can build levels from dest to src). //
     * 	start with destination Node:
     * 		while node!=null:
     * 			add the node to the output path List and progress to his parent through the HashMap.
     * - for returning a List of the shortest path between src to dest:
     * 	 we reverse the List to get src->dest path instead of dest->src path.
     * 	 then we return the List as requested.
     * - for returning the length of the shortest path:
     * 	 we remove one element of the List for getting the number of edged between src to dest (length).
     * 	 then we return the size of the List.
     * Complexity: O(n).
     * @param src
     * @param dest
     * @return List<node_info>
     */
    private double dijkstra(int src, int dest, HashMap<node_data,node_data> thePath) {
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
     * About nodeComp class:
     * in this class we define the Comparator that compare between the nodes
     * this comparison run by compare the weight of the
     * object type node_w
     */
    private static class nodeComp implements Comparator<node_w> {//define comparator
        @Override
        public int compare(node_w o1, node_w o2) {
            return Double.compare(o1.get_w(), o2.get_w());
        }
    }


    /**
     * About the node_w class:
     * in this class we have two object:
     *   _w type double
     *   _n type node_date
     * for every node we define the weight
     * that the node path from the src
     * this class is running for the function:
     *    shortestPathDist()
     *    shortestPath()
     * In addition we define the method:
     *    get_n -> return the node
     *    get_w -> return the weight
     */
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
}
