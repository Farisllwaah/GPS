/**
 * Vertex
 * Dylan Kario
 * 
 * This class simulates a vertex, a city, in the custom graph DS GPSGraph.
 */

package gps;

public class Vertex implements Comparable<Vertex> {
	
	private int ID; 			// Unique ID number for each Vertex; created sequentially when added to lookup table
	private String city; 		// Name of the city 
	private String state; 		// Name of the state
	private double lon;	 		// Longitude
	private double lat; 		// Latitude
	private int outCount; 		// Number of edges exiting this Vertex
	private int inCount;	  	// Number of edges entering this Vertex
	private int path;			// The previous node, in Dijkstra's algorithm
	private double distance; 	// The distance (in miles) between this node and the start node, in Dijkstra's
	private boolean known; 		// Marks whether Dijkstra's algorithm has come across this Vertex already

	
	
	public Vertex(int i, String c, String s, double lo, double la) {
		ID = i;
		city = c;
		state = s;
		lon = lo;
		lat = la;
		outCount = 0;
		inCount = 0;
		path = 0;
		known = false;
	}
	
	
	public void incInCount() {
		inCount++;
	}
	
	public void incOutCount() {
		outCount++;
	}
	
	/**
	 * clearCounts: set inCount and outCount to 0 (used when clearing adjacency list)
	 */
	public void clearCounts() {
		inCount = 0;
		outCount = 0;
	}

	/* Getters and setters */
	
	public int getID() {
		return ID;
	}
	
	public String getCity() {
		return city;
	}
	
	public String getState() {
		return state;
	}
	
	public double getLon() {
		return lon;
	}
	
	public double getLat() {
		return lat;
	}
	
	public int getInCount() {
		return inCount;
	}
	
	public int getOutCount() {
		return outCount;
	}
	
	public void setDistance(double d) {
		distance = d;
	}
	
	public double getDistance() {
		return distance;
	}
	
	public void setPath(int p) {
		path = p;
	}
	
	public int getPath() {
		return path;
	}
	
	public void setKnown(boolean k) {
		known = k;
	}
	
	public boolean getKnown() {
		return known;
	}
	
	
	
	/**
	 * toString: returns String revealing the Vertex's ID, city, state, long/lat, inCount, and outCount 
	 */
	public String toString() {
		String info = "City: " + city;
		info += "\nState: " + state;
		info += "\nID: " + ID;
		info += "\nLongitude / latitude: " + lon + ", " + lat;
		info += "\nIncoming connections: " + inCount;
		info += "\nOutgoing connections: " + outCount;
		
		return info;
	}
	
	
	/**
	 * compareTo: compares Vertices by their distances from a given source Vertex (in GPSGraph) in Dijkstra's
	 * @param other the other Vertex being compared to
	 * @return double: positive if this Vertex has greater distance than other, negative if less, 0 if equal
	 */
	public int compareTo(Vertex other) {
		return (int)(distance - other.getDistance());
	}
	
	
}
