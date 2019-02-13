package paymentmicroservice.Models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class Response {
    public List<String> options;
    public String orderId;
    public  float amount;
}
