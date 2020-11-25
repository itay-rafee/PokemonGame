package api;

public class EdgeData implements edge_data{
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