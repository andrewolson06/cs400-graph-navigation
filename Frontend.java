import java.util.List;


public class Frontend implements FrontendInterface{


private BackendInterface backend;


public Frontend(BackendInterface backend) {
        this.backend = backend;
    }



     /**
      * Returns an HTML fragment that can be embedded within the body of a
      * larger HTML page. This HTML output should include:
      *     - a text input field with the id="start", for the start location
      *     - a text input field with the id="end", for the end location
      *     - a button labelled "Find Shortest Path" to request this computation
      * Ensure these text fields are clearly labelled, so the user can understand
      * how to use them.
      * @return an HTML string containing input controls the user can use to
      *         request a shortest path computation
      */
     public String generateShortestPathPromptHTML(){
	return """
		<label for="start">Start Location:</label>
		<input type="text" id="start">

		<label for="end">End Location:</label>
            	<input type="text" id="end">

		<button type="button" id="button">Find Shortest Path</button>

		""";

	}

     /**
      * Returns an HTML fragment that can be embedded within the body of a
      * larger HTML page.  This HTML output should include:
      *     - a paragraph tag for the path's start and end locations
      *     - an ordered list tag for locations along that shortest path
      *     - a paragraph tag that includes the total time along this path
      * Or, if there is no such path, the HTML returned should instead indicate
      * the kind of problem encountered.
      * @param start is the starting location to find a shortest path from
      * @param end is the end location that this shortest path should end at
      * @return an HTML string for the shortest path between these two locations
      */
     public String generateShortestPathResponseHTML(String start, String end){
	List<String> shortestPath = backend.findLocationsOnShortestPath(start, end);

		if (shortestPath == null || shortestPath.size() <= 0){
			return "<p>No path exists between these two locations.</p>";
		}

		StringBuilder locations = new StringBuilder();

		for (String a : shortestPath) {
			locations.append("<li>").append(a).append("</li>\n");
		}

		List<Double> times = backend.findTimesOnShortestPath(start, end);

		return "<p>Shortest path from " + start + " to " + end + ":</p>\n" +
			"<ol>\n" +
			locations +
			"</ol>\n" +
			"<p>Total time: " +  times.get(times.size()-1) + " minutes</p>";

	}

     /**
      * Returns an HTML fragment that can be embedded within the body of a larger
      * HTML page. This HTML output should include:
      *     - a text input field with the id="from", for the start location
      *     - a button labelled "Furthest Location List" to submit this request
      * Ensure this text field is clearly labelled, so the user can understand
      * how to use it.
      * @return an HTML string that contains input controls that the user can use
      *         to request a calculation of the furthest locations list
      */
     public String generateFurthestLocationListFromPromptHTML(){
		return """
                	<label for="from">Start Location:</label>
             		<input type="text" id="from">

                	<button type="button">Furthest Location List</button>
        	        """;
	}

     /**
      * Returns an HTML fragment that can be embedded within the body of a larger
      * HTML page. This HTML output should include:
      *     - a paragraph tag for the path's start and end locations
      *     - an ordered list tag for the locations along that shortest path
      *     - a paragraph tag that includes the total number of locations
      * Or, if no such path can be found, the HTML returned should instead
      * indicate the kind of problem encountered.
      * @param start is the starting location to find the furthest locations from
      * @return an HTML string for the list of furthest locations
      *        along a shortest path starting from the specified location
      */
     public String generateFurthestLocationListFromResponseHTML(String start){
	try{
		List<String> furthestLocations = backend.getFurthestFromList(start);
		
		String furthestLocation = furthestLocations.get(furthestLocations.size()-1);

		List<String> shortestPath = backend.findLocationsOnShortestPath(start, furthestLocation);

		 if (shortestPath == null || shortestPath.size() <= 0){
                         return "<p>No path exists between these two locations.</p>";
                 }

                 StringBuilder locations = new StringBuilder();
                 for (String a : shortestPath) {
                         locations.append("<li>").append(a).append("</li>\n");
                 }

                 return "<p>Shortest path from " + start + " to " + furthestLocation + ":</p>\n" +
                         "<ol>\n" +
                         locations +
                         "</ol>\n" +
                         "<p>Number of Locations: " +  shortestPath.size();



		}catch (Exception e){
			return "<p>" + e.getMessage() + "</p>";
		}

	}
}
