package paymentmicroservice.Service;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class ConstantVariable {
    public static final String cod = "COD";
    public static final float codMaxBase = 50000;
    public static final float emiMinBase=3000;
    public static final String emi="EMI";
    public static  final String creditCard="CreditCard";
    public static  final String debitCard="DebitCard";



}
