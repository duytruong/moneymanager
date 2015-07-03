package duy.hw4.rest;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.security.auth.login.LoginException;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;

import duy.hw4.model.TokenJSON;
import duy.hw4.model.User;
import duy.hw4.model.UserJSON;
import duy.hw4.service.UserAuthenticator;

@Stateless
public class AuthenticationServiceImp implements AuthenticationService {

	private static final long serialVersionUID = -2459096313906088275L;
	
	@Inject
	UserAuthenticator authenticator;
	
	ObjectMapper mapper = new ObjectMapper();

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
			
			TokenJSON token = new TokenJSON();
			token.setAuth_token(authToken);
			String json = "";
			try {
				json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(token);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return getNoCacheResponseBuilder(Response.Status.OK).entity(
					json).build();

		} catch (final LoginException ex) {
			String json = "{\"message\":\"Problem matching service key, username and password\"}";

			return getNoCacheResponseBuilder(Response.Status.UNAUTHORIZED)
			        .entity(json).build();
		}
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
		User user = authenticator.getUser(authToken);
		
		UserJSON userJSOn = new UserJSON();
		userJSOn.setUserid(user.getId());
		userJSOn.setName(user.getName());
		String json = "";
		try {
			json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userJSOn);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return getNoCacheResponseBuilder(Response.Status.OK).entity(
		        json).build();
	}
}
