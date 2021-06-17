package com.sonu.unittesting.util;

import org.junit.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Random;

import static com.sonu.unittesting.util.DateUtil.GET_MONTH_ERROR;
import static com.sonu.unittesting.util.DateUtil.getMonthFromNumber;
import static com.sonu.unittesting.util.DateUtil.monthNumbers;
import static com.sonu.unittesting.util.DateUtil.months;
import static org.junit.jupiter.api.Assertions.*;

public class DateUtilTest {
    public static final String TODAY_DATE = "06-2021";

    @Test
    public void testCurrentTimeStamp_returnTimeStamp() {
        assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {

                assertEquals(TODAY_DATE, DateUtil.getCurrentTimeStamp());
                System.out.println("both dates are equal");
            }
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11})
    public void getMonthNumber_returnSuccess(int monthNumber, TestInfo testInfo, TestReporter testReporter) {
        assertEquals(months[monthNumber], DateUtil.getMonthFromNumber(monthNumbers[monthNumber]));
        System.out.println("values  are equal" + monthNumber);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11})
    public void getMonthNumber_returnError(int monthNumber, TestInfo testInfo, TestReporter testReporter) {
        int random = new Random().nextInt(90) + 13;
        assertEquals(getMonthFromNumber(String.valueOf(monthNumber * random)), GET_MONTH_ERROR);
        System.out.println("values errors are  equal");
    }

}
