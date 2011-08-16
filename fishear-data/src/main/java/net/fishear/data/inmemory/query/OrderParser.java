package net.fishear.data.inmemory.query;

import java.util.List;

import net.fishear.data.generic.query.AbstractQueryParser;
import net.fishear.data.generic.query.order.OrderBy;
import net.fishear.data.generic.query.order.SortedProperty;
import net.fishear.data.inmemory.InMemoryCriteria;
import net.fishear.utils.Globals;

import org.slf4j.Logger;

class OrderParser extends AbstractQueryParser<OrderBy, InMemoryCriteria> {

    private static final Logger LOG = Globals.getLogger();

    public void parse(OrderBy order, InMemoryCriteria output) {

		if(order != null) {
			
	        List<SortedProperty> sortedProperties = order.getSortedProperties();
	
	        for (SortedProperty sp : sortedProperties) {

	            switch (sp.getSortDirection()) {
	                case ASCENDING:
	                case DESCENDING:
	                    break;
	                default:
	                    String message = "Cannot recognize sorting direction: " + sp.getSortDirection();
	                    LOG.error(message);
	                    throw new IllegalStateException(message);
	            }
                output.addOrder(sp.getPropertyName(), sp.getSortDirection());
	        }
			throw new IllegalArgumentException("Ordering is not supported yet");
		}
    }
}
