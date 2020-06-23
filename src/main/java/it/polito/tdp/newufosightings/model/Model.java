package it.polito.tdp.newufosightings.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.newufosightings.db.NewUfoSightingsDAO;

public class Model {
	
	private Graph<State,DefaultWeightedEdge> grafo;
	private NewUfoSightingsDAO dao = new NewUfoSightingsDAO();
	private Map<String,State> mappaStati;
	
	public Model() {
		this.mappaStati = new HashMap<String,State>();
	}
	
	public void creaGrafo(int anno, int giorni) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo,this.dao.loadAllStates(mappaStati) );
		
		for(Arco a : this.dao.getArchi(anno, giorni, mappaStati)) {
			DefaultWeightedEdge edge = this.grafo.getEdge(a.getStato1(), a.getStato2());
			if(edge==null) {
			Graphs.addEdgeWithVertices(this.grafo, a.getStato1(),a.getStato2() , a.getPeso());
			}
		}
		System.out.println(this.grafo);
	}
	
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	
	public List<State> getArchiAdiacenti(){
		int somma = 0;
		List<State> stati = new LinkedList<State>();
		  for(State s: this.dao.loadAllStates(mappaStati)) {
			for(State r : Graphs.neighborListOf(this.grafo, s)) {
				somma+= this.grafo.getEdgeWeight(this.grafo.getEdge(s, r));
				s.setSommaPesi(somma);
				stati.add(s);
			}
		}
		return stati;
		
	}

}
