package gameClient;

import Server.Game_Server_Ex2;
import api.DWGraph_Algo;
import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import api.edge_data;
import api.game_service;
import api.node_data;
import gameClient.util.Point3D;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

public class Ex2_Client implements Runnable{
	private static MyFrame _win;
	private static Arena _ar;
	private static long sleep = 100;
	
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
		_ar.setGame(game); // added
		game.startGame();
		_win.setTitle("Ex2 - OOP: (NONE trivial Solution) "+game.toString());
		int ind=0;
		//long dt=100;
		
		String lg = game.move();
		List<CL_Agent> log = Arena.getAgents(lg, gg);
		_ar.setAgents(log);
		_ar.initHashMaps();
		
		while(game.isRunning()) {
			moveAgants(game, gg);
			try {
				if(ind%1==0) {_win.repaint();}
				Thread.sleep(sleep);
				ind++;
			}
			catch(Exception e) {e.printStackTrace();}
		}
		System.out.println(game.toString());
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
		//List<CL_Agent> log = _ar.getAgents();
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
				dest = nextNode(gg, src, ag);
				game.chooseNextEdge(ag.getID(), dest);
				System.out.println("Agent: "+id+", val: "+v+"   turned to node: "+dest);
			}
		}
		/*try {
			Thread.sleep(100*speed);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
	/**
	 * a very simple random walk implementation!
	 * @param g
	 * @param src
	 * @return
	 */
	private static int nextNode(directed_weighted_graph g, int src, CL_Agent ag) {
		sleep = 100;
		dw_graph_algorithms hh = new DWGraph_Algo();
		hh.init(g);
		double min = Integer.MAX_VALUE;
		double temp = -1;
		int destNode = -1;
		CL_Pokemon currPok;
		for (int i = 0; i < _ar.getPokemons().size(); i++) {
			currPok = _ar.getPokemons().get(i);
			Arena.updateEdge(currPok, _ar.getGraph());
			if (currPok.get_edge().getSrc()==src) {
				if (_ar.isPreviousPok(currPok, ag)) {
					sleep = 10;
					System.out.println("Turbo Mode!");
				} else _ar.setAsPreviousPok(currPok, ag);
				return currPok.get_edge().getDest();
			} else {
				temp = hh.shortestPathDist(src, currPok.get_edge().getSrc());
				if (temp<min&&_ar.isAvailableFruit(currPok, ag)) {
					min = temp;
					destNode = currPok.get_edge().getSrc();
					_ar.setFruit(currPok, ag);
				}
			}
		}
		int nextNode = -1;
		if (destNode!=-1) {
			List<node_data> directions = hh.shortestPath(src, destNode);
			nextNode = directions.get(1).getKey();
		} else {
			Collection<edge_data> ee = g.getE(src);
			Iterator<edge_data> itr = ee.iterator();
			int s = ee.size();
			int r = (int)(Math.random()*s);
			int i=0;
			while(i<r) {itr.next();i++;}
			nextNode = itr.next().getDest();
		}
		return nextNode;
	}
	
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
			// sort by value
			PriorityQueue<CL_Pokemon> poks = new PriorityQueue<CL_Pokemon>(new PokemonComparator());
			for(int a = 0;a<cl_fs.size();a++) { 
				Arena.updateEdge(cl_fs.get(a),gg);
				poks.add(cl_fs.get(a));
				}
			for(int a = 0;a<rs;a++) {
				int ind = a%cl_fs.size();
				CL_Pokemon c = cl_fs.get(ind);
				int nn = c.get_edge().getDest();
				if(c.getType()<0 ) {nn = c.get_edge().getSrc();}
				nn = poks.poll().get_edge().getSrc();
				game.addAgent(nn);
			}
		}
		catch (JSONException e) {e.printStackTrace();}
	}
	
	class PokemonComparator implements Comparator<CL_Pokemon> {
		@Override
		public int compare(CL_Pokemon o1, CL_Pokemon o2) {
			return Double.compare(o2.getValue(), o1.getValue()); // max first
		}
	}
}
