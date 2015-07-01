package duy.hw4.data;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import duy.hw4.model.Payment;

@ApplicationScoped
public class PaymentRepository {

	@Inject
	private EntityManager em;

	public List<Payment> findByUser(String userId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Payment> criteria = cb.createQuery(Payment.class);
		Root<Payment> payment = criteria.from(Payment.class);
		criteria.select(payment).where(cb.equal(payment.get("user_id"), userId));
		return em.createQuery(criteria).getResultList();
	}
}
