package net.fishear.web.t5.mixins;

import net.fishear.utils.Texts;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;

@SupportsInformalParameters
public class ConfirmDelete extends Confirm {

	@Parameter
	private String item;

	protected String confirmMessage() {
		String msg = crsc.getInformalParameter("confirmMessage", String.class);
		if(Texts.isEmpty(msg)) {
			String itStr = item == null || item.trim().length() == 0 ? crsc.getMessages().format("selected-item") : item;
			return crsc.getMessages().format("are-you-sure-to-delete", itStr);
		} else {
			return msg;
		}
	}
}
