package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import org.json.JSONObject;

public class CL_Agent {
		public static final double EPS = 0.0001;
		/*private static int _count = 0;
		private static int _seed = 3331;*/
		private int _id;
		/*private long _key;*/
		private geo_location _pos;
		private double _speed;
		private edge_data _curr_edge;
		private node_data _curr_node;
		private final directed_weighted_graph _gg;
		private CL_Pokemon _curr_fruit;
		private long _sg_dt;
		private double _value;
		
		/* Constructor */
		public CL_Agent(directed_weighted_graph g, int start_node) {
			_gg = g;
			setMoney(0);
			this._curr_node = _gg.getNode(start_node);
			_pos = _curr_node.getLocation();
			_id = -1;
			setSpeed(0);
		}
		
		/**
		 * About update(String json) method:
		 * The method update the variable of this agent from the input json file
		 * @param json - new agent
		 */
		public void update(String json) {
			JSONObject line;
			try {
				// "GameServer":{"graph":"A0","pokemons":3,"agents":1}}
				line = new JSONObject(json);
				JSONObject ttt = line.getJSONObject("Agent");
				int id = ttt.getInt("id");
				if(id==this.getID() || this.getID() == -1) {
					if(this.getID() == -1) {_id = id;}
					double speed = ttt.getDouble("speed");
					String p = ttt.getString("pos");
					Point3D pp = new Point3D(p);
					int src = ttt.getInt("src");
					int dest = ttt.getInt("dest");
					double value = ttt.getDouble("value");
					this._pos = pp;
					this.setCurrNode(src);
					this.setSpeed(speed);
					if (!this.setNextNode(dest)) {
						System.out.println("There is not next node!");
					}
					this.setMoney(value);
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * About getSrcNode() method:
		 * The method returns the current node of the agent
		 * @return this._curr_node.getKey()
		 */
		//@Override
		public int getSrcNode() {return this._curr_node.getKey();}
		
		/**
		 * About toJSON() method:
		 * The method returns the info of the agent
		 * @return ans
		 */
		public String toJSON() {
			int d = this.getNextNode();
			return "{\"Agent\":{"
					+ "\"id\":"+ this._id+","
					+ "\"value\":"+ this._value+","
					+ "\"src\":"+ this._curr_node.getKey()+","
					+ "\"dest\":"+d+","
					+ "\"speed\":"+ this.getSpeed()+","
					+ "\"pos\":\""+_pos.toString()+"\""
					+ "}"
					+ "}";
		}
		
		/**
		 * About setMoney(double v) method:
		 * The method sets the money of the agent from the input variable
		 */
		private void setMoney(double v) {_value = v;}
		
		/**
		 * About setNextNode(int dest) method:
		 * The method sets the next node using current src and the input dest
		 * if the edge = null return false else return true
		 * @return ans
		 */
		public boolean setNextNode(int dest) {
			int src = this._curr_node.getKey();
			this._curr_edge = _gg.getEdge(src, dest);
			return _curr_edge!=null;
		}
		
		/**
		 * About setCurrNode(int src) method:
		 * The method sets the current node of the agent from the input variable
		 */
		public void setCurrNode(int src) {
			this._curr_node = _gg.getNode(src);
		}
		
		/**
		 * About isMoving() method:
		 * The method returns true if the agent is moving 
		 * (by checking if the agent is on edge)
		 * @return this._curr_edge!=null
		 */
		public boolean isMoving() {
			return this._curr_edge!=null;
		}
		
		/**
		 * About toString() method:
		 * The method returns the info of the agent by calling toJSON() method
		 */
		public String toString() {
			return toJSON();
		}

		
		/**
		 * About getID() method:
		 * The method returns the id of the agent
		 * @return this._id
		 */
		public int getID() {
			// TODO Auto-generated method stub
			return this._id;
		}
		
		/**
		 * About getLocation() method:
		 * The method returns the geo location of the agent
		 * @return this._pos
		 */
		public geo_location getLocation() {
			// TODO Auto-generated method stub
			return _pos;
		}
		
		/**
		 * About getValue() method:
		 * The method returns the value of the agent
		 * @return this._value
		 */
		public double getValue() {
			// TODO Auto-generated method stub
			return this._value;
		}
		
		/**
		 * About getNextNode() method:
		 * The method returns the next node using current edge
		 * if the edge = null return -1
		 * @return ans
		 */
		public int getNextNode() {
			int ans = -1;
			if(this._curr_edge!=null) {ans = this._curr_edge.getDest();}
			return ans;
		}
		
		/**
		 * About getSpeed() method:
		 * The method returns the speed of the agent
		 * @return this._speed
		 */
		public double getSpeed() {
			return this._speed;
		}
		
		/**
		 * About setSpeed(double v) method:
		 * The method sets the speed of the agent from the input variable
		 */
		public void setSpeed(double v) {
			this._speed = v;
		}
		
		/**
		 * About get_curr_fruit() method:
		 * The method returns the current fruit of the agent
		 * @return this._curr_fruit
		 */
		public CL_Pokemon get_curr_fruit() {
			return _curr_fruit;
		}
		
		/**
		 * About set_curr_fruit(CL_Pokemon curr_fruit) method:
		 * The method sets the current fruit of the agent from the input variable
		 */
		public void set_curr_fruit(CL_Pokemon curr_fruit) {
			this._curr_fruit = curr_fruit;
		}
		
		/**
		 * About set_SDT(long ddtt) method:
		 * The method sets the SDT of the agent from the input variable
		 */
		public void set_SDT(long ddtt) {
			long ddt = ddtt;
			if(this._curr_edge!=null) {
				double w = get_curr_edge().getWeight();
				geo_location dest = _gg.getNode(get_curr_edge().getDest()).getLocation();
				geo_location src = _gg.getNode(get_curr_edge().getSrc()).getLocation();
				double de = src.distance(dest);
				double dist = _pos.distance(dest);
				if(this.get_curr_fruit().get_edge()==this.get_curr_edge()) {
					 dist = _curr_fruit.getLocation().distance(this._pos);
				}
				double norm = dist/de;
				double dt = w*norm / this.getSpeed(); 
				ddt = (long)(1000.0*dt);
			}
			this.set_sg_dt(ddt);
		}
		
		/**
		 * About get_curr_edge() method:
		 * The method returns the current edge of the agent
		 * @return this._curr_edge
		 */
		public edge_data get_curr_edge() {
			return this._curr_edge;
		}
		
		/**
		 * About get_sg_dt() method:
		 * The method returns the sg dt of the agent
		 * @return this._sg_dt
		 */
		public long get_sg_dt() {
			return _sg_dt;
		}
		
		/**
		 * About set_sg_dt(long _sg_dt) method:
		 * The method sets the sg dt of the agent from the input variable
		 */
		public void set_sg_dt(long _sg_dt) {
			this._sg_dt = _sg_dt;
		}
	}