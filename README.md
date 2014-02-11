GPS
===


A GPS system that implements Dijkstra's algorithm to show relationships between cities.

Dylan Kario :: November 2013

===


### 1. Files to use
  **a. worldcities.txt**  
  	Contains data for 9117 cities in the format:  
  	City, State  
  	Longitude  
  	Latitude  
	
  **b. fict100.txt**  
	Contains data for 100 fictional cities in the same format.

### 2. Sources
  Haversine function: http://andrew.hedges.name/experiments/haversine/  


### 3. Class descriptions
  **a. Vertex.java**  
  This class simulates a vertex, a city, in the custom graph DS GPSGraph.

  **b. Edge.java**  
  This class simulates an edge, a traversable path, between two vertices in the custom graph DS GPSGraph.

  **c. GPSGraph.java**  
  This custom graph DS contains a lookup table for all of its Vertices and an adjacency list that lists the Edges for each vertex. It also contains methods for searching this data and calculating optimized paths.

  **d. UI.java**  
  This class allows provides a user interface for loading new city info, searching cities, and generating optimized paths.


### 4. Some rationale
- Rationale for clearing adjacency list for every new call to makeAdjList():  
  - If a current adjacency list exists, this step clears its data and reconstructs a new one. Once parse() is called and saves city data into lookup_vertex, makeAdjList() is subsequently called and	generates connections through those cities. If another text file is read with parse(), makeAdjList() will get called on THAT data, which means that the original files' cities will have outgoing edges to only	other cities from the original files, whereas the cities from any new files will have outgoing edges to	cities from ALL files. Recreating the adjacency list (which means regenerating all random connections	between all cities every time a new text file is read) eliminates this inconsistency. However, note that if a city file has been parsed, once a new city file is read, the set of connections among all new cities will not preserve the old connections. 

- dijkstra()
  - This method uses one Priority Queue (heap) to temporarily store all Vertices by using a BFS to add them once the distance from the start Vertex to the ones in each layer are calculated. This self-sorts. Once each Vertex of a layer is reached with poll(), they are called in order, and their distances are improved if a shorter one is found. The gpsMode variable is 0 when using nClosest() to calculate distances by edge weights. It is 1 when using nClosest() and haversine() to calculate metric GPS distances using the haversine function.

- addEdge()
  - Since graphData only operates on existing Vertices gotten after calling parse(), addEdge() will never be called with nonexistent Vertices as arguments -- therefore in this program, there is no need to check whether the Vertices of ID1 and ID2 exist before adding an edge between them.

- Why searchCity() runs in O(V) instead of O(1)
  - Option 3, which calls searchCity(), allows the user to search by city. This runs in O(V) time because must search each city in lookup_vertex by ID number and check if it's name == city name.	This is contingent on lookup_vertex being Hashtable<Integer, Vertex> and not Hashtable<String, Vertex> with String meant for city name.	BUT, it's either this running in O(V) time or makeAdjList() running in O(V^2) time and addEdge() running in O(V) time, which is much worse/
		- makeAdjList() iterates from i=0 to i=size and adds a city by using the iterated integer as the key to put the value in (put (i, new ArrayList<Edge>())
		- If it weren't using the integer doubly as the city to operate on--meaining it used the city name as its lookup key--it would have to search each of lookup_vertex's cities to find the one with the correct ID number to operate on. In other words, whether lookup_vertex's key variable is a String (city name) or an Integer (ID), finding one of those variables for a city by searching the other variable (in a way translating between the two) takes O(V) time to search all of lookup_vertex. It is much better to make it so city searching's runtime is increased. This only runs when searching for a city on user input "3", which takes O(V) in the worst case, whereas madeAdjList() is called every time a new file is loaded and would then take O(V^2) in the worst case. It is better to have two methods that run in O(V) time than one method that runs in O(1) time and another that runs in O(V^2) time.


### 5. Miscellaneous Notes
- The weight/distance variables are all doubles, but they are displayed as ints for clarity as whole numbers. They are doubles primarily	so that I could implement Double.POSITIVE_INFINITY in dijkstra().

- I changed the name of Durango, Colorado to Duranggo, Colorado because there is also a city called Durango, Mexico, and I didn't want this to create errors when searching by city.


### 6. Runtimes
  Note: O(|V|+|E|) = O(|V|) because this is a sparse graph, so E is roughly on the order of V.
  a. parse(): O(|V|) 							// Reaches the data for each vertex exactly once  
  b. makeAdjList(): O(|V|+|E|) = O(|V|)		// Iterates through every vertex exactly once, constructing each edge; only operates one time per vertex and edge  
  c. searchState(): O(|V|) 					// Searches each vertex (O(1) per search) and checks its 'state' variable; worst-case searches all vertices  
  d. searchCity(): O(|V|) 					// Similar  
  e. dijkstra(): O(|V|+|E|) = O(|V|) 			// Only uses O(1) operations, but on each vertex and edge  
  f. nClosest(): O(|V|+|E|) = O(|V|) 			// Calls dijkstra in O(|V|), then runs n (up to O(|V|) calls to heap.poll() (O(1)); still at O(|V|)  
  g. shortestPath(): O(|V|+|E|) = O(|V|) 		// Main determiner is dijkstra()  
	h. addEdge(): O(1) 							// Getting from HT and incrementing in/out counts all run in constant time
