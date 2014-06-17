package net.fishear.web.t5.jquery;

import java.io.IOException;


import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.services.LibraryMapping;

public class FishearT5JQueryModule
{
	public static void bind(ServiceBinder binder) {
//		binder.bind(RemembermeT5Filter.class);
	}
	
	
    public void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) throws IOException {
        configuration.add(new LibraryMapping("fe", "net.fishear.web.t5.jquery"));
    }
}
