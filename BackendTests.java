///////////////////////////////////////////////////////////////////////////////
// Title:            P213.Integration
// Files:            BackendTests.java
// Semester:         CS 400, Spring 2026
//
// Author:           Andrew Olson
// Email:            apolson7@wisc.edu
// Lecturer's Name:  Florian Heimerl
//
// Credits:          No help given or received.
//////////////////////////// 80 columns wide //////////////////////////////////
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.util.List;
import java.util.NoSuchElementException;
import java.io.IOException;

/**
 * This test class is responsible for testing the backend implementation.
 * This class makes use of the Graph_Placeholder class we were given.
 * All 5 of the methods within Backend.java were used.
 */
public class BackendTests {

    /**
     * This test method tests the loadGraphData() method along with the getListOfAll Method.
     * This test uses the europeanRail.dot file to check that the cities in the file are correctly
     * loaded from the loadGraphData method.
     * @throws IOException
     */
    @Test
    public void roleTest1() throws IOException {
	//Create new objects
	Backend backend = new Backend(new Graph_Placeholder());

	//call methods
	backend.loadGraphData("europeanRail.dot");
	List<String> locations = backend.getListOfAll();

	//check if locations were correctly loaded
	Assertions.assertNotNull(locations);
	Assertions.assertTrue(locations.contains("Amsterdam"));
	Assertions.assertTrue(locations.contains("Barcelona"));
	Assertions.assertTrue(locations.contains("Cologne"));
    }

    /**
     * This test method tests the findLocationsOnShortestPath() and findTimesOnShortestPath()
     * methods. It then checks if the correct path is found and checks the order. It also checks
     * if the correct times from locations are found and the order.
     */
    @Test
    public void roleTest2() {
	//Create new objects
	Backend backend = new Backend(new Graph_Placeholder());

	//call methods
	List<String> path = backend.findLocationsOnShortestPath("Union South", "Weeks Hall for Geological Sciences");
	List<Double> time = backend.findTimesOnShortestPath("Union South", "Weeks Hall for Geological Sciences");

	//check path order and path times.
	Assertions.assertEquals(3, path.size());
	Assertions.assertEquals("Union South", path.get(0));
	Assertions.assertEquals("Computer Sciences and Statistics", path.get(1));
	Assertions.assertEquals("Weeks Hall for Geological Sciences", path.get(2));
	Assertions.assertEquals(2, time.size());
	Assertions.assertEquals(1.0, time.get(0));
	Assertions.assertEquals(2.0, time.get(1));
    }

    /**
     * This test method tests the getFurthestFromList() method by calling a invalid location.
     * This should cause a NoSuchElementException and this will b caught. This will not throw
     * any exceptions and the test should pass by default.
     */
    @Test
    public void roleTest3() {
	//Create new objects
	Backend backend = new Backend(new Graph_Placeholder());

	//Calling the getFurthestFromList method with an invalid location. Should cause a NoSuchElementException
	try {
	    backend.getFurthestFromList("Not a location");
	    Assertions.fail("NoSuchElementException should be thrown");
	} catch (NoSuchElementException e) {
	    //Success!!
	}

    }

    /**
     * This test method tests the generateShortestPathPromptHTML method within the frontend class.
     * The test tests whether the web server contains the correct output which asks for start and
     * end locations along with the button to find the shortest path.
     */
    @Test
    public void testShortestPathPromptIntegration() {
	//create objects
	DijkstraGraph<String, Double> graph = new DijkstraGraph<>();
	Backend backend = new Backend(graph);
	Frontend frontend = new Frontend(backend);

	//call method
	String html = frontend.generateShortestPathPromptHTML();

	//test html output
	Assertions.assertTrue(html.contains("start"));
	Assertions.assertTrue(html.contains("end"));
	Assertions.assertTrue(html.contains("Find Shortest Path"));
    }

    /**
     * This test method tests tje generateShortestPathResponseHTML method within the frontend class.
     * A new graph is made and the test makes sure that the shortest path is found and correctly
     * outputted to the user.
     */
    @Test
    public void testShortestPathResponseIntegration() {
	//create objects
	DijkstraGraph<String, Double> graph = new DijkstraGraph<>();
	Backend backend = new Backend(graph);
        Frontend frontend = new Frontend(backend);

	//create a new graph
	graph.insertNode("A");
	graph.insertNode("B");
	graph.insertNode("C");
	graph.insertEdge("A", "B", 5.0);
	graph.insertEdge("B", "C", 3.0);
	graph.insertEdge("A", "C", 15.0);

	//call method
	String html = frontend.generateShortestPathResponseHTML("A", "C");

	//test the html output with the correct nodes
	Assertions.assertTrue(html.contains("A"));
	Assertions.assertTrue(html.contains("C"));
	Assertions.assertTrue(html.contains("Total time"));
	Assertions.assertTrue(html.contains("<ol>"));
    }

    /**
     * This test method tests what is outputted if an invalid path is added inputted by the user.
     * The test makes sure the appropiate message is displayed to correctly notify the user of their
     * mistake.
     */
    @Test
    public void testNoPathExistsIntegration() {
	//create objects
	DijkstraGraph<String, Double> graph = new DijkstraGraph<>();
        Backend backend = new Backend(graph);
        Frontend frontend = new Frontend(backend);

	//insert nodes
	graph.insertNode("A");
	graph.insertNode("B");
	graph.insertNode("C");
	graph.insertEdge("A", "B", 1.0);

	//ask method to find path between two nodes that does not exist
	String html = frontend.generateShortestPathResponseHTML("A", "C");

	//check if the correct message is displayed
	Assertions.assertTrue(html.contains("No path exists between these two locations."));
    }

    /**
     * This test method tests what is outputted if an invalid start location is given by the user.
     * The test makes sure the appropiate error message is outputted to alert the user of their mistake.
     */
    @Test
    public void testInvalidStartIntegration() {
	//create objects
	DijkstraGraph<String, Double> graph = new DijkstraGraph<>();
        Backend backend = new Backend(graph);
        Frontend frontend = new Frontend(backend);

	//call method with invalid start location
	String html = frontend.generateFurthestLocationListFromResponseHTML("INVALID");

	//check if the correct message is outputted
	Assertions.assertTrue(html.contains("Start node not found"));
    }

}
