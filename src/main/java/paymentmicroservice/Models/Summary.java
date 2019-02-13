package paymentmicroservice.Models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Summary {
    @Id
    private String orderId;
    private String modOfPayment;
    @Column(unique = true)
    private String transactionId;
    private String status;
    private float amount;
    private Date date;
    public  Summary(){}
    public Summary(String orderId, String modOfPayment, String transactionId, String status, float amount, Date date) {
        this.orderId = orderId;
        this.modOfPayment = modOfPayment;
        this.transactionId = transactionId;
        this.status = status;
        this.amount = amount;
        this.date = date;
    }

    public String getorderId() {
        return orderId;
    }

    public void setorderId(String orderId) {
        this.orderId = orderId;
    }

    public String getModOfPayment() {
        return modOfPayment;
    }

    public void setModOfPayment(String modOfPayment) {
        this.modOfPayment = modOfPayment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTransactionId() {
        return transactionId;
    }


    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String  getStatus() {
        return status;
    }

    public void setSuccess(String  status) {
        this.status=status;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
