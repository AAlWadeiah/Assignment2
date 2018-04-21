// Abdullah Al-Wadeiah, 10171388
// CPSC 331, Assigment 4

import java.util.ArrayList;
import java.util.List;

public class Vertex implements Comparable<Vertex> {
	// adjacencyList = Pair<adjVertexID, weight>
	private List<Pair<Integer, Integer>> adjacencyList = new ArrayList<Pair<Integer, Integer>>();
	private Integer shortestDistance;
	private Vertex predecessor;
	private Integer id;
	private Boolean visited = false;

	public Vertex(int i) {
		this.id = i;
	}

	public Vertex(int i, List<Pair<Integer, Integer>> list) {
		this.id = i;
		this.setAdjacencyList(list);
	}

	/**
	 * @return the adjacencyList
	 */
	public List<Pair<Integer, Integer>> getAdjacencyList() {
		return adjacencyList;
	}

	/**
	 * @param adjacencyList
	 *            the adjacencyList to set
	 */
	public void setAdjacencyList(List<Pair<Integer, Integer>> adjacencyList) {
		this.adjacencyList = adjacencyList;
	}

	/**
	 * @return the shortestDistance
	 */
	public Integer getShortestDistance() {
		return shortestDistance;
	}

	/**
	 * @param shortestDistance
	 *            the shortestDistance to set
	 */
	public void setShortestDistance(Integer shortestDistance) {
		this.shortestDistance = shortestDistance;
	}

	/**
	 * @return the predecessor
	 */
	public Vertex getPredecessor() {
		return predecessor;
	}

	/**
	 * @param predecessor
	 *            the predecessor to set
	 */
	public void setPredecessor(Vertex predecessor) {
		this.predecessor = predecessor;
	}

	public void addAdjacency(Pair<Integer, Integer> adj) {
		this.adjacencyList.add(adj);

	}

	public void setVisited(Boolean f){
		this.visited = f;
	}

	public Boolean getVisited(){
		return visited;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	@Override
	public int compareTo(Vertex o) {
		return Integer.compare(this.shortestDistance, o.shortestDistance);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		final Vertex otherV = (Vertex) obj;
		if (!this.getId().equals(otherV.getId()))
			return false;

		return true;
	}

}
