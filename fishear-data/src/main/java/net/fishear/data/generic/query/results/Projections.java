package net.fishear.data.generic.query.results;

import java.util.ArrayList;
import java.util.List;

import net.fishear.data.generic.query.results.Projection.Type;

/**
 * projection of SQL result to Java object(s).
 * 
 * Similar to Hibernate's projections.
 * 
 * @author ffyxrr
 *
 */
public class 
	Projections
implements
	Cloneable
{

	private List<Projection> projections;

	public void setProjections(List<Projection> projections) {
		this.projections = projections;
	}

	public List<Projection> getProjections() {
		return projections;
	}

	/**
	 * adds the property projection type to the result.
	 * @param propertyName
	 * @return
	 */
	public Projections property(String propertyName) {
		return add(propertyName, Type.PROPERTY);
	}

	public Projections distinct(String propertyName) {
		return add(propertyName, Type.DISTINCT);
	}

	public Projections count(String propertyName) {
		return add(propertyName, Type.COUNT);
	}

	public Projections countDistinct(String propertyName) {
		return add(propertyName, Type.COUNTDISTINCT);
	}

	public Projections max(String propertyName) {
		return add(propertyName, Type.MAX);
	}

	public Projections min(String propertyName) {
		return add(propertyName, Type.MIN);
	}

	public Projections sum(String propertyName) {
		return add(propertyName, Type.SUM);
	}

	public Projections avg(String propertyName) {
		return add(propertyName, Type.AVG);
	}

	public Projections rowCount() {
		return add(null, Type.ROWCOUNT);
	}

	/**
	 * adds the property projection type to the result.
	 * @param propertyName
	 * @return
	 */
	public Projections group(String propertyName) {
		return add(propertyName, Type.GROUP);
	}
	
	public Projections add(Projection item) {
		check(item.getType());
		if(projections == null) {
			projections = new ArrayList<Projection>();
		}
		projections.add(item);
		return this;
	}

	public Projections add(String propertyName, Type type) {
		return add(Projection.create(propertyName, type));
	}
	
	protected void check(Type forType) {
		if(projections != null) {
			boolean isGrp = forType == Type.GROUP;
			boolean isProperty = forType == Type.PROPERTY;
			for(Projection pi : projections) {
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
					break;
//					throw new IllegalArgumentException(String.format("Unsupported projection type: '%s'. Only those types are allowed: %s ", pi.getType(), Arrays.asList(Projection.Type.GROUP, Projection.Type.PROPERTY)));
				}
			}
		}
	}
	
	public int size() {
		return projections == null ? 0 : projections.size();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		List<Projection> prjs = projections;
		for (Projection p : prjs) {
			if(sb.length() > 0) { sb.append(", "); }
			sb.append(p.toString());
		}
		return sb.toString();
	}
}
