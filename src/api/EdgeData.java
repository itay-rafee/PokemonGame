package api;

////////////////////////////////////////////////////////////////////////
////////////////////////////// Edge class //////////////////////////////
////////////////////////////////////////////////////////////////////////
public class EdgeData implements edge_data{
    private int _src, _dest, _tag;
    private double _weight;
    private String _info;

    /* Constructor */
    public EdgeData(){
        _src = 0;
        _dest = 0;
        _weight = 0;
        _tag = 0;
        _info = "";
    }

    /* Constructor */
    public EdgeData(int src, int dest, double weight){
        _src = src;
        _dest = dest;
        _weight = weight;
        _tag = 0;
        _info = "";
    }

    /* Copy Constructor */
    /* This constructor is mainly used by deep copy methods */
    public EdgeData(edge_data e){
        this();
        if (e==null) return;
        _src = e.getSrc();
        _dest = e.getDest();
        _weight = e.getWeight();
        _tag = e.getTag();
        _info = new String(e.getInfo());
    }

    /**
     * About getSrc() method:
     * this method return the source node key of this edge.
     * @return
     */
    @Override
    public int getSrc() {
        return _src;
    }

    /**
     * About getDest() method:
     * this method return the destination node key of this edge.
     * @return
     */
    @Override
    public int getDest() {
        return _dest;
    }

    /**
     * About getWeight() method:
     * this method returns the weight variable which can be used by algorithms.
     * @return
     */
    @Override
    public double getWeight() {
        return _weight;
    }

    /**
     * About getInfo(String s) method: this method returns the remark (meta
     * data) associated with this edge.
     * @return
     */
    @Override
    public String getInfo() {
        return _info;
    }

    /**
     * About setInfo(String s) method: this method allows changing the remark (meta
     * data) associated with this edge.
     * @param s
     */
    @Override
    public void setInfo(String s) {
        // if (s==null) return; // risky on tests if accessible from outside
        _info = s;
    }

    /**
     * About getTag() method: this method returns the temporal data (aka distance,
     * color, or state) which can be used by algorithms
     * @return
     */
    @Override
    public int getTag() {
        return _tag;
    }

    /**
     * About setTag(int t) method: this method allow setting the "tag" value for
     * temporal marking an edge (common practice for marking by algorithms).
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        _tag = t;
    }

    /**
     * About equals(Object n) method: the method returns false if the input object
     * isn't instanceof EdgeData. otherwise calls equals(EdgeData e)
     * method [below].
     * @param e
     * @return boolean
     */
    @Override
    public boolean equals(Object e) {
        if (e == this) { return true; }
        if (e instanceof EdgeData) {return this.equals((EdgeData) e);}
        return false;
    }

    /**
     * About equals(EdgeData e) method: this method compares
     * this edge with the input edge. The implementation of the method is simply by
     * checking equality of each edge variables.
     * @param e
     * @return boolean
     */
    public boolean equals(EdgeData e) {
        if (this._src==e._src&&this._dest==e._dest&&this._tag==e._tag
                &&this._weight==e._weight&&this._info.equals(e._info))
            return true;
        return false;
    }
}
