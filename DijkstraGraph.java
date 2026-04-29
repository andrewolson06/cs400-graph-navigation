import java.util.PriorityQueue;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * This class extends the BaseGraph data structure with additional methods for
 * computing the total cost and list of node data along the shortest path
 * connecting a provided starting to ending nodes. This class makes use of
 * Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number>
        extends BaseGraph<NodeType, EdgeType>
        implements GraphADT<NodeType, EdgeType> {

    /**
     * While searching for the shortest path between two nodes, a SearchNode
     * contains data about one specific path between the start node and another
     * node in the graph. The final node in this path is stored in its node
     * field. The total cost of this path is stored in its cost field. And the
     * predecessor SearchNode within this path is referenced by the predecessor
     * field (this field is null within the SearchNode containing the starting
     * node in its node field).
     *
     * SearchNodes are Comparable and are sorted by cost so that the lowest cost
     * SearchNode has the highest priority within a java.util.PriorityQueue.
     */
    protected class SearchNode implements Comparable<SearchNode> {
        public Node node;
        public double cost;
        public SearchNode pred;

        public SearchNode(Node startNode) {
            this.node = startNode;
            this.cost = 0;
            this.pred = null;
        }

        public SearchNode(SearchNode pred, Edge newEdge) {
            this.node = newEdge.succ;
            this.cost = pred.cost + newEdge.data.doubleValue();
            this.pred = pred;
        }

        public int compareTo(SearchNode other) {
            if (cost > other.cost)
                return +1;
            if (cost < other.cost)
                return -1;
            return 0;
        }
    }

    /**
     * Constructor that sets the map that the graph uses.
     */
    public DijkstraGraph() {
        super(new HashTableMap<>());
    }

    /**
     * Insert a new directed edge with a non-negative weight into the graph. If 
     * an edge between pred and succ already exists, update the data stored in 
     * that edge to the new weight.
     * 
     * @param pred is the data contained in the new edge's predecesor node
     * @param succ is the data contained in the new edge's succ node
     * @param weight is the non-negative data to be stored in the new edge
     * @return true if the edge could be inserted or updated, or false if the 
     * pred or succ data are not found in any graph nodes or the weight 
     * specified is negative.
     */
    @Override
    public boolean insertEdge(NodeType pred, NodeType succ, EdgeType weight) {
        if (weight.doubleValue() < 0)
            return false;
        return super.insertEdge(pred, succ, weight);
    }

    /**
     * This helper method creates a network of SearchNodes while computing the
     * shortest path between the provided start and end locations. The
     * SearchNode that is returned by this method represents the end of the
     * shortest path that is found: it's cost is the cost of that shortest path,
     * and the nodes linked together through predecessor references represent
     * all of the nodes along that shortest path (ordered from end to start).
     *
     * @param start the starting node for the path
     * @param end   the destination node for the path
     * @return SearchNode for the final end node within the shortest path
     * @throws NoSuchElementException if either the start or the end node
     * cannot be found, or there is no path from start node to end node
     * @throws NullPointerException if the start or end node are null
     */
    protected SearchNode computeShortestPath(Node start, Node end) {
	//null check
        if (start == null || end == null) {
	    throw new NullPointerException("Start and end nodes can't be null.");
	}

	//create priority queue and required objects
	PriorityQueue<SearchNode> queue = new PriorityQueue<>();
	HashTableMap<Node, Node> visited = new HashTableMap<>();

	//create new node and add it to queue
	SearchNode startNode = new SearchNode(start);
	queue.add(startNode);

	//follow dijkstra's function and process through the graph
	while (!queue.isEmpty()) {
	    SearchNode current = queue.poll();
	    Node currentNode = current.node;

	    //check if end of queue
	    if (currentNode.equals(end)) {
		return current;
	    }

	    //check whether node has been visited
	    if (visited.containsKey(currentNode)) {
		continue;
	    }

	    //mark as visited
	    visited.put(currentNode, currentNode);

	    // process each edge that is from the node
	    for (Edge edge : currentNode.edgesLeaving) {
		Node neighbor = edge.succ;

		//check whether neighbor has been visited
		if (visited.containsKey(neighbor)) {
		    continue;
		}

		//create and add neightbor to the queue
		SearchNode neighborNode = new SearchNode(current, edge);
		queue.add(neighborNode);
	    }

	}

	//loop was exited and no path was found
	throw new NoSuchElementException("No path exists from start to end nodes");
    }

    /**
     * Returns the list of data values from nodes along the shortest path
     * from the node with the provided start value through the node with the
     * provided end value. This list of data values starts with the start
     * value, ends with the end value, and contains intermediary values in the
     * order they are encountered while traversing this shortest path. This
     * method uses Dijkstra's shortest path algorithm to find this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return list of data item from nodes along this shortest path
     * @throws NoSuchElementException if either the start or the end node
     * cannot be found, or there is no path from start node to end node
     * @throws NullPointerException if the start or end node are null
     */
    public List<NodeType> shortestPathData(NodeType start, NodeType end) {
	//null check
        if (start == null || end == null) {
	    throw new NullPointerException("Nodes cannot be null");
	}

	//create start and end nodes
	Node startNode = nodes.get(start);
	Node endNode = nodes.get(end);

	//get shortest path and store in result node
	SearchNode result = computeShortestPath(startNode, endNode);

	//null check
	if (result == null) {
	    throw new NoSuchElementException("No such path exists");
	}

	//create list variable
	ArrayList<NodeType> path = new ArrayList<>();

	//put the path into the list variable created above.
	//all of data from nodes in the path should be in list
	SearchNode current = result;
	while (current != null) {
	    path.add(0, current.node.data);
	    current = current.pred;
	}

	//return the list of all the data on the path
	return path;
    }

    /**
     * Returns the cost of the path (sum over edge weights) of the shortest
     * path from the node containing the start data to the node containing the
     * end data. This method uses Dijkstra's shortest path algorithm to find
     * this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return the cost of the shortest path between these nodes
     * @throws NoSuchElementException if either the start or the end node
     * cannot be found, or there is no path from start node to end node
     * @throws NullPointerException if the start or end node are null
     */
    public double shortestPathCost(NodeType start, NodeType end) {
        //null check
	if (start == null || end == null) {
            throw new NullPointerException("Nodes cannot be null");
        }

	// create start and end nodes
	Node startNode = nodes.get(start);
        Node endNode = nodes.get(end);

	//get shortest path and store in result node
	SearchNode result = computeShortestPath(startNode, endNode);

	//null check
	if (result == null) {
            throw new NoSuchElementException("No such path exists");
        }

	//return the cost of the entire path
	return result.cost;
    }

    /**
     * This first test case tests a lecture example that we did in class.
     * This example creates a directed, weighted graph between 8 nodes.
     * This method tests finding the shortestPath between the futherest nodes apart.
     * The order of the nodes visited along with the cost are tested specifically.
     */
    @Test
    public void testShortestPathLectureExample() {
	//create graph
	DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();

	//insert nodes
	String[] nodes = {"A","B","C","D","E","F","G","H"};
	for (String node : nodes) {
	    graph.insertNode(node);
	}

	//insert edges and their costs
	graph.insertEdge("A","B",4);
	graph.insertEdge("A","C",2);
	graph.insertEdge("C","D",5);
	graph.insertEdge("B","D",1);
	graph.insertEdge("B","E",10);
	graph.insertEdge("D","F",0);
	graph.insertEdge("F","D",2);
	graph.insertEdge("F","H",4);
	graph.insertEdge("G","H",4);
	graph.insertEdge("D","E",3);

	//get shortest path
	List<String> path = graph.shortestPathData("A","H");
	double cost = graph.shortestPathCost("A","H");

	//test how many nodes are in path and order they are visited. 
	//test the cost
	Assertions.assertEquals(5, path.size());
	Assertions.assertEquals("A", path.get(0));
        Assertions.assertEquals("B", path.get(1));
        Assertions.assertEquals("D", path.get(2));
        Assertions.assertEquals("F", path.get(3));
        Assertions.assertEquals("H", path.get(4));
        Assertions.assertEquals(9.0, cost);
    }

    /**
     * This test method follows the same structure as the first test method
     * as the graphs are the exact same. However in this case, a dfferent 
     * start and end node are chosen so that the path between the two is different
     * than the first case and the cost on the path is also different.
     */
    @Test
    public void testDiffStartAndEnd() {
	//create graph
	DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();

	//insert nodes
        String[] nodes = {"A","B","C","D","E","F","G","H"};
        for (String node : nodes) {
            graph.insertNode(node);
        }

	//insert edges and their cost
        graph.insertEdge("A","B",4);
        graph.insertEdge("A","C",2);
        graph.insertEdge("C","D",5);
        graph.insertEdge("B","D",1);
        graph.insertEdge("B","E",10);
        graph.insertEdge("D","F",0);
        graph.insertEdge("F","D",2);
        graph.insertEdge("F","H",4);
        graph.insertEdge("G","H",4);
        graph.insertEdge("D","E",3);

        //get shortest path from C to E
        List<String> path = graph.shortestPathData("C","E");
        double cost = graph.shortestPathCost("C","E");

	//test how many nodes are in path and order they are visited.
        //test the cost
	Assertions.assertEquals(3, path.size());
        Assertions.assertEquals("C", path.get(0));
        Assertions.assertEquals("D", path.get(1));
        Assertions.assertEquals("E", path.get(2));
        Assertions.assertEquals(8.0, cost);
     }

    /**
     * This third and final test method tests the final two requirements of the 
     * testing assignment which is to test when the start and end node do not exist,
     * and if a path between two nodes do not exist. For each of these requirements,
     * I test all of the cases. For example when both of the nodes do not exist vs.
     * when only one of the nodes exist. I test the functionality of exceptions being
     * thrown when this is the case.
     */
     @Test
     public void testIncorrectPaths() {
	//create graph
	DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();

	//insert nodes
        String[] nodes = {"A","B","C","D","E","F","G","H"};
        for (String node : nodes) {
            graph.insertNode(node);
        }

	//insert edges and their cost
	graph.insertEdge("A","B",4);
        graph.insertEdge("A","C",2);
        graph.insertEdge("C","D",5);
        graph.insertEdge("B","D",1);
        graph.insertEdge("B","E",10);
        graph.insertEdge("D","F",0);
        graph.insertEdge("F","D",2);
        graph.insertEdge("F","H",4);
        graph.insertEdge("G","H",4);
        graph.insertEdge("D","E",3);

	//test if exceptions are thrown for when path does not exist
	//Case 1: G has no incoming edges
	try {
	    graph.shortestPathData("F","G");
	    Assertions.fail("Path does not exist");
	} catch (NoSuchElementException e) {
	    //success
	}
	//Case 2: E has no outgoing edges
        try {
            graph.shortestPathData("E","F");
            Assertions.fail("Path does not exist");
        } catch (NoSuchElementException e) {
            //success
        }

	//test if exceptions are thrown for when a start and end node do not exist
	//Case 1: start and end do not exist
	try {
            graph.shortestPathData("X","Y");
            Assertions.fail("Path does not exist");
        } catch (NoSuchElementException e) {
            //success
        }
	//Case 2: start node does not exist
        try {
            graph.shortestPathData("T","H");
            Assertions.fail("Path does not exist");
        } catch (NoSuchElementException e) {
            //success
        }
	//Case 3: end node does not exist
        try {
            graph.shortestPathData("A","Z");
            Assertions.fail("Path does not exist");
        } catch (NoSuchElementException e) {
            //success
        }

     }


}
