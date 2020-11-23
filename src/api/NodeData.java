package api;


public class NodeData implements node_data {
    /////////////////////////////////////////
    //////////////// Node class /////////////
    /////////////////////////////////////////

    private int _key, _tag;
    private double _weight;
    private geo_location _location;
    private String _info;

    public NodeData(int key) {
        _key = key;
        _weight = 0;
        _tag = 0;
        _location = null;
        _info = null;
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
        _location = p;
    }

    @Override
    public double getWeight() {
        return _weight;
    }

    @Override
    public void setWeight(double w) {
        _weight = w;
    }

    @Override
    public String getInfo() {
        return _info;
    }

    @Override
    public void setInfo(String s) {
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


    /////////////////////////////////////////
    ////////Node Location class /////////////
    /////////////////////////////////////////
    private class GeoLocation implements geo_location {
        private double _x, _y, _z, _distance;

        public GeoLocation(double x, double y, double z) {
            //// _distance = ?? ////
            _x = x;
            _y = y;
            _z = z;
        }

        @Override
        public double x() {
            return _x;
        }

        @Override
        public double y() {
            return _y;
        }

        @Override
        public double z() {
            return _z;
        }

        @Override
        public double distance(geo_location g) {
            /////// ?? ////////
            return _distance;
        }
    }
}
