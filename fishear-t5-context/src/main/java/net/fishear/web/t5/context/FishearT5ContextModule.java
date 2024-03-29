package net.fishear.web.t5.context;

import java.io.IOException;

import net.fishear.web.t5.context.services.T5ContextService;
import net.fishear.web.t5.context.services.impl.T5ContextServiceImpl;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.services.LibraryMapping;


/**
 * @author ffyxrr
 *
 */
public class FishearT5ContextModule
{

    public static void bind(ServiceBinder binder) {
        binder.bind(T5ContextService.class, T5ContextServiceImpl.class);
    }

    public void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) throws IOException {
        configuration.add(new LibraryMapping("fe", "net.fishear.web.t5.context"));
    }
}
