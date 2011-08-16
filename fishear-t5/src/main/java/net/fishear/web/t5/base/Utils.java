package net.fishear.web.t5.base;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.runtime.Component;

public class Utils
{

	public static Messages getMessagesForKey(Component component, String key) {
		
		return getMessagesForKey(((Component)component).getComponentResources(), key);
	}

	public static Messages getMessagesForKey(ComponentResources crsc, String key) {
		
		Messages msgs;
		while(crsc != null) {
			if((msgs = crsc.getMessages()).contains(key)) {
				return msgs;
			}
			crsc = crsc.getContainerResources();
		}
		return null;
	}

	public static String getMessage(Component component, String key, Object... args) {
		Messages msgs = getMessagesForKey(component, key);
		if(msgs == null) {
			return "[[missing key: ]]".concat(key);
		}
		return msgs.format(key, args);
	}

	public static String getMessage(ComponentResources crsc, String key, Object... args) {
		Messages msgs = getMessagesForKey(crsc, key);
		if(msgs == null) {
			return "[[missing key: ]]".concat(key);
		}
		return msgs.format(key, args);
	}
}
