package com.st.newtest;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestUtil {
    @Test
    public void test1() throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String a = "2020-8-31 19:12:00";
        String b = "2020-8-30 19:12:00";

        Date a1 = df.parse(a);
        Date b1 = df.parse(b);

        System.out.println(df.format(a1));

        System.out.println(a1);
        System.out.println(a1.getTime());
        System.out.println(b1.getTime());
        System.out.println(a1.getTime() - b1.getTime());
        System.out.println((a1.getTime() - b1.getTime()) / (1 * 1000) + "秒");
        System.out.println(((a1.getTime() - b1.getTime()) / (60 * 1000)) % 60 + "秒");
    }
}
