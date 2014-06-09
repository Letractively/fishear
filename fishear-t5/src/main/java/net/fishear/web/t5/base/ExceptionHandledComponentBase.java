package net.fishear.web.t5.base;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.exceptions.BreakException;
import net.fishear.exceptions.ValidationException;
import net.fishear.utils.Classes;
import net.fishear.utils.Exceptions;

import org.apache.tapestry5.runtime.ComponentEventException;

public class ExceptionHandledComponentBase extends ComponentBase {

	public Object onException(Throwable causingEx) {
		Throwable cause;
		if(causingEx instanceof ComponentEventException) {
			cause = Exceptions.getRootCause(((ComponentEventException)causingEx).getCause());
		} else {
			cause = Exceptions.getRootCause(causingEx);
		}
		String message;
		
		// BreakException indicates that processing was stopped but it is not error
		if(!(cause instanceof BreakException)) {
			if(cause instanceof ValidationException) {
				ValidationException vex = (ValidationException) cause;
				String msg = vex.getMessage();
				if(msg != null) {
					if(msg.regionMatches(0,	"localized:", 0, 10)) {
						message = msg.substring(10);
					} else {
						message = translate(msg, vex.getParams() == null ? new Object[0] : vex.getParams());
					}
				} else {
					String causeMsg = "(unknown)";
cont1:
					try {
						StackTraceElement[] stt = cause.getStackTrace();
						for(StackTraceElement st : stt) {
							Class<?> cl = getClass().getClassLoader().loadClass(st.getClassName());
							if(EntityI.class.isAssignableFrom(cl)) {
								causeMsg = Classes.getShortClassName(cl);
								break cont1;
							}
						}
						causeMsg = Classes.getShortClassName(getClass().getClassLoader().loadClass(stt[1].getClassName()));
					} catch(Exception ex) {}
					message = translate("validation-failed-at", causeMsg);
				}
			} else {
				message = translate("application-error-occurred", cause.toString());
				log.error("Applicatioon error occurred", causingEx);
				if(message.startsWith("[[missing key:")) {
					message = message + " :: " + cause.toString();
				}
			}
			alerts.error(message);
		}
		return getReturn();
	}
}
