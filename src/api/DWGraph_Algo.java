package api;

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

    @Override
    public boolean isConnected() {
    	HashMap<node_data, Boolean> vis = new HashMap<>();
		Queue<node_data> q = new LinkedList<node_data>();
		if (!this._graph.getV().iterator().hasNext())
			return vis.size() == this._graph.getV().size();
		node_data first = this._graph.getV().iterator().next();
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
		boolean found = false;
		// Checks if one of the vertices we visited has a edge to the first vertex.
		for (node_data node : vis.keySet()) {
		    if (_graph.getEdge(node.getKey(), first.getKey())!=null) {
		    	found = true;
		    	break;
		    }
		}
		if (!found) return false;
		return vis.size() == this._graph.getV().size();
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
        if (src == dest){
            theList.add(_graph.getNode(src));
            return theList;
        }
        HashMap<node_data,HashMap<node_data,node_data>> thePath = new HashMap<>();
        double w = shortestPathDist(dest,src,thePath);
        //if there is no path between them
        if (w == -1){
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
    @Override
    public boolean save(String file) {
        return false;
    }

    @Override
    public boolean load(String file) {
        return false;
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
