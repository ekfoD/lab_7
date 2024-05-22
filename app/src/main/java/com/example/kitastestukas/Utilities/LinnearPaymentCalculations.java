package com.example.kitastestukas.Utilities;

import android.util.Log;

import com.example.kitastestukas.Models.DateInterval;
import com.example.kitastestukas.Models.Payment;
import com.example.kitastestukas.Models.UserInput;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LinnearPaymentCalculations {
    private List<Payment> payments;
    private List<Payment> tempPayments;
    private UserInput userInput;
    public LinnearPaymentCalculations(UserInput userInput) {
        payments = new ArrayList<>();
        tempPayments = new ArrayList<>();
        this.userInput = userInput;
    }
    public List<Payment> fillList(UserInput input) {
        Payment payment;                                            // sukuriam payment
        int months = input.getMonths() + (input.getYears() * 12);   // gaunam kiek menesiu
        LocalDate localDate = LocalDate.now();                      // gaunam siandienos data

        double moneySum = input.getMoneySum();
        double total;
        double interestAmount;
        double principalAmount = moneySum / months;
        double monthlyInterestRate = input.getYearlyInterestRate() / 12.0 / 100.0;

        for (int i = 1; i <= months; ++i) {
            localDate = LocalDate.now().plusMonths(i);
            interestAmount = (float)moneySum * monthlyInterestRate;
            total = interestAmount + principalAmount;

            payment = formatPayment(moneySum, localDate, interestAmount, principalAmount, total);
            payments.add(payment);

            moneySum -= principalAmount;
        }

        return payments;
    }
    public List<Payment> filterList(DateInterval filterInterval) {
        tempPayments.clear();

        for (Payment payment : payments) {
            if (!payment.getPaymentDate().isBefore(filterInterval.getDateBegin()) && !payment.getPaymentDate().isAfter(filterInterval.getDateEnd()))
                tempPayments.add(payment);
        }
        return tempPayments;
    }
    public List<Payment> addDelayToList(DateInterval delayInterval) {
        // i sita delayju pritaiko ir returnina
        tempPayments.clear();
        for (Payment payment : payments) {
            Payment newPayment = new Payment(payment.getHowMuchLeftToPay(), payment.getPaymentDate(), payment.getInterestAmount(), payment.getPrincipalAmount(), payment.getTotal());
            tempPayments.add(newPayment);
        }

        // temp
        Payment payment;

        // nuo kurio payment prasideda delay, kiek menesiu truks delay, kiek menesiu truks paskolos grazinimas
        int delayStartingPaymentIndex = 0,
                delayEndingPaymentIndex = 0,
                morgageMonths = userInput.getYears() * 12 + userInput.getMonths();

        // gaunam nuo kurio men prasideda delay ir nuo kurio baigiasi
        for (int i = 0; i < morgageMonths; ++i) {
            LocalDate paymentDate = payments.get(i).getPaymentDate();

            Log.e("checking" , paymentDate.toString() + " -> start: " + delayInterval.getDateBegin().toString() + " - end: " + delayInterval.getDateEnd().toString());

            if (paymentDate.isBefore(delayInterval.getDateBegin()) || paymentDate.isEqual(delayInterval.getDateBegin())) {
                delayStartingPaymentIndex = i + 1;
            }
            else if (paymentDate.isAfter(delayInterval.getDateEnd())) {
                delayEndingPaymentIndex = i + 1;
                break;
            }
        }

        // jei bloga ivede tiesiog grazinam kaip buvo
        if (delayEndingPaymentIndex == 0 || delayStartingPaymentIndex == 0)
            return payments;

        // nuo kurio prasideda tuos pakeisim
        for (int i = delayStartingPaymentIndex; i < delayEndingPaymentIndex; ++i) {
            //wow
            Payment originalPayment = payments.get(i);
            payment = new Payment(originalPayment.getHowMuchLeftToPay(), originalPayment.getPaymentDate(), originalPayment.getInterestAmount(), originalPayment.getPrincipalAmount(), originalPayment.getTotal());

            payment.setTotal(0);
            payment.setInterestAmount(0);
            payment.setPrincipalAmount(0);
            formatPaymentText(payment);
            tempPayments.set(i, payment);
        }

        // kurie eina po to kai pasibaigia atostogos irgi pakeisim
        for (int i = delayEndingPaymentIndex; i < morgageMonths; ++i) {
            //wow
            Payment originalPayment = payments.get(i);
            payment = new Payment(originalPayment.getHowMuchLeftToPay(), originalPayment.getPaymentDate(), originalPayment.getInterestAmount(), originalPayment.getPrincipalAmount(), originalPayment.getTotal());

            payment.setPrincipalAmount(payment.getPrincipalAmount() + payment.getPrincipalAmount() * (Math.pow(1 + (userInput.getYearlyInterestRate() / 12 / 100), delayEndingPaymentIndex - delayStartingPaymentIndex)));
            payment.setTotal(payment.getInterestAmount() + payment.getPrincipalAmount());
            formatPaymentText(payment);
            tempPayments.set(i, payment);
        }
        return tempPayments;
    }
    public List<Payment> getPayments() {
        return payments;
    }
    public Payment formatPayment(double moneySum, LocalDate date, double interestAmount, double principalAmount, double total) {
        Payment payment = new Payment(moneySum, date, interestAmount, principalAmount, total);

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        payment.setFormattedPaymentDate(date.format(dateFormat));

        payment.setFormattedHowMuchLeftToPay(String.format("%.2f", moneySum));
        payment.setFormattedInterestAmount(String.format("%.2f", interestAmount));
        payment.setFormattedPrincipalAmount(String.format("%.2f", principalAmount));
        payment.setFormattedTotal(String.format("%.2f", total));

        return payment;
    }

    public void formatPaymentText(Payment payment) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        payment.setFormattedPaymentDate(payment.getPaymentDate().format(dateFormat));

        payment.setFormattedHowMuchLeftToPay(String.format("%.2f", payment.getHowMuchLeftToPay()));
        payment.setFormattedInterestAmount(String.format("%.2f", payment.getInterestAmount()));
        payment.setFormattedPrincipalAmount(String.format("%.2f", payment.getPrincipalAmount()));
        payment.setFormattedTotal(String.format("%.2f", payment.getTotal()));
    }
}
