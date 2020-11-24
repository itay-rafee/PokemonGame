package api;

/** not sure it's in this class */
/////////////////////////////////////////
//////////////// Node class /////////////
/////////////////////////////////////////
public class NodeData implements node_data{
    // This class should be accessible from outside
    // (the reason is because we initialize a vertex before adding it to the graph)
    // and hence this class is public
    private static int keys = 0;
    private int _key, _tag;
    private double _weight;
    private geo_location _location;
    private String _info;

    public NodeData(){
        _key = keys++;
        _location = new GeoLocation();
        _weight = 0;
        _tag = 0;
        _info = "";
    }

    public NodeData(int key,geo_location location){
        _key = key;
        _location = location;
        _weight = 0;
        _tag = 0;
        _info = "";
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
