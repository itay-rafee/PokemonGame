package api;

// Splitted the NodeData class due to an static 'key' variable
public class NodeData implements node_data{
	private static int _key;
    private int _tag;
    private double _weight;
    private geo_location _location;
    private String _info;
    
    /* Constructor */
	public NodeData() {
		this._tag = -1;
		this._weight = 0;
		this._info = "";
		this._key = _key++;
		this._location = null;
	}
    
    @Override
    public int getKey() {
        return _key;
    }

    @Override
    public geo_location getLocation() {
        return _location;
    }

    @Override
    public void setLocation(geo_location p) {

    }

    @Override
    public double getWeight() {
        return _weight;
    }

    @Override
    public void setWeight(double w) {

    }

    @Override
    public String getInfo() {
        return _info;
    }

    @Override
    public void setInfo(String s) {

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
