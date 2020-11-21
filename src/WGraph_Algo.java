package ex1.src;

import java.io.*;
import java.util.*;
/**
 * this class implements weighted_graph_algorithms interface
 */
public class WGraph_Algo implements weighted_graph_algorithms,Serializable
{
    private class comp implements Comparator<node_info>,Serializable {

        @Override
        public int compare(node_info o1, node_info o2) {
            return Double.compare(o1.getTag(), o2.getTag());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WGraph_Algo that = (WGraph_Algo) o;
        return Objects.equals(g, that.g);
    }

    @Override
    public int hashCode() {
        return Objects.hash(g);
    }

    private weighted_graph g;//The graph used by the class
    /**
     * connect the class with empty graph to work on
     *
     */
    public WGraph_Algo() {
        this.g = new WGraph_DS();

    }
    // Constructors
    /**
     * connect the class with given graph to work on
     * @param g
     */
    public WGraph_Algo(weighted_graph g) {
        this.init(g);
    }
    /**
     * Init the graph on which this set of algorithms operates on.
     * @param g
     */
    @Override
    public void init(weighted_graph g) {
        this.g = g;
    }
    /**
     * Return the underlying graph of which this class works.
     * @return weighted_graph
     */
    @Override
    public weighted_graph getGraph() {
        return this.g;
    }
    /**
     * Compute a deep copy of this weighted graph.
     * @return weighted_graph
     */
    @Override
    public weighted_graph copy() {
        weighted_graph g1 = new WGraph_DS(this.g);
        return g1;
    }

    /**
     * this function implements the BFS algorithm on the graph
     * marking any node that has been visited.
     *
     * @param key
     */
    private void BFS(int key) {
        Queue<node_info> q = new LinkedList<node_info>();
        node_info t = this.g.getNode(key);
        q.add(t);
        t.setTag(1);
        while (!q.isEmpty()) {
            t = q.poll();
            Iterator<node_info> e = this.g.getV(t.getKey()).iterator();
            while (e.hasNext()) {
                node_info ni = e.next();
                if (ni.getTag() != 1) {//check if  visited
                    ni.setTag(1);//set as visited
                    q.add(ni);
                }
            }
        }
    }

    /**
     * initialization of all the vertex, to avoid mistakes in the next operations.
     */
    private void initgraph() {//initialization of all the vertex, to avoid mistakes in the next operations.
        Iterator<node_info> e = this.g.getV().iterator();
        while (e.hasNext()) {
            node_info t = e.next();
            t.setTag(Double.MAX_VALUE);
            t.setInfo("");
        }
    }
    /**
     * Returns true if and only if (iff) there is a valid path from EVREY node to each
     * other node.  ubdirectional graph.
     * @return
     */
    @Override
    public boolean isConnected() {
        if (this.g == null)
            return true;
        if (this.g.getV().isEmpty()||this.g.nodeSize()==1)
            return true;
        if (this.g.nodeSize() > this.g.edgeSize() + 1)
            return false;
        initgraph();//initialization of all the vertex, to avoid mistakes in the next operations.
        Iterator<node_info> e = this.g.getV().iterator();
        node_info t = e.next();
        BFS(t.getKey());
        while (e.hasNext()) {
            t = e.next();
            if (t.getTag() != 1) { //check if not visited,if there is one vertex like that return false
                return false;
            }
        }
        return true;
    }
    /**
     * this function implements the Dijkstra algorithm on the graph
     * marking any node that has been visited and setting the Tag to the distance from the key node (source node)
     * for each node, in this search.
     * @param key
     * @return HashMap<Integer, node_info> that contain every parent of each node (consider minimum distance )
     *  in this search starting at key (id) node.
     */
    private HashMap<Integer, node_info> Dijkstra(int key) {
        HashMap<Integer, node_info> p = new HashMap<Integer, node_info>();
        comp compare = new comp();//comperator to determine the order in the priorityqueue.
        PriorityQueue<node_info> pq = new PriorityQueue(compare);
        node_info n = this.g.getNode(key);
        n.setTag(0);
        pq.add(n);

        while (!(pq.isEmpty())) {
            n = pq.poll();
            if(pq.peek()!=null&&pq.peek().getTag()<n.getTag()){//double check to avoid mistake in the order
                pq.add(n);
                n=pq.poll();
            }
            n.setInfo("v");//mark as visited when node has pollen from the queue
            Iterator<node_info>  e = this.g.getV(n.getKey()).iterator();
            while (e.hasNext()) {
                node_info t = e.next();
                if (!t.getInfo().equals("v")) {//do the next operation if not visited
                    double w = this.g.getEdge(n.getKey(), t.getKey()) + n.getTag();
                    if (w < t.getTag()) {//chek if it's the minimum distance from source's node.
                        t.setTag(w);//if so set the new distance
                        if (p.containsKey(t.getKey())) {//check it its not the first time we get to this node
                            p.replace(t.getKey(), n);//if so just replace his parent, to make sure that's the correct parent in shortest path
                        } else {
                            p.put(t.getKey(), n);//if not enter new key and parent to the map
                            pq.add(t);// add the new node (at the search ) to the queue
                        }
                    }
                }
            }

        }
        return p;
    }
    /**
     * returns the length of the shortest path between src to dest
     * if no such path --> returns -1
     * @param src - start node
     * @param dest - end (target) node
     * @return double
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        if (src == dest)
            return 0;
        initgraph();
        HashMap<Integer, node_info> p = Dijkstra(src);
        node_info n = this.g.getNode(dest);
        if (!p.containsKey(dest)) {
            return -1;
        }
        return n.getTag();
    }
    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * if no such path --> returns null;
     * @param src - start node
     * @param dest - end (target) node
     * @return List<node_info><
     */
    @Override
    public List<node_info> shortestPath(int src, int dest) {
        initgraph();
        HashMap<Integer, node_info> p = Dijkstra(src);//hash map with the parent of each node in this search
        if (!p.containsKey(dest)) {//if the map does not contain dest's key there is no path between theos nodes
            return null;
        }
        node_info t = this.g.getNode(dest);
        LinkedList<node_info> path = new LinkedList<>();
        path.add(t);
        while (t != this.g.getNode(src) && t != null) {
            t = p.get(t.getKey());
            path.addFirst(t);//add the nodes to the list in the correct order
        }
        if (t == null)
            return null;
        return path;
    }
    /**
     * taken from Elizabeth's presentation
     * Saves this weighted (undirected) graph to the given
     * file name
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */

    @Override
    public boolean save(String file) {
        boolean b = false;

        try {
            FileOutputStream out = new FileOutputStream(file);
            ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(this.g);
            out.close();
            os.close();
            b = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }
    /**
     * taken from Elizabeth's presentation
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     * @param file - file name
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {
        try {
            FileInputStream in = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(in);
            weighted_graph readCase = (weighted_graph) ois.readObject();
            in.close();
            ois.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  false;
    }

}
