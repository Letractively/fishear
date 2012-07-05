package net.fishear.web.t5.mixins;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;

public class ConfirmDelete extends Confirm {

	@Parameter
	private String item;
	
	@Inject
	private ComponentResources crsc;

	protected String confirmMessage() {
		String itStr = item == null || item.trim().length() == 0 ? crsc.getMessages().format("selected-item") : item;
		return crsc.getMessages().format("are-you-sure-to-delete", itStr);
	}
}
