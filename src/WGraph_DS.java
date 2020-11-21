package ex1.src;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.io.Serializable;
import java.util.Objects;
/**
 * this class implements weighted_graph interface
 */
    public class WGraph_DS  implements weighted_graph ,Serializable {
    /**
     * this private class implements node_info interface
     */
        private class NodeInfo implements node_info,Serializable {
            private int key;//unique id to each node
            private String Info;
            private double Tag;

            // Constructors

            /**
             * create a deep copy of given node
             * @param n
             */
            public NodeInfo(node_info n) {
                this.Tag = n.getTag();
                this.Info = n.getInfo();
                this.key = n.getKey();

            }
            /**
             * create a node with given key
             * @param key
             */
            public NodeInfo(int key) {

                this.Tag = Double.MAX_VALUE;
                this.Info = "";
                this.key = key;


            }

            /**
             * return true if the given node is equal to this node
             * @param o
             * @return
             */
            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                NodeInfo nodeInfo = (NodeInfo) o;

                if (key != nodeInfo.key) return false;
                if (Double.compare(nodeInfo.Tag, Tag) != 0) return false;
                return Info.equals(nodeInfo.Info);
            }

            @Override
            public int hashCode() {
                return key;
            }

            @Override
            public int compare(Object n) {
                node_info t = (node_info) n;
                return Double.compare(this.Tag, t.getTag());
            }
            /**
             * Return the key (id) associated with this node each node_data have a unique key.
             * @return int key
             */

            @Override
            public int getKey() {
                return this.key;
            }
            /**
             * return the remark (meta data) associated with this node.
             * @return Info
             */
            @Override
            public String getInfo() {
                return this.Info;
            }
            /**
             * Allows changing the remark (meta data) associated with this node.
             * @param s
             */
            @Override
            public void setInfo(String s) {
                this.Info = s;
            }
            /**
             * Temporal data
             * @return double Tag
             */
            @Override
            public double getTag() {
                return this.Tag;
            }
            /**
             * Allow setting the "tag" value for temporal marking an node.
             * @param t
             */
            @Override
            public void setTag(double t) {
                this.Tag = t;
            }

            @Override
            public String toString() {
                return "{" +
                        "key=" + key +
                        ", Tag=" + Tag +
                        '}';
            }
        }

        private HashMap<Integer, node_info> V = new HashMap<Integer, node_info>();//contain all the nodes in the graph
        private int ModeCount;//number of changes done in this graph
        private int edges;//number of edges in the graph
        private HashMap<Integer, HashMap<node_info, Double>> N = new HashMap<Integer, HashMap<node_info, Double>>();//contain all the naiber for each nodes in the graph
        // Constructors
        /**
         * create an empty graph
         */
        public WGraph_DS() {
            this.ModeCount = 0;
            this.edges = 0;
        }
        /**
         * create a deep copy of given graph
         * @param g
         */
        public WGraph_DS(weighted_graph g) {
            node_info n1;
            Iterator<node_info> e = g.getV().iterator();
            while (e.hasNext()) {
                node_info t = e.next();
                this.addNode(t.getKey());
            }

            e = g.getV().iterator();
            while (e.hasNext()) {
                n1 = e.next();
                Iterator<node_info> e2 = g.getV(n1.getKey()).iterator();
                while (e2.hasNext()) {
                    node_info t = e2.next();
                    double w = g.getEdge(t.getKey(), n1.getKey());
                    this.connect(n1.getKey(), t.getKey(), w);
                }
            }
            this.ModeCount = g.getMC();
        }
        /**
         * return true if the given graph is equal to this graph
         * @param o
         * @return
         */

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            WGraph_DS wGraph_ds = (WGraph_DS) o;

            if (edges != wGraph_ds.edges) return false;
            if (!V.equals(wGraph_ds.V)) return false;
            return N.equals(wGraph_ds.N);
        }

        @Override
        public int hashCode() {
            int result = V.hashCode();
            result = 31 * result + edges;
            result = 31 * result + N.hashCode();
            return result;
        }
        /**
         * return the node_data by the node_id,
         * @param key - the node_id
         * @return the node_data by the node_id, null if none.
         */
        @Override
        public node_info getNode(int key) {
            return V.get(key);
        }
        /**
         * return true iff (if and only if) there is an edge between node1 and node2
         * @param node1
         * @param node2
         * @return
         */
        @Override
        public boolean hasEdge(int node1, int node2) {
            if (this.getNode(node1) != null && this.getNode(node2) != null) {
                return N.get(node1).get(this.getNode(node2))!=null && N.get(node2).get(this.getNode(node1))!=null;
            }
            return false;
        }
        /**
         * return the weight if the edge (node1, node1). In case
         * there is no such edge - should return -1
         * @param node1
         * @param node2
         * @return double
         */
        @Override
        public double getEdge(int node1, int node2) {
            if (this.hasEdge(node1, node2)) {
                return N.get(node1).get(this.getNode(node2));
            }
            return -1;
        }
        /**
         * add a new node to the graph with the given key.
         * if there is already a node with such a key -> no action performed.
         * @param key
         */
        @Override
        public void addNode(int key) {
            if (!V.containsKey(key)) {
                node_info n = new NodeInfo(key);
                V.put(key, n);
                HashMap<node_info, Double> ni = new HashMap<node_info, Double>();
                N.put(key, ni);
                ModeCount++;

            }
        }
        /**
         * Connect an edge between node1 and node2, with an edge with weight >=0.
         * if the edge node1-node2 already exists - the method simply updates the weight of the edge.
         * @param node1
         * @param node2
         * @param w
         */
        @Override
        public void connect(int node1, int node2, double w) {
            if(node1!=node2) {
                if (this.getNode(node1) != null && this.getNode(node2) != null) {
                    if (!this.hasEdge(node1, node2)) {
                        N.get(node1).put(this.getNode(node2), w);
                        N.get(node2).put(this.getNode(node1), w);
                        edges++;
                        ModeCount++;
                    } else if(w!=this.getEdge(node1,node2)){
                        N.get(node1).replace(this.getNode(node2), w);
                        N.get(node2).replace(this.getNode(node1), w);
                        ModeCount++;
                    }

                }
            }
        }

        /**
         * This method return a pointer (shallow copy) for a
         * Collection representing all the nodes in the graph.
         * @return Collection<node_data>
         */
        @Override
        public Collection<node_info> getV() {
            return V.values();
        }
        /**
         * This method returns a Collection containing all the
         * nodes connected to node_id
         * @return Collection<node_data>
         */
        @Override
        public Collection<node_info> getV(int node_id) {
            return N.get(node_id).keySet();
        }
        /**
         * Delete the node (with the given ID) from the graph -
         * and removes all edges which starts or ends at this node.
         * @return the data of the removed node (null if none).
         * @param key
         */
        @Override
        public node_info removeNode(int key) {
            node_info t = this.getNode(key);//the node we want to remove
            if (t != null) {
                V.remove(key, t);
                Iterator<node_info> i = this.getV(key).iterator();
                while (i.hasNext()) {
                    node_info n = i.next();
                    this.removeEdge(key, n.getKey());
                    edges--;
                    ModeCount++;
                }
                N.remove(t.getKey(), N.get(t.getKey()));
            }
            return t;
        }
        /**
         * Delete the edge from the graph,
         * @param node1
         * @param node2
         */
        @Override
        public void removeEdge(int node1, int node2) {
            if (V.containsKey(node1) && V.containsKey(node2)) {
                if (this.hasEdge(node1, node2)) {
                    double w = this.getEdge(node1, node2);
                    N.get(node1).remove(this.getNode(node2), w);
                    N.get(node2).remove(this.getNode(node1), w);
                    edges--;
                }
            }
        }
        /** return the number of vertices (nodes) in the graph.
         * @return int
         */
        @Override
        public int nodeSize() {
            return V.size();
        }
        /**
         * return the number of edges (undirectional graph).
         * @return int
         */
        @Override
        public int edgeSize() {
            return edges;
        }
        /**
         * return the Mode Count - for testing changes in the graph.
         * Any change in the inner state of the graph should cause an increment in the ModeCount
         * @return int
         */
        @Override
        public int getMC() {
            return this.ModeCount;
        }

        @Override
        public String toString() {
            String s="{";
            Iterator<node_info> i =this.getV().iterator();
            while (i.hasNext()){
                node_info n=i.next();
                s=s+"{key:"+n.getKey()+",degree:"+N.get(n.getKey()).size()+"}";
            }
            return s+"}";

        }
    }


