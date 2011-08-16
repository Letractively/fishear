package net.fishear.web.t5.data;

import java.util.List;

import net.fishear.data.generic.query.QueryConstraints;


public interface SourceWrapperI {

    List<?> getItems(QueryConstraints constraints);

    long getItemsCount(QueryConstraints constraints);
}
