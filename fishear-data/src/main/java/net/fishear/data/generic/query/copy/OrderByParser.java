package net.fishear.data.generic.query.copy;

import net.fishear.data.generic.query.AbstractQueryParser;
import net.fishear.data.generic.query.QueryConstraints;
import net.fishear.data.generic.query.order.OrderBy;
import net.fishear.data.generic.query.order.SortedProperty;

class OrderByParser extends AbstractQueryParser<OrderBy, QueryConstraints> {

    public void parse(OrderBy sourceObject, QueryConstraints output) {
        for (SortedProperty sp : sourceObject.getSortedProperties()) {
            output.orderBy().add(sp.getPropertyName(), sp.getSortDirection());
        }
    }
}
