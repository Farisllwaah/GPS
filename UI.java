/**
 * drk2130
 * 
 * This class allows provides a user interface for loading new city info, searching cities, and generating optimized paths.
 */

package gps;

import java.util.*;


public class UI {

	public static void main(String[] args) {
	
		GPSGraph graph = new GPSGraph();				// Graph DS
		Scanner in = new Scanner(System.in);
		String input; 											// Tracks user input
		boolean again = true; 									// Loops program
		
		System.out.println("Welcome to GPS!!!!!!");
		
		/* Check if user supplied an incorrect amount of args (2 or greater) */
		if (args.length > 1) {
			System.out.println("Too many arguments!!!! Goodbye.");
			System.exit(1);
		}
		
		/* If user provided 1 arg (the name of a text file), program loads it automatically */
		if (args.length == 1) {
			graph.parse(args[0]);
			graph.makeAdjList();
		}
		
		/* Loops until user quits */
		while (again) { 			
			
			/* Greet user and prompt for choice of action */
			System.out.println("\n== MAIN MENU ==");
			System.out.println("1 - Load city data from file");
			System.out.println("2 - Display all cities in a state");
			System.out.println("3 - Display info about a city");
			System.out.println("4 - Set the current city by providing an ID number");
			System.out.println("5 - Display info about the current city");
			System.out.println("6 - Find n closest cities using GPS distances");
			System.out.println("7 - Find n closest cities using edge weights");
			System.out.println("8 - Print the shortest path from the current city to a destination");
			System.out.println("9 - Quit");
			
			input = in.nextLine(); 	// Get choice number
			
			switch (input) {
			
			/* Load city data from new file and generate adjacency list data from it */
			case "1":
				
				/* If there is already data loaded from a file, program asks user if they would like to clear
				 * this data before proceeding to load the current file's data */ 
				if (graph.howManyFiles() != 0) {
					System.out.println("There is already graphical data stored. Please type \"Y\" " +
							"(without quotation marks) if you would like to clear it before loading new data, " +
							"or type anything else to proceed.");
					if (in.nextLine().equalsIgnoreCase("Y"))
						graph.clearAll();
				}
				
				System.out.println("Please enter a filename, including .txt extension:");
				String filename = in.nextLine();
				
				graph.parse(filename); // Loads file, parses city data into Vertices, and generates Vertex lookup table
				graph.makeAdjList();   // Generate adjacency list
				break;
				
				
			/* Search for all cities associated with a state given by user */
			case "2":
				/* Prompt user for state */
				System.out.println("Please enter the name of the state you would like to search:");
				String state = in.nextLine();
				System.out.println("Displaying all cities in " + state + ":");
				
				/* Search lookup_vertex for all cities in this state, and display it with its ID */
				graph.searchState(state);
				break;
				
				
			/* Search for a specific city by name, and display info about it if found */
			case "3":
				/* Prompt user for city */
				System.out.println("Please enter the name of the city you would like to search:");
				graph.searchCity(in.nextLine());
				break;

				
			/* Designate a given Vertex ID as the current city */ 
			case "4":
				/* Prompt user for a Vertex ID */
				System.out.println("Please enter a city ID to assign as the current city.");
				System.out.println("[Choose a number between 0 and " + (graph.getSize()-1) + " (the number of " +
						"cities currently stored in data)]");
				try {
					int num = Integer.parseInt(in.nextLine());
					if (num >= graph.getSize() || num < 0) {
						System.out.println("Invalid ID entered. Returning to main menu.");
						break;
					}
					graph.setCurID(num); 	// Set current city to this ID number
					System.out.println("Current city set to " + num + ".");
				} catch (NumberFormatException e) {
					System.out.println("Invalid ID entered. Returning to main menu.");
				}
				break;				
				
			
			/* Print info about the current city */
			case "5":
				graph.showInfo(); 	// Uses toString() in Vertex class
				break;
			
				
			/* Return the n closest cities to the currently chosen city, using GPS distances; n chosen by user */	
			case "6":
				/* Prompt user for n */
				System.out.println("Please enter a positive integer for how many of the nearest cities to locate.");
				System.out.println("[Choose a number between 0 and " + (graph.getSize()-1) + " (the number of " +
						"cities currently stored in data)]");
				try {
					int n = Integer.parseInt(in.nextLine());
					if (n >= graph.getSize() || n < 0) {
						System.out.println("Invalid number entered. Returning to main menu.");
						break;
					}
					graph.nClosest(n, true); 	// GPS distance mode 
					break;
				} catch (NumberFormatException e) {
					System.out.println("Invalid number entered. Returning to main menu.");
				}
				break;	
				
				
			/* Return the n closest cities to the currently chosen city, using edge weights; n chosen by user */
			case "7":
				/* Prompt user for n */
				System.out.println("Please enter a positive integer for how many of the nearest cities to locate.");
				System.out.println("[Choose a number between 0 and " + (graph.getSize()-1) + " (the number of " +
						"cities currently stored in data)]");
				try {
					int n = Integer.parseInt(in.nextLine());
					if (n >= graph.getSize() || n < 0) {
						System.out.println("Invalid number entered. Returning to main menu.");
						break;
					}
					graph.nClosest(n, false); 	// Edge weight mode
					break;
				} catch (NumberFormatException e) {
					System.out.println("Invalid number entered. Returning to main menu.");
				}
				break;			
				
				
			/* Return the shortest path from the currently set city to the provided destination city */
			case "8":
				/* Prompts user for a destination city */
				System.out.println("Please enter the ID number for the destination city.");
				System.out.println("[Choose a number between 0 and " + (graph.getSize()-1) + " (the number of " +
						"cities currently stored in data)]");
				try {
					int dest = Integer.parseInt(in.nextLine());
					if (dest >= graph.getSize() || dest < 0) {
						System.out.println("Invalid ID entered. Returning to main menu.");
						break;
					}
					System.out.println("\nStart city:");
					graph.shortestPath(dest); 	// Print shortest path from current city to dest 
				} catch (NumberFormatException e) {
					System.out.println("Invalid ID entered. Returning to main menu.");
				}
				break;
				
			/* Quit */	
			case "9":
				System.out.println("~Goodbye~");
				again = false; 		// Exits while loop, ending the program
				break;
				
			/* Invalid input */	
			default:
				System.out.println("Invalid input. Please try again.");
				break;
			}
			
		}	
	
	in.close();	// Close scanner
	
	}
	
}
