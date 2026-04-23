import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class FrontendTests {




	//tests generateShortestPathPromptHTML() by checking for vital components for html structure
	@Test
	public void roleTest1(){
		GraphADT graph = new Graph_Placeholder();
		BackendInterface backend = new Backend_Placeholder(graph);
		Frontend frontend = new Frontend(backend);

		String html = frontend.generateShortestPathPromptHTML();

		assertTrue(html.contains("id=\"start\"")); //checks for each component, and makes sure syntax is correct
		assertTrue(html.contains("id=\"end\""));
		assertTrue(html.contains("id=\"button\""));

		assertTrue(html.contains("Find Shortest Path")); //checks for proper button label


	}


	//tests generateShortestPathResponseHTML() by making sure start location, end location, and time are outputted
	@Test
	public void roleTest2(){
		GraphADT graph = new Graph_Placeholder();
                BackendInterface backend = new Backend_Placeholder(graph);
                Frontend frontend = new Frontend(backend);

		String shortestPath = frontend.generateShortestPathResponseHTML("Union South", "Computer Sciences and Statistics");

		assertTrue(shortestPath.contains("Computer Sciences and Statistics")); //start and end
		assertTrue(shortestPath.contains("Union South"));
		assertTrue(shortestPath.contains("2.0 minutes")); // expected total time, based on placeholder math


	}



	//tests generateFurthestLocationListFromPromptHTML() and generateFurthestLocationListFromResponseHTML()
	//by making sure that required id's and labels exist in html format
	//and by making sure output contains the expected end location and number of locations
	@Test
	public void roleTest3(){
		GraphADT graph = new Graph_Placeholder();
                BackendInterface backend = new Backend_Placeholder(graph);
                Frontend frontend = new Frontend(backend);

		String html = frontend.generateFurthestLocationListFromPromptHTML();

		assertTrue(html.contains("id=\"from\"")); //checks for each component, and makes sure syntax is correct
		assertTrue(html.contains("Furthest Location List")); //checks for proper button label

		String furthestPath = frontend.generateFurthestLocationListFromResponseHTML("Union South");

		assertTrue(furthestPath.contains("Weeks Hall for Geological Sciences")); // expected end location
		assertTrue(furthestPath.contains("Number of Locations: 3")); // expected number of locations based on placeholder


	}














}
