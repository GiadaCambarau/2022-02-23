package it.polito.tdp.yelp.model;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	private YelpDao dao;
	private List<String> cities;
	
	private List<Business> locali;
	private Graph<Review, DefaultWeightedEdge> grafo;
	private List<Review> best;
	private int max;
	
	public Model() {
		super();
		this.dao = new YelpDao();
		this.cities = dao.getCities();
		this.locali = new ArrayList<>();
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
	}
	
	public List<String> getCities(){
		return this.cities;
	}
	public List<Business> getBusinesses(String c){
		List<Business> lista = dao.getBusiness(c);
		Collections.sort(lista);
		return lista;
	}
	 public void creaGrafo(Business b) {
	        List<Review> reviews = dao.getReviews(b);
	        Graphs.addAllVertices(this.grafo, reviews);

	        for (Review r1 : this.grafo.vertexSet()) {
	            for (Review r2: this.grafo.vertexSet()) {
	            	if (!r1.equals(r2)) {
		                // Calcola il peso come differenza in giorni
		                int peso = (int) ChronoUnit.DAYS.between(r1.getDate(), r2.getDate());
		                if (peso > 0) {
		                    // r2 è più recente di r1
		                    Graphs.addEdgeWithVertices(this.grafo, r1, r2, peso);
		                } else if (peso < 0) {
		                    // r1 è più recente di r2
		                    Graphs.addEdgeWithVertices(this.grafo, r2, r1, -peso);
		                }
	            	}
	            }
	        }
	    }

	public int getV() {
		return this.grafo.vertexSet().size();
	}
	public int getA() {
		return this.grafo.edgeSet().size();
	}
	
	public List<ArchiUscenti>  trovaMaxUscenti(){
		int max =0;
		for (Review r : this.grafo.vertexSet()) {
			int n = this.grafo.outDegreeOf(r);
			if (n>max) {
				max = n;
			}
		}
		List<ArchiUscenti> result = new ArrayList<>();
		for (Review r : this.grafo.vertexSet()) {
			int n = this.grafo.outDegreeOf(r);
			if (n == max) {
				result.add(new ArchiUscenti(r, n));
			}
		}
		return result;
	}
	
	public List<Review> trovaSequenza(){
		this.best = new ArrayList<>();
		this.max = 0;
		List<Review> parziale = new ArrayList<>();
		List<Review> rec = new ArrayList<>(this.grafo.vertexSet());
		for (Review r: this.grafo.vertexSet()) {
			ricorsione(parziale, r);
		}
		return best;
	}

	private void ricorsione(List<Review> parziale, Review r) {
		//uscita
		List<Review> successori = Graphs.successorListOf(this.grafo, r);
		if (successori.isEmpty()) {
			if (parziale.size() >= max) {
				this.max = parziale.size();
				this.best= new ArrayList<>(parziale);
			}
			return;
		}
		
		//normale 
		Review corrente = r;
		
		//aggiungo recensioni alla lista 
		for (Review r1: successori) {
			if (r1.getStars()>= r.getStars()) {
				parziale.add(r1);
				ricorsione(parziale,r);
				parziale.remove(parziale.size()-1);
			}
		}
		
	}
	
}
