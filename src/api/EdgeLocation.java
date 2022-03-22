package api;

///////////////////////////////////////////////////////////////////////
///////////////////////// Edge Location class /////////////////////////
///////////////////////////////////////////////////////////////////////
public class EdgeLocation implements edge_location{
    private edge_data _edge;
    private double _ratio;

    /* Constructor */
    public EdgeLocation(){
        _edge = new EdgeData(); // EdgeData should be public to implement this
        _ratio = 0;
    }

    /* Constructor */
    public EdgeLocation(edge_data e, double r){
        _edge = e;
        _ratio = r;
    }

    /* Copy Constructor */
    /* This constructor is mainly used by deep copy methods */
    public EdgeLocation(EdgeLocation el){
        _edge = new EdgeData(el.getEdge());
        _ratio = el.getRatio();
    }

    /**
     * About getEdge() method:
     * this method return the edge_data (object) associated with this EdgeLocation.
     * @return
     */
    @Override
    public edge_data getEdge() {
        return _edge;
    }

    /**
     * About getRatio() method:
     * this method returns the ratio variable of this EdgeLocation.
     * @return
     */
    @Override
    public double getRatio() {
        return _ratio;
    }
}