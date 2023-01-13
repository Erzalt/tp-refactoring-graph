package org.acme.graph.routing;

import org.acme.graph.model.Edge;
import org.acme.graph.model.Graph;
import org.acme.graph.model.PathNode;
import org.acme.graph.model.Vertex;
import org.acme.graph.routing.DijkstraPathFinder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;


public class PathTree {
    private static final Logger log = LogManager.getLogger(DijkstraPathFinder.class);
    private Map<Vertex, PathNode> nodes = new HashMap<Vertex, PathNode> () ;

    /**
     * Prépare le graphe pour le calcul du plus court chemin
     *
     * @param source
     */
    public PathTree(Graph graph, Vertex source) {
        log.trace("initGraph({})", source);
        for (Vertex vertex : graph.getVertices()) {
            PathNode node = new PathNode();
            node.setCost(source == vertex ? 0.0 : Double.POSITIVE_INFINITY);
            node.setReachingEdge(null);
            node.setVisited(false);
            nodes.put(vertex, node);
        }
    }

    public PathNode getNode(Vertex vertex){
        return nodes.get(vertex);
    }
    public Map<Vertex, PathNode> getNodes(){
        return nodes;
    }

    /**
     * Construit le chemin en remontant les relations incoming edge
     *
     * @param target
     * @return
     */
    public List<Edge> buildPath(Vertex target) {
        List<Edge> result = new ArrayList<>();

        Edge current = getNode(target).getReachingEdge();
        do {
            result.add(current);
            current = getNode(current.getSource()).getReachingEdge();
        } while (current != null);

        Collections.reverse(result);
        return result;
    }

}
