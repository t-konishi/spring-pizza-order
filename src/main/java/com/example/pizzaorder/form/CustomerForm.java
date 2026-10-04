package com.example.pizzaorder.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CustomerForm {

    @NotBlank(message = "顧客名を入力してください")
    @Size(max = 100, message = "顧客名は100文字以内で入力してください")
    private String customerName;

    @NotBlank(message = "メールアドレスを入力してください")
    @Email(message = "正しいメールアドレス形式で入力してください")
    @Size(max = 255, message = "メールアドレスは255文字以内で入力してください")
    private String email;

    @Size(max = 30, message = "電話番号は30文字以内で入力してください")
    private String phone;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}