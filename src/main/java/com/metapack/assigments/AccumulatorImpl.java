package com.metapack.assigments;

import static java.util.Arrays.stream;

public class AccumulatorImpl implements Accumulator {

    private static final int DEFAULT_TOTAL_SUM = 0;

    private int currentTotalSum;

    public AccumulatorImpl() {
        this.currentTotalSum = 0;
    }

    @Override
    public int accumulate(int... values) {
        long sum = (long) currentTotalSum + stream(values).asLongStream().sum();
        validate(sum);
        currentTotalSum = (int) sum;
        return currentTotalSum;
    }

    @Override
    public int getTotal() {
        return currentTotalSum;
    }

    @Override
    public void reset() {
        currentTotalSum = DEFAULT_TOTAL_SUM;
    }

    private void validate(long sum) {
        if (sum > Integer.MAX_VALUE || sum < Integer.MIN_VALUE) {
            throw new ArithmeticException("Given sum exceeded the range of an integer (overflow or underflow occurred)");
        }
    }
}
