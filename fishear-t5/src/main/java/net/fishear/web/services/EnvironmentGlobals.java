package net.fishear.web.services;

import javax.servlet.ServletContext;

/** designed to replace {@link ApplicationContext} in {@link RTsSystemServiceImpl}.
 * TODO: It is not used yet - should be impolemented and refactored.
 * 
 * @author terber
 *
 */
public interface EnvironmentGlobals
{

    void storeServletContext(ServletContext context);

    ServletContext getServletContext();

}
