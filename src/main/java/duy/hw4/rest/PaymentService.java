package duy.hw4.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import duy.hw4.data.PaymentRepository;
import duy.hw4.model.Payment;
import duy.hw4.model.User;
import duy.hw4.service.PaymentRegistration;

@Path("/payment")
@RequestScoped
@Stateful
public class PaymentService {
	@Inject
	private Logger log;

	@Inject
	private Validator validator;
	
	@Inject
	private PaymentRepository paymentRepository;
	
	@Inject
	private PaymentRegistration paymentRegistration;
	
	@POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(Payment payment) {
    	
        Response.ResponseBuilder builder = null;

        try {
        	// Validates member using bean validation
        	validateMember(payment);
            
            registration.register(payment);

            // Create an "ok" response
            builder = Response.ok();
        } catch (ConstraintViolationException ce) {
            // Handle bean validation issues
            builder = createViolationResponse(ce.getConstraintViolations());
        } catch (ValidationException e) {
            // Handle the unique constrain violation
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("email", "Email taken");
            builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
        } catch (Exception e) {
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();
    }
}
