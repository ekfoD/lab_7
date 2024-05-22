package com.example.kitastestukas.Models;

public class UserInput {
    private double moneySum;
    private double yearlyInterestRate;
    private int years;
    private int months;
    private GraphType graphType;
    public UserInput(double moneySum, double yearlyInterestRate, int years, int months, GraphType graphType) {
        setMoneySum(moneySum);
        setYearlyInterestRate(yearlyInterestRate);
        setYears(years);
        setMonths(months);
        setGraphType(graphType);
    }

    public double getMoneySum() {
        return moneySum;
    }
    public void setMoneySum(double moneySum) {
        this.moneySum = (moneySum < 0) ? 0 : moneySum;
    }
    public double getYearlyInterestRate() {
        return yearlyInterestRate;
    }
    public void setYearlyInterestRate(double yearlyInterestRate) {
        this.yearlyInterestRate = yearlyInterestRate;
    }
    public int getYears() {
        return years;
    }
    public void setYears(int years) {
//        this.years = (years < 0) ? 0 : years;
        this.years = Math.max(years, 0);
    }
    public int getMonths() {
        return months;
    }
    public void setMonths(int months) {
        this.months = Math.max(months, 0);
    }
    public GraphType getGraphType() {
        return graphType;
    }
    public void setGraphType(GraphType graphType) {
        this.graphType = graphType;
    }
}

