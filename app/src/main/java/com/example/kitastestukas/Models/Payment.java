package com.example.kitastestukas.Models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.time.LocalDate;

public class Payment implements Cloneable {
    private double howMuchLeftToPay;
    private LocalDate paymentDate;
    private double interestAmount;
    private double principalAmount;
    private double total;
    private String formattedHowMuchLeftToPay;
    private String formattedPaymentDate;
    private String formattedInterestAmount;
    private String formattedPrincipalAmount;
    private String formattedTotal;

    public String getFormattedHowMuchLeftToPay() {
        return formattedHowMuchLeftToPay;
    }

    public void setFormattedHowMuchLeftToPay(String formattedHowMuchLeftToPay) {
        this.formattedHowMuchLeftToPay = formattedHowMuchLeftToPay;
    }

    public String getFormattedPaymentDate() {
        return formattedPaymentDate;
    }

    public void setFormattedPaymentDate(String formattedPaymentDate) {
        this.formattedPaymentDate = formattedPaymentDate;
    }

    public String getFormattedInterestAmount() {
        return formattedInterestAmount;
    }

    public void setFormattedInterestAmount(String formattedInterestAmount) {
        this.formattedInterestAmount = formattedInterestAmount;
    }

    public String getFormattedPrincipalAmount() {
        return formattedPrincipalAmount;
    }

    public void setFormattedPrincipalAmount(String formattedPrincipalAmount) {
        this.formattedPrincipalAmount = formattedPrincipalAmount;
    }

    public String getFormattedTotal() {
        return formattedTotal;
    }

    public void setFormattedTotal(String formattedTotal) {
        this.formattedTotal = formattedTotal;
    }

    public Payment(double howMuchLeftToPay, LocalDate paymentDate, double interestAmount, double principalAmount, double total) {
        this.howMuchLeftToPay = howMuchLeftToPay;
        this.paymentDate = paymentDate;
        this.interestAmount = interestAmount;
        this.principalAmount = principalAmount;
        this.total = total;
    }

    // Copy constructor
    public Payment(Payment other) {
        this.paymentDate = other.paymentDate;
        this.principalAmount = other.principalAmount;
        this.interestAmount = other.interestAmount;
        this.total = other.total;
        this.formattedHowMuchLeftToPay = other.formattedHowMuchLeftToPay;
        this.formattedPaymentDate = other.formattedPaymentDate;
        this.formattedTotal = other.formattedTotal;
        this.formattedInterestAmount = other.formattedInterestAmount;
        this.formattedPrincipalAmount = other.formattedPrincipalAmount;
        this.howMuchLeftToPay = other.howMuchLeftToPay;
    }

    // Optionally, a clone method
    @Override
    public Payment clone() {
        try {
            return (Payment) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clone not supported", e);
        }
    }

    public double getHowMuchLeftToPay() {
        return howMuchLeftToPay;
    }

    public void setHowMuchLeftToPay(double howMuchLeftToPay) {
        this.howMuchLeftToPay = howMuchLeftToPay;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(double interestAmount) {
        this.interestAmount = interestAmount;
    }

    public double getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(double principalAmount) {
        this.principalAmount = principalAmount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Payment{ howMuchLeftToPay=" + formattedHowMuchLeftToPay + ", date=" + formattedPaymentDate +
                ", interestAmount=" + formattedInterestAmount + ", principalAmount=" + formattedPrincipalAmount + ", total=" + formattedTotal + " }\n";
    }
}
