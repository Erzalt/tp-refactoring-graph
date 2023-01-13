package org.acme.graph.routing;

import java.util.*;

import org.acme.graph.errors.NotFoundException;
import org.acme.graph.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * Utilitaire pour le calcul du plus court chemin dans un graphe
 * 
 * @author MBorne
 *
 */
public class DijkstraPathFinder {

	private static final Logger log = LogManager.getLogger(DijkstraPathFinder.class);

	private Graph graph;
	public PathTree pathTree;

	/*public PathNode getNode(Vertex vertex){
		return nodes.get(vertex);
	}*/

	public DijkstraPathFinder(Graph graph) {
		this.graph = graph;
	}
	/**
	 * Calcul du plus court chemin entre une origine et une destination
	 * 
	 * @param origin
	 * @param destination
	 * @return
	 */
	public Path findPath(Vertex origin, Vertex destination) {
		log.info("findPath({},{})...", origin, destination);
		this.pathTree = new PathTree(graph,origin);
		Vertex current;
		while ((current = findNextVertex()) != null) {
			visit(current);
			if (pathTree.getNode(destination).getReachingEdge() != null) {
				log.info("findPath({},{}) : path found", origin, destination);

				return new Path(pathTree.buildPath(destination));
			}
		}
		log.info("findPath({},{}) : path not found", origin, destination);
		throw new NotFoundException(String.format("Path not found from '%s' to '%s'",origin, destination ));
	}

	/**
	 * Parcourt les arcs sortants pour atteindre les sommets avec le meilleur coût
	 * 
	 * @param vertex
	 */
	private void visit(Vertex vertex) {
		log.trace("visit({})", vertex);
		List<Edge> outEdges = graph.getOutEdges(vertex);
		/*
		 * On étudie chacun des arcs sortant pour atteindre de nouveaux sommets ou
		 * mettre à jour des sommets déjà atteint si on trouve un meilleur coût
		 */
		for (Edge outEdge : outEdges) {
			Vertex reachedVertex = outEdge.getTarget();
			/*
			 * Convervation de arc permettant d'atteindre le sommet avec un meilleur coût
			 * sachant que les sommets non atteint ont pour coût "POSITIVE_INFINITY"
			 */
			//PathTree pathTree = new PathTree(graph,reachedVertex);

			double newCost = pathTree.getNode(vertex).getCost() + outEdge.getCost();
			if (newCost < pathTree.getNode(reachedVertex).getCost()) {
				pathTree.getNode(reachedVertex).setCost(newCost);
				pathTree.getNode(reachedVertex).setReachingEdge(outEdge);
			}
		}
		/*
		 * On marque le sommet comme visité
		 */
		pathTree.getNode(vertex).setVisited(true);
	}

	/**
	 * Construit le chemin en remontant les relations incoming edge
	 * 
	 * @param target
	 * @return
	 */
	/*
	private List<Edge> buildPath(Vertex target) {
		List<Edge> result = new ArrayList<>();

		Edge current = getNode(target).getReachingEdge();
		do {
			result.add(current);
			current = getNode(current.getSource()).getReachingEdge();
		} while (current != null);

		Collections.reverse(result);
		return result;
	}*/

	/**
	 * Prépare le graphe pour le calcul du plus court chemin
	 * 
	 * @param source
	 */
	/*private void initGraph(Vertex source) {
		log.trace("initGraph({})", source);
		for (Vertex vertex : graph.getVertices()) {
			PathNode node = new PathNode();
			node.setCost(source == vertex ? 0.0 : Double.POSITIVE_INFINITY);
			node.setReachingEdge(null);
			node.setVisited(false);
			nodes.put(vertex,node);
		}
	}*/


	/**
	 * Recherche le prochain sommet à visiter. Dans l'algorithme de Dijkstra, ce
	 * sommet est le sommet non visité le plus proche de l'origine du calcul de plus
	 * court chemin.
	 * 
	 * @return
	 */
	private Vertex findNextVertex() {
		double minCost = Double.POSITIVE_INFINITY;
		Vertex result = null;

		for (Vertex vertex : graph.getVertices()) {
			PathTree pathTree = new PathTree(graph,vertex);

			// sommet déjà visité?
			if (pathTree.getNodes().get(vertex).isVisited()) {
				continue;
			}
			// sommet non atteint?
			if (pathTree.getNodes().get(vertex).getCost() == Double.POSITIVE_INFINITY) {
				continue;
			}
			// sommet le plus proche de la source?
			if (pathTree.getNodes().get(vertex).getCost() < minCost) {
				result = vertex;
			}
		}
		return result;
	}

}
