package paymentmicroservice.Validation;

import org.springframework.stereotype.Service;
import paymentmicroservice.Models.CardDetail;

import java.util.Calendar;
import java.util.Date;

@Service
public class CardValidation {

    public boolean validateDebitCard(CardDetail cardDetail)
    {
        if(validateName(cardDetail.getName())&&validateInitialCardDigit(cardDetail.getCardNumber())&&validateCvv(cardDetail.getCvv())&&validateExpiry(cardDetail.getExpiryDate()))
            return true;
        return false;
    }
    public boolean validateCreditCard(CardDetail cardDetail)
    {
        if(validateName(cardDetail.getName())&&validateInitialCardDigit(cardDetail.getCardNumber())&&validateCard(cardDetail.getCardNumber())&&validateCvv(cardDetail.getCvv())&&validateExpiry(cardDetail.getExpiryDate()))
            return true;
        return  false;

    }

    public boolean validateCvv(String cvv)
    {
        if(cvv.length()!=3&&cvv.length()!=4)
            return false;
        for(int i=0;i<cvv.length();i++)
        {
            if(!(cvv.charAt(i)>='0'&&cvv.charAt(i)<='9'))
                return false;
        }

        return  true;
    }
    public boolean validateExpiry(String expiryDate)
    {
        if(expiryDate.length()!=4&&expiryDate.length()!=5)
            return false;
        int month,year;
        if(expiryDate.charAt(1)=='/') {
             month = expiryDate.charAt(0)-'0';
             year = (expiryDate.charAt(2)-'0')*10+(expiryDate.charAt(3)-'0');
        }
        else
        {
            month = (expiryDate.charAt(0)-'0')*10+(expiryDate.charAt(1)-'0');
            year = (expiryDate.charAt(3)-'0')*10+(expiryDate.charAt(4)-'0');
        }
        year+=2000;
        if(month<=0||month>12)
            return false;
        Date date=new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if(cal.get(Calendar.YEAR)>year)
             return  false;
        if(cal.get(Calendar.YEAR)<year)
            return  true;
        if(cal.get(Calendar.MONTH)<=month)
            return  true;

        return  false;
    }

    public boolean validateCard(String cardNumber)
    {
        int oddSum=0;
        int even = 0;
        if(cardNumber.length()!=16)
            return false;
        for(int i=15;i>=0;i--)
        {
            if(i%2==1)
                oddSum+=cardNumber.charAt(i)-'0';
            else
            {
                int temp = (cardNumber.charAt(i)-'0')*2;
                even += temp/10+temp%10;
            }
        }
        if((even+oddSum)%10==0)
            return true;
        else
            return false;
    }
    public  boolean validateInitialCardDigit(String cardNumber){
        if(cardNumber.length()!=16)
            return false;
        if(!(cardNumber.charAt(0)>='3'&&cardNumber.charAt(0)<='6'))
            return false;
        for(int i=1;i<16;i++){
            if(!(cardNumber.charAt(i)>='0'&&cardNumber.charAt(i)<='9'))
                return false;
        }
        return true;

    }

    public  boolean validateName(String name) {

        name = name.trim();
        name =name.toLowerCase();
        if(name.length()<=0)
            return false;

        for(int i=0;i<name.length();i++){
            if(!((name.charAt(i)>='a'&&name.charAt(i)<='z')||name.charAt(i)==' '))
                return false;
        }
        return true;


    }
}
