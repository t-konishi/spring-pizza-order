package com.example.pizzaorder.form;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class OrderForm {

    @NotNull
    private Long pizzaId;

    @NotNull(message = "顧客を選択してください")
    private Long customerId;

    @NotNull(message = "数量を入力してください")
    @Min(value = 1, message = "数量は1以上で入力してください")
    @Max(value = 99, message = "数量は99以下で入力してください")
    private Integer quantity;

    @NotBlank(message = "支払方法を選択してください")
    @Pattern(
            regexp = "CREDIT_CARD|CASH|QR|BANK_TRANSFER",
            message = "正しい支払方法を選択してください"
    )
    private String paymentMethod;

    public OrderForm() {
    }

    public Long getPizzaId() {
        return pizzaId;
    }

    public void setPizzaId(Long pizzaId) {
        this.pizzaId = pizzaId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}