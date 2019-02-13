package paymentmicroservice.Models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CodSupport {
    @Id
    private String ProductId;
    private String ProductName;
    public  CodSupport(){}
    public CodSupport(String productId, String productName) {
        ProductId = productId;
        ProductName = productName;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }
}
