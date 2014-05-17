package dijkstra;

import java.util.ArrayList;
import java.util.Arrays;

public class Dijkstra {

	public static void printDijkstra(int[] kanten) {
		int[] distance = new int[kanten[0] + 1];
		int[] predecessors = new int[kanten[0] + 1];
		ArrayList<Integer> q = new ArrayList<Integer>();

		Dijkstra.initialize(kanten, 1, distance, predecessors, q);
		
		Dijkstra.printHead(q, 1);

		while (!q.isEmpty()) {
			int node = Dijkstra.getLeastDistance(q, distance), i = 0, n = 0;
			q.remove(new Integer(node));
			Dijkstra.printLineStart(node);
			while((n = Dijkstra.getNeighbors(node, kanten)[i++]) != 0){	// man beachte das Postinkrement ;)
				if (q.contains(new Integer(n))) {
					Dijkstra.updateDistance(node, n, distance, predecessors,
							kanten);
				}
			}
			Dijkstra.printBody(distance, predecessors, 1);
		}

		System.out.println(Arrays.toString(distance) + "|" + Arrays.toString(predecessors));
	}

	private static void printBody(int[] distance, int[] predecessors, int start) {
		for(int i = 1; i < distance.length; ++i){
			if(i != start){
				if(predecessors[i] == 0){
					System.out.print(" --");
				} else {
					System.out.printf("%3d", distance[i]);
				}
			}
		}
		System.out.print("|");
		
		for(int i = 1; i < predecessors.length; ++i){
			if(i != start){
				if(predecessors[i] == 0){
					System.out.print(" --");
				} else {
					System.out.printf("%3d", predecessors[i]);
				}
			}
		}
		System.out.println("|");
	}

	private static void printLineStart(int node) {
		System.out.printf("%3d|", node);
	}

	private static void printHead(ArrayList<Integer> q, int start) {
		System.out.print(" vi|");
		for(int i = 0; i < 2; ++i){
			for(int j: q){
				if(j != start){
					System.out.printf("%3d", j);
				}
			}
			System.out.print("|");
		}
		System.out.println();
		System.out.print("----");
		for(int i = 1; i < q.size() * 2; ++i){
			System.out.print("---");
		}
		System.out.println();
	}

	private static void updateDistance(int node, int n, int[] distance,
			int[] predecessors, int[] kanten) {
		int nDistance = distance[node] + Dijkstra.getWeight(node, n, kanten);
		if (nDistance < distance[n]) {
			distance[n] = nDistance;
			predecessors[n] = node;
		}
	}

	private static int[] getNeighbors(int node, int[] kanten) {
		int[] neighbors = new int[kanten[0] + 1];

		for (int i = 1, j = 0; i < kanten.length; i += 3) {
			if (kanten[i] == node) {
				neighbors[j] = kanten[i + 1];
				++j;
			}
		}

		return neighbors;
	}

	private static int getLeastDistance(ArrayList<Integer> q, int[] distance) {
		int value = Integer.MAX_VALUE;
		int node = -1;

		for (int i : q) {
			if (distance[i] < value) {
				node = i;
				value = distance[i];
			}
		}

		if (node == -1)
			throw new RuntimeException();
		return node;
	}

	private static void initialize(int[] kanten, int start, int[] distance,
			int[] predecessors, ArrayList<Integer> q) {
		for (int i = 1; i < kanten[0] + 1; ++i) {
			distance[i] = Integer.MAX_VALUE;
			predecessors[i] = 0;
			q.add(i);
		}
		if (start > kanten[0])
			throw new IndexOutOfBoundsException();
		distance[start] = 0;
	}

	private static int getWeight(int from, int to, int[] edgeList) {
		if (from < 1 || from > edgeList[0])
			throw new RuntimeException();
		if (to < 1 || to > edgeList[0])
			throw new RuntimeException();

		for (int i = 1; i < edgeList.length; i += 3) {
			if (edgeList[i] == from) {
				if (edgeList[i + 1] == to) {
					return edgeList[i + 2];
				}
			}
		}

		throw new RuntimeException();
	}

	public static void main(String[] args) {
		int[] graph1 = { 4, 1, 2, 2, 1, 4, 5, 2, 4, 1, 2, 3, 4, 3, 1, 1, 4, 3, 1 };
		Dijkstra.printDijkstra(graph1);
		System.out.println();

		int[] garph2 = { 10, 1, 2, 30, 1, 3, 10, 2, 5, 15, 2, 8, 55, 3, 4, 5,
				3, 9, 35, 4, 2, 10, 4, 5, 45, 4, 6, 10, 5, 3, 20, 5, 7, 15, 5,
				9, 25, 6, 7, 5, 7, 10, 20, 8, 10, 15, 9, 8, 10, 9, 10, 30 };
		Dijkstra.printDijkstra(garph2);			
		System.out.println();
	}

}
