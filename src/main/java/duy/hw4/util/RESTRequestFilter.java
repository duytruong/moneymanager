package duy.hw4.util;

import java.io.IOException;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import duy.hw4.rest.HTTPHeaderName;
import duy.hw4.service.UserAuthenticator;

@Provider
@PreMatching
public class RESTRequestFilter implements ContainerRequestFilter {

	private final static Logger log = Logger.getLogger(RESTRequestFilter.class
	        .getName());

	@Inject
	UserAuthenticator authenticator;

	@Override
	public void filter(ContainerRequestContext requestCtx) throws IOException {

		String path = requestCtx.getUriInfo().getPath();
		log.info("Filtering request path: " + path);
		
		if (path.startsWith("/user/register")) {
			return;
		}

		// IMPORTANT!!! First, Acknowledge any pre-flight test from browsers for
		// this case before validating the headers (CORS stuff)
		if (requestCtx.getRequest().getMethod().equals("OPTIONS")) {
			requestCtx.abortWith(Response.status(Response.Status.OK).build());

			return;
		}

		// Then check is the service key exists and is valid.
		// UserAuthenticator authenticator = UserAuthenticator.getInstance();
		//String serviceKey = requestCtx.getHeaderString(HTTPHeaderName.SERVICE_KEY);
		String serviceKey = "";

		if (!authenticator.isServiceKeyValid(serviceKey)) {
			// Kick anyone without a valid service key
			requestCtx.abortWith(Response.status(Response.Status.UNAUTHORIZED)
			        .build());

			return;
		}

		// For any other methods besides login, the authToken must be verified
		if (!path.startsWith("/auth/login")) {
			String authToken = requestCtx
			        .getHeaderString(HTTPHeaderName.AUTH_TOKEN);

			// if it isn't valid, just kick them out.
			if (!authenticator.isAuthTokenValid(serviceKey, authToken)) {
				requestCtx.abortWith(Response.status(
				        Response.Status.UNAUTHORIZED).build());
			}
		}
	}
}
