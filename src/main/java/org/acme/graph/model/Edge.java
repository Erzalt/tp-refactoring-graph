package org.acme.graph.model;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

import org.n52.jackson.datatype.jts.GeometrySerializer;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 *
 * Un arc matérialisé par un sommet source et un sommet cible
 *
 * @author MBorne
 *
 */
public class Edge {
	/**
	 * Identifiant de l'arc
	 */
	private String id;

	/**
	 * Sommet initial
	 */
	private Vertex source;

	/**
	 * Sommet final
	 */
	private Vertex target;

	private LineString geometry;


	public void setGeometry(LineString geometry) {
		this.geometry = geometry;
	}


	Edge(Vertex source, Vertex target) {
		this.source = source;
		this.target = target;
		source.getOutEdges().add(this);
		target.getInEdges().add(this);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Source avec rendu JSON sous forme d'identifiant
	 * 
	 * @return
	 */
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
	@JsonIdentityReference(alwaysAsId = true)
	public Vertex getSource() {
		return source;
	}

	/**
	 * Cible avec rendu JSON sous forme d'identifiant
	 * 
	 * @return
	 */
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
	@JsonIdentityReference(alwaysAsId = true)
	public Vertex getTarget() {
		return target;
	}

	/**
	 * dijkstra - coût de parcours de l'arc (distance géométrique)
	 * 
	 * @return
	 */
	public double getCost() {
		if(geometry != null){
			return geometry.getLength();
		}else{
			return source.getCoordinate().distance(target.getCoordinate());
		}
	}

	@JsonSerialize(using = GeometrySerializer.class)
	public LineString getGeometry() {
		if(geometry != null){
			return geometry;
		}
		else{
			GeometryFactory gf = new GeometryFactory();
			return gf.createLineString(new Coordinate[] {
				source.getCoordinate(),
				target.getCoordinate()
			});
		}
	}

	@Override
	public String toString() {
		return id + " (" + source + "->" + target + ")";
	}

}
