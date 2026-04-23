///////////////////////////////////////////////////////////////////////////////
// Title:            P209.RoleCode
// Files:            Backend.java
// Semester:         CS 400, Spring 2026
//
// Author:           Andrew Olson
// Email:            apolson7@wisc.edu
// Lecturer's Name:  Florian Heimerl
//
// Credits:          No help given or received.
//////////////////////////// 80 columns wide //////////////////////////////////
import java.io.IOException;
import java.io.File;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This backend class is responsible for taking data from files, extracting locations, finding
 * the shortest path, the time on that shortest path and the furthest location from
 * a location.
 */
public class Backend implements BackendInterface {

    private GraphADT<String, Double> graph;
    private Set<String> nodes;


    /**
     * This is the public constructor method that builds the graph variable.
     */
    public Backend(GraphADT<String, Double> graph) {
	this.graph = graph;
	this.nodes = new HashSet<>();
    }

    /**
     * This method is responsible for loading the data from files. It takes these files and correctly
     * seperates the locations by syntax.
     */
    @Override
    public void loadGraphData(String filename) throws IOException {
	for (String node : new ArrayList<>(nodes)) {
	    graph.removeNode(node);
	}
	nodes.clear();

	try (Scanner scanner = new Scanner(new File(filename))) {

	    while(scanner.hasNextLine()) {
	        String line = scanner.nextLine().trim();
	        if (line.isEmpty() || line.startsWith("digraph") || line.equals("{") || line.equals("}")) {
		    continue;
	        }

	        if (line.contains("->")) {
		    String[] parts = line.split("->");
		    String from = parts[0].trim().replace("\"", "");

		    String right = parts[1].trim();

		    int bracketIndex = right.indexOf("[");
		    int minutesIndex = right.indexOf("minutes=");
		    int endBracketIndex = right.indexOf("]");

		    if (bracketIndex == -1 || minutesIndex == -1 || endBracketIndex == -1) {
			continue;
		    }

		    String to = right.substring(0, bracketIndex).trim().replace("\"", "");
		    String weightStr = right.substring(minutesIndex + 8, endBracketIndex);

		    double weight = Double.parseDouble(weightStr);

		    if (!nodes.contains(from)) {
		        graph.insertNode(from);
		        nodes.add(from);
		    }

		    if (!nodes.contains(to)) {
		        graph.insertNode(to);
		        nodes.add(to);
		    }

		    graph.insertEdge(from, to, weight);

	        }
	    }
	}
    }

    /**
     * This method gets all of the locations from the file and places them in a array.
     * @return ArrayList this is the array with all the locations
     */
    @Override
    public List<String> getListOfAll() {
	return new ArrayList<>(nodes);
    }

    /**
     * This method gets all of the locations on the shortest path between two locations.
     * The locations that are on this path are then put onto an array.
     * @return ArrayList this is the array with all the locations on the shortest path
     */
    @Override
    public List<String> findLocationsOnShortestPath(String start, String end) {
	try {
	    return graph.shortestPathData(start, end);
	} catch (NoSuchElementException e) {
	    return new ArrayList<>();
	}
    }

    /**
     * This method gets all of the times between the locations that are on the shortest
     * path between two locations. The times are then placed within an array of doubles.
     * @return ArrayList this is the array with all the times from location to location
     */
    @Override
    public List<Double> findTimesOnShortestPath(String start, String end) {
	try {
	    List<String> path = graph.shortestPathData(start, end);
	    List<Double> times = new ArrayList<>();

	    for (int i = 0; i <path.size() - 1; i++) {
		double weight = graph.getEdge(path.get(i), path.get(i + 1)).doubleValue();
		times.add(weight);
	    }

	    return times;
	} catch (NoSuchElementException e) {
	    return new ArrayList<>();
	}
    }

    /**
     * This method gets the location that is farthest from a specified location. It is then
     * returned in a List.
     * @return List this is the location that is the farthest from a certain start
     * 			location.
     */
    @Override
    public List<String> getFurthestFromList(String start) throws NoSuchElementException {
	if (!nodes.contains(start)) {
	    throw new NoSuchElementException("Start node not found");
	}

	double maxDistance = -1;
	List<String> result = new ArrayList<>();

	for (String node : nodes) {
	    if (node.equals(start)) {
		continue;
	    }

	    try {
		double distance = graph.shortestPathCost(start, node);

		if (distance > maxDistance) {
		    maxDistance = distance;
		    result.clear();
		    result.add(node);
		} else if (distance == maxDistance) {
		    result.add(node);
		}
	    } catch (NoSuchElementException e) {
		//ignore since they are unreachable nodes
	    }
	}

	if (result.isEmpty()) {
	    throw new NoSuchElementException("No reachable nodes");
	}

	return result;
    }
}
