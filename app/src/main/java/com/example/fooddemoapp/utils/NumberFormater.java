package com.example.fooddemoapp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class NumberFormater {
    public String budgetIdFormat(int num) {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss_ddMMyyyy");

        sdf.setTimeZone(TimeZone.getTimeZone("GMT-5"));

        String str = String.format("%05d",num) + "_" + sdf.format(new Date());
        return str;
    }

    public String format(double num){
        return String.format( Locale.US,"%,.2f", num);
    }

    public String format(int num){
        return String.format( Locale.US,"%,.2f", (double)num);
    }
}
