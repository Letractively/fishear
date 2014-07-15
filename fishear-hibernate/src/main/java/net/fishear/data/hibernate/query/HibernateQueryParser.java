package net.fishear.data.hibernate.query;

import net.fishear.data.generic.query.AbstractQueryParser;
import net.fishear.data.generic.query.QueryConstraints;
import net.fishear.data.generic.query.results.Projection;
import net.fishear.data.generic.query.results.ProjectionType;
import net.fishear.data.generic.query.results.SqlProjection;
import net.fishear.utils.Texts;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.type.Type;

public class 
	HibernateQueryParser
extends
	AbstractQueryParser<QueryConstraints, Criteria> 
{

    private OrderParser orderParser;
    private WhereParser whereParser;
    private ResultsParser resultsParser;

    public HibernateQueryParser() {
        this.orderParser = new OrderParser();
        this.whereParser = new WhereParser();
        this.resultsParser = new ResultsParser();
    }

    /**
     * Parse query constraints with all 'qc', filters, restrictions etc.
     *
     * @param criteria
     * @param qc
     */
    public void parse(QueryConstraints qc, Criteria criteria) {

    	org.hibernate.criterion.Projection projection = prepareProjection(qc);
    	ResultTransformer transformer;

    	if(projection != null) {
    		transformer = CriteriaSpecification.PROJECTION;
            criteria.setProjection(projection);
    	} else {
        	if(qc.results().getResultType() == null) {
        		transformer = CriteriaSpecification.DISTINCT_ROOT_ENTITY;
        	} else {
        		switch(qc.results().getResultType()) {
        		case ENTITY:
            		transformer = CriteriaSpecification.DISTINCT_ROOT_ENTITY;
            		break;
        		case PROJECTION:
            		transformer = CriteriaSpecification.PROJECTION;
            		break;
        		case MAP:
            		transformer = CriteriaSpecification.ALIAS_TO_ENTITY_MAP;
            		break;
            	default:
            		throw new IllegalStateException(String.format("Unknown result transformer type: %s", qc.results().getResultType()));
        		}
        	}
    	}

		criteria.setResultTransformer( transformer );

        whereParser.parse( qc.where(), criteria );
        resultsParser.parse( qc.getResults(), criteria );
        orderParser.parse( qc.getOrderBy(), criteria );
    }

	private org.hibernate.criterion.Projection prepareProjection(QueryConstraints qc) {
		net.fishear.data.generic.query.results.Projections be2p = qc.getProjections();
		if(be2p != null && be2p.getProjections() != null && be2p.getProjections().size() > 0) {
			ProjectionList pList = org.hibernate.criterion.Projections.projectionList();
			for (Projection p : be2p.getProjections()) {
				org.hibernate.criterion.Projection px;
				switch(p.getType()) {
				case DISTINCT:
					px = org.hibernate.criterion.Projections.distinct(org.hibernate.criterion.Projections.property(p.getPropertyName()));
					break;
				case PROPERTY:
					px = org.hibernate.criterion.Projections.property(p.getPropertyName());
					break;
				case SQL:
					SqlProjection sqi = (SqlProjection) p;
					//px = org.hibernate.criterion.Projections.sqlProjection(sql, columnAliases, types);
					px = org.hibernate.criterion.Projections.sqlProjection(sqi.getSql(), sqi.getAliases(), transformTypes(sqi.getTypes()));
					break;
				case GROUP:
					px = org.hibernate.criterion.Projections.groupProperty(p.getPropertyName());
					break;
				case COUNT:
					px = org.hibernate.criterion.Projections.count(p.getPropertyName());
					break;
				case MAX:
					px = org.hibernate.criterion.Projections.max(p.getPropertyName());
					break;
				case MIN:
					px = org.hibernate.criterion.Projections.min(p.getPropertyName());
					break;
				case SUM:
					px = org.hibernate.criterion.Projections.sum(p.getPropertyName());
					break;
				case COUNTDISTINCT:
					px = org.hibernate.criterion.Projections.countDistinct(p.getPropertyName());
					break;
				case AVG:
					px = org.hibernate.criterion.Projections.avg(p.getPropertyName());
					break;
				case ROWCOUNT:
					px = org.hibernate.criterion.Projections.rowCount();
					break;
				default:
					throw new IllegalStateException("Unknown projection type: " + p.getType());
				}
				String alias = Texts.tos(p.getAlias());
				if(alias.length() > 0) {
					pList.add(org.hibernate.criterion.Projections.alias(px, alias), alias);
				} else {
					pList.add(px);
				}
			}
			return pList;
		}
		return null;
	}

	private Type[] transformTypes(ProjectionType[] types) {
		// TODO Auto-generated method stub
		return null;
	}
}
