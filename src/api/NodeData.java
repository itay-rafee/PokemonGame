package api;

public class NodeData implements node_data {
    private static int keys = 0;
    private int _key, _tag;
    private double _weight;
    private String _info;
    private geo_location _location;
    
    /* Constructor */
    public NodeData(){
        _key = keys++;
        _location = new GeoLocation();
        _weight = 0;
        _tag = 0;
        _info = "";
    }
    
    /* Constructor */
    public NodeData(int key,geo_location location){
    	this();
        _key = key;
        if (location==null) return;
        _location = location;
    }
    
    /* Constructor */
    public NodeData(int key, int tag, double weight, String info ,geo_location location){
        _key = key;
        _tag = tag;
        _weight = weight;
        if (info==null) _info = new String();
        else _info = info;
        if (location==null) _location = new GeoLocation();
        else _location = location;
    }
    
    /* Copy Constructor */
	/* This constructor is mainly used by deep copy methods */
    public NodeData(NodeData n){
    	this();
    	if (n==null) return;
        _key = n.getKey();
        _location = new GeoLocation(n.getLocation());
        _weight = n._weight;
        _tag = n._tag;
        _info = new String(n._info);
    }
    
    /**
	 * About getKey() method:
	 * this method return the key (id) associated with this node.
	 * @return
	 */
    @Override
    public int getKey() {
        return _key;
    }

    /**
	 * About getLocation() method:
	 * this method return the geo_location (object) associated with this node.
	 * @return
	 */
    @Override
    public geo_location getLocation() {
        return _location;
    }
    
    /**
	 * About setLocation(geo_location p) method:
	 * this method allows changing the geo_location (object) associated with this node.
	 * @param p
	 */
    @Override
    public void setLocation(geo_location p) {
    	if (p==null) this._location = new GeoLocation();
    	else this._location = p;
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
	 * About setWeight(double w) method:
	 * this method allow setting the "weight" variable which can be used by algorithms.
	 * @param w - the new value of the weight
	 */
    @Override
    public void setWeight(double w) {
    	this._weight = w;
    }

    /**
	 * About getInfo(String s) method: this method returns the remark (meta
	 * data) associated with this node.
	 * @return
	 */
    @Override
    public String getInfo() {
        return _info;
    }

    /**
	 * About setInfo(String s) method: this method allows changing the remark (meta
	 * data) associated with this node.
	 * @param s
	 */
    @Override
    public void setInfo(String s) {
    	if (s==null) this._info = new String();
    	else this._info = s;
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
	 * temporal marking an node (common practice for marking by algorithms).
	 * @param t - the new value of the tag
	 */
    @Override
    public void setTag(int t) {
        _tag = t;
    }
    
    /**
	 * About equals(Object n) method: the method returns false if the input object
	 * isn't instanceof NodeData. otherwise calls equals(NodeData n)
	 * method [below].
	 * @param n
	 * @return boolean
	 */
    @Override
	public boolean equals(Object n) {
    	if (n == this) { return true; } 
		if (n instanceof NodeData) {return this.equals((NodeData) n);} 
		return false;
	}

    /**
	 * About equals(NodeData n) method: this method compares
	 * this node with the input node. The implementation of the method is simply by
	 * checking equality of each node variables.
	 * @param n
	 * @return boolean
	 */
	public boolean equals(NodeData n) {
		if (this.getKey()==n.getKey()&&this.getTag()==n.getTag()&&this.getWeight()==n.getWeight()
				&&this.getInfo().equals(n.getInfo())&&this._location.equals(n.getLocation()))
			return true;
		return false;
	}
}