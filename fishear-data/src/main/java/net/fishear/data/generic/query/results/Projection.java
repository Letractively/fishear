package net.fishear.data.generic.query.results;

import java.util.ArrayList;
import java.util.List;

public class 
	Projection
implements
	Cloneable
{

	private List<ProjectionItem> projections;

	public void setProjections(List<ProjectionItem> projections) {
		this.projections = projections;
	}

	public List<ProjectionItem> getProjections() {
		return projections;
	}

	public Projection distinct(String propertyName) {
		if(projections == null) {
			projections = new ArrayList<ProjectionItem>();
		}
		projections.add(ProjectionItem.distinct(propertyName));
		return this;
	}
	
	public int size() {
		return projections == null ? 0 : projections.size();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		List<ProjectionItem> prjs = projections;
		for (ProjectionItem p : prjs) {
			if(sb.length() > 0) { sb.append(", "); }
			sb.append(p.toString());
		}
		return sb.toString();
	}
}
