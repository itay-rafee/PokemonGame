package api;

import gameClient.util.Point3D;

public class GeoLocation implements geo_location {
    private double _x, _y, _z;

    /* Constructor */
    public GeoLocation(){
        _x = 0;
        _y = 0;
        _z = 0;
    }

    /* Constructor */
    public GeoLocation(double x, double y, double z) {
        _x = x;
        _y = y;
        _z = z;
    }
    
    /* Copy Constructor */
	/* This constructor is mainly used by deep copy methods */
    public GeoLocation(geo_location gl) {
        _x = gl.x();
        _y = gl.y();
        _z = gl.z();
    }

    /**
	 * About x() method:
	 * this method return the x variable associated with this GeoLocation.
	 * @return
	 */
    @Override
    public double x() {
        return _x;
    }

    /**
	 * About y() method:
	 * this method return the y variable associated with this GeoLocation.
	 * @return
	 */
    @Override
    public double y() {
        return _y;
    }

    /**
	 * About z() method:
	 * this method return the z variable associated with this GeoLocation.
	 * @return
	 */
    @Override
    public double z() {
        return _z;
    }
    
    /**
	 * About distance(geo_location g) method:
	 * this method returns the distance between this geo_location
	 * to the input geo_location.
	 * @return
	 */
    @Override
    public double distance(geo_location g) {
        double dx = this.x() - g.x();
        double dy = this.y() - g.y();
        double dz = this.z() - g.z();
        double t = (dx*dx+dy*dy+dz*dz);
        return Math.sqrt(t);
    }
    
    /**
	 * About distance(Point3D g) method:
	 * this method returns the distance between this geo_location
	 * to the input Point3D object.
	 * @return
	 */
    public double distance(Point3D g) {
        double dx = this.x() - g.x();
        double dy = this.y() - g.y();
        double dz = this.z() - g.z();
        double t = (dx*dx+dy*dy+dz*dz);
        return Math.sqrt(t);
    }

    /**
	 * About equals(Object n) method: the method returns false if the input object
	 * isn't instanceof GeoLocation. otherwise calls equals(GeoLocation gl)
	 * method [below].
	 * @param gl
	 * @return boolean
	 */
    @Override
	public boolean equals(Object gl) {
    	if (gl == this) { return true; } 
		if (gl instanceof GeoLocation) {return this.equals((GeoLocation) gl);} 
		return false;
	}

    /**
	 * About equals(GeoLocation gl) method: this method compares
	 * this GeoLocation with the input GeoLocation.
	 * The implementation of the method is simply by checking
	 * equality of each GeoLocation variables.
	 * @param gl
	 * @return boolean
	 */
	public boolean equals(GeoLocation gl) {
		if (this._x==gl._x&&this._y==gl._y&&this._z==gl._z)
			return true;
		return false;
	}
	
	/**
	 * About toString() method:
	 * this method returns a string with the values (represents location)
	 * of this GeoLocation.
	 * @return String
	 */
	@Override
	public String toString() {
		return ""+_x+", "+_y+", "+_z;
	}
}