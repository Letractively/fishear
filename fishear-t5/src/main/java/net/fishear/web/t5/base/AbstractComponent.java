package net.fishear.web.t5.base;

import java.text.NumberFormat;
import java.util.Date;
import java.util.Formatter;

import net.fishear.exceptions.ValidationException;
import net.fishear.utils.Exceptions;
import net.fishear.utils.Globals;
import net.fishear.utils.Texts;
import net.fishear.web.t5.exceptions.FieldValidationException;

import org.apache.tapestry5.Field;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.runtime.ComponentEventException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class 
	AbstractComponent 
extends
	AbstractFragment
{

	private final Logger log = LoggerFactory.getLogger(getClass());

	private net.fishear.web.t5.components.Messages messages;

    @Persist()
	private LocaleData localeData;

	public Object onException(Throwable iEx) {
		if(findMessages() != null) {
			Throwable ex = Exceptions.getRootCause(iEx);
			if(ex instanceof ValidationException) {
				setErrorText(getMessage(ex.getMessage()));
			} else {
				log.error("", ex);
				setErrorText(ex.toString());
			}
		}
		return this;
	}
	
    public Date getCurrentTime() {
        return new Date();
    }

    protected Logger getLog() {
        return log;
    }

    /**
     * Returns relative link to this page.
     * Link is relative to project's root.
     */
    public String getPageLink() {
        return getResources().getPageName();
    }

    /**
     * returns localized formatted message.
     *
     * @param key  key for obtain localized (unformatted) message
     * @param args if this is non-empty, it is used as arguments for replace with format matkers (%s, %d ...)
     */
    public final String getMessage(String key, Object... args) {
        if (key == null || (key = key.trim()).length() == 0) {
            return "";
        }
        if (key.startsWith("localized:")) {
            key = key.substring("localized:".length());
            if (args == null || args.length == 0) {
                return key;
            } else {
            	new Formatter().format(key, args).out().toString();
            }
        }
        if (args == null || args.length == 0) {
            return getResources().getMessages().get(key);
        } else {
            return getResources().getMessages().getFormatter(key).format(args);
        }
    }

    public final Messages getMessages() {
        return getResources().getMessages();
    }

    public LocaleData getLocaleData() {
    	if(localeData == null) {
    		localeData = new LocaleData(this);
    	}
    	return localeData;
    }
    
    /**
     * returns common date format
     *
     * @return
     */
    public String getDateTimeFormat() {
    	return getLocaleData().getDateTimeFormat();
    }

    public String getDateFormat() {
    	return getLocaleData().getDateFormat();
    }

    public NumberFormat getCurrencyFormat() {
    	return getLocaleData().getCurrencyFormat();
    }

    protected void setError(Form form, Throwable ex) {
        setError(form, ex, null);
    }

    private String exMsg(Throwable ex) {
        if (ex == null) {
            return "";
        }
        String s = ex.getMessage();
        if (s == null || (s = s.trim()).length() == 0) {
            s = ex.toString();
        }
        return s;
    }
    private Throwable checkForKnownCauses(Throwable ex) {
        Throwable cause = ex.getCause();
        while (cause != null) {
            if (cause instanceof ValidationException) {
                return cause;
            }
            cause = cause.getCause();
        }
        return ex;
    }

    protected void setError(Form form, Throwable iEx, String msg) {
        Throwable ex = checkForKnownCauses(iEx);
        if (ex == iEx) {
            if (iEx instanceof ComponentEventException) {
                ex = ((ComponentEventException) iEx).getCause();
            } else {
                ex = iEx;
            }
        }

        String emsg;
        if (ex instanceof ValidationException) {
            emsg = getMessage(exMsg(ex), ((ValidationException) ex).getParams());
            Field field;
            if (ex instanceof FieldValidationException && (field = ((FieldValidationException) ex).getField()) != null) {
                form.recordError(field, emsg);
            } else {
                form.recordError(emsg);
            }
            Globals.getLogger().error(((Texts.tos(msg)).length() > 0 ? msg.concat(" / ") : "") + emsg);

        } else {
            Globals.getLogger().error(msg, iEx);
            form.recordError(exMsg(ex));
        }
    }

	private net.fishear.web.t5.components.Messages findMessages() {
		if(messages != null) {
			return messages;
		}
		try {
			messages = (net.fishear.web.t5.components.Messages) getResources().getEmbeddedComponent("messages");
			return messages;
		} catch(Exception ex) {}
		org.apache.tapestry5.runtime.Component par = getResources().getContainer();
		while(par != null) {
			try {
				messages = (net.fishear.web.t5.components.Messages) par.getComponentResources().getEmbeddedComponent("messages");
				return messages;
			} catch(Exception ex) {}
			par = par.getComponentResources().getContainer();
		}
		throw new IllegalStateException(String.format("The component or any of parents must contain '%s' component with ID '%s'", net.fishear.web.t5.components.Messages.class.getName(),"messages"));
	}

	public void setMessageText(String msg) {
		findMessages().setText(msg);
	}

	public void setErrorText(String msg) {
		findMessages().setError(msg);
	}
}
