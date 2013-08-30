package net.fishear.web.t5commons.components;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.web.services.EncoderSelectModel;
import net.fishear.web.services.EncoderSelectModel.Option;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.corelib.data.BlankOption;
import org.apache.tapestry5.ioc.annotations.Inject;

@SupportsInformalParameters
public class Select {

	@Inject
	private ComponentResources crsc;

	@Parameter(principal = true, autoconnect = true, required = true, allowNull = true)
	private EntityI<?> value;

	@Persist
	private String key;

	private String[] columns;

//	@Parameter(principal = true, autoconnect = true, required = true, allowNull = false)
	private SelectModel model;

	private ValueEncoder<EntityI<?>> encoder;

	@Parameter(defaultPrefix=BindingConstants.LITERAL) 
	@Property
	String onchange;
	
    @Parameter(value = "auto", defaultPrefix = BindingConstants.LITERAL)
    @Property
    private BlankOption blankOption;

    /**
     * The label to use for the blank option, if rendered. If not specified, the container's message catalog is
     * searched for a key, <code><em>id</em>-blanklabel</code>.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    @Property
    private String blankLabel;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SetupRender
	void setupRenser() {
		if(model == null) {
			model = crsc.getInformalParameter("model", SelectModel.class);
			encoder = crsc.getInformalParameter("encoder", ValueEncoder.class);
			if(model == null && encoder != null && encoder instanceof EncoderSelectModel) {
				model = ((EncoderSelectModel)encoder).getModel();
			}
			if(model == null) {
				throw new IllegalArgumentException("Parameter 'model' must be specified or parameter 'encoder' must be specified and it must be 'EncoderSelectModel'.");
			}
			String cols = crsc.getInformalParameter("columns", String.class);
			if(cols != null && cols.trim().length() > 0) {
				this.columns = cols.split(","); 
			}
			this.key = value == null ? null : value.getId().toString();
		}
	}

	public EntityI<?> getValue() {
		return value;
	}

	public void setValue(EntityI<?> entity) {
		value = entity;
		key = entity == null ? null : entity.getId().toString();
	}

	/**
	 * @return the columns
	 */
	public String[] getColumns() {
		return columns;
	}

	/**
	 * @param columns the columns to set
	 */
	public void setColumns(String... columns) {
		this.columns = columns;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		setValue(key == null ? null : findEntity(key));
	}
	
	@SuppressWarnings("rawtypes")
	private EntityI<?> findEntity(String key) {
		setupRenser();
		for(OptionModel om : model.getOptions()) {
			if(key.equals(om.getValue())) {
				if(om instanceof Option) {
					return ((Option)om).getEntity();
				} else {
					break;
				}
			}
		}
		if(encoder != null) {
			return encoder.toValue(key);
		}
		return null;
	}

	public SelectModel getModel() {
		return model;
	}
}
