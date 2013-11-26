package my.app.browser.application;

import android.app.Application;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import my.app.browser.utility.FileX;
import my.app.browser.utility.ViewHolder;

/**
 * Created by Rajiv on 10/12/13.
 */
public class MyApp extends Application {

    public static final String []timeFilter={"In Last one Hour","In Last one Day","In Last one Week","In Last one Month","In Last one Year","Before Last Year"};

    /*
       for images only
     */
    public static ArrayList<FileX> mLasthrImages = new ArrayList<FileX>();
    public static ArrayList<FileX> mLastDayImages = new ArrayList<FileX>();
    public static ArrayList<FileX> mLastWeekImages = new ArrayList<FileX>();
    public static ArrayList<FileX> mLastMonthImages = new ArrayList<FileX>();
    public static ArrayList<FileX> mLastYearImages = new ArrayList<FileX>();
    public static ArrayList<FileX> mBeforeLastYear = new ArrayList<FileX>();

/*
        for videos only
 */
    public static ArrayList<FileX> mLasthrVideos = new ArrayList<FileX>();
    public static ArrayList<FileX> mLastDayVideos = new ArrayList<FileX>();
    public static ArrayList<FileX> mLastWeekVideos = new ArrayList<FileX>();
    public static ArrayList<FileX> mLastMonthVideos = new ArrayList<FileX>();
    public static ArrayList<FileX> mLastYearVideos = new ArrayList<FileX>();
    public static ArrayList<FileX> mBeforeLastYearVideos = new ArrayList<FileX>();

    /*
      for Non-Media Files only
     */

    public static ArrayList<FileX> mLasthrFiles = new ArrayList<FileX>();
    public static ArrayList<FileX> mLastDayFiles = new ArrayList<FileX>();
    public static ArrayList<FileX> mLastWeekFiles = new ArrayList<FileX>();
    public static ArrayList<FileX> mLastMonthFiles = new ArrayList<FileX>();
    public static ArrayList<FileX> mLastYearFiles = new ArrayList<FileX>();
    public static ArrayList<FileX> mBeforeLastFiles = new ArrayList<FileX>();

    public static Map<String, FileX> thumbNailMap = new HashMap<String, FileX>();

    public static ArrayList<Bitmap> thumbNails = new ArrayList<Bitmap>();
    public static HashMap<String, ArrayList<FileX>> ImagefilteredMap = new HashMap<String, ArrayList<FileX>>();
    public static HashMap<String, ArrayList<FileX>> VideofilteredMap = new HashMap<String, ArrayList<FileX>>();
    public static HashMap<String, ArrayList<FileX>> FilefilteredMap = new HashMap<String, ArrayList<FileX>>();
    public static ArrayList<ViewHolder> AllSelectables = new ArrayList<ViewHolder>();

    public static int Last_Mode;

    @Override
    public void onCreate() {
        super.onCreate();
    }



    public static void clearListnMaps(){
        mLastDayImages.clear();
        mLasthrImages.clear();
        mBeforeLastYear.clear();
        mLastWeekImages.clear();
        mLastMonthImages.clear();
        mLastYearImages.clear();

        mLastYearFiles.clear();
        mLastMonthFiles.clear();
        mLastWeekFiles.clear();
        mLastDayFiles.clear();
        mBeforeLastFiles.clear();
        mLasthrFiles.clear();

        mLastDayVideos.clear();
        mLasthrVideos.clear();
        mLastMonthVideos.clear();
        mLastWeekVideos.clear();
        mLastYearVideos.clear();
        mBeforeLastYearVideos.clear();

        thumbNailMap.clear();
        ImagefilteredMap.clear();
        VideofilteredMap.clear();
        FilefilteredMap.clear();
    }
}
