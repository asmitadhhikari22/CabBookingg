package app.rent_likeme.com.rent_likeme.util;

import android.content.Context;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.rent_likeme.com.rent_likeme.R;

/**
 * Created by anto004 on 3/9/18.
 */

public class Utility {
    public static SimpleDateFormat getDateFormat(){
        return new SimpleDateFormat("EEE, MMM d, ''yy", Locale.US);
    }

    public static String convertLongToDate(long timeInMillis){
        Date date = new Date(timeInMillis);
        return getDateFormat().format(date);
    }

    public static SimpleDateFormat getCurrentDateFormat(){
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    }

    public static NumberFormat getDistanceFormat(){
        return new DecimalFormat("#.##");
    }

    public static String getFriendlyDistFormat(Context context, Double dist){
        return context.getString(R.string.format_distance, dist);
    }

    public static String formatAddress(String s){
        StringBuilder stringBuilder = new StringBuilder();
        String[] strArray = s.split("\\s");

        for(int i = 0; i < strArray.length - 1; i++){
            String sb = strArray[i].trim();
            if(sb.length() > 0) {
                if (Character.isDigit(sb.charAt(0))) {
                    stringBuilder.append(sb);
                } else {
                    sb = sb.toLowerCase();
                    char firstChar = (char) (sb.charAt(0) - 'a' + 'A');
                    String remaining = sb.substring(1, sb.length());
                    stringBuilder.append(firstChar).append(remaining);
                }
                stringBuilder.append(" ");
            }
        }
        stringBuilder.append(strArray[strArray.length - 1]);

        return stringBuilder.toString();
    }

    public static String getFormattedPrice(Context context, double price){
        return context.getString(R.string.format_price, price);
    }

    //Using Haversine formula
    //return distance in miles
    public static double calculateDistance(double lat1, double long1,
                                           double lat2, double long2){
        double R = 3958.756; //earth's radius in miles
        double φ1 = Math.toRadians(lat1);
        double φ2 = Math.toRadians(lat2);
        double Δφ = Math.toRadians(lat2-lat1);
        double Δλ = Math.toRadians(long2-long1);

        double a = Math.sin(Δφ/2) * Math.sin(Δφ/2) +
                Math.cos(φ1) * Math.cos(φ2) *
                        Math.sin(Δλ/2) * Math.sin(Δλ/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return R * c;
    }

    public static List<String> getImageNames(){
        List<String> names = new ArrayList<>();
        for(int i = 1; i <= 10; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append("disney_car_").append(i).append(".jpeg");
            names.add(sb.toString());
        }
        return names;
    }
}
