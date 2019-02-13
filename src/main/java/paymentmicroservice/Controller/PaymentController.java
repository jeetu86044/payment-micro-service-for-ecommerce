package paymentmicroservice.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import paymentmicroservice.Models.*;
import paymentmicroservice.Service.*;


@RestController
public class PaymentController {
    @Autowired
    PaymentOptionService paymentOptionService;
    @Autowired
    BankAvailableService bankAvailableService;
    @Autowired
    SummaryService summaryService;
    @Autowired
    CardDetailService cardDetailService;
    @Autowired
    CancelOrderService cancelOrderService;

    @RequestMapping(method = RequestMethod.POST,value = "/checkout")
    public ResponseEntity<CustomResponse> checkOut(@RequestBody Order order)
    {
        if(summaryService.validateOrderId(order.orderId))
            return  ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(404,"Payment has already been made",null));
        return paymentOptionService.getOptions(order);
    }

    @RequestMapping(method = RequestMethod.POST,value = "/payment")
    public ResponseEntity<CustomResponse> payment(@RequestBody PaymentOptionSelected paymentOptionSelected)
    {
        if(summaryService.validateOrderId(paymentOptionSelected.orderId))
            return  ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(404,"Payment has already been made",null));
        return paymentOptionService.validate(paymentOptionSelected);
    }

    @RequestMapping(method = RequestMethod.POST,value = "/payment/netbanking")
    public ResponseEntity<CustomResponse> netBanking(@RequestBody Order order)
    {
        if(summaryService.validateOrderId(order.orderId))
            return  ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(404,"Payment has already been made",null));
       return bankAvailableService.getBanks(order);
    }

    @RequestMapping(method = RequestMethod.POST,value = "/payment/netbanking/data")
    public  ResponseEntity<CustomResponse>  getBankName(@RequestBody PaymentOptionSelected paymentOptionSelected)
    {
        if(summaryService.validateOrderId(paymentOptionSelected.orderId))
            return  ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(404,"Payment has already been made",null));
        return bankAvailableService.validateBank(paymentOptionSelected);
    }

    @RequestMapping(method = RequestMethod.POST,value = "/payment/card")
    public ResponseEntity<CustomResponse> getCard(@RequestBody CardDetail cardDetail)
    {
        if(summaryService.validateOrderId(cardDetail.getOrderId()))
            return  ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(404,"Payment has already been made",null));
        return cardDetailService.validateCard(cardDetail);
    }

    @RequestMapping(method = RequestMethod.POST ,value = "/payment/pay")
    public  ResponseEntity<CustomResponse> finalPay(@RequestBody Order order)
    {
        if(summaryService.validateOrderId(order.orderId))
            return  ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(404,"Payment has already been made",null));
        return  summaryService.finalPay(order);
    }

    @RequestMapping(method = RequestMethod.GET,value = "/payment/cancel/{orderId}")
    public ResponseEntity<CustomResponse> cancel(@PathVariable String orderId)
    {
       return cancelOrderService.cancelOrder(orderId);
    }

}
