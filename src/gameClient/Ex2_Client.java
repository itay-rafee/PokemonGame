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
	private static int stuckagent = -1;
	private static int stucktry = 0;
	private static HashMap<Integer, Integer> lastNode = new HashMap<>();
	private static HashMap<Integer, Integer> lastPoks = new HashMap<>();
	private static HashMap<Integer, Point3D> lastPos = new HashMap<>();
	private static HashMap<Integer, Integer> stucks = new HashMap<>(); // added
	private static Stack<Long> t = new Stack<>();
	
	public static void main(String[] a) {
		Thread client = new Thread(new Ex2_Client());
		client.start();
	}
	
	//@Override
	public void run() {
		int scenario_num = 23;
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
		long dt=100;
		/*
		HashMap<Integer, HashMap<Integer, Boolean>> gameTrick = new HashMap<>();
		gameTrick.put(0, new HashMap<>());
		gameTrick.get(0).put(0, true);
		gameTrick.get(0).put(1, true);
		gameTrick.get(0).put(2, true);
		gameTrick.get(0).put(3, true);
		gameTrick.get(0).put(4, true);
		gameTrick.get(0).put(5, true);
		gameTrick.get(0).put(6, true);
		gameTrick.get(0).put(7, true);
		gameTrick.get(0).put(8, true);
		gameTrick.get(0).put(9, true);
		gameTrick.get(0).put(10, true);
		gameTrick.get(0).put(11, true);
		gameTrick.get(0).put(12, true);
		gameTrick.get(0).put(13, true);
		gameTrick.put(1, new HashMap<>());
		gameTrick.get(1).put(40, true);
		gameTrick.get(1).put(41, true);
		gameTrick.get(1).put(42, true);
		gameTrick.get(1).put(43, true);
		gameTrick.get(1).put(44, true);
		gameTrick.get(1).put(45, true);
		gameTrick.get(1).put(46, true);
		gameTrick.get(1).put(47, true);
		gameTrick.put(2, new HashMap<>());
		gameTrick.get(2).put(14, true);
		gameTrick.get(2).put(15, true);
		gameTrick.get(2).put(16, true);
		gameTrick.get(2).put(17, true);
		gameTrick.get(2).put(18, true);
		gameTrick.get(2).put(19, true);
		gameTrick.get(2).put(20, true);
		gameTrick.get(2).put(21, true);
		gameTrick.get(2).put(22, true);
		gameTrick.get(2).put(23, true);
		gameTrick.get(2).put(24, true);
		gameTrick.get(2).put(25, true);
		gameTrick.get(2).put(26, true);
		gameTrick.get(2).put(27, true);
		gameTrick.get(2).put(28, true);
		gameTrick.get(2).put(29, true);
		gameTrick.get(2).put(30, true);
		gameTrick.get(2).put(31, true);
		gameTrick.get(2).put(32, true);
		gameTrick.get(2).put(33, true);
		gameTrick.get(2).put(34, true);
		gameTrick.get(2).put(35, true);
		gameTrick.get(2).put(36, true);
		gameTrick.get(2).put(37, true);
		gameTrick.get(2).put(38, true);
		gameTrick.get(2).put(39, true);
		_ar.setgameTrick(gameTrick);
		*/
		
		
		lastNode.put(0, -1);
		lastNode.put(1, -1);
		lastNode.put(2, -1);
		lastPos.put(0, new Point3D(-1,-1,-1));
		lastPos.put(1, new Point3D(-1,-1,-1));
		lastPos.put(2, new Point3D(-1,-1,-1));
		
		String lg = game.move();
		List<CL_Agent> log = Arena.getAgents(lg, gg);
		_ar.setAgents(log);
		
		while(game.isRunning()) {
			moveAgants(game, gg);
			try {
				if(ind%1==0) {_win.repaint();}
				Thread.sleep(sleep);
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
			/*String lg = _ar.getGame().move();
			List<CL_Agent> logg = Arena.getAgents(lg, gg);
			_ar.setAgents(logg);*/
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
		int index = -1; // here we save the index of the target pokimon on pokimons list.
		int returnint = -1; // here we save the src of the edge that the pokimon is on.
		double temp = -1; // here we save the minimum destination to get nearest pokimon.
		for (int i = 0; i < _ar.getPokemons().size(); i++) {
			Arena.updateEdge(_ar.getPokemons().get(i), _ar.getGraph());
			if (_ar.getPokemons().get(i).get_edge().getSrc()==src) {
				int dest = _ar.getPokemons().get(i).get_edge().getDest();
				if (_ar.getPokemons().get(i).getLocation().distance(lastPos.get(ag.getID()))<0.00000000000001) { // dest==lastNode.get(ag.getID())&&_ar.getPokemons().get(i).getPos().equals(lastPos.get(ag.getID()))
					sleep = 10;
					System.out.println("Turbo Mode!");
				}
				//lastNode.put(ag.getID(), _ar.getPokemons().get(i).get_edge().getDest());
				lastPos.put(ag.getID(), _ar.getPokemons().get(i).getLocation());
				return _ar.getPokemons().get(i).get_edge().getDest();
			} else {
				temp = hh.shortestPathDist(src, _ar.getPokemons().get(i).get_edge().getSrc());
				if (temp<min&&!_ar.getPokemons().get(i).getUsed()&&_ar.closestAgent(_ar.getPokemons().get(i),ag)>0.0035) { // &&!_ar.getPokemons().get(i).getUsed()
					min = temp;
					index = i;
					returnint = _ar.getPokemons().get(i).get_edge().getSrc();
				}
			}
		}
		if (index!=-1) {_ar.getPokemons().get(index).setUsed(true);}
		int nextone = -1;
		if (returnint!=-1) {
			List<node_data> directions = hh.shortestPath(src, returnint);
			nextone = directions.get(1).getKey();
		} else {
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
		return nextone;
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
				//int nn = poks.poll().get_edge().getSrc();
				
				/*for(int i = 0;i<cl_fs.size();i++) {
					if (_ar.getgameTrick()!=null&&_ar.getgameTrick().get(a)!=null&&_ar.getgameTrick().get(a).containsKey(_ar.getPokemons().get(i).get_edge().getSrc())) {
						nn = _ar.getPokemons().get(i).get_edge().getSrc();
						break;
					}
				}*/
				
				nn = poks.poll().get_edge().getSrc();
				game.addAgent(nn);
			}
			/*while (!poks.isEmpty()) {
				System.out.println(poks.poll().getValue());
			}*/
		}
		catch (JSONException e) {e.printStackTrace();}
	}
	
	class PokemonComparator implements Comparator<CL_Pokemon> {
		@Override
		public int compare(CL_Pokemon o1, CL_Pokemon o2) {
			// TODO Auto-generated method stub
			return Double.compare(o2.getValue(), o1.getValue()); // max first
		}
	}
}
