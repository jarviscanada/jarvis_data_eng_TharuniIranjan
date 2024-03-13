package com.tharuni.lil.jdbc;

public class SimpleCalculatorImpl implements SimpleCalculator {

    @Override
    public int add(int x, int y) {
        // TODO Auto-generated method stub
        return x+y;
    }

    @Override
    public int subtract(int x, int y) {
        // TODO Auto-generated method stub
        return x-y;
    }

    @Override
    public int multiply(int x, int y) {
        // TODO Auto-generated method stub
        return x*y;
    }

    @Override
    public double divide(int x, int y) {
        // TODO Auto-generated method stub
        double answer = 0.0;
        try {
            answer = (double)x/y;
        } catch (ArithmeticException e) {
            System.out.println("Failed to perform calculation");
        }
        return answer;
    }


    @Override
    public int power(int x, int y) {
        // TODO Auto-generated method stub
        return (int) Math.pow(x, y);
    }

}