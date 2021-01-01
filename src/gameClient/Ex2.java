package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;

public class Ex2 implements Runnable{
	private static MyFrame _win;
	private static Arena _ar;
	private static long sleep = 100;
	//can select the level of the game here
	//or in the open frame by init the scenario_num = -1
	private static int scenario_num = -1;
	//Object for the open screen
	private static int count = 0;
	private static int id = 0;
	private static boolean endOfGame = false;
	private static int[] data = new int[4];
	
	/**
	 * In the main function we check if the arguments received by the code line are
	 * correct in order to run the class.
	 * Otherwise the login window is activated automatically.
	 */
	public static void main(String[] args) {
		if (args.length >= 2){
			try{
				id = Integer.parseInt(args[0]);
				scenario_num = Integer.parseInt(args[1]);
				game_service game = Game_Server_Ex2.getServer(scenario_num);
				String g = game.getGraph();
				count = 1;
			}
			catch (Exception e){
				System.out.println("Invalid Data!");
				Ex2 ex2 = new Ex2();
				ex2.openFrame();
			}
		}
		Ex2 e = new Ex2();
		if (count == 0){
			e.openFrame();
		}
		else {
			Thread client = new Thread(e);
			client.start();
		}
	}

	/**
	 * In this function we manage the game by the thread with frequent calls
	 * to the server.
	 *  The function activates a visual interface for the game display.
	 */
	public void run() {
		game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
		if (count > 0) game.login(id);
		String g = game.getGraph();
		String pks = game.getPokemons();
		directed_weighted_graph gg =  json2graph(g); // game.getJava_Graph_Not_to_be_used();
		init(game);
		_ar.setGame(game); // added
		game.startGame();
		_win.setTitle("Ex2 - OOP: (NONE trivial Solution) "+game.toString());
		int ind=0;
		_win.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {System.exit(0);}
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
			} catch(Exception e) {e.printStackTrace();}}
		System.out.println(game.toString());
		_win.setVisible(false);
		setData();
		openFrame();
		//System.exit(0);
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
	}
	/**
	 * About nextNode(directed_weighted_graph g, int src, CL_Agent ag) method:
	 * This method calculates the next vertex to which the agent approaches.
	 * For this method we used the isAvailableFruit() method in the Arena class
	 * which marks a certain area in the graph as an area that only a specific agent
	 * can access. The area is dynamic according to the Pokemon marked as the "fruit"
	 * of the agent (As explained there).
	 * The implementation of the method is by iteration between all the Pokemons
	 * in the game.
	 * If the agent is on the src vertex of a edge on which Pokemon is located
	 * then the function returns the dest of that edge as the next vertex to which
	 * the agent approaches. Otherwise, the function goes iterated over the rest of
	 * the Pokemon and calculates the closest Pokemon to the agent with the
	 * shortestPathDist() method (by the dijkstra algorithm) in DWGraph_Algo class
	 * and marks the nearest Pokemon marked as available by the isAvailableFruit()
	 * method implemented in the Arena class.
	 * Once such a Pokemon is found the method calculate the path from the agent
	 * to the Pokemon and return the second vertex in the path
	 * (since the agent is on the first vertex).
	 * Otherwise, since no Pokemon is found then the agent will move randomly until
	 * a relevant Pokemon appears.
	 * Note! 
	 *   In advanced stages when the agent runs fast and the edge he is running
	 *   on does not weigh much, the agent cannot "catch" the Pokemon on that edge.
	 *   This happens because the number of calls to the server is not frequent enough
	 *   so that the server does not receive the information that the agent is close
	 *   to this Pokemon to "catch" it.
	 *   How to solve this problem?
	 *   In this method when we run iteratively on the list of Pokemon (as we explained)
	 *   we check whether the Pokemon that the agent is trying to "catch" is the same
	 *   Pokemon that the agent tried to catch before.
	 *   If so, the agent is probably unable to catch the Pokemon because of the problem
	 *   we described above.
	 *   Then the algorithm will temporarily accelerate the number of calls to the server
	 *   until the next call to the nextNode function which will return the number of
	 *   calls to the server to the default.
	 * @param g
	 * @param src
	 * @return nextNode
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
	public void init(game_service game) {
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
	public void initAgentDisconnectedGraph(game_service game, List<CL_Pokemon> pokemonS, int agentsNum) {
		HashSet<Collection<node_data>> allGroup = new HashSet<>();
		HashSet<node_data> vis = new HashSet<>();
		directed_weighted_graph g = _ar.getGraph();
		DWGraph_Algo ga = new DWGraph_Algo(g);
		Collection<node_data> nodes = g.getV();
		int ageSum = 0;
		//here we fine all the graphing component of a graph
		allGroup = ga.connected_component();

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
	public List<CL_Pokemon> findPokInGroup(List<CL_Pokemon> pokemonS, Collection<node_data> group) {
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
	 * About json2numAgent(game_service game) method:
	 *   this method find the agents
	 *   that in the game by checking
	 *   the json string of the game.
	 * @param game - the game
	 * @return numAgent - the graphing component
	 */
	public int json2numAgent(game_service game) {
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
	public void initAgentNearPok(game_service game, List<CL_Pokemon> pokemonS, int agentsNum, Collection<node_data> nodes) {
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
	@SuppressWarnings("deprecation")
	public void initFrame(game_service game) {
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



	static class PokemonComparator implements Comparator<CL_Pokemon> {
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
	public directed_weighted_graph json2graph(String s){
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

	/**
	 * About setData method:
	 *   this method we using to set the data of
	 *   the id, scenario_num, grade and moves.
	 *   this is the resulte of the game.
	 *   used to show it on the open screen after the
	 *   game is over.
	 */
	private void setData(){
		count = 0;
		endOfGame = true;
		data[0] = id;
		data[1] = scenario_num;
		try {
			data[2] = _ar.getGrade();
			data[3] = _ar.getMoves();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	//////// open screen //////
	/**
	 * About openFrame() method:
	 *  in this method we define a JFrame that
	 *  contains all the elements that help us to
	 *  create a good looking open screen.
	 *  We define a 'text field' that we can read
	 *  the ID at the beginning and the level of the
	 *  game after.
	 *  We define a 'button' that after we read the ID
	 *  or the level of the game we can click on it and
	 *  it lead as to the next window in addition we can
	 *  click on the 'Enter' in the keyboard and it is
	 *  make the same effect.
	 *  in case of the ID or the level of the game
	 *  isn't correct then appear 'Try again' ro
	 *  'Put a correct number!' in the 'text field'.
	 *  also we can leave the game by click on the button
	 *  of the exit in the top right corner.
	 */
	@SuppressWarnings("deprecation")
	public void openFrame(){
		JFrame f = new JFrame("Welcome!");
		f.setSize(1000,700);
		final JTextField tf = new JTextField("Enter ID"); // Enter ID (skip to play offline)
		tf.setMaximumSize(new Dimension(300, 40));
		tf.setBounds(f.getWidth()/2-150,f.getHeight()/15*7,300,40);
		tf.setHorizontalAlignment(JTextField.CENTER);
		tf.setBackground(Color.LIGHT_GRAY);
		f.setFocusable(true);
		tf.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(java.awt.event.FocusEvent evt) {
				if (tf.getText().equals("Enter level")||tf.getText().equals("Try again!")||
						tf.getText().equals("Put a correct number!")||
						tf.getText().equals("Enter ID")) {
					tf.setText("");
				}
			}
		});
		JButton b = new JButton("Login");
		f.getRootPane().setDefaultButton(b);
		b.setBounds(f.getWidth()/2-150,f.getHeight()/15*7+40,300,40);
		b.setBackground(Color.GRAY);
		b.setDoubleBuffered(true);
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String a = tf.getText();
				int num;
				if (count == 0) {
					try {
						id = Integer.parseInt(a);
						count = 1;
						tf.setText("Enter level");
						b.setText("Start");
						f.requestFocus();
					} catch (Exception r) {
						tf.setText("Try again!");
						f.requestFocus();
					}
				} else {
					try {
						num = Integer.parseInt(a);
						game_service game = Game_Server_Ex2.getServer(num); // you have [0,23] games
						String g = game.getGraph();
						scenario_num = num;
						f.setVisible(false);
						start();
					} catch (Exception r){
						count++;
						if (count > 3) tf.setText("Put a correct number!");
						else tf.setText("Try again!");
						f.requestFocus();
					}
				}
			}
		});
		f.addComponentListener(new ComponentAdapter() {
		    public void componentMoved(ComponentEvent e) {
		    	int w = f.getWidth();
		    	int h = f.getHeight();
		    	tf.setBounds(w/2-2*w/11,h/15*7, w/3,h/17);
		    	b.setBounds(w/2-2*w/11,h/15*7+h/17, w/3,h/17);
		    }
		});
		f.add(b);
		f.add(tf);
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

	/**
	 * About function start():
	 *  in this function we start the Thread
	 *  that turns on the run function of the game
	 */
	public static void start() throws InterruptedException {
		Thread client = new Thread(new Ex2());
		client.start();
	}

	/**
	 * About OpenFrame class:
	 *  in this class we define the open image that appeared
	 *  in the open screen of the game using the 'extends JPanel'
	 *  and put it in the center of the screen.
	 *  After we play we write the results in the screen
	 */
	private static class OpenFrame extends JPanel {
		private static final long serialVersionUID = 1L;
		Image openS = new ImageIcon(getClass().getResource("/go1.jpg")).getImage();
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			int w = this.getWidth();
			int h = this.getHeight();
			g.drawImage(openS, 0, 0, w, h, this);
			if (endOfGame == true) {
				g.drawRect(w/2-w/10,(2*25)*h/75,w/5,h/5);
				Font font = new Font("Verdana", Font.TYPE1_FONT, w/60);
				g.setFont(font);
				g.drawString("Last Results:",w/2-w/10+30, (2*25)*h/71);
				font = new Font("Verdana", Font.CENTER_BASELINE, w/60);
				g.setFont(font);
				g.drawString("ID: "+data[0], w/2-w/10+30, (2*25)*h/68);
				g.drawString("Level: "+data[1], w/2-w/10+30, (2*25)*h/65);
				g.drawString("Grade: "+data[2], w/2-w/10+30, (2*25)*h/62);
				g.drawString("Moves: "+data[3], w/2-w/10+30, (2*25)*h/59);
			}
		}
	}
}