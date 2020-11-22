package api;

import java.util.List;

public class DWGraph_Algo implements dw_graph_algorithms{
    @Override
    public void init(directed_weighted_graph g) {

    }

    @Override
    public directed_weighted_graph getGraph() {
        return null;
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
