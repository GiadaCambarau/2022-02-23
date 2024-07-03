package it.polito.tdp.yelp.model;

import java.util.Objects;

public class ArchiUscenti {
	private Review r;
	private int uscenti;
	public ArchiUscenti(Review r, int uscenti) {
		super();
		this.r = r;
		this.uscenti = uscenti;
	}
	public Review getR() {
		return r;
	}
	public void setR(Review r) {
		this.r = r;
	}
	public int getUscenti() {
		return uscenti;
	}
	public void setUscenti(int uscenti) {
		this.uscenti = uscenti;
	}
	@Override
	public int hashCode() {
		return Objects.hash(r, uscenti);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArchiUscenti other = (ArchiUscenti) obj;
		return Objects.equals(r, other.r) && uscenti == other.uscenti;
	}
	@Override
	public String toString() {
		return r.getReviewId() + " -> " + uscenti ;
	}
	

}
