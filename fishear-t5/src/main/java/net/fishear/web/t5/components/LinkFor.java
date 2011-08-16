package net.fishear.web.t5.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractComponentEventLink;
import org.apache.tapestry5.runtime.Component;

/**
 * Component that triggers an action on the server with a subsequent full page refresh.
 */
@Events(EventConstants.ACTION)
public class LinkFor extends AbstractComponentEventLink
{

	@Parameter(name = "component", allowNull = false, required = true)
	private Component forComponent;
	
	@Parameter(defaultPrefix = BindingConstants.LITERAL, value = EventConstants.ACTION)
	private String type;

    @Override
    protected Link createLink(Object[] contextArray)
    {
        return forComponent.getComponentResources().createEventLink(type, contextArray);
    }

}
