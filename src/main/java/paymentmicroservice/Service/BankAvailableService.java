package paymentmicroservice.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import paymentmicroservice.Models.*;
import paymentmicroservice.Repository.BankAvailableRepo;
import paymentmicroservice.Repository.SummaryRepo;
import paymentmicroservice.Validation.BasicValidation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BankAvailableService {

    @Autowired
    BankAvailableRepo bankAvailableRepo;
    @Autowired
    SummaryService summaryService;
    @Autowired
    BasicValidation basicValidation;
    /*
    This will returns the list of banks available based on the status of bank
     if user has choosen NetBanking
     */
    public ResponseEntity<CustomResponse> getBanks(Order order)
    {
        try {
            Response response = new Response();
            if (!(basicValidation.validateString(order.orderId) && validateNetbanking(order.orderId)))
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(404, "You have not selected NetBanking", new Response()));

            response.options = bankAvailableRepo.getAllBankS();
            //if(response.options==null)
              //  throw new NullPointerException();
            response.orderId = order.orderId;
            response.amount =summaryService.getAmount(order.orderId);
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(200, "OK", response));
        }
        catch (NullPointerException e){
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(404, "Not Able to fetch details", new Response()));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(404, "Something Went Wrong", new Response()));
        }
    }
   /*
   Checks whether user has choosen NetBanking
    */
    public boolean validateNetbanking(String orderId)
    {
        Optional optional =summaryService.getSummary(orderId);
        if(optional.isPresent()) {
            Summary summary = (Summary)optional.get();
            if (summary.getModOfPayment().equalsIgnoreCase("NetBanking")) {
                return true;
            }
        }
        return false;
    }
    /*
    This will validate the Bank choosen by user if user has opted NetBanking
     */
    public ResponseEntity<CustomResponse> validateBank(PaymentOptionSelected paymentOptionSelected)
    {


        try {
            Response response = new Response();
            if (!(basicValidation.validateString(paymentOptionSelected.orderId) && validateNetbanking(paymentOptionSelected.orderId)))
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(404, "You have not selected NetBanking", new Response()));
            response.orderId = paymentOptionSelected.orderId;
            response.amount = summaryService.getAmount(paymentOptionSelected.orderId);
            BankAvailable bankAvailable = bankAvailableRepo.findByName(paymentOptionSelected.optionSelected);
            List<String> temp = new ArrayList<>();
            if (bankAvailable == null) {
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(404, "Please select from the given options", new Response()));
            }
            temp.add(paymentOptionSelected.optionSelected);
            response.options = temp;
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(200, "Ok", response));
        }
        catch (NullPointerException e){
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(404, "Something Went Wrong", new Response()));
        }
    }


}
