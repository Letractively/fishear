package net.fishear.web.t5.components;

import net.fishear.utils.Texts;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Field;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationDecorator;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.services.Heartbeat;


/**
 * Generates a &lt;label&gt; element for a particular field.
 * <p/>
 * A Label will render its body, if it has one.  However, in most cases it will not have a body, and will render its
 * {@linkplain org.apache.tapestry5.Field#getLabel() field's label} as it's body. Remember, however, that it is the
 * field label that will be used in any error messages. The Label component allows for client- and server-side
 * validation error decorations.
 */
@SupportsInformalParameters
public class GLabel
{
    /**
     * The for parameter is used to identify the {@link Field} linked to this label (it is named this way because it
     * results in the for attribute of the label element).
     */
    @Parameter(name = "for", required = true, allowNull = false, defaultPrefix = BindingConstants.COMPONENT)
    private Field field;

    @Parameter(name = "class", required = false, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    private String className;

    @Environmental
    private Heartbeat heartbeat;

    @Environmental
    private ValidationDecorator decorator;

    @Inject
    private ComponentResources resources;

    private Element labelElement;
    private Element divElement;

    private static String labelClassName = "formLabel";

    /**
     * If true, then the body of the label element (in the template) is ignored. This is used when a designer places a
     * value inside the &lt;label&gt; element for WYSIWYG purposes, but it should be replaced with a different
     * (probably, localized) value at runtime. The default is false, so a body will be used if present and the field's
     * label will only be used if the body is empty or blank.
     */
    @Parameter
    private boolean ignoreBody;

    boolean beginRender(MarkupWriter writer)
    {
        final Field field = this.field;

        decorator.beforeLabel(field);

        divElement = writer.element("div");
        labelElement = writer.element("label");

        resources.renderInformalParameters(writer);

        // Since we don't know if the field has rendered yet, we need to defer writing the for and id
        // attributes until we know the field has rendered (and set its clientId property). That's
        // exactly what Heartbeat is for.

        Runnable command = new Runnable()
        {
			public void run()
            {
                String fieldId = field.getClientId();
                divElement.forceAttributes("id", fieldId.concat("LDiv"), "class", (labelClassName + " " + Texts.tos(className, "label")).trim());
                labelElement.forceAttributes("for", fieldId, "id", fieldId.concat("Label"), "class", labelClassName);
                decorator.insideLabel(field, labelElement);
            }
        };

        heartbeat.defer(command);

        return !ignoreBody;
    }

    void afterRender(MarkupWriter writer)
    {
        // If the Label element has a body that renders some non-blank output, that takes precendence
        // over the label string provided by the field.

        boolean bodyIsBlank = InternalUtils.isBlank(labelElement.getChildMarkup());

        if (bodyIsBlank) writer.write(field.getLabel());

        writer.end(); // label
        writer.end(); // div

        decorator.afterLabel(field);
    }
}
