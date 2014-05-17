package net.fishear.web.t5.data;

import java.util.List;

import net.fishear.data.generic.query.QueryConstraints;
import net.fishear.data.generic.services.ServiceI;


/**
 * interface that provides methods need for data manipulation.
 * Allows data pagination regarding given query constraints.
 * 
 * @author raterwork
 *
 */
public interface SourceWrapperI {

    /**
     * @param constraints
     * @return see {@link ServiceI#list(QueryConstraints)}
     */
    List<?> getItems(QueryConstraints constraints);

    /**
     * @param constraints
     * @return number of items with applying given constraints.
     */
    long getItemsCount(QueryConstraints constraints);
}
