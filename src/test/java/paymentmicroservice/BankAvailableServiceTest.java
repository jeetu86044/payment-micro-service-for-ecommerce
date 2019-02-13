package paymentmicroservice;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import paymentmicroservice.Models.BankAvailable;
import paymentmicroservice.Models.Order;
import paymentmicroservice.Models.PaymentOptionSelected;
import paymentmicroservice.Models.Summary;
import paymentmicroservice.Repository.BankAvailableRepo;
import paymentmicroservice.Service.BankAvailableService;
import paymentmicroservice.Service.SummaryService;
import paymentmicroservice.Validation.BasicValidation;

import java.lang.reflect.Array;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class BankAvailableServiceTest {
    @InjectMocks
    BankAvailableService bankAvailableService;
    @Mock
    BasicValidation basicValidation;
    @Mock
    SummaryService summaryService;
    @Mock
    BankAvailableRepo bankAvailableRepo;


    Summary summary=null;
    List<String>  bankList;
    PaymentOptionSelected paymentOptionSelected;
    BankAvailable bankAvailable;

    Order order;
    @Before
    public  void  setup(){
        order=new Order();
        order.orderId="133";
        paymentOptionSelected=new PaymentOptionSelected();
        paymentOptionSelected.orderId="133";
        paymentOptionSelected.optionSelected="SBI";
        bankAvailable=new BankAvailable(1,"SBI",true);
        summary = new Summary("133","Nsmn","jksdjd","Success",1323,new Date());
        bankList = Arrays.asList("PNB","SBI");
        when(basicValidation.validateString("133")).thenReturn(true);
        when(summaryService.getSummary("133")).thenReturn(Optional.of(summary));
        when(bankAvailableRepo.getAllBankS()).thenReturn(bankList);
        when(summaryService.getAmount("133")).thenReturn(5645.76f);

    }
    @Test
    public void test1(){
        assertEquals("You have not selected NetBanking",bankAvailableService.getBanks(order).getBody().getMessage());
    }
    @Test
    public void test2(){
        summary.setModOfPayment("NetBanking");
        assertEquals("OK",bankAvailableService.getBanks(order).getBody().getMessage());
    }
    @Test
    public void test3(){
        summary.setModOfPayment("NetBanking");
        assertEquals("OK",bankAvailableService.getBanks(order).getBody().getMessage());
    }

    @Test
    public void ValidateWhetherNetBanking(){
        assertEquals("You have not selected NetBanking",bankAvailableService.validateBank(paymentOptionSelected).getBody().getMessage());

    }

    @Test
    public void invalideBankSelected(){
        summary.setModOfPayment("NetBanking");
        when(bankAvailableRepo.findByName("SBI")).thenReturn(null);
        assertEquals("Please select from the given options",bankAvailableService.validateBank(paymentOptionSelected).getBody().getMessage());

    }
    @Test
    public void allValidated(){
        summary.setModOfPayment("NetBanking");
        when(bankAvailableRepo.findByName("SBI")).thenReturn(bankAvailable);
        assertEquals("Ok",bankAvailableService.validateBank(paymentOptionSelected).getBody().getMessage());

    }



}
