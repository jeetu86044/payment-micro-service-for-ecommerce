package paymentmicroservice.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import paymentmicroservice.Models.*;
import paymentmicroservice.Repository.CodSupportRepo;
import paymentmicroservice.Repository.PaymentOptionRepo;
import paymentmicroservice.Validation.BasicValidation;

import java.util.*;

import static paymentmicroservice.Service.ConstantVariable.*;

@Service
public class PaymentOptionService {

    @Autowired
    PaymentOptionRepo paymentOptionRepo;
    @Autowired
    CodSupportRepo codSupportRepo;
    @Autowired
    SummaryService summaryService;
    @Autowired
    BasicValidation basicValidation;
    @Autowired
    ConstantVariable constantVariable;




      //Controller Function
    /*
    This Function returns the list of available payment options for a particular orderId based on
    1.ProductIds supported for COD
    2.Total amount
    3.Whether particular option is currently active or not
     */
    public ResponseEntity<CustomResponse> getOptions(Order order)
    {
        try {
            String orderId = order.orderId;
            if (!basicValidation.validateString(orderId)) {
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(400, "OrderId is not Valid", null));
            }
            CheckOut checkOut = getAllDetails(orderId);
            List<String> productIds = checkOut.getProductIds();
            float amount = checkOut.getAmount();
            if (!(basicValidation.validateAmount(amount) && basicValidation.validateList(productIds))) {
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(400, "Inconsistent Data", null));
            }
            List<String> paymentOptions = getPaymentOption(productIds, amount);
            Summary summary = new Summary(orderId, null, null, null, amount, null);
            summary=summaryService.save(summary);
            if(summary==null)
                throw new NullPointerException();
            Response response = new Response();
            response.options = paymentOptions;
            response.orderId = orderId;
            response.amount = amount;
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(200, "OK", response));
        }
        catch (NullPointerException e)
        {
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(400, "Couldn't Save Data", null));
        }
    }

    //Controller Function
    /*
    This Function Varyfies the payment user has selected
    1.ProductIds supported for COD
    2.Total amount
    3.Whether particular option is currently active or not
     */

    public ResponseEntity<CustomResponse> validate(PaymentOptionSelected paymentOptionSelected)
    {
        try {
            String optionSelected = paymentOptionSelected.optionSelected;
            String orderId = paymentOptionSelected.orderId;
            if ((basicValidation.validateString(orderId) && basicValidation.validateString(optionSelected))) {
                float amount = summaryService.getAmount(paymentOptionSelected.orderId);
                CheckOut checkOut = getAllDetails(orderId);
                List<String> productIds = checkOut.getProductIds();
                if (basicValidation.validateList(productIds)&&basicValidation.validateAmount(amount)) {
                    List<String> paymentOptions = getPaymentOption(productIds, amount);
                    List<String> temp = new ArrayList<>();
                    Response response = new Response();
                    response.orderId = paymentOptionSelected.orderId;
                    if (paymentOptions.contains(optionSelected)) {
                        temp.add(optionSelected);
                        response.options = temp;
                        response.amount =amount;
                        Summary summary = new Summary(paymentOptionSelected.orderId, optionSelected, null, null, amount, null);
                        summary=summaryService.save(summary);
                        System.out.println(summary.getModOfPayment());
                        if(summary!=null)
                        return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(200, "OK", response));
                    }
                }
            }

            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(400, "You have selected an incorrect option", new Response()));
        }
        catch (NullPointerException e){
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(400, "Something Went Wrong", new Response()));
        }
    }

     //Returns the list of payment options
    public List<String> getPaymentOption(List<String>productIds,float amount)
    {
        List<String> paymentOptions=paymentOptionRepo.getOptions();
        if(hasCOD(paymentOptions)&& (codMaxBase<amount||codNotAvailable(productIds)))
        {
            paymentOptions.remove(cod);
        }
        if(amount<emiMinBase&&paymentOptions.contains(emi))
        {
            paymentOptions.remove(emi);
        }
        return paymentOptions;
    }

    //It checks whether COD is present in the payment option list
    public boolean hasCOD(List<String>paymentOptions)
    {
        for(String paymentOption:paymentOptions)
        {
            if(paymentOption.equalsIgnoreCase(cod))
                return true;
        }
        return false;
    }

    //It checks whether products not supported COD
    public boolean codNotAvailable(List<String>productIds)
    {
        for (String id:productIds)
        {
            if(codSupportRepo.findById(id).isPresent())
                return true;
        }
        return false;
    }

  /*
  This will return the list of productIds , amount corresponding to particular order Id
  from Order-Micro service(MS-4)
   */
    public CheckOut getAllDetails(String orderId)
    {
        try {
            final String uri = "http://d034eb08.ngrok.io/orderDetails/"+orderId;
            Order order = new Order();
            order.orderId = orderId;
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(order.orderId);
            String response = restTemplate.getForObject(uri,String.class);
            JSONObject obj = new JSONObject(response);
            JSONObject arr = obj.getJSONObject("responseData");
            ObjectMapper mapper = new ObjectMapper();
            CheckOut checkOut = mapper.readValue(arr.toString(),CheckOut.class);
            System.out.println(checkOut.getProductIds());
            return checkOut;

        }
        catch (Exception e){
            return new CheckOut();
        }

    }

}
