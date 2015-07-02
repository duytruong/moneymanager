package duy.hw4.rest;

import java.security.GeneralSecurityException;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.security.auth.login.LoginException;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import duy.hw4.service.UserAuthenticator;

@Stateless
public class AuthenticationServiceImp implements AuthenticationService {

	private static final long serialVersionUID = -2459096313906088275L;
	
	@Inject
	UserAuthenticator authenticator;

	@Override
	public Response login(@Context HttpHeaders httpHeaders,
	        @FormParam("username") String username,
	        @FormParam("password") String password) {

		//UserAuthenticator authenticator = UserAuthenticator.getInstance();
		//String serviceKey = httpHeaders.getHeaderString(HTTPHeaderName.SERVICE_KEY);
		String serviceKey = "";

		try {
			String authToken = authenticator.login(serviceKey, username,
			        password);
			
			JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
			jsonObjBuilder.add("auth_token", authToken);
			JsonObject jsonObj = jsonObjBuilder.build();

			return getNoCacheResponseBuilder(Response.Status.OK).entity(
			        jsonObj.toString()).build();

		} catch (final LoginException ex) {
			JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
			jsonObjBuilder.add("message",
			        "Problem matching service key, username and password");
			JsonObject jsonObj = jsonObjBuilder.build();

			return getNoCacheResponseBuilder(Response.Status.UNAUTHORIZED)
			        .entity(jsonObj.toString()).build();
		}
	}

	@Override
	public Response demoGetMethod() {
		JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
		jsonObjBuilder.add("message", "Executed demoGetMethod");
		JsonObject jsonObj = jsonObjBuilder.build();

		return getNoCacheResponseBuilder(Response.Status.OK).entity(
		        jsonObj.toString()).build();
	}

	@Override
	public Response demoPostMethod() {
		JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
		jsonObjBuilder.add("message", "Executed demoPostMethod");
		JsonObject jsonObj = jsonObjBuilder.build();

		return getNoCacheResponseBuilder(Response.Status.ACCEPTED).entity(
		        jsonObj.toString()).build();
	}

	@Override
	public Response logout(@Context HttpHeaders httpHeaders) {
		try {
			//UserAuthenticator authenticator = UserAuthenticator.getInstance();
			//String serviceKey = httpHeaders.getHeaderString(HTTPHeaderName.SERVICE_KEY);
			String serviceKey = "";
			String authToken = httpHeaders
			        .getHeaderString(HTTPHeaderName.AUTH_TOKEN);

			authenticator.logout(serviceKey, authToken);

			return getNoCacheResponseBuilder(Response.Status.NO_CONTENT)
			        .build();
		} catch (final GeneralSecurityException ex) {
			return getNoCacheResponseBuilder(
			        Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	private Response.ResponseBuilder getNoCacheResponseBuilder(Response.Status status) {
		CacheControl cc = new CacheControl();
		cc.setNoCache(true);
		cc.setMaxAge(-1);
		cc.setMustRevalidate(true);

		return Response.status(status).cacheControl(cc);
	}

	@Override
	public Response getUserId(String authToken) {
		long userid = authenticator.getUserId(authToken);
		
		JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
		jsonObjBuilder.add("user_id", userid);
		JsonObject jsonObj = jsonObjBuilder.build();

		return getNoCacheResponseBuilder(Response.Status.OK).entity(
		        jsonObj.toString()).build();
	}
}
