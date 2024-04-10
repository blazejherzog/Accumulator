package com.metapack.assigments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class AccumulatorImplTest {

    private Accumulator accumulator;

    @BeforeEach
    void setUp() {
        this.accumulator = new AccumulatorImpl();
    }

    @ParameterizedTest
    @CsvSource({
            "5, 10, 15",
            "2, 3, 5",
            "-3, 7, 4",
            "0, 1, 1",
            "0, 0, 0",
            "-5, -10, -15",
            "1073741823, 1073741824, 2147483647"
    })
    void shouldAccumulateTwoValuesAndGetTotalSum(int firstValue, int secondValue, int expectedResult) {
        // when
        accumulator.accumulate(firstValue, secondValue);

        // then
        assertEquals(expectedResult, accumulator.getTotal());
    }

    @ParameterizedTest
    @MethodSource("provideExpectedResultAndValuesToAccumulate")
    void shouldAccumulateMoreThanTwoValuesAndGetTotalSum(int expectedResult, int... values) {
        //when
        accumulator.accumulate(values);

        //then
        assertEquals(expectedResult, accumulator.getTotal());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 0, -2, 15, 2147483647})
    void shouldAccumulateOneValueAndGetTotalSumEqualToGivenValue(int value) {
        // when
        accumulator.accumulate(value);

        //then
        assertEquals(value, accumulator.getTotal());
    }

    @Test
    void shouldNotResetTotalSumWhenAccumulatingMoreThanOnce() {
        //when
        accumulator.accumulate(2, 3, 7);
        accumulator.accumulate(100);

        //then
        assertEquals(112, accumulator.getTotal());
    }

    @Test
    void shouldReturnTotalSumEqualToZeroAfterReset() {
        // given
        accumulator.accumulate(5, 10);
        accumulator.accumulate(5);

        // when
        accumulator.reset();

        // then
        assertEquals(0, accumulator.getTotal());
    }

    @Test
    void shouldAccumulateAndReturnOnlyTotalSumAfterReset() {
        // given
        accumulator.accumulate(5, 10);
        accumulator.reset();

        // when
        accumulator.accumulate(5, 10);

        //then
        assertEquals(15, accumulator.getTotal());
    }

    @Test
    void shouldThrowWhenOverflowOccurred() {
        // when
        Executable action = () -> accumulator.accumulate(Integer.MAX_VALUE, 1);

        // then
        assertThrows(ArithmeticException.class, action, "Given sum exceeded the range of an integer (overflow or underflow occurred)");
    }

    @Test
    void shouldThrowWhenUnderflowOccurred() {
        // when
        Executable action = () -> accumulator.accumulate(Integer.MIN_VALUE, -1);

        // then
        assertThrows(ArithmeticException.class, action, "Given sum exceeded the range of an integer (overflow or underflow occurred)");
    }

    private static Stream<Arguments> provideExpectedResultAndValuesToAccumulate() {
        return Stream.of(
                Arguments.of(9, new int[]{3, 3, 3}),
                Arguments.of(8, new int[]{2, 2, 2, 2}),
                Arguments.of(10, new int[]{2, 2, 2, 2, 2}),
                Arguments.of(10, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}),
                Arguments.of(0, new int[]{1, -1, 1, -1, 1, -1})
        );
    }
}