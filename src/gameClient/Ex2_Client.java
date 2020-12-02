package gameClient;

import Server.Game_Server_Ex2;
import api.DWGraph_Algo;
import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import api.edge_data;
import api.game_service;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Ex2_Client implements Runnable{
	private static MyFrame _win;
	private static Arena _ar;
	public static void main(String[] a) {
		Thread client = new Thread(new Ex2_Client());
		client.start();
	}
	
	//@Override
	public void run() {
		int scenario_num = 11;
		game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
	//	int id = 999;
	//	game.login(id);
		String g = game.getGraph();
		String pks = game.getPokemons();
		directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
		init(game);
		
		game.startGame();
		_win.setTitle("Ex2 - OOP: (NONE trivial Solution) "+game.toString());
		int ind=0;
		long dt=100;
		
		while(game.isRunning()) {
			moveAgants(game, gg);
			try {
				if(ind%1==0) {_win.repaint();}
				Thread.sleep(dt);
				ind++;
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		String res = game.toString();

		System.out.println(res);
		System.exit(0);
	}
	
	
	
	/** 
	 * Moves each of the agents along the edge,
	 * in case the agent is on a node the next destination (next edge) is chosen (randomly).
	 * @param game
	 * @param gg
	 * @param
	 */
	private static void moveAgants(game_service game, directed_weighted_graph gg) {
		String lg = game.move();
		List<CL_Agent> log = Arena.getAgents(lg, gg);
		_ar.setAgents(log);
		//ArrayList<OOP_Point3D> rs = new ArrayList<OOP_Point3D>();
		String fs =  game.getPokemons();
		List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
		_ar.setPokemons(ffs);
		for(int i=0;i<log.size();i++) {
			CL_Agent ag = log.get(i);
			int id = ag.getID();
			int dest = ag.getNextNode();
			int src = ag.getSrcNode();
			double v = ag.getValue();
			if(dest==-1) {
				dest = nextNode(gg, src);
				game.chooseNextEdge(ag.getID(), dest);
				System.out.println("Agent: "+id+", val: "+v+"   turned to node: "+dest);
			}
		}
	}
	/**
	 * a very simple random walk implementation!
	 * @param g
	 * @param src
	 * @return
	 */
	private static int nextNode(directed_weighted_graph g, int src) {
		int ans = -1;
		Collection<edge_data> ee = g.getE(src);
		Iterator<edge_data> itr = ee.iterator();
		
		dw_graph_algorithms hh = new DWGraph_Algo();
		hh.init(g);
		double min = Integer.MAX_VALUE;
		
		
		int index = -1; // here we save the index of the target pokimon on pokimons list.
		int returnint = -1; // here we save the src of the edge that the pokimon is on.
		double temp = -1; // here we save the minimum destination to get nearest pokimon.
		
		// iterate on the pokemons
		for (int i = 0; i < _ar.getPokemons().size(); i++) {
			// for each pokemon update the current edge which the pokemon on now.
			Arena.updateEdge(_ar.getPokemons().get(i), _ar.getGraph());
			// if the current agent is on the src of a edge that the Pokemon 
			// on it go to this edge dest. (and return).
			if (_ar.getPokemons().get(i).get_edge().getSrc()==src) {
				return _ar.getPokemons().get(i).get_edge().getDest();
			}
			// else get minimum destination between agent src and pokimon edge src (temp variable)
			temp = hh.shortestPathDist(src, _ar.getPokemons().get(i).get_edge().getSrc());
			// switch to minimum if this pokimon is nearest.
			if (temp<min&&!_ar.getPokemons().get(i).getUsed()) {
				min = temp;
				index = i;
				returnint = _ar.getPokemons().get(i).get_edge().getSrc();
			}
		}
		// mark the pokimon as 'used' (by one of the agents).
		_ar.getPokemons().get(index).setUsed(true);
		// here we save the next edge that the agent need to go.
		// (using shortestPath method between agent src to pokimon src)
		int nextone;
		// shortestPath(src, returnint).get(0) -> current agent src.
		// shortestPath(src, returnint).get(1) -> the next edge for the agent to go.
		// (not null because if src==edge.src so the method return on start).
		nextone = hh.shortestPath(src, returnint).get(1).getKey();
		// return next edge
		return nextone;
		
		
		/* //// this is the original code ////
		int s = ee.size();
		int r = (int)(Math.random()*s);
		int i=0;
		while(i<r) {itr.next();i++;}
		ans = itr.next().getDest();
		return ans;
		*/
	}
	
	/*
	private static int nextNode(directed_weighted_graph g, int src) {
		int ans = -1;
		Collection<edge_data> ee = g.getE(src);
		Iterator<edge_data> itr = ee.iterator();
		int s = ee.size();
		int r = (int)(Math.random()*s);
		int i=0;
		while(i<r) {itr.next();i++;}
		ans = itr.next().getDest();
		return ans;
	}
	*/
	
	private void init(game_service game) {
		String g = game.getGraph();
		String fs = game.getPokemons();
		directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
		//gg.init(g);
		_ar = new Arena();
		_ar.setGraph(gg);
		_ar.setPokemons(Arena.json2Pokemons(fs));
		_win = new MyFrame("test Ex2");
		_win.setSize(1000, 700);
		_win.update(_ar);

	
		_win.show();
		String info = game.toString();
		JSONObject line;
		try {
			line = new JSONObject(info);
			JSONObject ttt = line.getJSONObject("GameServer");
			int rs = ttt.getInt("agents");
			System.out.println(info);
			System.out.println(game.getPokemons());
			int src_node = 0;  // arbitrary node, you should start at one of the pokemon
			ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());
			for(int a = 0;a<cl_fs.size();a++) { Arena.updateEdge(cl_fs.get(a),gg);}
			for(int a = 0;a<rs;a++) {
				int ind = a%cl_fs.size();
				CL_Pokemon c = cl_fs.get(ind);
				int nn = c.get_edge().getDest();
				if(c.getType()<0 ) {nn = c.get_edge().getSrc();}
				
				game.addAgent(nn);
			}
		}
		catch (JSONException e) {e.printStackTrace();}
	}
}
