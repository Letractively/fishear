package net.fishear.web.t5.data;

import java.util.List;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.query.QueryConstraints;
import net.fishear.data.generic.services.ServiceI;
import net.fishear.utils.Defender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/** basic source wrapper - provides data as result of service's {@link ServiceI#query(QueryConstraints)} method.
 * @author terber
 */
public class
	DefaultSourceWrapper
implements
	SourceWrapperI
{
    private static Logger LOG = LoggerFactory.getLogger(DefaultSourceWrapper.class);

	private final ServiceI<? extends EntityI<?>> listService;

	DefaultSourceWrapper(ServiceI<? extends EntityI<?>> listService) {
		this.listService = listService;
	}

	public List<?> getItems(QueryConstraints constraints) {
    	Defender.notNull(constraints, "constraints");
    	if (LOG.isDebugEnabled()) {
        	LOG.debug("DefaultSourceWrapper.getItems(): {}", constraints.where().getConditions());
        }
        List<?> list = listService.list(constraints);
//		List list = listService.query(constraints);
		return list;
	}

	public long getItemsCount(QueryConstraints constraints) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("DefaultSourceWrapper.getItemsCount(): {}", constraints.where().getConditions());
		}

		return listService.queryCount(constraints);
	}

	public ServiceI<?> getListService() {
		return listService;
	}
}
