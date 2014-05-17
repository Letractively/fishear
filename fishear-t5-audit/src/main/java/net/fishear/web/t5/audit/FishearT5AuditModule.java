package net.fishear.web.t5.audit;

import java.io.IOException;

import net.fishear.data.audit.services.AuditService;
import net.fishear.data.generic.dao.DaoSourceManager;
import net.fishear.data.generic.services.AuditServiceI;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.services.LibraryMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FishearT5AuditModule
{

	private static Logger log = LoggerFactory.getLogger(FishearT5AuditModule.class);
	
	public static void bind(ServiceBinder binder) {
//		binder.bind(RemembermeT5Filter.class);
	}
	
	
	public static AuditService buildAuditService() {
		AuditServiceI am = DaoSourceManager.getDefaultDaoSource().getAdons().getAuditService();
		if(am != null) {
			if(am instanceof AuditService) {
				return (AuditService) am;
			} else {
				log.warn("AuditServiceI is not implementation of AuditService for default DAO source {}", DaoSourceManager.getDefaultDaoSource());
			}
		} else {
			log.warn("No audit service is set at current DAO source {}", DaoSourceManager.getDefaultDaoSource());
		}
		return null;
	}

    public void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) throws IOException {
        configuration.add(new LibraryMapping("fe", "net.fishear.web.t5.audit"));
    }

//	public void contributeRequestHandler( OrderedConfiguration<RequestFilter> configuration, @Local RemembermeT5Filter filter ) {
//		configuration.add("RemembermeT5Request", filter, "after:HiberT5RequestFilter");
//	}

}
