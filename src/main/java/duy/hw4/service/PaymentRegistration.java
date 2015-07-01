package duy.hw4.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import duy.hw4.model.Payment;

@Stateless
public class PaymentRegistration {

	@Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<Payment> memberEventSrc;

    public void addPayment(Payment payment) throws Exception {
        log.info("Adding " + payment.getName());
        em.persist(payment);
        memberEventSrc.fire(payment);
    }
}
