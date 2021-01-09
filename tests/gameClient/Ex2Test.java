package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.junit.jupiter.api.Test;
import java.util.Collection;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashSet;

class Ex2Test {

    @Test
    void findGroup() {
        Ex2 e = new Ex2();
        directed_weighted_graph g = new DWGraph_DS();
        for (int i = 0; i < 100; i++) {
            g.addNode(new NodeData(i));
        }
        for (int i = 10; i <= 100; i += 10) {
            for (int j = i-10; j < i-1; j++) {
                g.connect(j,j+1,1);
                g.connect(j+1,j,1);
            }
        }
        HashSet<Collection<node_data>> allGroup;
        DWGraph_Algo ga = new DWGraph_Algo(g);

        //here we fine all the graphing component of a graph
        allGroup = ga.connected_components();
        assertEquals(allGroup.size(),10);
        for (int i = 10; i < 100; i += 10) {
            g.connect(i-1,i,1);
            g.connect(i,i-1,1);
        }

        //here we fine all the graphing component of a graph
        allGroup = ga.connected_components();

        assertEquals(allGroup.size(),1);

    }

    @Test
    void json2numAgent() {
        Ex2 e = new Ex2();
        for (int i = 0; i < 24; i++) {
            game_service game = Game_Server_Ex2.getServer(i); // you have [0,23] games
            String[] s = game.toString().split("\"agents\":");
            int a2 = e.json2numAgent(game);
            if (a2 < 10){
                int a1 = Integer.parseInt(s[1].charAt(0)+"");
                assertEquals(a1,a2);
            }
        }
    }


    @Test
    void isConnectedBFS() {
        directed_weighted_graph g = new DWGraph_DS();
        DWGraph_Algo ga = new DWGraph_Algo(g);
        for (int i = 0; i < 10; i++) {
            g.addNode(new NodeData(i));
        }
        for (int i = 0; i < 9; i++) {
            g.connect(i,i+1,1);
        }
        for (int i = 0; i < 10; i++) {
            HashSet<node_data> c = ga.isConnectedBFS(i, g);
            assertEquals(c.size(),10-i);
        }
    }

    @Test
    void flipedGraph() {
        directed_weighted_graph g = new DWGraph_DS();
        DWGraph_Algo ga = new DWGraph_Algo(g);
        for (int i = 0; i < 10; i++) {
            g.addNode(new NodeData(i));
        }
        for (int i = 0; i < 9; i++) {
            g.connect(i,i+1,1);
        }
        g = ga.flipedGraph();
        for (int i = 0; i < 10; i++) {
            HashSet<node_data> c = ga.isConnectedBFS(i, g);
            assertEquals(c.size(),i+1);
        }
    }

    @Test
    void json2graph() {
        Ex2 e = new Ex2();
        directed_weighted_graph g;
        for (int i = 0; i < 24; i++) {
            game_service game = Game_Server_Ex2.getServer(i); // you have [0,23] games
            g = e.json2graph(game.getGraph());
            assertNotNull(g);
        }
    }


}