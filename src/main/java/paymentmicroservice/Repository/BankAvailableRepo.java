package paymentmicroservice.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import paymentmicroservice.Models.BankAvailable;

import java.util.List;

public interface BankAvailableRepo extends CrudRepository<BankAvailable,Integer> {
    @Query("SELECT p.name FROM BankAvailable p WHERE p.isActive=true")
    public List<String> getAllBankS();
    public BankAvailable findByName(String Name);
}
