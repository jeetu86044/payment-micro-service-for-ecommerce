package paymentmicroservice;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import paymentmicroservice.Controller.PaymentController;
import paymentmicroservice.Models.CancelOrder;
import paymentmicroservice.Models.CustomResponse;
import paymentmicroservice.Models.Response;
import paymentmicroservice.Models.Summary;
import paymentmicroservice.Repository.CancelOrderRepo;
import paymentmicroservice.Repository.SummaryRepo;
import paymentmicroservice.Service.CancelOrderService;
import paymentmicroservice.Service.SummaryService;
import paymentmicroservice.Validation.BasicValidation;

import javax.validation.constraints.Null;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
@RunWith(MockitoJUnitRunner.class)
public class CanclelOrderTest {


    @Mock
    SummaryService summaryService ;
    @Mock
    CancelOrderRepo cancelOrderRepo;

    @InjectMocks
    CancelOrderService cancelOrderService;
    @Mock
    BasicValidation basicValidation;

    Summary summary=null;
    CancelOrder cancelOrder =null;
    @Before
    public void setup(){
//        MockitoAnnotations.initMocks(this);
        summary = new Summary("133","NetBanking","jksdjd","Success",1323,new Date());
        cancelOrder = new CancelOrder("133",new Date(),"Initiated");


    }
    @Test
    public void test1()
    {
        when(basicValidation.validateString("133")).thenReturn(false);
        assertEquals("order id is null",cancelOrderService.cancelOrder("133").getBody().getMessage());
    }

    @Test
    public void test2()
    {
       when(summaryService.getSummary("133")).thenReturn(Optional.of(summary));
       when(cancelOrderRepo.findById("133")).thenReturn(Optional.of(cancelOrder));
       when(basicValidation.validateString("133")).thenReturn(true);
       assertEquals("Cancellation is already initiated !",cancelOrderService.cancelOrder("133").getBody().getMessage());
      // verify(summaryService).getSummary("133");
    }
    @Test
    public void test3()
    {
        when(summaryService.getSummary("133")).thenReturn(Optional.of(summary));
        when(cancelOrderRepo.findById("133")).thenReturn(Optional.empty());
        when(basicValidation.validateString("133")).thenReturn(true);
        when(cancelOrderRepo.save(any(CancelOrder.class))).thenReturn(cancelOrder);
        assertEquals("Cancellation Initiated",cancelOrderService.cancelOrder("133").getBody().getMessage());
    }
    @Test
    public void test4()
    {
        when(summaryService.getSummary("133")).thenReturn(Optional.of(summary));
        when(cancelOrderRepo.findById("133")).thenReturn(Optional.empty());
        when(basicValidation.validateString("133")).thenReturn(true);
        //when(cancelOrderRepo.save(any(CancelOrder.class))).thenReturn(cancelOrder);
        assertEquals("Please Try again Later!",cancelOrderService.cancelOrder("133").getBody().getMessage());
    }
    @Test
    public void test5()
    {
        summary.setModOfPayment("COD");
        when(summaryService.getSummary("133")).thenReturn(Optional.of(summary));
        when(cancelOrderRepo.findById("133")).thenReturn(Optional.empty());
        when(basicValidation.validateString("133")).thenReturn(true);
       // when(cancelOrderRepo.save(any(CancelOrder.class))).thenReturn(cancelOrder);
        assertEquals("Cancellation is not allowed",cancelOrderService.cancelOrder("133").getBody().getMessage());
    }


}
