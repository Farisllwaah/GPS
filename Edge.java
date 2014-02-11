/**
 * Edge
 * Dylan Kario
 * 
 * This class simulates an edge, a traversable path, between two vertices in the custom graph DS GPSGraph.
 */

package gps;

public class Edge {
	
	private int startID; 		// ID of start Vertex
	private int endID;			// ID of end Vertex
	private double weight; 		// Path weight

	
	public Edge(int start, int end, int w) {
		startID = start;
		endID = end;
		weight = w;
	}

	
	/* Getters and setters */
	
	public int getStart() {
		return startID;
	}
	
	public void setStart(int s) {
		startID = s;
	}

	public int getEnd() {
		return endID;
	}
	
	public void setEnd(int e) {
		endID = e;
	}
	
	public double getWeight() {
		return weight;
	}
	
	public void setWeight(double w) {
		weight = w;
	}
}



