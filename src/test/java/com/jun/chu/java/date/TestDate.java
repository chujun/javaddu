package com.jun.chu.java.date;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * @author jun.chu
 * @date 2019-01-09 14:36
 */
public class TestDate {

    @Test
    public void test235959() {
        System.out.println(setDateForLastSecond235959(new Date()));
        Date date = new Date(System.currentTimeMillis() - 86400000L * 3);
        System.out.println(date);
        System.out.println(setDateForLastSecond235959(date));
    }

    @Test
    public void test(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -30);
        Date thirtyDaysAgo = calendar.getTime();
        System.out.println(thirtyDaysAgo);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(new Date());
        calendar2.add(Calendar.DAY_OF_MONTH, -30);
        Date thirtyDaysAgo2 = calendar2.getTime();
        System.out.println(thirtyDaysAgo2);

        System.out.println(String.format("%sR", "2019031909455188415306"));
    }

    private Date setDateForLastSecond235959(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }
}
