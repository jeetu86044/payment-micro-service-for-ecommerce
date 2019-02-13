package paymentmicroservice.Validation;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BasicValidation {


    public boolean validateString(String orderId)
    {
        if(orderId==null||orderId.isEmpty())
        {
            return false;
        }
        else
            return true;

    }
    public boolean validateAmount(float amount)
    {
        if(amount<=1)
            return false;
        else
            return  true;
    }
    public boolean validateList(List<String> productIds)
    {
        if(productIds.size()<=0)
            return false;
        return true;
    }

}
