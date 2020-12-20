package gameClient;

import gameClient.util.Point3D;

import api.edge_data;

public class CL_Pokemon {
	private edge_data _edge;
	private double _value;
	private int _type;
	private Point3D _pos;
	private double min_dist;
	private int min_ro;
	public int pokNum;
	public static int counter;
	private double dist = Integer.MAX_VALUE;
	
	/* Constructor */
	public CL_Pokemon(Point3D p, int t, double v, double s, edge_data e) {
		_type = t;
	//	_speed = s;
		_value = v;
		set_edge(e);
		_pos = p;
		min_dist = -1;
		min_ro = -1;
		pokNum = counter++;
	}
	
	/**
	 * About toString() method:
	 * The method returns the info of the pokimon
	 * @return "F:{v="+_value+", t="+_type+"}"
	 */
	public String toString() {return "F:{v="+_value+", t="+_type+"}";}
	
	/**
	 * About get_edge() method:
	 * The method returns the edge of the pokimon
	 * @return this._edge
	 */
	public edge_data get_edge() {return this._edge;}
	
	/**
	 * About set_edge(edge_data _edge) method:
	 * The method sets the edge of the pokimon from the input variable
	 */
	public void set_edge(edge_data _edge) {this._edge = _edge;}
	
	/**
	 * About getLocation() method:
	 * The method returns the position of the pokimon
	 * @return this._pos
	 */
	public Point3D getLocation() {return this._pos;}
	
	/**
	 * About getType() method:
	 * The method returns the type of the pokimon
	 * @return this._type
	 */
	public int getType() {return this._type;}
	
	/*public double getSpeed() {return _speed;}*/
	
	/**
	 * About getValue() method:
	 * The method returns the value of the pokimon
	 * @return this._value
	 */
	public double getValue() {return this._value;}
	
	/**
	 * About getMin_dist() method:
	 * The method returns the minimum destination of the pokimon
	 * @return this.min_dist
	 */
	public double getMin_dist() {return this.min_dist;}
	
	/**
	 * About setMin_dist(double mid_dist) method:
	 * The method sets the minimum destination of the pokimon from the input variable
	 */
	public void setMin_dist(double mid_dist) {this.min_dist = mid_dist;}
	
	/**
	 * About getMin_ro() method:
	 * The method returns the minimum ro of the pokimon
	 * @return this.min_ro
	 */
	public int getMin_ro() {return this.min_ro;}

	/**
	 * About setMin_ro(int min_ro) method:
	 * The method sets the minimum ro of the pokimon from the input variable
	 */
	public void setMin_ro(int min_ro) {this.min_ro = min_ro;}
	
	/**
	 * About getPos() method:
	 * The method returns the position of the pokimon
	 * @return this._pos
	 */
	public Point3D getPos() {return this._pos;}
	
	/**
	 * About set_dist(double d) method:
	 * The method sets the destination of the pokimon from the input variable
	 */
	public void set_dist(double d) {this.dist = d;}
	
	/**
	 * About get_dist() method:
	 * The method returns the destination of the pokimon
	 * @return this.dist
	 */
	public double get_dist() {return this.dist;}
	
	/**
	 * About getPokNum() method:
	 * The method returns the pokimon number
	 * @return this.pokNum
	 */
	public int getPokNum() {return this.pokNum;}
}