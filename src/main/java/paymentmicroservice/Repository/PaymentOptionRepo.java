package paymentmicroservice.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import paymentmicroservice.Models.PaymentOption;

import java.util.List;

public interface PaymentOptionRepo extends CrudRepository<PaymentOption,Integer> {

    @Query("SELECT p.name FROM PaymentOption p WHERE p.isActive=true")
    public List<String> getOptions();
}
