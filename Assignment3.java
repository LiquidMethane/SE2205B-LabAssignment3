/**
 * group members:
 * Ruiqi Tian rtian25
 * Wei Dai wdai49
 */

public class Assignment3 {

    //declare parent array as class variable
    private int parent[];

    /**
     * this function uses breadth-first search to find an augmenting path connecting vertices s and d to which flow can
     * be added without infringing on capacity constraints of the network
     *
     * @param FN graph
     * @param s  source vertex
     * @param d  destination vertex
     * @return 1 if path is found, 0 otherwise
     */
    public int breadthFirstPathSearch(Graph FN, int s, int d) {

        //declare adjacency matrix of the graph
        Edge adjacencyMatrix[][] = new Edge[FN.numVertices()][FN.numVertices()];

        //fill in adjacency matrix using nested loop
        for (Vertex u : FN.vertices()) {
            for (Vertex v : FN.vertices()) {
                //assign edge to adjacency matrix, assign null if no edge exists
                adjacencyMatrix[u.getLabel()][v.getLabel()] = FN.getEdge(u, v);
            }
        }

        //declare queue structure
        LinkedListQueue<Vertex> LLQ = new LinkedListQueue();

        //initialize visitedNode array
        int visitedNodes[] = new int[FN.numVertices()];

        //initialize parent array
        parent = new int[FN.numVertices()];

        //initialize the arrays
        for (int i = 0; i < FN.numVertices(); i++) {
            visitedNodes[i] = 0;
            parent[i] = -1;
        }

        //enqueue source node
        LLQ.enqueue(FN.getVertex(s));

        //loop until queue is empty
        while (!LLQ.isEmpty()) {
            //dequeue first node
            Vertex vi = LLQ.dequeue();

            //loop through a single row in adjacency matrix
            for (int vj = 0; vj < FN.numVertices(); vj++) {
                //check if the node vj is adjacent to node vi
                if (adjacencyMatrix[vi.getLabel()][vj] != null) {
                    //check if this adjacent node is unvisited and more flow can be added
                    if (visitedNodes[vj] == 0 && FN.getEdge(vi, FN.getVertex(vj)).getFlow() < FN.getEdge(vi, vj).getFlowCap()) {
                        //mark the adjacent node as visited
                        visitedNodes[vj] = 1;
                        //set the parent of vj to vi
                        parent[vj] = vi.getLabel();
                        //enqueue adjacent node vj into queue
                        LLQ.enqueue(FN.getVertex(vj));
                    }
                }
            }
        }
        //return 1 if destination node is visited, 0 otherwise
        return visitedNodes[d];

    }//end of breadthFirstPathSearch method

    /**
     * this function computes the maximum flow in the flow network
     *
     * @param FN graph
     * @param s  source vertex
     * @param t  destination vertex
     */
    public void maximizeFlowNetwork(Graph FN, int s, int t) {

        //loop while an augmented path can be found
        while (breadthFirstPathSearch(FN, s, t) == 1) {

            //set pathflow to max int value
            int pathFlow = Integer.MAX_VALUE;

            int index = t;
            Edge e;
            //go through augmented path to find the minimal flow
            while (index != s) {
                e = FN.getEdge(FN.getVertex(parent[index]), FN.getVertex(index));
                pathFlow = Math.min(pathFlow, e.getFlowCap() - e.getFlow());
                index = parent[index];
            }

            index = t;
            //go through the augmented path again to augment each edge with calculated flow
            while (index != s) {
                //adding pathflow to the forward direction
                e = FN.getEdge(FN.getVertex(parent[index]), FN.getVertex(index));
                e.setFlow(e.getFlow() + pathFlow);
                //subtracting pathflow to the reverse direction
                e = FN.getEdge(FN.getVertex(FN.getVertex(index), parent[index]));
                e.setFlow(e.getFlow() - pathFlow);
                index = parent[index];
            }

        }
    }

}//end of class def
