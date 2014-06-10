package net.fishear.web.t5.base;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.exceptions.BreakException;
import net.fishear.exceptions.ValidationException;
import net.fishear.exceptions.ValidationException.Type;
import net.fishear.exceptions.ValidationExceptionMultiple;
import net.fishear.utils.Classes;
import net.fishear.utils.Exceptions;
import net.fishear.utils.Texts;

import org.apache.tapestry5.runtime.ComponentEventException;

public class ExceptionHandledComponentBase extends ComponentBase {

	/**
	 * handles exception that occurs in component or nested components.
	 * Cascade exceptions are "stripped" to root cause, that is displayed as message.
	 * {@link BreakException} is silently ignored (inly logged at "trace" level).
	 * In case {@link ValidationException}, its message is translated and showed with parameters (id {@link ValidationException#getType()} is set, in corresponding type of message. 
	 * 
	 * @param causingEx the main exception. 
	 * @return calls {@link #getReturn()} to obtain return value
	 */
	public Object onException(Throwable causingEx) {
		
		if(causingEx != null) {
			Throwable cause;
			cause = causingEx instanceof ComponentEventException ? 
					Exceptions.getRootCause(((ComponentEventException)causingEx).getCause()) :
					Exceptions.getRootCause(causingEx)
			;
			if(cause instanceof ValidationExceptionMultiple) {
				ValidationExceptionMultiple viex = (ValidationExceptionMultiple)cause;
				log.debug("ValidationExceptionMultiple is proccessed");
				if(viex.getCause() != null || Texts.tos(viex.getMessage()).length() > 0 || viex.getRootException() != null) {
					onExceptionInternal(viex);
				}
				
				for(ValidationException ex : viex.getExceptions()) {
					if(ex != null) {
						onExceptionInternal(ex);
					}
				}
			} else {
				onExceptionInternal(causingEx);
			}
		}
		return getReturn();
	}

	private void onExceptionInternal(Throwable causingEx) {
		Throwable cause;
		if(causingEx instanceof ComponentEventException) {
			cause = Exceptions.getRootCause(((ComponentEventException)causingEx).getCause());
		} else {
			cause = Exceptions.getRootCause(causingEx);
		}
		String message;
		Type type = ValidationException.Type.ERROR;
		
		// BreakException indicates that processing was stopped but it is not error
		if(cause instanceof BreakException) {
			log.trace("BreakException thrown from component - IGNORED", cause);
		} else {
			log.trace("Exception thrown from  component that is proccesed ty 'ExceptionHandledComponentBase'", cause);
			if(cause instanceof ValidationException) {
				ValidationException vex = (ValidationException) cause;
				if(vex.getType() != null) {
					type = vex.getType();
				}
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
			switch(type) {
			case WARN:
				alerts.warn(message);
				break;
			case INFO:
				alerts.info(message);
				break;
			case OK:
				alerts.success(message);
				break;
			default:
				alerts.error(message);
				break;
			}
		}
	}
}
