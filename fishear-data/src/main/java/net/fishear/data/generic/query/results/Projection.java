package net.fishear.data.generic.query.results;

import java.util.ArrayList;
import java.util.List;

import net.fishear.data.generic.query.results.ProjectionItem.Type;

/**
 * projection of SQL result to Java object(s).
 * 
 * Similar to Hibernate's projections.
 * 
 * @author ffyxrr
 *
 */
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

	/**
	 * adds the property projection type to the result.
	 * @param propertyName
	 * @return
	 */
	public Projection property(String propertyName) {
		return add(propertyName, Type.PROPERTY);
	}

	public Projection distinct(String propertyName) {
		return add(propertyName, Type.DISTINCT);
	}

	public Projection count(String propertyName) {
		return add(propertyName, Type.COUNT);
	}

	public Projection countDistinct(String propertyName) {
		return add(propertyName, Type.COUNTDISTINCT);
	}

	public Projection max(String propertyName) {
		return add(propertyName, Type.MAX);
	}

	public Projection min(String propertyName) {
		return add(propertyName, Type.MIN);
	}

	public Projection sum(String propertyName) {
		return add(propertyName, Type.SUM);
	}

	public Projection avg(String propertyName) {
		return add(propertyName, Type.AVG);
	}

	/**
	 * adds the property projection type to the result.
	 * @param propertyName
	 * @return
	 */
	public Projection group(String propertyName) {
		return add(propertyName, Type.GROUP);
	}

	public Projection add(String propertyName, Type type) {
		check(type);
		if(projections == null) {
			projections = new ArrayList<ProjectionItem>();
		}
		projections.add(ProjectionItem.create(propertyName, type));
		return this;
	}
	
	protected void check(Type forType) {
		if(projections != null) {
			boolean isGrp = forType == Type.GROUP;
			boolean isProperty = forType == Type.PROPERTY;
			for(ProjectionItem pi : projections) {
				switch(pi.getType()) {
				case GROUP:
					if(isProperty) {
						throw new IllegalArgumentException(String.format("Cannot add projection %s when the %s has been added already", forType, pi.getType()));
					}
					isGrp = true;
					break;
				case PROPERTY:
					if(isGrp) {
						throw new IllegalArgumentException(String.format("Cannot add projection %s when the %s has been added already", forType, pi.getType()));
					}
					isProperty = true;
					break;
				default:
					throw new IllegalArgumentException(String.format("Unsupported projection %s type: %s ", pi.getType()));
				}
			}
		}
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
