package com.example.foodorder.validation;

import android.util.Patterns;

public class Validation {

    public static boolean isNotEmpty(String input){
        if(input.trim().isEmpty()){
            return false;
        }
        return true;
    }

    public static boolean isValidEmail(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPasswordLenght(String password){
        return password.trim().length() >= 8;
    }

    public static boolean isValidName(String name){
        String regex = "^[a-zA-Z\\s]+$";
        return name.matches(regex);
    }

    public static boolean isValidPhone(String phoneNumber){
        return Patterns.PHONE.matcher(phoneNumber).matches();
    }

    public static boolean isMatchPassword(String password, String cfPassword){
        return password.equals(cfPassword);
    }

}
