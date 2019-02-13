package paymentmicroservice.Models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class CancelOrder {
    @Id
    private String orderId;
    private Date dateOfCancellation;
    private String statusOfRefund;

    public  CancelOrder(){}
    public CancelOrder(String orderId, Date dateOfCancellation, String statusOfRefund) {
        this.orderId = orderId;
        this.dateOfCancellation = dateOfCancellation;
        this.statusOfRefund = statusOfRefund;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getDateOfCancellation() {
        return dateOfCancellation;
    }

    public void setDateOfCancellation(Date dateOfCancellation) {
        this.dateOfCancellation = dateOfCancellation;
    }

    public String getStatusOfRefund() {
        return statusOfRefund;
    }

    public void setStatusOfRefund(String statusOfRefund) {
        this.statusOfRefund = statusOfRefund;
    }
}
