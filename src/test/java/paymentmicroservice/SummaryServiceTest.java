package paymentmicroservice;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import paymentmicroservice.Models.BankAvailable;
import paymentmicroservice.Models.CustomResponse;
import paymentmicroservice.Models.Order;
import paymentmicroservice.Models.Summary;
import paymentmicroservice.Repository.SummaryRepo;
import paymentmicroservice.Service.CancelOrderService;
import paymentmicroservice.Service.MerchantService;
import paymentmicroservice.Service.SummaryService;
import paymentmicroservice.Validation.BasicValidation;

import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SummaryServiceTest {

    @InjectMocks
    SummaryService summaryService;
    @Mock
    SummaryRepo summaryRepo;
    @Mock
    MerchantService merchantService;
    @Mock
    BasicValidation basicValidation;
    @Mock
    CancelOrderService cancelOrderService;

    Summary summary=null;
    Order order;

    @Before
    public void setUp(){
        order= new Order();
        order.orderId="133";
        summary=new Summary("133","NetBanking","12345","Success",123,new Date());
        when(summaryRepo.save(summary)).thenReturn(summary);
        when(summaryRepo.findById(summary.getorderId())).thenReturn(Optional.of(summary));
        when(merchantService.getDate()).thenReturn(new Date());
        when(merchantService.getStatus()).thenReturn("Success");
        when(merchantService.getTransctionId()).thenReturn("999999999");
        when(basicValidation.validateString(summary.getorderId())).thenReturn(true);
        when(cancelOrderService.cancelOrder(summary.getorderId())).thenReturn(ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(200,"OK",null)));
       // mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public  void TestSave(){
        assertEquals(summary.getTransactionId(),summaryService.save(summary).getTransactionId());
    }
    @Test
    public void TestGetAmountNull(){
        when(summaryRepo.findById(summary.getorderId())).thenReturn(Optional.empty());
        assertEquals(Float.toString(-1),Float.toString(summaryService.getAmount(summary.getorderId())));
    }

    @Test
    public void TestGetAmount(){
        assertEquals(Float.toString(summary.getAmount()),Float.toString(summaryService.getAmount(summary.getorderId())));
    }

    @Test
    public void testOrderIdNotPresent(){
        when(summaryRepo.findById(summary.getorderId())).thenReturn(Optional.empty());
        assertEquals(false,summaryService.isOrderIdPresent(summary.getorderId()));
    }
    @Test
    public void testOrderIdPresent(){
        assertEquals(true,summaryService.isOrderIdPresent(summary.getorderId()));
    }
    @Test
    public void testSummary(){
        assertEquals(Optional.of(summary),summaryService.getSummary(summary.getorderId()));
    }

    @Test
    public void PayFailed(){
        assertEquals("Payment failed!",summaryService.finalPay(order).getBody().getMessage());
    }



}
