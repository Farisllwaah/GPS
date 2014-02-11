/** 
 * GPSGraph
 * Dylan Kario
 * 
 * This custom graph DS contains a lookup table for all of its Vertices and an adjacency list that lists 
 * the Edges for each vertex. It also contains methods for searching this data and calculating optimized paths.
 */

package gps;
import java.util.*;
import java.io.*;

public class GPSGraph {

	private Hashtable<Integer, Vertex> lookup_vertex; 		// Lookup list for all Vertices <ID #, Vertex>
	private Hashtable<Integer, ArrayList<Edge>> graphData; 	// Adjacency list <ID, Edge list>
	private ArrayList<String> fileTitles; 					// Names of all files that have been loaded in current session
	private int size; 										// # of cities: first line of citiesFile
	private int curID; 										// ID for city currently set by the user

	
	public GPSGraph() {
		lookup_vertex =  new Hashtable<Integer, Vertex>();
		graphData = new Hashtable<Integer, ArrayList<Edge>>();
		fileTitles = new ArrayList<String>();
		size = 0;
		curID = -1; 			// -1 is indicator that user has not yet chosen a valid value for curID
	}
	
	
	/**
	 * parse: loads a file, parses city data into Vertices, and generates Vertex lookup table
	 * @param filename name of file to load graph data from
	 */
	public void parse(String filename) {
		
		/* Checks if file has already been loaded, and returns to main menu if so */
		if (fileTitles.contains(filename)) {
			System.out.println("This file has already been loaded.");
		}
		
		else {
			File citiesFile = new File(filename);
			
			try {
				BufferedReader br = new BufferedReader(new FileReader(citiesFile));
				int newID; 						// Incrementing custom ID for each new city

				/* If city data already exists, begin assigning new cities ID #s starting from 'size' */
				if (size == 0)
					newID = 0;
				else
					newID = size;
				
				/* If city data already exists, add incoming # of files to current size; otherwise, current size = 0 */ 
				size += Integer.parseInt(br.readLine()); 	// Assumes first line of file is # of citiesFile the file contains
				
				/* Add each city file to a unique vertex in lookup_vertex */
				String curCS = br.readLine(); 		// current city state string (in format "City, State")

				
				/* Parse the data for city and state name, longitude, and latitude of current location */
				while (curCS != null && !curCS.isEmpty()) {
					
					/* If only a city is given without a state in the file, just copy the city name as its state */
					if (curCS.indexOf(", ") == -1) {
						curCS += ", " + curCS;
					}
	
					String[] curCS_split = curCS.split(", "); 			// Delimiter between City, State
					double lo = Double.parseDouble(br.readLine()); 		// Assumes long and lat are on separate lines after name
					double la = Double.parseDouble(br.readLine());
					
					/* Assign parsed data to new Vertex, and add it to lookup_vertex */
					Vertex v = new Vertex(newID, curCS_split[0], curCS_split[1], lo, la); 	
					lookup_vertex.put(newID, v);
					newID++; 	// Increase counter / unique Vertex identifier 
					
					curCS = br.readLine();
					
				}
				
				fileTitles.add(filename); 	// On successful load, add filename to list of seen files
				System.out.println("Data from " + filename + " successfully loaded.");
				
				br.close();
			} catch (FileNotFoundException e) {
				System.out.println("Invalid filename. Returning to main menu.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	
	/**
	 * makeAdjList: Generates an adjacency list from the Vertices obtained from parse(); for each vertex, generate
	 * direct Edges from it to 2-8 randomly chosen other Vertices (cities), each with a random weight between 100-2000  
	 */
	public void makeAdjList() {
		
		/* If a current adjacency list exists, this step clears its data and reconstructs a new one. See readme for rationale. */
		if (!graphData.isEmpty())
			clearAdjList();

		
		/* Generate random connections between cities */
		
		Random r = new Random(System.currentTimeMillis());
		int randEdgeAmt; 		// # of random Edges to generate per Vertex
		int randID; 			// ID number corresponding to a randomly chosen Vertex that will be joined by an Edge
		int randWeight; 		// Random weight for new Edge
		
		for (int i=0; i<size; i++) { 						// i iterates through all Vertices (represents ID)
			graphData.put(i, new ArrayList<Edge>()); 		// Create row in adjacency list for each Vertex
			randEdgeAmt = 2 + r.nextInt(7); 				// Random integer between 2 and 8
			
			for (int j = 0; j < randEdgeAmt; j++) {			// Assigns an Edge randEdgeAmt times
				// Only make the Edge if this jth other Vertex is not Vertex i, and if Edge from i to randID doens't exist yet
				do {
					randID = r.nextInt(size); 					// Pick a random other Vertex (size-1 = last Vertex ID)
				} while (randID == i || hasEdge(graphData.get(i), randID));
				
				randWeight = 100 + r.nextInt(1901); 	// Weight for this edge, randomly between 100-2000
				addEdge(i, randID, randWeight); 	// Add edge from i to randID with randWeight	
			}

		}
	}

	
	/**
	 * hasEdge: checks a row of the adjacency list to see if a given Vertex is adjacent to the Vertex at the head of the row
	 */
	public boolean hasEdge(ArrayList<Edge> edges, int i) {
		for (Edge e : edges) {
			if (e.getEnd() == i)
				return true;
		}
		return false;
	}
	
		
	
	/**
	 * searchState: Print all cities in a given state, along with their ID
	 */
	public void searchState(String state) {
		for (int i=0; i<size; i++) {	// Search each Vertex
			Vertex v = lookup_vertex.get(i); 	
			if (v.getState().equalsIgnoreCase(state))
				System.out.println("#" + v.getID() + ": " + v.getCity() + " [Incount: " + v.getInCount() +
						" || Outcount: " + v.getOutCount() + "]");
		}
	}
	
	
	/**
	 * searchCity: Returns info about a given city 
	 */
	public void searchCity(String city) {
		boolean found = false;
		int i = 0; 		// Search each Vertex
		while (i < size && !found) {
			Vertex v = lookup_vertex.get(i);
			if (v.getCity().equalsIgnoreCase(city)) {
				System.out.println(v.toString());
				found = true;
			}
			i++;
		}
		if (!found)
			System.out.println("Invalid city. Returning to main menu.");
	}
	
	
	
	/**
	 * showInfo: calls toString() in the Vertex class to display information about the current Vertex.
	 */
	public void showInfo() {
		
		/* User hasn't chosen curID yet -- randomly assign one */
		if (curID == -1) {
			Random r = new Random(System.currentTimeMillis());
			curID = r.nextInt(size);
			System.out.println("[randomly determined]");
		}	
		System.out.println(lookup_vertex.get(curID).toString());
	}
	
	
	
	/**
	 * dijkstra: Find the shortest distance from the current city to all other cities (see readme for details)
	 * @param startID the start city from which to find all shortest distances
	 * @param gpsMode 0 when calculating distances from edge weights; 1 when calculating GPS distances with haversine function
	 * @return a Priority Queue of all Vertices, updated with shortest distances and paths from start Vertex
	 */
	public PriorityQueue<Vertex> dijkstra(int startID, boolean gpsMode) {
		
		Vertex start = lookup_vertex.get(startID);
		PriorityQueue<Vertex> tempHeap = new PriorityQueue<Vertex>(size); 	// Inserts all Vertices for easy min finding
		PriorityQueue<Vertex> heap = new PriorityQueue<Vertex>(size); 	// To be returned for use in other methods
		
		/* Set all nodes initially to infinity, so that each new distance is smaller than it */
		for (int i = 0; i < size; i++) {
			lookup_vertex.get(i).setDistance(Double.POSITIVE_INFINITY); 	// Set all distances arbitrarily high
			lookup_vertex.get(i).setKnown(false);
			tempHeap.add(lookup_vertex.get(i));
		}
		
		start.setDistance(0); 			// Overwrite arbitrary dist of infinity for start Vertex (0 away from itself)

		/* Computes shortest distance from start Vertex to every other Vertex */
		while (!tempHeap.isEmpty()) {
			start = tempHeap.poll(); 	// Gets next shortest Vertex from old instance of start, beginning with the original arg
			
			/* Iterates over each edge that the currently analyzed Vertex has coming out of it */
			for (Edge e : graphData.get(start.getID())) {

				Vertex startV = lookup_vertex.get(e.getStart()); 	// Represents the Vertex being evaluated (start of Edge)
				Vertex endV = lookup_vertex.get(e.getEnd());		// Represents Vertex at end of Edge
				
				double weight; 	// Distance 
				if (gpsMode)
					weight = haversine(startV, endV); 				// GPS distance
				else
					weight = e.getWeight(); 						// Weight of this Edge

				double curDist = startV.getDistance() + weight;  	// Distance to endV via startV
				
				/* A shorter distance has been found that spans 2 edges */
				if (curDist < endV.getDistance()) {
					
					/* Remove this Vertex from the heap and re-add it with updated minimum distance */
					tempHeap.remove(endV); 		// Runs in O(log |V|) since not necessarily the minimum
					endV.setDistance(curDist);
					tempHeap.add(endV);
					endV.setPath(start.getID());
				}
			}

			/* If seen already, update with shorter distance */
			if (start.getKnown())
				heap.remove(start);
			else
				start.setKnown(true);
			heap.add(start);
		}
		return heap;
	}


	/**
	 * haversine: helper function for dijkstra(); calculates GPS distance between 2 vertices (see readme for source) 
	 * @param start beginning Vertex
	 * @param end ending Vertex
	 * @return GPS distance in miles
	 */
	private double haversine(Vertex start, Vertex end) {
		double dlon = end.getLon() - start.getLon();
		double dlat = end.getLat() - start.getLat();
		double a = Math.sin(dlat/2) * (Math.sin(dlat/2)) + Math.cos(start.getLat()) *
				Math.cos(end.getLat()) * Math.sin(dlon/2) * Math.sin(dlon/2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		double d = 3959 * c; 		// 3959 = radius of Earth in miles
		
		return d;
	}
	
	
	/**
	 * nClosest: Returns the n closest cities to the current city (curID) by calculating shortest distance via edge weights
	 * @param n the number of closest cities to find
	 * @param gpsMode 0 when calculating distances from edge weights; 1 when calculating GPS distances with haversine function 
	 */
	public void nClosest(int n, boolean gpsMode) {
		
		/* User hasn't chosen curID yet -- randomly assign one */
		if (curID == -1) {
			Random r = new Random(System.currentTimeMillis());
			curID = r.nextInt(size);
		}
		
		System.out.println("Current city: #" + curID + " (" + lookup_vertex.get(curID).getCity() + ", " +
				lookup_vertex.get(curID).getState() + ")");
		
		PriorityQueue<Vertex> heap = dijkstra(curID, gpsMode);
		heap.poll(); 		// Calling poll() the first time returns the Vertex of startID, which we don't want to output
		for (int i=0; i<n; i++) {
			Vertex v = heap.poll(); 	// Gets the n closest cities
			System.out.println("Closest city #" + (i+1) + ": " + v.getCity() + ", " + v.getState()
					+ " (distance: " + (int)v.getDistance() + ")");
		}
	}
	
	/**
	 * shortestPath: calculates the shortest path from the current city to the given one using the 'path' variable
	 * @param endID the destination city
	 */
	public void shortestPath(int endID) {
	
		/* User hasn't chosen curID yet -- randomly assign one */
		if (curID == -1) {
			Random r = new Random(System.currentTimeMillis());
			curID = r.nextInt(size);
		}		
		
		Vertex v = lookup_vertex.get(endID); 	// Get destination vertex
		dijkstra(curID, false);				 	// Edge weight mode

		/* Work backwards from destination, checking each path variable, until start Vertex is reached */
		if (v.getPath() != 0) {
			shortestPath(v.getPath());
			System.out.println("...to...");
		}
		System.out.println(v.getCity() + ", " + v.getState() + " [distance so far: " + (int)v.getDistance() + "]");
	}
	
	
	/**
	 * addEdge adds an Edge between two Vertices, and updates the adjacency list (see readme for more details)
	 * @param ID1 ID key in lookup_vertex of edge's start node
	 * @param ID2 ID key in lookup_vertex of edge's end node
	 * @param weight edge weight
	 */
	public void addEdge(int ID1, int ID2, int weight) {
		
		/* NOTE: since graphData only operates on existing Vertices gotten after calling parse(), addEdge()
		 * will never be called with nonexistent Vertices as arguments -- therefore in this program, there is no need
		 * to check whether the Vertices of ID1 and ID2 exist before adding an edge between them */
		graphData.get(ID1).add(new Edge(ID1, ID2, weight)); 		// Adds new edge to the adjacency list
		
		/* Update counts */
		lookup_vertex.get(ID1).incOutCount();
		lookup_vertex.get(ID2).incInCount();
	}
	
	
	/**
	 * howManyFiles: returns the number of files that have had their data loaded into the graph DS
	 */
	public int howManyFiles() {
		return fileTitles.size();
	}
	
	
	/**
	 * clearAll() Resets all info for new session
	 */
	public void clearAll() {
		clearAdjList(); 			// Clears the adjacency list (resets counts and Edges)
		lookup_vertex.clear(); 		// Clears all city info
		fileTitles.clear(); 		// Clears list of seen files
		size = 0; 					// Reset size
	}
	
	/**
	 * clearAdjList: only clears graphData and resets inCount and outCount variables of each Vertex to 0
	 */
	private void clearAdjList() {
		for (int i=0; i<size; i++) {
			lookup_vertex.get(i).clearCounts();
		}
		graphData.clear();
	}
	
	
	
	/* Getters and setters */
	
	public int getSize() {
		return size;
	}
	
	public void setCurID(int id) {
		curID = id;
	}

	
}
