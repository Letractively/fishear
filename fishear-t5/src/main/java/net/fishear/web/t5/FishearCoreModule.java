package net.fishear.web.t5;

import java.io.IOException;

import net.fishear.web.services.EnvironmentService;
import net.fishear.web.services.impl.EnvironmentServiceImpl;
import net.fishear.web.t5.base.bindings.MsgBindingFactory;
import net.fishear.web.t5.base.bindings.TBindingFactory;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.ioc.services.CoercionTuple;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.BindingFactory;
import org.apache.tapestry5.services.BindingSource;
import org.apache.tapestry5.services.LibraryMapping;

public class FishearCoreModule
{
	public static void bind(ServiceBinder binder) {
		
		binder.bind(EnvironmentService.class, EnvironmentServiceImpl.class);
	}

    public void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) throws IOException {
        configuration.add(new LibraryMapping("fe", "net.fishear.web.t5"));
        configuration.add(new LibraryMapping("fe", "net.fishear.web.t5commons"));
    }

    @Contribute(TypeCoercer.class)
    public static void provideBasicTypeCoercions(@SuppressWarnings("rawtypes") Configuration<CoercionTuple> configuration)
    {

    	Coercion<Object, Long> coercion = new Coercion<Object, Long>()
        {
            public Long coerce(Object input)
            {
            	String s;
            	if(input == null || (s = input.toString().trim()).length() == 0) {
            		return null;
            	}
            	try {
            		return new Long(s);
            	} catch (NumberFormatException ex) {
            		ex.printStackTrace();
            		return null;
				}
            }
        };
        CoercionTuple<Object, Long> tuple = new CoercionTuple<Object, Long>(Object.class, Long.class, coercion);
        configuration.add(tuple);
    }

	public static void contributeBindingSource(
			MappedConfiguration<String, BindingFactory> configuration,
			BindingSource bindingSource
	) {
		configuration.add("msg", new MsgBindingFactory());
		configuration.add("t", new TBindingFactory());
//		configuration.add("cycle", new CycleBindingFactory(bindingSource));
	}
}
