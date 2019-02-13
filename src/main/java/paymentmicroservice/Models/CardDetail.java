package paymentmicroservice.Models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CardDetail {
    @Id
    private String orderId;
    private String cardNumber;
    private String cvv;
    private String expiryDate;
    private String name;
    private String cardType;

    public CardDetail(){}

    public CardDetail(String orderId, String cardNumber, String cvv, String expiryDate, String name, String cardType) {
        this.orderId = orderId;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
        this.name = name;
        this.cardType = cardType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
}
