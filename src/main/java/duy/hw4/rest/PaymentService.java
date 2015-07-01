package duy.hw4.rest;

import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import duy.hw4.data.PaymentRepository;
import duy.hw4.model.Payment;
import duy.hw4.service.PaymentRegistration;

@Path("/payment")
@RequestScoped
@Stateful
public class PaymentService {
	
	@Inject
	private PaymentRegistration paymentRegistration;
	
	@Inject
	private PaymentRepository paymentRepository;
	
	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Payment> getPaymentsByUserId(@PathParam("id") String userId) {
		return paymentRepository.findByUser(userId);
	}
	
	@POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Payment add(Payment payment) {
        try {
	        paymentRegistration.addPayment(payment);
	        return payment;
        } catch (Exception e) {
	        e.printStackTrace();
	        return null;
        }
    }
}
