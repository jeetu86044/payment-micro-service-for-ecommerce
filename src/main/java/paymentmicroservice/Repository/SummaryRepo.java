package paymentmicroservice.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import paymentmicroservice.Models.Summary;

import java.util.List;

public interface SummaryRepo extends CrudRepository<Summary,String> {
}
