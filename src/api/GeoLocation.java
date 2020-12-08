package api;

import gameClient.util.Point3D;

/////////////////////////////////////////
////////Node Location class /////////////
/////////////////////////////////////////
public class GeoLocation implements geo_location {
    // This class should be accessible from outside
    // (the reason is because we initialize a GeoLocation before adding it to an NodeData)
    // and hence this class is public
    private double _x, _y, _z, _distance;

    public GeoLocation(){
        _distance = 0;
        _x = 0;
        _y = 0;
        _z = 0;
    }

    public GeoLocation(double x, double y, double z){
        //// _distance = ?? ////
        _x = x;
        _y = y;
        _z = z;
    }
    
    /* Copy Constructor */
	/* This constructor is mainly used by deep copy methods */
    public GeoLocation(geo_location gl){
    	_distance = distance(gl); // ??? have no idea how to deep copy it
        _x = gl.x();
        _y = gl.y();
        _z = gl.z();
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

    /*@Override
    public double distance(geo_location g) {
        /////// ?? ////////
        return _distance;
    }*/
    
    @Override
    public double distance(geo_location g) {
        double dx = this.x() - g.x();
        double dy = this.y() - g.y();
        double dz = this.z() - g.z();
        double t = (dx*dx+dy*dy+dz*dz);
        return Math.sqrt(t);
    }

    
    public double distance(Point3D g) {
        double dx = this.x() - g.x();
        double dy = this.y() - g.y();
        double dz = this.z() - g.z();
        double t = (dx*dx+dy*dy+dz*dz);
        return Math.sqrt(t);
    }

    
    @Override
	public boolean equals(Object gl) {
    	if (gl == this) { return true; } 
		if (gl instanceof GeoLocation) {return this.equals((GeoLocation) gl);} 
		return false;
	}

	public boolean equals(GeoLocation gl) {
		if (this._x==gl._x&&this._y==gl._y&&this._z==gl._z) // distance ???
			return true;
		return false;
	}
	
	@Override
	public String toString() {
		return ""+_x+", "+_y+", "+_z;
	}
	
	
}