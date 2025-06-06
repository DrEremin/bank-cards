package com.example.bankcards.util;

public class CardNumberMasker {

    public static String maskNumber(String number) {
        return "**** **** **** " + number.substring(12);
    }
}
