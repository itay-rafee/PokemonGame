package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.game_service;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a multi Agents Arena which move on a graph - grabs Pokemons and avoid the Zombies.
 * @author boaz.benmoshe
 */
public class Arena {
	public static final double EPS1 = 0.001, EPS2=EPS1*EPS1, EPS=EPS2;
	private directed_weighted_graph _gg;
	private List<CL_Agent> _agents;
	private List<CL_Pokemon> _pokemons;
	private List<String> _info;
	private game_service game;
	private static HashMap<Integer, Point3D> lastPos = new HashMap<>();
	private static HashMap<Integer, CL_Pokemon> fruits = new HashMap<>();

	/* Constructor */
	public Arena() {;
		_info = new ArrayList<String>();
	}
	
	/**
	 * About setPokemons(List<CL_Pokemon> f) method:
	 * The method sets the pokemmons list to the input list.
	 * @param f
	 */
	public void setPokemons(List<CL_Pokemon> f) {
		this._pokemons = f;
	}
	
	/**
	 * About setAgents(List<CL_Agent> f) method:
	 * The method sets the agents list to the input list.
	 * @param f
	 */
	public void setAgents(List<CL_Agent> f) {
		this._agents = f;
	}
	
	/**
	 * About setGraph(directed_weighted_graph g) method:
	 * The method sets the graph to the input graph.
	 * @param g
	 */
	public void setGraph(directed_weighted_graph g) {this._gg =g;}
	
	/**
	 * About getAgents() method:
	 * The method returns the agents list.
	 * @return _agents
	 */
	public List<CL_Agent> getAgents() {return _agents;}
	
	/**
	 * About getPokemons() method:
	 * The method returns the pokemons list.
	 * @return _pokemons
	 */
	public List<CL_Pokemon> getPokemons() {return _pokemons;}
	
	/**
	 * About getGraph() method:
	 * The method returns the graph object.
	 * @return _gg
	 */
	public directed_weighted_graph getGraph() {
		return _gg;
	}
	
	/**
	 * About get_info() method:
	 * The method returns the info list.
	 * @return _info
	 */
	public List<String> get_info() {
		return _info;
	}
	
	/**
	 * About set_info(List<String> _info) method:
	 * The method sets the info list to the input info list.
	 * @param _info
	 */
	public void set_info(List<String> _info) {
		this._info = _info;
	}
	
	/**
	 * About getAgents(String aa, directed_weighted_graph gg) method:
	 * The method returns the agents list from the json file
	 * in accordance with the input objects.
	 * @param aa
	 * @param gg
	 * @return ans
	 */
	public static List<CL_Agent> getAgents(String aa, directed_weighted_graph gg) {
		ArrayList<CL_Agent> ans = new ArrayList<CL_Agent>();
		try {
			JSONObject ttt = new JSONObject(aa);
			JSONArray ags = ttt.getJSONArray("Agents");
			for(int i=0;i<ags.length();i++) {
				CL_Agent c = new CL_Agent(gg,0);
				c.update(ags.get(i).toString());
				ans.add(c);
			}
			//= getJSONArray("Agents");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ans;
	}
	
	/**
	 * About json2Pokemons(String fs) method:
	 * The method returns the pokemons list from the json file
	 * in accordance with the input object.
	 * @param fs
	 * @return ans
	 */
	public static ArrayList<CL_Pokemon> json2Pokemons(String fs) {
		ArrayList<CL_Pokemon> ans = new  ArrayList<CL_Pokemon>();
		try {
			JSONObject ttt = new JSONObject(fs);
			JSONArray ags = ttt.getJSONArray("Pokemons");
			for(int i=0;i<ags.length();i++) {
				JSONObject pp = ags.getJSONObject(i);
				JSONObject pk = pp.getJSONObject("Pokemon");
				int t = pk.getInt("type");
				double v = pk.getDouble("value");
				//double s = 0;//pk.getDouble("speed");
				String p = pk.getString("pos");
				CL_Pokemon f = new CL_Pokemon(new Point3D(p), t, v, 0, null);
				ans.add(f);
			}
		}
		catch (JSONException e) {e.printStackTrace();}
		return ans;
	}
	
	/**
	 * About updateEdge(CL_Pokemon fr, directed_weighted_graph g) method:
	 * The method checks the edge on which the Pokemon is located
	 * (by the isOnEdge() method) and then updates the Pokemon with the appropriate edge.
	 * @param fr
	 * @param g
	 */
	public static void updateEdge(CL_Pokemon fr, directed_weighted_graph g) {
		//	oop_edge_data ans = null;
		Iterator<node_data> itr = g.getV().iterator();
		while(itr.hasNext()) {
			node_data v = itr.next();
			Iterator<edge_data> iter = g.getE(v.getKey()).iterator();
			while(iter.hasNext()) {
				edge_data e = iter.next();
				boolean f = isOnEdge(fr.getLocation(), e,fr.getType(), g);
				if(f) {fr.set_edge(e);}
			}
		}
	}
	
	/**
	 * About isOnEdge method:
	 * The method returns if the input object is on the given edge.
	 * @param p
	 * @param src
	 * @param dest
	 * @return ans
	 */
	private static boolean isOnEdge(geo_location p, geo_location src, geo_location dest) {
		boolean ans = false;
		double dist = src.distance(dest);
		double d1 = src.distance(p) + p.distance(dest);
		if(dist>d1-EPS2) {ans = true;}
		return ans;
	}
	
	/**
	 * About isOnEdge method:
	 * The method returns if the input object is on the given edge.
	 * @param p
	 * @param s
	 * @param d
	 * @param g
	 */
	private static boolean isOnEdge(geo_location p, int s, int d, directed_weighted_graph g) {
		geo_location src = g.getNode(s).getLocation();
		geo_location dest = g.getNode(d).getLocation();
		return isOnEdge(p,src,dest);
	}
	
	/**
	 * About isOnEdge method:
	 * The method returns if the input object is on the given edge.
	 * @param p
	 * @param e
	 * @param type
	 * @param g
	 */
	private static boolean isOnEdge(geo_location p, edge_data e, int type, directed_weighted_graph g) {
		int src = g.getNode(e.getSrc()).getKey();
		int dest = g.getNode(e.getDest()).getKey();
		if(type<0 && dest>src) {return false;}
		if(type>0 && src>dest) {return false;}
		return isOnEdge(p,src, dest, g);
	}
	
	/**
	 * About GraphRange(directed_weighted_graph g) method:
	 * The method returns the graph range according to the given graph.
	 * @param g
	 * @return new Range2D(xr,yr)
	 */
	private static Range2D GraphRange(directed_weighted_graph g) {
		Iterator<node_data> itr = g.getV().iterator();
		double x0=0,x1=0,y0=0,y1=0;
		boolean first = true;
		while(itr.hasNext()) {
			geo_location p = itr.next().getLocation();
			if(first) {
				x0=p.x(); x1=x0;
				y0=p.y(); y1=y0;
				first = false;
			}
			else {
				if(p.x()<x0) {x0=p.x();}
				if(p.x()>x1) {x1=p.x();}
				if(p.y()<y0) {y0=p.y();}
				if(p.y()>y1) {y1=p.y();}
			}
		}
		Range xr = new Range(x0,x1);
		Range yr = new Range(y0,y1);
		return new Range2D(xr,yr);
	}
	
	/**
	 * About w2f(directed_weighted_graph g, Range2D frame) method:
	 * The method returns Range2Range object from the given graph and frame.
	 * @param g
	 * @param frame
	 * @return ans
	 */
	public static Range2Range w2f(directed_weighted_graph g, Range2D frame) {
		Range2D world = GraphRange(g);
		Range2Range ans = new Range2Range(world, frame);
		return ans;
	}
	
	/**
	 * About setGame(game_service game) method:
	 * The method initializes this class to the input game_service game.
	 */
	public void setGame(game_service game) {
		this.game=game;
	}
	
	/**
	 * About getGame() method:
	 * The method returns this game_service object.
	 * @return this.game
	 */
	public game_service getGame() {
		return this.game;
	}
	
	/**
	 * About getGrade() method:
	 * The method returns the grade of the game from the json file.
	 * @return grade
	 */
	public int getGrade() throws JSONException {
		String info = game.toString();
		JSONObject line = new JSONObject(info);
		JSONObject ttt = line.getJSONObject("GameServer");
		int grade = ttt.getInt("grade");
		return grade;
	}
	
	/**
	 * About getTime() method:
	 * The method returns the moves of the game from the json file.
	 * @return moves
	 */
	public int getMoves() throws JSONException {
		String info = game.toString();
		JSONObject line = new JSONObject(info);
		JSONObject ttt = line.getJSONObject("GameServer");
		int moves = ttt.getInt("moves");
		return moves;
	}
	
	/**
	 * About getTime() method:
	 * The method returns the remaining time for the end of the game.
	 * @return time
	 */
	public String getTime() {
		long time = game.timeToEnd();
		return time/1000+"."+time%1000;
	}
	
	/**
	 * About initHashMaps() method:
	 * The method initializes all HashMap data structures according to the number
	 * of agents (received from the server) for the use of the structures during the game
	 * in order to avoid a null error.
	 */
	public void initHashMaps() {
		List<CL_Agent> ag = this.getAgents();
		if (ag==null) return;
		for (int i = 0; i<ag.size(); i++) {
			Point3D p = new Point3D(-1, -1, -1);
			lastPos.put(ag.get(i).getID(), p);
			fruits.put(ag.get(i).getID(), new CL_Pokemon(p, -1, -1, -1, null));
		}
	}
	
	/**
	 * About setAsPreviousPok(CL_Pokemon p, CL_Agent a) method:
	 * The method marks a given Pokemon as the last Pokemon the agent tried to access.
	 * Implementation is by adding the Pokemon to 'lastPos' HashMap.
	 * @param p
	 * @param a
	 */
	public void setAsPreviousPok(CL_Pokemon p, CL_Agent a) {
		lastPos.put(a.getID(), p.getLocation());
	}
	
	/**
	 * About isPreviousPok(CL_Pokemon p, CL_Agent a) method:
	 * The method checks if the given Pokemon is the previous Pokemon that
	 * the agent tried to access. The previous Pokemon is stored inside HashMap
	 * by calling the setAsPreviousPok(CL_Pokemon p, CL_Agent a) method.
	 * Equality check is performed by a tiny distance between the previous Pokemon
	 * and the current Pokemon.
	 * @param p
	 * @param a
	 * @return boolean
	 */
	public boolean isPreviousPok(CL_Pokemon p, CL_Agent a) {
		if (p.getLocation().distance(lastPos.get(a.getID()))<0.00000000000001) {
			return true;
		}
		return false;
	}
	
	/**
	 * About setFruit(CL_Pokemon p, CL_Agent a) method:
	 * The method marks the given Pokemon as the target Pokemon of the given agent.
	 * The implementation is by adding the Pokemon as a value to HashMap so that
	 * the agent number is the key.
	 * @param p
	 * @param a
	 */
	public void setFruit(CL_Pokemon p, CL_Agent a) {
		if (p!=null&&a!=null) {
			fruits.put(a.getID(), p);
		}
	}
	
	/**
	 * About isAvailableFruit(CL_Pokemon p, CL_Agent a) method:
	 * This method checks if a Pokemon is in a marked area of a particular agent.
	 * If the Pokemon is "in use" the method will return false.
	 * Otherwise the function returns true so that the agent can access the given Pokemon.
	 * The implementation of the function is by iterating between all the agents
	 * and checking if the given Pokemon is in a marked area (by a small distance)
	 * of each agent (the area is measured from the fruit of each agent).
	 * @param p
	 * @param a
	 * @return boolean
	 */
	public boolean isAvailableFruit(CL_Pokemon p, CL_Agent a) {
		List<CL_Agent> ag = this.getAgents();
		for (int i = 0; i<ag.size(); i++) {
			if (ag.get(i).getID()!=a.getID()&&fruits.containsKey(ag.get(i).getID())) {
				if (p.getPokNum()==fruits.get(ag.get(i).getID()).pokNum) {return false;}
				double distance = fruits.get(ag.get(i).getID()).getLocation().distance(p.getLocation());
				if (distance<0.0045) {return false;}
			}
		}
		return true;
	}
}