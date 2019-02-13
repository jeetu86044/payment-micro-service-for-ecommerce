package paymentmicroservice.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import paymentmicroservice.Models.CancelOrder;
import paymentmicroservice.Models.CustomResponse;
import paymentmicroservice.Models.Order;
import paymentmicroservice.Models.Summary;
import paymentmicroservice.Repository.CancelOrderRepo;
import paymentmicroservice.Repository.CardDetailRepo;
import paymentmicroservice.Validation.BasicValidation;

import java.util.Date;
import java.util.Optional;



@Service
public class CancelOrderService {
    @Autowired
    BasicValidation basicValidation;
    @Autowired
    SummaryService summaryService;
    @Autowired
    CancelOrderRepo cancelOrderRepo;
    private static final int  days= 10;

    public ResponseEntity<CustomResponse> cancelOrder(String orderId){
        try {

            if (orderId == null || !basicValidation.validateString(orderId))
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(404, "order id is null", null));

            Optional optional = summaryService.getSummary(orderId);
            if (optional.isPresent()) {
                Summary summary = (Summary) optional.get();
                if (summary.getDate() != null) {
                    if(cancelOrderRepo.findById(orderId).isPresent())
                        return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(404, "Cancellation is already initiated !", null));
                    long diffInDays = (int) (new Date().getTime() - summary.getDate().getTime()) / (1000 * 60 * 60 * 24);
                    if (diffInDays <= days && !summary.getModOfPayment().equalsIgnoreCase("COD")&&summary.getStatus().equalsIgnoreCase("Success")) {
                        CancelOrder cancelOrder = new CancelOrder(orderId, new Date(), "Initiated");
                        cancelOrder = cancelOrderRepo.save(cancelOrder);
                        System.out.println(cancelOrder==null);
                        if (cancelOrder == null)
                            throw new NullPointerException();

                        return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(200, "Cancellation Initiated", null));

                    }
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(404, "Cancellation is not allowed", null));
        }
        catch (NullPointerException e){
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(404, "Please Try again Later!", null));
        }


    }
}
