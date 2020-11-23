package api;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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

    @Override
    public double shortestPathDist(int src, int dest) {
        return 0;
    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        return null;
    }

    @Override
    public boolean save(String file) {
        return false;
    }

    @Override
    public boolean load(String file) {
        return false;
    }
}
