package api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;

class DWGraph_AlgoTest {
	/* Most tests are performed by the following graph */
	public directed_weighted_graph graph() {
		directed_weighted_graph g = new DWGraph_DS();
		node_data c0 = new NodeData(0, new GeoLocation());
    	node_data c1 = new NodeData(1, new GeoLocation());
    	node_data c2 = new NodeData(2, new GeoLocation());
    	node_data c3 = new NodeData(3, new GeoLocation());
    	node_data c4 = new NodeData(4, new GeoLocation());
    	node_data c5 = new NodeData(5, new GeoLocation());
    	node_data c6 = new NodeData(6, new GeoLocation());
    	node_data c7 = new NodeData(7, new GeoLocation());
    	node_data c8 = new NodeData(8, new GeoLocation());
    	node_data c9 = new NodeData(9, new GeoLocation());
    	node_data c10 = new NodeData(10, new GeoLocation());
    	node_data c11 = new NodeData(11, new GeoLocation());
    	node_data c12 = new NodeData(12, new GeoLocation());
    	node_data c13 = new NodeData(13, new GeoLocation());
    	node_data c14 = new NodeData(14, new GeoLocation());
    	g.addNode(c0);
    	g.addNode(c1);
    	g.addNode(c2);
    	g.addNode(c3);
    	g.addNode(c4);
    	g.addNode(c5);
    	g.addNode(c6);
    	g.addNode(c7);
    	g.addNode(c8);
    	g.addNode(c9);
    	g.addNode(c10);
    	g.addNode(c11);
    	g.addNode(c12);
    	g.addNode(c13);
    	g.addNode(c14);
		g.connect(c1.getKey(), c2.getKey(),0);
		g.connect(c2.getKey(), c3.getKey(),4);
		g.connect(c3.getKey(), c7.getKey(),9);
		g.connect(c7.getKey(), c4.getKey(),7);
		g.connect(c4.getKey(), c5.getKey(),5);
		g.connect(c5.getKey(), c4.getKey(),6);
		g.connect(c5.getKey(), c6.getKey(),2);
		g.connect(c6.getKey(), c7.getKey(),15);
		g.connect(c7.getKey(), c9.getKey(),2);
		g.connect(c9.getKey(), c8.getKey(),1);
		g.connect(c8.getKey(), c1.getKey(),3);
		g.connect(c10.getKey(), c11.getKey(),7.5);
		g.connect(c10.getKey(), c12.getKey(),0);
		g.connect(c11.getKey(), c13.getKey(),0);
		g.connect(c12.getKey(), c13.getKey(),0);
		g.connect(c9.getKey(), c14.getKey(),3);
	/*	
	   Graph illustration:
	   
	   (0)
	              w=0
	   (1)>------>-------->----->(2)
	    ^           w=2           \>
	    |      (5)>----->(6)        \
	    |      ^   >        >        \
	    ^      |    \        \        \
	    |      |  w=6\    w=15\     w=4\
	    |   w=5|      \        \        |
	 w=3|      |       \>       \>      >(3)
	    |      ^-<-----<(4)<----<(7)<-----<
	    |         w=5       w=7  \>   w=9
	    ^                         \
	    |                       w=2\      
	    |                           \>                    
	    |--------<------<(8)<----<---(9)----->(14)                     
	            w=3            w=1       w=3

	  	      w=7.5
	  	(10)>-------->(11)
	  	 >             |
	  w=0|             |w=0
	  	 |             >
	  	(12)>-------->(13)
	  	       w=0
	  	*/
		
		return g;
    }
	directed_weighted_graph graph = graph();
	
    @Test
    void init() {
    }

    @Test
    void getGraph() {
    }

    @Test
    void copy() {
    }

    @Test
    void isConnected() {
    	directed_weighted_graph g = graph();
    	dw_graph_algorithms ga = new DWGraph_Algo();
    	ga.init(g);
    	assertFalse(ga.isConnected());
    	g.removeNode(0);
    	assertFalse(ga.isConnected());
    	g.removeNode(10);
    	g.removeNode(11);
    	g.removeNode(12);
    	g.removeNode(13);
    	assertFalse(ga.isConnected());
    	g.removeNode(14);
    	assertTrue(ga.isConnected());
    }

    @Test
    void shortestPathDist() {
    	// we need to fix this method
    	

    	directed_weighted_graph g = graph();
    	dw_graph_algorithms ga = new DWGraph_Algo();
    	ga.init(g);
    	double answer;
    	answer = ga.shortestPathDist(1,7);
    	System.out.print("Your answer: "+answer+" >> ");
    	if (answer==13) System.out.println("Correct Answer!");
    	else System.out.println("Wrong! Should be 13!");
    	
    	answer = ga.shortestPathDist(1,10);
    	System.out.print("Your answer: "+answer+" >> ");
    	if (answer==-1) System.out.println("Correct Answer!");
    	else System.out.println("Wrong! Should be -1!");
    	
    	answer = ga.shortestPathDist(10,13);
    	System.out.print("Your answer: "+answer+" >> ");
    	if (answer==0) System.out.println("Correct Answer!");
    	else System.out.println("Wrong! Should be 0!");
    	
    	answer = ga.shortestPathDist(7,6);
    	System.out.print("Your answer: "+answer+" >> ");
    	if (answer==14) System.out.println("Correct Answer!");
    	else System.out.println("Wrong! Should be 14!");

    }

    @Test
    void shortestPath() {
    	// we need to fix this method
    	

    	directed_weighted_graph g = graph();
    	dw_graph_algorithms ga = new DWGraph_Algo();
    	ga.init(g);
    	List<node_data> directions = ga.shortestPath(0, 0);
    	String answer;
    	
    	directions = ga.shortestPath(1, 7);
		System.out.print("The Path from 1 to 7: ");
		answer = "";
		for (Iterator<node_data> iterator = directions.iterator(); iterator.hasNext();)
			answer = answer + " "+iterator.next().getKey()+" >";
		System.out.print(answer);
		if (answer.equals(" 1 > 2 > 3 > 7 >")) System.out.print("> Correct Answer!");
		else System.out.print("> Wrong Answer!");
		System.out.println();
		//assertEquals(" 1 > 2 > 3 > 7 >", answer);
    	
		directions = ga.shortestPath(1, 10);
		System.out.print("The Path from 1 to 10 >> ");
		if (ga.shortestPath(1, 10)==null) System.out.print("> Correct Answer!");
		else System.out.print("> Wrong Answer! Should be Null!");
		System.out.println();
		//assertNull(answer);
    	
		directions = ga.shortestPath(10, 13);
		System.out.print("The Path from 10 to 13: ");
		answer = "";
		for (Iterator<node_data> iterator = directions.iterator(); iterator.hasNext();)
			answer = answer + " "+iterator.next().getKey()+" >";
		System.out.print(answer);
		if (answer.equals(" 10 > 12 > 13 >")) System.out.print("> Correct Answer!");
		else System.out.print("> Wrong Answer!");
		System.out.println();
		//assertEquals(" 10 > 12 > 13 >", answer);
    	
		directions = ga.shortestPath(7, 6);
		System.out.print("The Path from 7 to 6: ");
		answer = "";
		for (Iterator<node_data> iterator = directions.iterator(); iterator.hasNext();)
			answer = answer + " "+iterator.next().getKey()+" >";
		System.out.print(answer);
		if (answer.equals(" 7 > 4 > 5 > 6 >")) System.out.print("> Correct Answer!");
		else System.out.print("> Wrong Answer!");
		System.out.println();
		//assertEquals(" 7 > 4 > 5 > 6 >", answer);

    }

    @Test
    void save() {
    }

    @Test
    void load() {
    }
}