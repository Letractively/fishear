package net.fishear.data.inmemory.query;



import net.fishear.data.generic.query.AbstractQueryParser;
import net.fishear.data.generic.query.QueryConstraints;
import net.fishear.data.inmemory.InMemoryCriteria;

public class 
	InMemoryQueryParser
extends
	AbstractQueryParser<QueryConstraints, InMemoryCriteria> 
{

    private OrderParser orderParser;
    private WhereParser whereParser;
    private ResultsParser resultsParser;

    public InMemoryQueryParser() {
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
    public void parse( QueryConstraints qc, InMemoryCriteria criteria) {

//    	Projection projection = prepareProjection(qc);
//    	if(projection != null) {
//            criteria.setProjection(projection);
//    		criteria.setResultTransformer( CriteriaSpecification.PROJECTION );
//    	} else {
//    		criteria.setResultTransformer( CriteriaSpecification.DISTINCT_ROOT_ENTITY );
//    	}
        orderParser.parse( qc.getOrderBy(), criteria );
        whereParser.parse( qc.where(), criteria );
        resultsParser.parse( qc.getResults(), criteria );
    }

//	private Projection prepareProjection(QueryConstraints qc) {
//		net.fishear.data.generic.query.results.Projection be2p = qc.getProjection();
//		List<ProjectionItem> list;
//		if(be2p != null && (list = be2p.getProjections()) != null) {
//			ProjectionList pList = Projections.projectionList();
//			for (ProjectionItem p : list) {
//				switch(p.getType()) {
//				case DISTINCT:
//					pList.add(Projections.distinct(Projections.property(p.getPropertyName())));
//				default:
//					throw new IllegalStateException("Unknown peojwction type: " + p.getType());
//				}
//			}
//			return pList;
//		}
//		return null;
//	}
}
