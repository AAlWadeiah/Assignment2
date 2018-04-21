// Abdullah Al-Wadeiah, 10171388
// CPSC 331, Assigment 4

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

import javax.swing.JFrame;

public class PathFinder {

	private static Integer WEIGHTED = 0;
	private static Integer UNWEIGHTED = 1;
	private Vertex[] adjList;
	private List<Integer[]> queryList;
	private int N;
	private Integer flag = -1;

	/**
	 * Takes a source file containing maze information and creates a scanner to read
	 * the maze information. Initializes and populates adjacency list.
	 * 
	 * @param mazeFile
	 */
	public PathFinder(String mazeFile, String f) {
		try {
			System.out.println(f);
			if (f.equals("--unweighted")) {
				this.setFlag(UNWEIGHTED);
			}

			else if (f.equals("--weighted")) {
				this.setFlag(WEIGHTED);
			}

			else
				System.out.println("Invalid flag");

			// Begin reading the maze file
			Scanner file = new Scanner(new FileReader(mazeFile));

			// Grab N to determine number of vertices
			N = Integer.parseInt(file.nextLine());

			// Initialized adjacency with length of N^2
			adjList = new Vertex[(int) Math.pow(N, 2)];

			// Create ArrayLists for adjacent edges
			for (int i = 0; i < adjList.length; i++)
				adjList[i] = new Vertex(i);

			// Populate adjacency list
			while (file.hasNextLine()) {
				String[] e = file.nextLine().split("\\s+");

				// Adjacency list format
				// adjList[currentVertex].add(adjacentVertex, weight)
				if (this.flag == UNWEIGHTED) {
					adjList[Integer.parseInt(e[0]) - 1].addAdjacency(new Pair<>(Integer.parseInt(e[1]), 1));
				}

				else {
					adjList[Integer.parseInt(e[0]) - 1]
							.addAdjacency(new Pair<>(Integer.parseInt(e[1]), Integer.parseInt(e[2])));
				}
			}

			file.close();
			
			for(Vertex v: adjList) {
				v.getAdjacencyList().sort(Comparator.comparing(Pair::getX));
			}

		} catch (FileNotFoundException e) {
			System.out.println("Could not find MAZE file.");
			e.printStackTrace();
		}
	}

	/**
	 * Uses the MazeVisualizer class to visualize a given maze and the shortest
	 * paths for the given queries
	 */
	private void visualizeMaze() {
		JFrame f = new JFrame("MazeVisualizer");
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		MazeVisualizer applet = new MazeVisualizer(N);

		visualizeEdges(applet);

		// Find and visualize the shortest path for each query
		for (Integer[] query : queryList) {
			applet.addPath(getShortestPath(query[0], query[1]));
		}
		// Create window and visualize contents
		f.getContentPane().add("Center", applet);
		applet.init();
		f.pack();
		f.setBackground(Color.WHITE);
		f.setSize(new Dimension(512, 512));
		f.setVisible(true);
	}

	/**
	 * Get the shortest path from a source vertex to a target vertex. Returns a list
	 * containing the path which consists of the vertex ids.
	 */
	private List<Integer> getShortestPath(Integer sourceId, Integer targetId) {
		findShortestPath(sourceId);

		List<Integer> pathList = new ArrayList<Integer>();
		Vertex temp = adjList[targetId - 1];
		// Make the path starting from the target vertex to the source vertex
		
		// Reset visited flag for all vertices
		for(Vertex v: adjList)
			v.setVisited(false);

		while (temp != null) {
			if (!temp.getVisited()) {
				pathList.add(temp.getId() + 1);
				temp.setVisited(true);
				temp = temp.getPredecessor();
			}
			else {
				System.out.println(String.format("No path from %s to %s.", sourceId, targetId));
				return new ArrayList<>(sourceId);
			}
		}

		// Reverse the path list so that the order is: source to target
		Collections.reverse(pathList);
		return pathList;
	}

	private void findShortestPath(Integer sourceId) {
		// Priority queue containing unvisited vertices
		PriorityQueue<Vertex> unvisitedQ = new PriorityQueue<Vertex>();

		// Initialize and vertices to priority queue. Set distance of source to 0
		for (Vertex v : adjList) {
			if (v.getId().equals(sourceId - 1))
				v.setShortestDistance(0);
			else
				v.setShortestDistance(Integer.MAX_VALUE);

			v.setPredecessor(null);
			unvisitedQ.add(v);
		}

		while (!unvisitedQ.isEmpty()) {
			Vertex current = unvisitedQ.remove();

			// Visit each edge exiting current vertex
			for (Pair<Integer, Integer> edge : current.getAdjacencyList()) {
				Vertex adjV = adjList[edge.getX() - 1];

				int weight = edge.getY();
				int distFromCurrent = current.getShortestDistance() + weight;

				// If a shorter path is found, then update the path
				if (distFromCurrent < adjV.getShortestDistance()) {
					unvisitedQ.remove(adjV);

					adjV.setShortestDistance(distFromCurrent);
					adjV.setPredecessor(current);
					unvisitedQ.add(adjV);
				}
			}
		}
	}

	/**
	 * Adds edges from adjList to the graph representation.
	 * 
	 * @param applet
	 */
	private void visualizeEdges(MazeVisualizer applet) {
		int currentVertex = 1;
		for (Vertex v : adjList) {
			for (Pair<Integer, Integer> edge : v.getAdjacencyList())
				// Format: addEdge( from, to )
				applet.addEdge(currentVertex, edge.getX());
			currentVertex++;
		}
	}

	/**
	 * Reads the passed query file and parses the vertices, then populate the query
	 * list with the source and target vertices.
	 * 
	 * @param queryFile
	 */
	private void readQuery(String queryFile) {
		try {
			// Begin reading the query file
			Scanner file = new Scanner(new FileReader(queryFile));

			queryList = new ArrayList<Integer[]>();

			// Parse source and target vertices, then store in query list.
			while (file.hasNextLine()) {
				String s = file.nextLine();
				if (s.trim().length() > 0)
					queryList.add(parseIntArray(s.split("\\s+")));
			}
			file.close();

		} catch (FileNotFoundException e) {
			System.out.println("Could not find QUERY file.");
			e.printStackTrace();
		}

	}

	/**
	 * Parses a String Array containing integers into an Integer Array.
	 * 
	 * @param strArray
	 * @return integer array
	 */
	private Integer[] parseIntArray(String[] strArray) {
		Integer[] intArray = new Integer[strArray.length];
		int i = 0;
		for (String str : strArray) {
			intArray[i] = Integer.parseInt(str);
			i++;
		}
		return intArray;
	}

	private void setFlag(int f) {
		this.flag = f;
	}

	public static void main(String[] args) throws FileNotFoundException {
		PathFinder pf = new PathFinder(args[1], args[0]);
		pf.readQuery(args[2]);
		pf.visualizeMaze();

	}

}