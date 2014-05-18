package dijkstra;

import java.util.ArrayList;

public class Dijkstra {

	public static int START = 1;

	public static void printDijkstra(int[] kanten) {
		int[] distance = new int[kanten[0] + 1];
		int[] predecessors = new int[kanten[0] + 1];
		ArrayList<Integer> q = new ArrayList<Integer>();

		Dijkstra.initialize(kanten, START, distance, predecessors, q);

		Dijkstra.printHead(q, START);

		while (!q.isEmpty()) { // Solange noch Knoten existieren, zu denen noch
								// nicht geprüft wurde, ob es über diese kürzere
								// Wege zu anderen Knoten des Graphen gibt.
			int node = Dijkstra.getLeastDistance(q, distance), i = 0, n = 0; // Speichere Knoten, der in diesem Schritt geprüft werden soll in node. Initialisiere i und n.
			q.remove(new Integer(node)); // lösche den Knoten, der gerade bearbeitet wird aus der Liste der noch nicht bearbeiteten Knoten.
			Dijkstra.printLineStart(node);
			while ((n = Dijkstra.getNeighbors(node, kanten)[i++]) != 0) { // Durchläuft die Liste der Nachbarn von node bis zum ersten "Dummy Nachbarn." Der aktuelle Nachbar wird in n gespeichert.
				if (q.contains(new Integer(n))) { // Prüft, ob zu diesem Nachbarn schon der kürzest mögliche Weg gefunden wurde.
					Dijkstra.updateDistance(node, n, distance, predecessors,
							kanten);
				}
			}
			Dijkstra.printBody(distance, predecessors, START);
		}

		// System.out.println(Arrays.toString(distance) + "|"
		// + Arrays.toString(predecessors));
	}

	/**
	 * Gibt die einzelnen Zeilen der Ergebnistabelle aus.
	 * 
	 * @param distance
	 *            die kürzesten Distanzen zu allen Knoten des Graphs, soweit sie
	 *            nach diesem Schritt bekannt sind.
	 * @param predecessors
	 *            die Vorgängerknoten auf den kürzesten Wegen zu den Knoten des
	 *            Graphen, soweit sie nach diesem Schritt bekannt sind.
	 * @param start
	 *            der Startknoten.
	 */
	private static void printBody(int[] distance, int[] predecessors, int start) {
		for (int i = 1; i < distance.length; ++i) {
			if (i != start) {
				if (predecessors[i] == 0) {
					System.out.print(" --");
				} else {
					System.out.printf("%3d", distance[i]);
				}
			}
		}
		System.out.print("|");

		for (int i = 1; i < predecessors.length; ++i) {
			if (i != start) {
				if (predecessors[i] == 0) {
					System.out.print(" --");
				} else {
					System.out.printf("%3d", predecessors[i]);
				}
			}
		}
		System.out.println("|");
	}

	/**
	 * Gibt den Anfang jeder Zeile der Ergebnistabelle aus.
	 * 
	 * @param node
	 *            der Knoten, der in dieser Zeile der Ergebnistabelle bearbeitet
	 *            wurde.
	 */
	private static void printLineStart(int node) {
		System.out.printf("%3d|", node);
	}

	/**
	 * Gibt die Kopfzeile der Ergebnistabelle auf dem Bildschirm aus.
	 * 
	 * @param q
	 *            die Liste der Knoten, zu denen Wege gefunden werden sollen +
	 *            der Startknoten. Zu diesem Zeitpunkt enthält die Liste noch
	 *            alle Knoten. Später ändert sich dies.
	 * @param start
	 *            der Startknoten.
	 */
	private static void printHead(ArrayList<Integer> q, int start) {
		System.out.print(" vi|");
		for (int i = 0; i < 2; ++i) {
			for (int j : q) {
				if (j != start) {
					System.out.printf("%3d", j);
				}
			}
			System.out.print("|");
		}
		System.out.println();
		System.out.print("----");
		for (int i = 1; i < q.size() * 2; ++i) {
			System.out.print("---");
		}
		System.out.println();
	}

	/**
	 * Prüft, ob der Weg zu Knoten n über Knoten node kürzer ist, als der bisher
	 * gefundene Weg zu n. Falls ja, aktualisiert den Vorgänger und die Weglänge
	 * zu n.
	 * 
	 * @param node
	 *            der Knoten, über den versucht wird n zu erreichen.
	 * @param n
	 *            der Knoten, zu dem geprüft wird, ob ein kürzerer Weg als der
	 *            bisher bekannte existiert.
	 * @param distance
	 *            enthält die bisher gefundenen, kürzesten Distanzen zu den
	 *            Knoten des Graphs.
	 * @param predecessors
	 *            enthält die Vorgänger für den kürzesten Weg zu den Knoten des
	 *            Graphs.
	 * @param kanten
	 *            die Kantenliste des Graphs.
	 */
	private static void updateDistance(int node, int n, int[] distance,
			int[] predecessors, int[] kanten) {
		int nDistance = distance[node] + Dijkstra.getWeight(node, n, kanten);
		if (nDistance < distance[n]) {
			distance[n] = nDistance;
			predecessors[n] = node;
		}
	}

	/**
	 * Erstellt ein Array mit allen Knoten, die vom übergebenen Knoten aus
	 * direkt erreichbar sind und gibt es zurück. Aus Performence Gründen kann die Liste in dieser Implementation länger sein, als die Anzahl tatsächlich gefundener Nachbarn. Deshalb kann die Liste "Dummy Nachbarn" enthalten. Dise zeigen auf 0 und befinden sich am Ende der Liste.
	 * 
	 * @param node
	 *            der Knoten, zu dem Nachbarn gesucht werden sollen.
	 * @param kanten
	 *            die Kantenliste des Graphs.
	 * @return ein Array mit allen Knoten, die vom Knoten node aus direkt
	 *         erreichbar sind.
	 */
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

	/**
	 * Sucht den Knoten in q, mit dem geringsten Abstand zum Startknoten und
	 * gibt ihn zurück.
	 * 
	 * @param q
	 *            liste mit Knoten, aus der der Knoten mit dem geringsten
	 *            Abstand zum Startknoten gesucht wird.
	 * @param distance
	 *            enthält die bisher gefundenen, kürzesten Distanzen zu den
	 *            Knoten des Graphs.
	 * @return der Knoten aus q mit dem geringsten Abstand zum Startknoten.
	 */
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

	/**
	 * Initialisiert die Arrays distance und predecessors, sowie die Liste q.
	 * Dazu werden die informationen aus der Kantenliste des Graphs, sowie der
	 * Startknoten benötigt. An der jeweiligen Position der Arrays stehen die
	 * Informationen zum entsprechendem Knoten. Dazu enthalten beide Arrays an
	 * Position 0 einen Dummy Knoten. Die Distanz zu allen Knoten wir mit
	 * Integer.MAX_VALUE initialisiert. Die Distanz zum Startknoten wird
	 * anschließend auf 0 gesetzt. Die Vorgänger aller Knoten werden mit dem
	 * Dummy Knoten 0 initialisiert.
	 * 
	 * @param kanten
	 *            die Kantenliste des Graphs.
	 * @param start
	 *            der Startknoten, von dem aus die kürzesten Wege zu allen
	 *            anderen Knoten des Graphs berechnet werden.
	 * @param distance
	 *            das Array enthält die kürzesten, bisher gefundenen Distanzen
	 *            zu den Knoten des Graphs. An Position 0 befindet sich ein
	 *            Dummy Knoten.
	 * @param predecessors
	 *            das Array enthält den Vorgängerknoten auf dem bisher kürzest
	 *            gefundenem Weg zum jeweiligem Knoten. An Position 0 befindet
	 *            sich ein Dummy Knoten.
	 * @param q
	 *            die Liste enthält alle Knoten, zu denen noch geprüft werden
	 *            muss, ob über diese kürzere Wege zu anderen Knoten des Graphs
	 *            existieren.
	 */
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

	/**
	 * Ließt das Gewicht einer Kante aus der Kantenliste aus.
	 * 
	 * @param from
	 *            der Knoten, von dem die Kante ausgeht.
	 * @param to
	 *            der Knoten, zu dem die Kante geht.
	 * @param edgeList
	 *            die Kantenliste des Graphs.
	 * @return das Gewicht der Kante, die die Knoten from und to verbindet.
	 */
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
		int[] graph1 = { 4, 1, 2, 2, 1, 4, 5, 2, 4, 1, 2, 3, 4, 3, 1, 1, 4, 3,
				1 };
		Dijkstra.printDijkstra(graph1);
		System.out.println();

		int[] garph2 = { 10, 1, 2, 30, 1, 3, 10, 2, 5, 15, 2, 8, 55, 3, 4, 5,
				3, 9, 35, 4, 2, 10, 4, 5, 45, 4, 6, 10, 5, 3, 20, 5, 7, 15, 5,
				9, 25, 6, 7, 5, 7, 10, 20, 8, 10, 15, 9, 8, 10, 9, 10, 30 };
		Dijkstra.printDijkstra(garph2);
		System.out.println();
	}

}
