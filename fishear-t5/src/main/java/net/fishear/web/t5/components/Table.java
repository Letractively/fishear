package net.fishear.web.t5.components;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.web.t5.base.Utils;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PropertyOverrides;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.runtime.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Table extends Grid
{
	
	@SuppressWarnings("unused")
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	@Property
	private String editZone;
	
	private static final Logger log = LoggerFactory.getLogger(Table.class);
	
	@Inject
	private ComponentResources crsc;

    /**
     * Defines where block and label overrides are obtained from. By default, the Grid component provides block
     * overrides (from its block parameters).
     */
    @Parameter(value = "this", allowNull = false)
    private PropertyOverrides overrides;

    @Cached
    public PropertyOverrides getOverrides() {
    	return new PropertyOverrides() {
			
			@Override
			public Messages getOverrideMessages() {
				return overrides.getOverrideMessages();
			}
			
			@Override
			public Block getOverrideBlock(String name) {
				Block ovr = overrides.getOverrideBlock(name);
				if(ovr == null) {
					if(name.equals("editCell")) {
						ovr = crsc.getBlock("edit");
					}
					if(name.equals("deleteCell")) {
						ovr = crsc.getBlock("delete");
					}
				}
				return ovr;
			}
		};
    }

    public String deleteConfirmMessage(EntityI<?> entity) {
    	return Utils.getMessage(crsc, "really-delete-record-query", entity.recordDescription().replace('\'', '\"'));

    }
    
    public String rowId(EntityI<?> entity) {
    	return entity.getIdString();
    }

    public boolean isInGrid() {
    	return getParentGrid() != null;
    }
    
	@Cached
    public Component getParentGrid() {
    	Component ag = (Component) findParent(FormContainerI.class);
    	if(ag == null) {
    		log.warn("The Table component should be placed inside the FormContainerI implementation (AbstractGrid...)");
    	}
    	return ag;
	}
	
    @SuppressWarnings("unchecked")
	private <T> T findParent(Class<T> clazz) {
    	ComponentResources par = crsc.getContainerResources();
    	while(par != null) {
    		if(clazz.isAssignableFrom(par.getComponent().getClass())) {
    			return (T) par.getComponent();
    		}
    		par = par.getContainerResources();
    	}
    	return null;
    }

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setRow(Object row) {
    	Component ag;
		if((ag = getParentGrid()) != null && ag instanceof AbstractGrid) {
			((AbstractGrid)ag).setRow((EntityI) row);
    	}
    	super.setRow(row);
    }
}
