package my.app.browser.utility;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Rajiv on 8/27/13.
 */
public class DateUtility {

    public static SimpleDateFormat responseFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    public static SimpleDateFormat targetFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss a");

    public static SimpleDateFormat mDay = new SimpleDateFormat("EEE");
    public static SimpleDateFormat mTime = new SimpleDateFormat("hh:mm a");

    public static long getDateinLong(String date) {
        Date startTime = null;
        try {
            Date dt = responseFormat.parse(date);
            //Log.i("getDateinLong-inResponseFormat", date);
            String dt1 = targetFormat.format(dt);
            Log.i("getDateinLong-inTargetFormat", dt1);

            startTime = targetFormat.parse(dt1);
            System.out.println("startTime : " + startTime.getTime() + "     " + dt.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return startTime.getTime();
    }


    public static String getDate(String date1){
        setTimeZone();
        String formattedDate= null;
        try {
            Date date = responseFormat.parse(date1);
            formattedDate = targetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    public static String getDate(Date date){
        setTimeZone();
        String tdate = targetFormat.format(date);
        return tdate;
    }
    public static String getTime(String date1){
        setTimeZone();
        String formattedTime= null;
        try {
            Date date = responseFormat.parse(date1);
            formattedTime = mTime.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedTime;
    }

    public static String getWeekDay(String date1){
        setTimeZone();
        String weekDay= null;
        try {
            Date date = responseFormat.parse(date1);
            weekDay = mDay.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return weekDay;
    }

    public static String getWeekDay(Date date){
        String weekday =null;
        setTimeZone();
        weekday = mDay.format(date);
        return weekday;
    }


    private static void setTimeZone(){
        responseFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        targetFormat.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        mTime.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        mDay.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
    }

    public static String getTodaysDate(){
        String date =null;
        Date today = new Date();
        date= targetFormat.format(today);
        return date;
    }

    public static String getTodaysDay(){
        String date=null;
        Date today = new Date();
        date=mDay.format(today);
        return date;
    }
}
