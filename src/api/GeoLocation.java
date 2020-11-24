package api;

/////////////////////////////////////////
////////Node Location class /////////////
/////////////////////////////////////////
public class GeoLocation implements geo_location {
    // This class should be accessible from outside
    // (the reason is because we initialize a GeoLocation before adding it to an NodeData)
    // and hence this class is public
    private double _x, _y, _z, _distance;

    public GeoLocation() {
        _distance = 0;
        _x = 0;
        _y = 0;
        _z = 0;
    }

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