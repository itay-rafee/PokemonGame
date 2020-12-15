package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import gameClient.util.Point3D;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;

public class Ex2_Client implements Runnable{
	private static MyFrame _win;
	private static Arena _ar;
	private static long sleep = 100;

	//can select the level of the game here
	//or in the open frame by init the scenario_num = -1
	private static int scenario_num = -1;
	private static int count = 0;
	private static int id = 0;



	public static void main(String[] a) {
		Ex2_Client e = new Ex2_Client();
		if (scenario_num == -1){
			e.openFrame();
		}
		else {
			Thread client = new Thread(e);
			client.start();
		}
	}

	//@Override
	public void run() {
		game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
		//int id = 203201389;
		if (count != 0) game.login(id);
		String g = game.getGraph();
		String pks = game.getPokemons();
		directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
		init(game);
		_ar.setGame(game); // added
		game.startGame();
		_win.setTitle("Ex2 - OOP: (NONE trivial Solution) "+game.toString());
		int ind=0;
		//long dt=100;

		_win.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

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
				if (temp<min&&temp!=-1&&_ar.isAvailableFruit(currPok, ag)) {
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


	////////// init function /////////

	/**
	 * About init(game_service game) method: this method init the agents
	 *   by consideration if the graph is connected using the method initAgentNearPok()
	 *   or disconnected by using the method initAgentDisconnectedGraph()
	 * @param game - the game
	 */
	private void init(game_service game) {
		initFrame(game);
		directed_weighted_graph g = _ar.getGraph();
		dw_graph_algorithms ga = new DWGraph_Algo(g);
		int agentsNum = json2numAgent(game);
		if (ga.isConnected()){

			//while the graph is connected
			initAgentNearPok(game,_ar.getPokemons(),agentsNum,g.getV());
		}
		else{

			//while the graph is disconnected
			initAgentDisconnectedGraph(game,_ar.getPokemons(),agentsNum);
		}
	}

	/**
	 * About initAgentDisconnectedGraph(game_service game) method:
	 *   this method init the agents
	 *   first - we find all the graphing component
	 *           with the method findGroup()
	 *   after - we init 1 agent for each graphing component
	 *   finally - we init the rest of the agents by equals
	 *   by using the function findPokInGroup()
	 * @param game - the game
	 * @param pokemonS - the pokemons that in the group
	 * @param agentsNum - the agents that we need to add
	 */
	private void initAgentDisconnectedGraph(game_service game, List<CL_Pokemon> pokemonS, int agentsNum) {
		HashSet<Collection<node_data>> allGroup = new HashSet<>();
		HashSet<node_data> vis = new HashSet<>();
		directed_weighted_graph g = _ar.getGraph();
		Collection<node_data> nodes = g.getV();
		int ageSum = 0;

		//here we fine all the graphing component of a graph
		for (node_data n : nodes) {
			if (!vis.contains(n)){
				Collection<node_data> group = findGroup(n.getKey(),vis);
				allGroup.add(group);
			}
		}

		//here we init 1 agent for each graphing component
		for (Collection<node_data> group : allGroup) {
			List<CL_Pokemon> pokInGroup = findPokInGroup(pokemonS,group);
			if (agentsNum > 0){
				initAgentNearPok(game,pokInGroup,1,group);
				agentsNum--;
				ageSum++;
			}
		}

		//here we init the rest of the agents by equals
		for (Collection<node_data> group : allGroup) {
			List<CL_Pokemon> pokInGroup = findPokInGroup(pokemonS,group);
			int agentForGroup = agentsNum*(group.size()/g.nodeSize());
			if (agentForGroup > 0){
				ageSum += agentForGroup;
				initAgentNearPok(game,pokInGroup,agentForGroup,group);
			}
		}


		//if there is more agents
		Iterator<node_data> n = nodes.iterator();
		while (ageSum < agentsNum){
			game.addAgent(n.next().getKey());
			ageSum++;
		}
	}

	/**
	 * About findPokInGroup() method:
	 *   this method find the pokemons
	 *   that in the graphing component
	 *   this work by checking if the edge of the pokemon
	 *   on the same edge of the group
	 * @param pokemonS - the pokemons that in the group
	 * @param group - the graphing component
	 */
	private List<CL_Pokemon> findPokInGroup(List<CL_Pokemon> pokemonS, Collection<node_data> group) {
		List<CL_Pokemon> pokForGroup = new LinkedList<>();
		directed_weighted_graph g = _ar.getGraph();
		HashSet<node_data> nodes = new HashSet<>(group);
		for (CL_Pokemon p : pokemonS) {
			if (p.get_edge() != null && nodes.contains(g.getNode(p.get_edge().getSrc()))){
				pokForGroup.add(p);
			}
		}
		return pokForGroup;
	}

	/**
	 * About findGroup() method:
	 *   this method find the graphing component
	 *   of the key.
	 *   working by two BFS algo:
	 *     one for the right graph.
	 *     second for the fliped graph .
	 *   then all the element that in the two
	 *   group is the graphing component of key
	 * @param key - the pokemons that in the group
	 * @param vis - the node that have a graphing component
	 * @return group - the graphing component
	 */
	private Collection<node_data> findGroup (int key, HashSet<node_data> vis){
		Collection<node_data> group = new ArrayList<>();
		directed_weighted_graph g = _ar.getGraph();
		HashSet<node_data> ni1 = isConnectedBFS(key, g);
		g = flipedGraph();
		HashSet<node_data> ni2 = isConnectedBFS(key, g);
		for (node_data n1 : ni1) {
			if (ni2.contains(n1)) {
				group.add(n1);
				vis.add(n1);
			}
		}
		return group;
	}

	/**
	 * About json2numAgent(game_service game) method:
	 *   this method find the agents
	 *   that in the game by checking
	 *   the json string of the game.
	 * @param game - the game
	 * @return numAgent - the graphing component
	 */
	private int json2numAgent(game_service game) {
		try {
			JSONObject line = new JSONObject(game.toString());
			JSONObject jsonGame = line.getJSONObject("GameServer");
			return  (int) jsonGame.get("agents");
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * About initAgentNearPok() method:
	 *   this method init the agents near the pokemon with
	 *   the biggest value on the graphing component
	 *   using with PriorityQueue and the
	 *   class PokemonComparator.
	 * @param game - the game
	 * @param pokemonS - the pokemons that in the group
	 * @param agentsNum - the agents that we need to add
	 * @param nodes - the nodes of the graphing component
	 */
	private void initAgentNearPok(game_service game, List<CL_Pokemon> pokemonS, int agentsNum, Collection<node_data> nodes) {
		directed_weighted_graph gg = _ar.getGraph();

		// sort by value
		PriorityQueue<CL_Pokemon> poks = new PriorityQueue<CL_Pokemon>(new PokemonComparator());
		for (CL_Pokemon pokemon : pokemonS) {
			Arena.updateEdge(pokemon, gg);
			poks.add(pokemon);
		}
		int a = 0;
		while (a < agentsNum && a < pokemonS.size()) {
			int nn;
			CL_Pokemon p = poks.poll();
			assert p != null;
			nn = p.get_edge().getSrc();
			game.addAgent(nn);
			game.chooseNextEdge(a,p.get_edge().getDest());
			a++;
		}
		Iterator<node_data> n = nodes.iterator();
		while (a < agentsNum){//if there is more agent then pokemon
			node_data nn = n.next();
			game.addAgent(nn.getKey());
			a++;
		}
	}

	/**
	 * About initFrame(game_service game) method:
	 *   this method init the frame if the game
	 *   and in addition init the Arena.
	 * @param game - the game
	 */
	private void initFrame(game_service game) {
		String info = game.toString();
		System.out.println(info);
		System.out.println(game.getPokemons());
		String fs = game.getPokemons();
		directed_weighted_graph gg = json2graph(game.getGraph());
		//gg.init(g);
		_ar = new Arena();
		_ar.setGraph(gg);
		_ar.setPokemons(Arena.json2Pokemons(fs));
		_win = new MyFrame("test Ex2");
		_win.setSize(1000, 700);
		_win.update(_ar);
		_win.show();
	}


	/**
	 * About isConnectedBFS method:
	 *   this method we using the same BFS algo as on DWGraph_Algo.
	 * @param startNode - the game
	 * @param g - the game
	 * @return vis - the group of connect
	 */
	public HashSet<node_data> isConnectedBFS(int startNode, directed_weighted_graph g) {
		HashSet<node_data> vis = new HashSet<>();
		Queue<node_data> q = new LinkedList<node_data>();
		node_data current = g.getNode(startNode);
		q.add(current);
		vis.add(current);
		while (!q.isEmpty()) {
			current = q.remove();
			for (edge_data node : g.getE(current.getKey())) {
				node_data dest = g.getNode(node.getDest());
				if (!vis.contains(dest)) {
					q.add(dest);
					vis.add(dest);
				}
			}
		}
		return vis;
	}

	/**
	 * About flipedGraph method:
	 *   this method we using the same flipedGraph algo as on DWGraph_Algo.
	 * @return temp - the fliped graph
	 */
	public directed_weighted_graph flipedGraph() {
		directed_weighted_graph g = _ar.getGraph();
		directed_weighted_graph temp = new DWGraph_DS();
		for (node_data node : g.getV())
			temp.addNode(node);
		for (node_data node : g.getV())
			for (edge_data e : g.getE(node.getKey()))
				temp.connect(e.getDest(), e.getSrc(), e.getWeight());
		return temp;
	}

	class PokemonComparator implements Comparator<CL_Pokemon> {
		@Override
		public int compare(CL_Pokemon o1, CL_Pokemon o2) {
			return Double.compare(o2.getValue(), o1.getValue()); // max first
		}
	}


	/**
	 * About json2graph method:
	 *   this method we using the same algo as the load method in DWGraph_Algo.
	 * @param s - the json graph
	 * @return g - the graph
	 */
	private directed_weighted_graph json2graph(String s){
		try{
			directed_weighted_graph g = new DWGraph_DS();
			JSONObject graphFile = new JSONObject(s);
			JSONArray nodes = graphFile.getJSONArray("Nodes");

			int t = nodes.length();

			// add the nodes
			for (int i = 0; i < t; i++) {
				JSONObject node = nodes.getJSONObject(i);
				String pos = node.getString("pos");
				String[] xyz = pos.split(",");
				double x = Double.parseDouble(xyz[0]), y = Double.parseDouble(xyz[1]), z = Double.parseDouble(xyz[2]);
				geo_location geo = new GeoLocation(x,y,z);
				int id = (int)node.get("id");
				node_data n1 = new NodeData(id,geo);
				g.addNode(n1);
			}

			JSONArray edges = graphFile.getJSONArray("Edges");
			t = edges.length();

			//add the edges
			for (int i = 0; i < t; i++) {
				JSONObject ed = edges.getJSONObject(i);
				int src = ed.getInt("src"), dest = ed.getInt("dest");
				double w = ed.getDouble("w");
				g.connect(src,dest,w);
			}

			return g;
		}
		catch (JSONException e){
			e.printStackTrace();
		}
		return null;
	}

	//////// open screen //////

	private void openFrame(){

		JFrame f = new JFrame("Welcome!");
		final JTextField tf = new JTextField("Enter ID");
		tf.setBounds(150,50, 150,20);
		tf.setBackground(Color.LIGHT_GRAY);
		tf.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(java.awt.event.FocusEvent evt) {
				if (tf.getText().equals("Enter level")||tf.getText().equals("Try again!")||
						tf.getText().equals("Put a correct number!")||tf.getText().equals("Enter ID")) {
					tf.setText("");
				}
			}
		});
		JButton b = new JButton("Login");
		f.getRootPane().setDefaultButton(b);
		b.setBounds(50,50,95,30);
		b.setBackground(Color.GRAY);
		b.setDoubleBuffered(true);
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String a = tf.getText();
				int num;
				if (count == 0){
					try{
						id = Integer.parseInt(a);
						count = 1;
						tf.setText("Enter level");
						b.setText("Start");
					}
					catch (Exception r){
						tf.setText("Try again!");
					}
				}
				else{
					try{
						num = Integer.parseInt(a);
						game_service game = Game_Server_Ex2.getServer(num); // you have [0,23] games
						String g = game.getGraph();
						scenario_num = num;
						f.setVisible(false);
						start();
					}
					catch (Exception r){
						count++;
						if (count > 3) tf.setText("Put a correct number!");
						else tf.setText("Try again!");
					}
				}

			}
		});
		//f.add(b);f.add(tf);
		JMenuBar m = new JMenuBar();
		m.add(b);
		m.add(tf);
		f.setJMenuBar(m);
		f.setSize(1000,700);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		f.setLocation(dim.width/2-f.getSize().width/2, dim.height/2-f.getSize().height/2);
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		Container contentPane = f.getContentPane();
		contentPane.add(new OpenFrame());
		f.show();
	}

	public static void start() throws InterruptedException {
		Thread client = new Thread(new Ex2_Client());
		client.start();
	}

	private static class OpenFrame extends JPanel {
		Image openS = new ImageIcon(getClass().getResource("/go1.jpg")).getImage();
		//private Image openS = Toolkit.getDefaultToolkit().getImage("images\\go1.jpg");
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(openS, 0, 0, this.getWidth(), this.getHeight(), this);
		}
	}
}