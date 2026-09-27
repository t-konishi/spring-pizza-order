package com.example.pizzaorder.form;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class PizzaForm {

    @NotBlank(message = "ピザ名を入力してください")
    @Size(max = 100, message = "ピザ名は100文字以内で入力してください")
    private String pizzaName;

    @Size(max = 500, message = "説明は500文字以内で入力してください")
    private String description;

    @NotNull(message = "価格を入力してください")
    @DecimalMin(value = "0", message = "価格は0円以上で入力してください")
    @Digits(
            integer = 10,
            fraction = 0,
            message = "価格は整数で入力してください"
    )
    private BigDecimal price;

    @NotBlank(message = "カテゴリを選択してください")
    @Pattern(
            regexp = "STANDARD|MEAT|CHEESE",
            message = "正しいカテゴリを選択してください"
    )
    private String category;

    public String getPizzaName() {
        return pizzaName;
    }

    public void setPizzaName(String pizzaName) {
        this.pizzaName = pizzaName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}