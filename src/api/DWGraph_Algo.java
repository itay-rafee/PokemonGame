package api;

import java.util.List;

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

        return false;
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
