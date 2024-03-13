package com.tharuni.lil.jdbc;

public class NotSoSimpleCalculatorImpl implements NotSoSimpleCalculator {

    public SimpleCalculator calc;

    public NotSoSimpleCalculatorImpl(SimpleCalculator calc) {
        this.calc = calc;
    }

    @Override
    public int power(int x, int y) {
        // TODO Auto-generated method stub
        return (int) Math.pow(x, y);
    }

    @Override
    public int abs(int x) {
        return Math.abs(x);
    }

    @Override
    public double sqrt(int x) {
        // TODO Auto-generated method stub
        return Math.sqrt(x);
    }

}
