package net.fishear.data.hibernate.query;

import net.fishear.data.generic.query.AbstractQueryParser;
import net.fishear.data.generic.query.order.OrderBy;
import net.fishear.data.generic.query.order.SortDirection;
import net.fishear.data.generic.query.order.SortedProperty;
import net.fishear.utils.Globals;

import org.hibernate.Criteria;
import org.hibernate.criterion.Property;
import org.slf4j.Logger;

class OrderParser extends AbstractQueryParser<OrderBy, Criteria> {

    private static final Logger LOG = Globals.getLogger();

    public void parse(OrderBy order, Criteria output) {

    	if(order != null) {

	        for (SortedProperty sp : order.getSortedProperties()) {
	
	            String propertyName = sp.getPropertyName();
	            Property property = Property.forName(propertyName);
	
	            SortDirection direction = sp.getSortDirection();
	            switch (direction) {
	                case ASCENDING:
	                    output.addOrder(property.asc());
	                    break;
	                case DESCENDING:
	                    output.addOrder(property.desc());
	                    break;
	                default:
	                    String message = "Cannot recognize sorting direction!";
	                    LOG.error(message);
	                    throw new IllegalStateException(message);
	            }
	        }
    	}
    }
}
