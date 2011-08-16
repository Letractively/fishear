package net.fishear.web.rights.t5;

import java.io.IOException;

import net.fishear.web.rights.t5.services.RemembermeT5Filter;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.RequestFilter;

public class FishearRightsModule
{

	public static void bind(ServiceBinder binder) {
		binder.bind(RemembermeT5Filter.class);
	}
	
	
    public void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) throws IOException {
        configuration.add(new LibraryMapping("feac", "net.fishear.web.rights.t5"));
    }

/**
 * This is a contribution to the RequestHandler service configuration. This
 * is how we extend Tapestry using the timing filter. A common use for this
 * kind of filter is transaction management or security. The @Local
 * annotation selects the desired service by type, but only from the same
 * module. Without @Local, there would be an error due to the other
 * service(s) that implement RequestFilter (defined in other modules).
 */
	public void contributeRequestHandler( OrderedConfiguration<RequestFilter> configuration, @Local RemembermeT5Filter filter ) {
		configuration.add("RemembermeT5Request", filter, "after:HiberT5RequestFilter");
	}

}
