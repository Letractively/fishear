package net.fishear.web.t5.components;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.PageAttached;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.Error;

import java.util.ArrayList;
import java.util.List;


/** puts message component to page.
 * Unlike the Tapestry's {@link Error} component, this component need not ne placed inside form.
 * 
 * @author terber
 */
public class Messages {

	@Parameter
	private String text;

	private List<String> texts;
	
	private List<String> errors;

	@Parameter(name = "class")
	private String cssClass;
	
	@Parameter(name = "errorClass", value = "literal:error", allowNull = false)
	private String errClass;
	
	@Parameter(value = "false", defaultPrefix = BindingConstants.LITERAL)
	private boolean stayOn;
	
    // Allow null so we can generate a better error message if missing
    @Environmental(false)
    private ValidationTracker tracker;

	@PageAttached
	public void pageAttached() {
		addText(text);
	}

	public void addText(String s) {
		if(s != null && (s = s.trim()).length() > 0) {
//			if(errors != null) {
//				throw new IllegalStateException(String.format("Some error is already set. %s", "You cannot combine errors and normal texts"));
//			}
			if(texts == null) {
				texts = new ArrayList<String>();
			}
			texts.add(s);
		}
	}

	public void addError(String s) {
		if(s != null && (s = s.trim()).length() > 0) {
//			if(texts != null) {
//				throw new IllegalStateException(String.format("Some text is already set. %s", "You cannot combine errors and normal texts"));
//			}
			if(errors == null) {
				errors = new ArrayList<String>();
			}
			errors.add(s);
		}
	}

	private String prepareText(List<String> texts) {
		StringBuilder sb = new StringBuilder();
		if(texts != null) {
			for (String s : texts) {
				if(s != null && s.length() > 0) {
					if(sb.length() > 0) { sb.append("<br />"); }
					sb.append(s);
				}
			}
		}
		return sb.toString();
	}

	@BeginRender
	public void beginRender(MarkupWriter wr) {

		prepareFormErrors();
		boolean anyOk = false;
		if (errors != null) {
			anyOk |= renderContent(
					wr, 
					prepareText(errors), 
					StringUtils.isEmpty(this.errClass) ? "error" : this.errClass
			);
		} 
		if(texts != null) {
			anyOk |= renderContent(
					wr, 
					prepareText(texts), 
					StringUtils.isEmpty(this.cssClass) ? "message" : this.cssClass
			);
		}
		if(!anyOk && stayOn) {
			wr.element("div", "class", (StringUtils.isEmpty(this.cssClass) ? "message" : this.cssClass).concat("Empty"));
			wr.writeRaw("&#160;");
			wr.end();
		}
	}

	private boolean renderContent(MarkupWriter wr, String text, String cssClass) {
		if(text != null && text.length() > 0) {
			wr.element("div", "class", cssClass);
			wr.writeRaw(text.toString());
			wr.end();
			return true;
		}
		return false;
	}

	private void prepareFormErrors() {
		if(tracker == null || !tracker.getHasErrors()) {
			return;
		}
        List<String> errors = tracker.getErrors();

        for (String message : errors) {
        	addError(message);
        }
		
	}

	/** returns text currently set as error or message. 
	 * returns empty string in case no message or error is set.
	 * Call {@link #isError()} or {@link #isMessage()} to determine message or error is set.
	 */
	public String getText() {
		if (errors != null) {
			return prepareText(errors);
		} else if(texts != null) {
			return prepareText(texts);
		} else {
			return "";
		}
	}

	/** returns true if any error text is set. Returns false in case no error - that means normal text may be set or nothing.
	 * @return
	 */
	public boolean isError() {
		return errors != null;
	}

	/** returns true if any message text is set. Returns false in case no message is set - that means error text may be set or nothing.
	 * @return
	 */
	public boolean isMessage() {
		return errors != null;
	}

	/** Sets the single message text. This method clear all previously added messge or error texts (if any).
	 * @param text
	 */
	public void setText(String text) {
		texts = null;
		errors = null;
		addText(text);
	}

	/** Sets the single error text. This method clear all previously added messge or error texts (if any).
	 * @param text
	 */
	public void setError(String text) {
		texts = null;
		errors = null;
		addError(text);
	}
	
}
