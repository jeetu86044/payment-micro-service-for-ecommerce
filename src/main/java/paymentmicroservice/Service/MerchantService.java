package paymentmicroservice.Service;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
public class MerchantService {

    public String getTransctionId()
    {
        Random random = new Random();
        return String.valueOf(random.nextInt((100000000 - 10000000) + 1) + 100000);
    }
    public Date getDate()
    {
        return new Date();
    }
    public String getStatus()
    {
        return "Success";
    }
}
