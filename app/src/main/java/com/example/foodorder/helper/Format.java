package com.example.foodorder.helper;

import java.text.DecimalFormat;

public class Format {
    public static String formatCurrency(double amount) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(amount) + "đ";
    }
}
