package paymentmicroservice.Repository;

import org.springframework.data.repository.CrudRepository;
import paymentmicroservice.Models.CancelOrder;

public interface CancelOrderRepo extends CrudRepository<CancelOrder,String> {
}
