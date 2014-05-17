package net.fishear.web.rights.t5.services;

import java.io.IOException;

import net.fishear.web.rights.services.LoginLogoutService;

import org.apache.tapestry5.TapestryFilter;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.services.ApplicationGlobals;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;

public class RemembermeT5Filter implements RequestFilter
{

	private ApplicationGlobals sbRsc;
	
	private LoginLogoutService llSvc;
	
	private boolean initialized = false;
	
	public RemembermeT5Filter(ApplicationGlobals sbRsc) {
		super();
		this.sbRsc = sbRsc;
	}

	public boolean service(Request request, Response response, RequestHandler handler) throws IOException {
		
		if(!initialized) {
			try {
				Registry reg = (Registry) sbRsc.getContext().getAttribute(TapestryFilter.REGISTRY_CONTEXT_NAME);
				llSvc = (LoginLogoutService) reg.getService(LoginLogoutService.class);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		if(llSvc != null) {
	        llSvc.checkRememberMe();
		}
		return handler.service(request, response);
	}

}
