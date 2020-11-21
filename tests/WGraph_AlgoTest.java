package ex1.tests;
import ex1.src.*;
import ex1.src.WGraph_Algo;
import ex1.src.node_info;
import ex1.src.weighted_graph;
import ex1.src.weighted_graph_algorithms;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * the goal of this test class is to check the reliability of WGraph_Algo.
 * part of the testes taken from or based on the testes from the course's github.(including creating graph)
 */

class WGraph_AlgoTest {
    /**
     * this test check if the graph connected or not when we remove or add node/edge
     */
    @Test
    void isConnected() {
        weighted_graph g0 = WGraph_DSTest.graph_creator(0,0,1);
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertTrue(ag0.isConnected());

        g0 = WGraph_DSTest.graph_creator(1,0,1);
        ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertTrue(ag0.isConnected());

        g0 = WGraph_DSTest.graph_creator(2,0,1);
        ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertFalse(ag0.isConnected());

        g0 = WGraph_DSTest.graph_creator(2,1,1);
        ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertTrue(ag0.isConnected());

        g0 = WGraph_DSTest.graph_creator(10,30,1);
        ag0.init(g0);
        boolean b = ag0.isConnected();
        assertTrue(b);

       g0 = WGraph_DSTest.graph_creator(2,1,1);
         ag0 = new WGraph_Algo();
         g0.removeNode(1);
        ag0.init(g0);
        assertTrue(ag0.isConnected());

        g0 = WGraph_DSTest.graph_creator(2,1,1);
        ag0 = new WGraph_Algo();
        g0.removeEdge(0,1);
        ag0.init(g0);
        assertFalse(ag0.isConnected());

        g0 = WGraph_DSTest.graph_creator(2,1,1);
        ag0 = new WGraph_Algo();
        g0.addNode(2);
        ag0.init(g0);
        assertFalse(ag0.isConnected());

    }

    /**
     * check the shortestPathDist function
     */
    @Test
    void shortestPathDist() {
        weighted_graph g0 = small_graph();
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertTrue(ag0.isConnected());

        double d = ag0.shortestPathDist(0,10);
        assertEquals(d, 5.1);
        d = ag0.shortestPathDist(0,9);
        assertEquals(d, 6.1);
        g0 = WGraph_DSTest.graph_creator(2,0,1);
        ag0 = new WGraph_Algo();
        ag0.init(g0);

        d = ag0.shortestPathDist(0,1);
        assertEquals(d, -1);

        g0 = WGraph_DSTest.graph_creator(2,0,1);
        ag0 = new WGraph_Algo();
        ag0.init(g0);

        d = ag0.shortestPathDist(0,0);
        assertEquals(0, d);
    }
    /**
     * check the shortestPath function
     */
    @Test
    void shortestPath() {

        weighted_graph g0 = small_graph();
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        List<node_info> sp = ag0.shortestPath(0,10);
        double[] checkTag = {0.0, 1.0, 2.0, 3.1, 5.1};
        int[] checkKey = {0, 1, 5, 7, 10};
        int i = 0;
        for(node_info n: sp) {

            assertEquals(checkKey[i], n.getKey());
            assertEquals(checkTag[i], n.getTag());
            i++;
        }

        g0 = WGraph_DSTest.graph_creator(2,1,1);
        ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertTrue(ag0.isConnected());
         sp = ag0.shortestPath(0,1);
         int[] checkKey2 = {0, 1};
        i = 0;
        for(node_info n: sp) {

            assertEquals(checkKey2[i], n.getKey());
            i++;
        }


    }
    /**
     * check save and load of the program
     */
    @Test
    void save_load() {
        weighted_graph g0 = WGraph_DSTest.graph_creator(10,30,1);
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        String str = "g0.obj";
        ag0.save(str);
        weighted_graph g1 = WGraph_DSTest.graph_creator(10,30,1);
        boolean b=ag0.load(str);
        assertTrue(b);
        assertEquals(g0,g1);
        g0.removeNode(0);
        assertNotEquals(g0,g1);
    }
    /**
     * init check
     */
    @Test
    void init() {
        weighted_graph g0 = small_graph();
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertEquals(g0,ag0.getGraph());
    }

    /**
     * getgraph check
     */
    @Test
    void getGraph() {
        weighted_graph g0 = small_graph();
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        weighted_graph g1 = ag0.getGraph();
        assertEquals(g0,g1);
    }

    /**
     * copy check
     */
    @Test
    void copy() {
        weighted_graph g0 = small_graph();
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        weighted_graph g1 = ag0.copy();
        assertEquals(g0,g1);
    }

    /**
     * create a small graph
     */
    private weighted_graph small_graph() {
        weighted_graph g0 = WGraph_DSTest.graph_creator(11,0,1);
        g0.connect(0,1,1);
        g0.connect(0,2,2);
        g0.connect(0,3,3);

        g0.connect(1,4,17);
        g0.connect(1,5,1);
        g0.connect(2,4,1);
        g0.connect(3, 5,10);
        g0.connect(3,6,100);
        g0.connect(5,7,1.1);
        g0.connect(6,7,10);
        g0.connect(7,10,2);
        g0.connect(6,8,30);
        g0.connect(8,10,10);
        g0.connect(4,10,30);
        g0.connect(3,9,10);
        g0.connect(9,10,1);
        g0.connect(8,10,10);

        return g0;
    }



}