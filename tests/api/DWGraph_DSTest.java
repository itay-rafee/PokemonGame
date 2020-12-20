package api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DWGraph_DSTest {


    private static directed_weighted_graph _g0;//empty graph
    private static directed_weighted_graph _g1;//graph with 1 node
    private static directed_weighted_graph _g2;//graph with 1000*50 node
    private static int _node = 1000*50;


    //help function
    void buildEdge(directed_weighted_graph g, int e){
        for (int i = 0; i < e; i++) {
            double w = Math.random()*10;
            g.connect(i,i+1,w);
        }
    }

    void buildEdge_2(directed_weighted_graph g, int e){
        for (node_data n : g.getV()) {
            g.connect(n.getKey(),e,1.0);
        }
    }

    void buildEdge_3(directed_weighted_graph g, int e){
        for (node_data n : g.getV()) {
            g.connect(e,n.getKey(),1.0);
        }
    }

    //define graphs
    @BeforeEach
    void setUp() {
        _g0 = new DWGraph_DS();
        _g1 = new DWGraph_DS();
        _g2 = new DWGraph_DS();
        _g1.addNode(new NodeData(1,new GeoLocation()));
        for (int i = 0; i < _node; i++) {
            _g2.addNode(new NodeData(i,new GeoLocation()));
        }

    }

    @Test
    void getNode_1() {
        assertNull(_g0.getNode(0));
    }

    @Test
    void getNode_2() {
        assertNull(_g1.getNode(0));
    }

    @Test
    void getNode_3() {
        node_data n = _g1.getNode(1);
        assertEquals(n.getKey(),1);//the key of the node
        assertEquals(n.getTag(),0);//tag always zero
    }

    @Test
    void getEdge_1() {
        assertNull(_g2.getEdge(1, 2));
    }

    @Test
    void getEdge_2() {
        _g2.connect(1,2,1);
        assertEquals(_g2.getEdge(1,2).getWeight(), 1);
    }

    @Test
    void getEdge_3() {
        _g2.connect(1,2,10.3);
        assertEquals(_g2.getEdge(1,2).getWeight(), 10.3);
    }

    @Test
    void getEdge_4() {
        assertNull(_g1.getEdge(1, 1));
    }

    @Test
    void getEdge_5() {
        assertNull(_g0.getEdge(1, 1));
    }

    @Test
    void addNode_1() {
        assertEquals(_g0.nodeSize(),0);
        _g0.addNode(new NodeData(1,new GeoLocation()));
        assertEquals(_g0.nodeSize(), 1);
    }

    @Test
    void addNode_2() {
        assertEquals(_g1.nodeSize(),1);
        _g1.addNode(new NodeData(1,new GeoLocation()));
        assertEquals(_g1.nodeSize(), 1);
    }

    @Test
    void connect_2() {
        int edge = 1000*50-2;
        buildEdge(_g2,edge);
        for (int i = 0; i < edge; i++) {
            assertNotNull(_g2.getEdge(i, i + 1));
        }
        assertNull(_g2.getEdge(edge, edge + 1));
    }

    @Test
    void getV() {
        Collection<node_data> list = _g2.getV();
        assertEquals(list.size(),_node);
    }

    @Test
    void testGetV() {
        for (int i = 1; i < _node; i++){
            _g2.connect(0,i,1);
        }
        assertEquals(_g2.getE(0).size(),_node-1);
        int t = 50;
        for (int i = 1; i < t; i++) {
            _g2.removeNode(i);
        }
        assertEquals(_g2.getE(0).size(),_node-t);
    }

    @Test
    void removeNode_1() {
        _g0.addNode(new NodeData(1,new GeoLocation()));
        assertEquals(_g0.getNode(1).getKey(),1);
        _g0.removeNode(1);
        assertNull(_g0.getNode(1));
    }

    @Test
    void removeNode_2() {
        _g0.addNode(new NodeData(1,new GeoLocation()));
        node_data n = _g0.removeNode(1);
        assertEquals(n.getKey(),1);
    }

    @Test
    void removeNode_3() {
        for (int i = 1; i < _node; i++){
            _g2.connect(0,i,1);
        }
        assertEquals(_node-1,_g2.edgeSize());
        for (int i = 1; i < _node; i++){
            assertNotNull(_g2.getEdge(0,i));
        }
        _g2.removeNode(0);
        for (int i = 1; i < _node; i++){
            assertNull(_g2.getEdge(0,i));
        }
        assertEquals(0,_g2.edgeSize());
    }

    @Test
    void removeEdge() {
        _g0.addNode(new NodeData(1,new GeoLocation()));
        _g0.addNode(new NodeData(2,new GeoLocation()));
        _g0.connect(1,2,1);
        assertNotNull(_g0.getEdge(1,2));
        _g0.removeEdge(1,2);
        assertNull(_g0.getEdge(1,2));
    }

    @Test
    void nodeSize_1() {
        assertEquals(_g0.nodeSize(),0);
    }

    @Test
    void nodeSize_2() {
        assertEquals(_g1.nodeSize(),1);
    }

    @Test
    void nodeSize_3() {
        assertEquals(_g2.nodeSize(),_node);
    }

    @Test
    void edgeSize_1() {
        assertEquals(_g2.edgeSize(),0);
    }

    @Test
    void edgeSize_2() {
        _g2.connect(1,2,1);
        assertEquals(_g2.edgeSize(),1);
        _g2.removeEdge(1,2);
        assertEquals(_g2.edgeSize(),0);
    }

    @Test
    void edgeSize_3() {
        buildEdge(_g2,_node-1);
        assertEquals(_g2.edgeSize(),_node-1);
    }

    @Test
    void getMC() {
        int mc = 0, t = 100;
        for (int i = 0; i < t; i++) {
            _g0.addNode(new NodeData(i,new GeoLocation()));
        }
        mc += t;
        buildEdge_2(_g0,0);
        buildEdge_3(_g0,0);
        mc += t-1;
        mc += t-1;
        _g0.removeNode(0);
        mc += t-1;
        mc += t;
        assertEquals(mc,_g0.getMC());
    }

}