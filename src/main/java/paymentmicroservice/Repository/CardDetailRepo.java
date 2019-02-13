package paymentmicroservice.Repository;

import org.springframework.data.repository.CrudRepository;
import paymentmicroservice.Models.CardDetail;

public interface CardDetailRepo extends CrudRepository<CardDetail,String> {
}
