package net.fishear.web.t5.base;

import net.fishear.utils.Classes;
import net.fishear.utils.Texts;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.SymbolSource;
import org.apache.tapestry5.runtime.Component;



/** simplest ancestor of all application components.
 * Each component should inherit from that class.
 * 
 * @author terber
 */
public abstract class 
	AbstractFragment
{

//	@Inject
//	private ComponentResources componentResources;

	@Inject
	private SymbolSource symbolSource;

	private int debugEnabled;

	@Cached
	private boolean debugEnabled() {
		if (debugEnabled == 0) {
			synchronized (this) {
				if (debugEnabled == 0) {
					debugEnabled =
						Boolean.valueOf(symbolSource.valueForSymbol(SymbolConstants.PRODUCTION_MODE)) ||
							Boolean.valueOf(symbolSource.valueForSymbol(SymbolConstants.COMPRESS_WHITESPACE))
							? -1 : 1;
				}
			}
		}
		return debugEnabled == 1;
	}

	@BeginRender
	void writeComponentDescriptionTextBegin(MarkupWriter wr) {
		if (debugEnabled()) {
			if (isPage()) {
				wr.writeRaw("\n<!-- PAGE: " + getClass().getName() + " -->\n");
			} else {
				wr.writeRaw("\n<!-- COMPONENT: " + getClass().getName() + " - BEGIN -->\n");
			}
		}
	}

	@AfterRender
	void writeComponentDescriptionTextEnd(MarkupWriter wr) {
		if (debugEnabled()) {
			if (isPage()) {
				// do nothing in case of page
			} else {
				wr.writeRaw("\n<!-- COMPONENT: "+getClass().getName()+" - END -->\n");
			}
		}
	}

	public ComponentResources getResources() {
		return ((Component)this).getComponentResources();
	}

	/**
	 * returns true if this stuff is the generated page itself; returns false if it is a component.
	 */
	public boolean isPage() {
		return getResources().getPage().getComponentResources() == getResources();
	}

	public String createEventLink(String eventType) {
		return getResources().createEventLink(eventType).toString();
	}

	/** returns script path based on this class name (with "js" sufiix added to it) and package.
	 * @param path subdirectory (relative to classes package directory) where the scripts resides. May be null or empty.
	 * @return
	 */
	public String getScriptPath(String path) {
		Class<?> clazz = getClass();
		return Classes.getPackage(clazz).replace('.', '/') + "/" + path;
	}

	/** returns script path based on this class name (with "js" sufiix added to it) and package.
	 * @param subdir subdirectory (relative to classes package directory) where the scripts resides. May be null or empty.
	 * @return
	 */
	public String getClassScriptPath(String subdir) {
		String s;
		if((s = Texts.tos(subdir, null)) != null) {
			return getScriptPath(s + "/" + Classes.getShortClassName(getClass()) + ".js");
		} else {
			return getScriptPath(Classes.getShortClassName(getClass()) + ".js");
		}
	}
	
	public String getGPage() {
		return "main.jsp?id=";
	}
	public String getUrlBase() {
		return "./";
	}
}
