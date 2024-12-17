package com.web_project.zayavki.models;


import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentRequest {

    @JsonProperty("amount")
    private Amount amount;

    @JsonProperty("confirmation")
    private Confirmation confirmation;

    @JsonProperty("capture")
    private boolean capture;

    @JsonProperty("description")
    private String description;

    // Конструктор по умолчанию
    public PaymentRequest() {}

    // Конструкторы, геттеры и сеттеры

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public Confirmation getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(Confirmation confirmation) {
        this.confirmation = confirmation;
    }

    public boolean isCapture() {
        return capture;
    }

    public void setCapture(boolean capture) {
        this.capture = capture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static class Amount {
        @JsonProperty("value")
        private String value;

        @JsonProperty("currency")
        private String currency;

        public Amount() {}

        public Amount(String value, String currency) {
            this.value = value;
            this.currency = currency;
        }

        // Геттеры и сеттеры

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }

    public static class Confirmation {
        @JsonProperty("type")
        private String type;

        public Confirmation() {}

        public Confirmation(String type) {
            this.type = type;
        }

        // Геттеры и сеттеры

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}



