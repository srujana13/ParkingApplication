package com.ncsu.csc540.parking.utils;

import java.util.Calendar;

public class AppUtils {

    public static String generatePermitID(String zone){

        StringBuffer permitId = new StringBuffer();
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        permitId.append(year.substring(2,4));
        permitId.append(zone);
        permitId.append(getAlphaNumericString(8-permitId.length()));

        return permitId.toString();
    }
    static String getAlphaNumericString(int n)
    {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
}
