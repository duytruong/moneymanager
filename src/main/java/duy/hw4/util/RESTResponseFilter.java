package duy.hw4.util;

import java.io.IOException;
import java.util.logging.Logger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import duy.hw4.rest.HTTPHeaderName;

@Provider
public class RESTResponseFilter implements ContainerResponseFilter {

	private final static Logger log = Logger.getLogger(RESTResponseFilter.class
	        .getName());

	@Override
	public void filter(ContainerRequestContext requestCtx,
	        ContainerResponseContext responseCtx) throws IOException {

		log.info("Filtering REST Response");

		responseCtx.getHeaders().add("Access-Control-Allow-Origin", "*");
		responseCtx.getHeaders().add("Access-Control-Allow-Credentials", "true");
		responseCtx.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
		responseCtx.getHeaders().add("Access-Control-Allow-Headers",
		        HTTPHeaderName.SERVICE_KEY + ", " + HTTPHeaderName.AUTH_TOKEN);
	}
}
