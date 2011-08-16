package net.fishear.web.t5.components;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;

public class Raw
{
	
	@Parameter(required = true, allowNull = false)
	private String value;

    @Inject
    private ComponentResources resources;

    @BeginRender
	public void beginRender(MarkupWriter wr) {
        String elementName = resources.getElementName();
        if(elementName == null) {
        	elementName = "div";
        }
        wr.element(elementName);
        wr.writeRaw(value);
	}

    /**
     * never render itself body
     */
    boolean beforeRenderBody()
    {
        return false;
    }

    @AfterRender
    void endRender(MarkupWriter writer)
    {
    	writer.end();
    }
}
