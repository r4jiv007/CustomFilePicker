package my.app.browser.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import my.app.browser.R;
import my.app.browser.application.MyApp;
import my.app.browser.utility.FileUtil;
import my.app.browser.utility.FileX;
import my.app.browser.utility.ViewHolder;

public class CustomGallery extends Activity {
    private final static long HOUR = 3600;
    private final static long DAY = 86400;
    private final static long WEEK = 604800;
    private final static long MONTH = 2592000;
    private final static long YEAR = 31536000;
    final Uri thumbUri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
    final String thumb_DATA = MediaStore.Images.Thumbnails.DATA;
    final String thumb_IMAGE_ID = MediaStore.Images.Thumbnails.IMAGE_ID;
    private final int MODE_IMAGE = 0;
    private final int MODE_VIDEO = 1;
    private final int MODE_FILE = 2;
    GridView gv;
    Bitmap bitmap1[];
    private Button addButton;
    private HashMap<String, FileX> selectedView = new HashMap<String, FileX>();
    private ArrayList<String> selectedTags = new ArrayList<String>();
    private String STRING_IDENTIFIER = "result";

    private DisplayMetrics disp_mtx;
    private LinearLayout containerLayout;
    private static int mode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);
        mode = getIntent().getIntExtra("mode", 5);
        initView(mode);
        setListener();

    }

    private void returnResult(ArrayList<String> selectedPaths) {
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(STRING_IDENTIFIER, selectedPaths);
        resultIntent.putExtra("bundle", bundle);
        setResult(Activity.RESULT_OK, resultIntent);
        Toast.makeText(this, selectedPaths.size() + "", 2000).show();
        finish();
    }

    private void setListener() {

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numSelection = selectedTags.size();
                ArrayList<String> selectedFiles = new ArrayList<String>();
                for (int i = 0; i < numSelection; i++) {
                    String tag = selectedTags.get(i);
                    FileX fileX = selectedView.get(tag);
                    //Toast.makeText(getApplicationContext(),fileX.getAbsolutePath(),2000).show();
                    selectedFiles.add(fileX.getAbsolutePath());

                }
                returnResult(selectedFiles);
            }
        });


    }

    private void initView(int MODE) {

        addButton = (Button) findViewById(R.id.btn_add);
        containerLayout = (LinearLayout) findViewById(R.id.container);
        ProgressDialog pd;
        switch(MODE) {

            case MODE_IMAGE:
            {
                if(MyApp.ImagefilteredMap.size()==0){
                    new ImageThumbnailFetcher(this).execute();
                }else{
                    pd = ProgressDialog.show(this, "Browser", "Loading Thumbnails");
                    pd.setCancelable(false);
                    creatView(MyApp.ImagefilteredMap,MyApp.timeFilter,MODE_IMAGE);
                    pd.dismiss();
                }
                MyApp.Last_Mode=MODE_IMAGE;
                  break;
            }

            case MODE_VIDEO:
            {
                if(MyApp.VideofilteredMap.size()==0){
                new VideoThumbnailFetcher(this).execute();
            } else{
                    pd = ProgressDialog.show(this, "Browser", "Loading Thumbnails");
                    pd.setCancelable(false);
                    creatView(MyApp.VideofilteredMap,MyApp.timeFilter,MODE_VIDEO);
                    pd.dismiss();
                }
            }
                MyApp.Last_Mode=MODE_VIDEO;
            break;
            case MODE_FILE:{
                if(MyApp.FilefilteredMap.size()==0){
                    new NonMediaFileFetcher(this).execute();
                }else{
                    pd = ProgressDialog.show(this, "Browser", "Loading Thumbnails");
                    pd.setCancelable(false);

                    creatView(MyApp.FilefilteredMap,MyApp.timeFilter,MODE_FILE);
                    pd.dismiss();
                }

            }
            MyApp.Last_Mode=MODE_FILE;
            break;
        }
     }

    private int getSquareSize() {

        disp_mtx = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(disp_mtx);

        int screen_width = disp_mtx.widthPixels;
        int req_width = screen_width / 3;

        return req_width;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private class ImageThumbnailFetcher extends AsyncTask<Void, Void, Void> {

        private Context myContext;
        private ProgressDialog pd;
        private ArrayList<Bitmap> myThumbNails;
        private Cursor myCursor;

        public ImageThumbnailFetcher(Context context) {
            myContext = context;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = ProgressDialog.show(myContext, "Browser", "Loading Thumbnails");
            pd.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Cursor c = FileUtil.dumpImages(myContext);
            int cursorCount = c.getCount();
            Date date = new Date();
            long now = date.getTime();
            Log.i("current time", now + "");
            for (int i = 0; i < cursorCount; i++) {
                c.moveToPosition(i);
                String path = c.getString(c.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                String display_name = getFileName(path);
                Log.i("fileName", display_name);
                long timeAdded = c.getLong(c.getColumnIndex(MediaStore.Images.ImageColumns.DATE_ADDED));
                long timediff = (now - (timeAdded * 1000)) / 1000;
                Log.i("time diff", timediff + "");
                int int_ID = c.getInt(c.getColumnIndex(MediaStore.Images.ImageColumns._ID));
                Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(
                        getContentResolver(), int_ID,
                        MediaStore.Images.Thumbnails.MICRO_KIND,
                        (BitmapFactory.Options) null);

                storeImageCat(timediff, display_name, path, Uri.parse(path), timeAdded, bitmap);
            }
            c.close();
            putImagesInMap(MyApp.ImagefilteredMap);
            //Log.i("mapSizeputinmap", "" + MyApp.filteredMap.size());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pd.dismiss();
            creatView(MyApp.ImagefilteredMap, MyApp.timeFilter, MODE_IMAGE);
            super.onPostExecute(aVoid);
        }
    }


    private class VideoThumbnailFetcher extends AsyncTask<Void, Void, Void> {

        private Context myContext;
        private ProgressDialog pd;
        private ArrayList<Bitmap> myThumbNails;
        private Cursor myCursor;

        public VideoThumbnailFetcher(Context context) {
            myContext = context;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            pd = ProgressDialog.show(myContext, "Browser", "Loading Thumbnails");
            pd.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Cursor c = FileUtil.dumpVideos(myContext);
            Log.i("VideoCursorSize", c.getCount() + "");
            int cursorCount = c.getCount();
            Date date = new Date();
            long now = date.getTime();
            Log.i("current time", now + "");
            for (int i = 0; i < cursorCount; i++) {
                c.moveToPosition(i);
                String path = c.getString(c.getColumnIndex(MediaStore.Video.VideoColumns.DATA));
                String display_name = getFileName(path);
                Log.i("fileName", display_name);
                long timeAdded = c.getLong(c.getColumnIndex(MediaStore.Video.VideoColumns.DATE_ADDED));
                long timediff = (now - (timeAdded * 1000)) / 1000;
                Log.i("time diff", timediff + "");
                int int_ID = c.getInt(c.getColumnIndex(MediaStore.Video.VideoColumns._ID));
                Bitmap bitmap = MediaStore.Video.Thumbnails.getThumbnail(
                        getContentResolver(), int_ID,
                        MediaStore.Video.Thumbnails.MICRO_KIND,
                        (BitmapFactory.Options) null);

                storeVideosCat(timediff, display_name, path, Uri.parse(path), timeAdded, bitmap);
            }
            c.close();
            putVideosInMap(MyApp.VideofilteredMap);
            //Log.i("mapSizeputinmap", "" + MyApp.filteredMap.size());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pd.dismiss();
            creatView(MyApp.VideofilteredMap, MyApp.timeFilter, MODE_VIDEO);
            super.onPostExecute(aVoid);
        }
    }


    private class NonMediaFileFetcher extends AsyncTask<Void, Void, Void> {

        private Context myContext;
        private ProgressDialog pd;
        private ArrayList<Bitmap> myThumbNails;
        private Cursor myCursor;

        public NonMediaFileFetcher(Context context) {
            myContext = context;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            pd = ProgressDialog.show(myContext, "Browser", "Loading Thumbnails");
            pd.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Cursor c = FileUtil.dumpFiles(myContext);
            Log.i("FileCursorSize", c.getCount() + "");
            int cursorCount = c.getCount();
            Date date = new Date();
            long now = date.getTime();
            Log.i("current time", now + "");
            for (int i = 0; i < cursorCount; i++) {
                c.moveToPosition(i);
                String path = c.getString(c.getColumnIndex(MediaStore.Files.FileColumns.DATA));
                String display_name = getFileName(path);
                Log.i("fileName", display_name);
                long timeAdded = c.getLong(c.getColumnIndex(MediaStore.Files.FileColumns.DATE_ADDED));
                long timediff = (now - (timeAdded * 1000)) / 1000;
                Log.i("time diff", timediff + "");
                int int_ID = c.getInt(c.getColumnIndex(MediaStore.Files.FileColumns._ID));
                Bitmap bitmap = null;
                storeFileCat(timediff, display_name, path, Uri.parse(path), timeAdded, bitmap);
            }
            c.close();
            putFilesInMap(MyApp.FilefilteredMap);
            Log.i("mapSizeputinmap", "" + MyApp.FilefilteredMap.size());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pd.dismiss();
            creatView(MyApp.FilefilteredMap, MyApp.timeFilter, MODE_FILE);
            super.onPostExecute(aVoid);
        }
    }


    private void putImagesInMap(Map map) {
        if (MyApp.mLasthrImages.size() > 0) {
            map.put("In Last one Hour", MyApp.mLasthrImages);
        }
        if (MyApp.mLastDayImages.size() > 0) {
            map.put("In Last one Day", MyApp.mLastDayImages);
        }
        if (MyApp.mLastWeekImages.size() > 0) {
            map.put("In Last one Week", MyApp.mLastWeekImages);
        }
        if (MyApp.mLastMonthImages.size() > 0) {
            map.put("In Last one Month", MyApp.mLastMonthImages);
        }
        if (MyApp.mLastYearImages.size() > 0) {
            map.put("In Last one Year", MyApp.mLastYearImages);
        }
        if (MyApp.mBeforeLastYear.size() > 0) {
            map.put("Before Last Year", MyApp.mBeforeLastYear);
        }
    }
    private void putVideosInMap(Map map) {
        if (MyApp.mLasthrVideos.size() > 0) {
            map.put("In Last one Hour", MyApp.mLasthrVideos);
        }
        if (MyApp.mLastDayVideos.size() > 0) {
            map.put("In Last one Day", MyApp.mLastDayVideos);
        }
        if (MyApp.mLastWeekVideos.size() > 0) {
            map.put("In Last one Week", MyApp.mLastWeekVideos);
        }
        if (MyApp.mLastMonthVideos.size() > 0) {
            map.put("In Last one Month", MyApp.mLastMonthVideos);
        }
        if (MyApp.mLastYearVideos.size() > 0) {
            map.put("In Last one Year", MyApp.mLastYearVideos);
        }
        if (MyApp.mBeforeLastYearVideos.size() > 0) {
            map.put("Before Last Year", MyApp.mBeforeLastYearVideos);
        }
    }
    private void putFilesInMap(Map map) {
        if (MyApp.mLasthrFiles.size() > 0) {
            map.put("In Last one Hour", MyApp.mLasthrFiles);
        }
        if (MyApp.mLastDayFiles.size() > 0) {
            map.put("In Last one Day", MyApp.mLastDayFiles);
        }
        if (MyApp.mLastWeekFiles.size() > 0) {
            map.put("In Last one Week", MyApp.mLastWeekFiles);
        }
        if (MyApp.mLastMonthFiles.size() > 0) {
            map.put("In Last one Month", MyApp.mLastMonthFiles);
        }
        if (MyApp.mLastYearFiles.size() > 0) {
            map.put("In Last one Year", MyApp.mLastYearFiles);
        }
        if (MyApp.mBeforeLastFiles.size() > 0) {
            map.put("Before Last Year", MyApp.mBeforeLastFiles);
        }
    }

    private void storeImageCat(long timediff, String name, String path, Uri uri, long timeadded, Bitmap bitmap) {
        if (timediff <= HOUR) {
            MyApp.mLasthrImages.add(new FileX(name, path, uri, timeadded, bitmap));
        } else if (timediff > HOUR && timediff <= DAY) {
            MyApp.mLastDayImages.add(new FileX(name, path, uri, timeadded, bitmap));
        } else if (timediff > DAY && timediff <= WEEK) {
            MyApp.mLastWeekImages.add(new FileX(name, path, uri, timeadded, bitmap));
        } else if (timediff > WEEK && timediff <= MONTH) {
            MyApp.mLastMonthImages.add(new FileX(name, path, uri, timeadded, bitmap));
        } else if (timediff > MONTH && timediff <= YEAR) {
            MyApp.mLastYearImages.add(new FileX(name, path, uri, timeadded, bitmap));
        } else {
            MyApp.mBeforeLastYear.add(new FileX(name, path, uri, timeadded, bitmap));
        }


    }

    private void storeVideosCat(long timediff, String name, String path, Uri uri, long timeadded, Bitmap bitmap) {
        if (timediff <= HOUR) {
            MyApp.mLasthrVideos.add(new FileX(name, path, uri, timeadded, bitmap));
        } else if (timediff > HOUR && timediff <= DAY) {
            MyApp.mLastDayVideos.add(new FileX(name, path, uri, timeadded, bitmap));
        } else if (timediff > DAY && timediff <= WEEK) {
            MyApp.mLastWeekVideos.add(new FileX(name, path, uri, timeadded, bitmap));
        } else if (timediff > WEEK && timediff <= MONTH) {
            MyApp.mLastMonthVideos.add(new FileX(name, path, uri, timeadded, bitmap));
        } else if (timediff > MONTH && timediff <= YEAR) {
            MyApp.mLastYearVideos.add(new FileX(name, path, uri, timeadded, bitmap));
        } else {
            MyApp.mBeforeLastYearVideos.add(new FileX(name, path, uri, timeadded, bitmap));
        }


    }
    private void storeFileCat(long timediff, String name, String path, Uri uri, long timeadded, Bitmap bitmap) {
        if (timediff <= HOUR) {
            MyApp.mLasthrFiles.add(new FileX(name, path, uri, timeadded, bitmap));
        } else if (timediff > HOUR && timediff <= DAY) {
            MyApp.mLastDayFiles.add(new FileX(name, path, uri, timeadded, bitmap));
        } else if (timediff > DAY && timediff <= WEEK) {
            MyApp.mLastWeekFiles.add(new FileX(name, path, uri, timeadded, bitmap));
        } else if (timediff > WEEK && timediff <= MONTH) {
            MyApp.mLastMonthFiles.add(new FileX(name, path, uri, timeadded, bitmap));
        } else if (timediff > MONTH && timediff <= YEAR) {
            MyApp.mLastYearFiles.add(new FileX(name, path, uri, timeadded, bitmap));
        } else {
            MyApp.mBeforeLastFiles.add(new FileX(name, path, uri, timeadded, bitmap));
        }


    }

    private void creatView(HashMap<String, ArrayList<FileX>> myMap, String[] filterKey, int mode) {

        MyApp.thumbNailMap.clear();
        //int numCategory = myMap.size();
        int numCategory=filterKey.length;
        ArrayList<FileX> myThumbnails = null;
        int imageHW = getSquareSize();
        Log.i("mapSize", numCategory + "");
        for (int i = 0; i < numCategory; i++) {
            int numThumbnails = 0;
            String key = filterKey[i];
            if (myMap.containsKey(key)) {
                myThumbnails = myMap.get(key);
                numThumbnails = myThumbnails.size();

                View filterView = LayoutInflater.from(this).inflate(R.layout.seperator, null);
                TextView filter = (TextView) filterView.findViewById(R.id.filter);
                filter.setText(key);

                TableLayout myTable = new TableLayout(this);
                TableRow filterRow = new TableRow(this);
                filterRow.addView(filterView);
                myTable.addView(filterRow);
                containerLayout.addView(myTable);
                int j = 0;
                TableLayout rowHolder = new TableLayout(this);
                while (j < numThumbnails) {
                    TableRow row = new TableRow(this);
                    Log.i("Tag new row created", "Tag new row created");

                    for (int k = 0; k < 3 && j < numThumbnails; k++) {
                        View v = LayoutInflater.from(this).inflate(R.layout.selectable_image, null);
                        FrameLayout thumbHolder = (FrameLayout) v.findViewById(R.id.thumbHolder);
                        ImageView imageView = (ImageView) v.findViewById(R.id.thumbImage);
                        TextView titleText = (TextView) v.findViewById(R.id.titletxt);
                        CheckBox checkBox = (CheckBox) v.findViewById(R.id.itemCheckBox);
                        FileX customFileObj = myThumbnails.get(j);
                        if (mode != MODE_FILE) {
                            imageView.setImageBitmap(customFileObj.getBitmap());
                            titleText.setVisibility(View.INVISIBLE);
                        } else {
                            titleText.setVisibility(View.VISIBLE);
                            if (customFileObj.getAbsolutePath().toUpperCase().contains("PDF")) {
                                imageView.setBackgroundResource(R.drawable.pdf);
                                titleText.setText(customFileObj.getName());
                            } else {
                                imageView.setBackgroundResource(R.drawable.other);
                                titleText.setText(customFileObj.getName());
                            }
                        }
                        String tag = "" + customFileObj.getAbsolutePath().hashCode() + j + k;
                        imageView.setTag(tag);
                       // checkBox.setTag(tag);
                        MyApp.thumbNailMap.put(tag, customFileObj);
                        imageView.setMinimumHeight(imageHW - 10);
                        imageView.setMinimumWidth(imageHW - 10);
                        imageView.setMaxHeight(imageHW - 10);
                        imageView.setMaxWidth(imageHW - 10);

                        titleText.setMaxWidth(imageHW - 10);
                        titleText.setMaxLines(1);

                        setImageViewListener(imageView, checkBox, customFileObj);
                      //  setCheckBoxListener(checkBox,customFileObj);
                        row.addView(v);
                        j++;
                        //  Log.i("value of j", j + "");
                    }
                    rowHolder.addView(row);
                    // Log.i("Row added to table", "Row added to table");
                }

                containerLayout.addView(rowHolder);

            } else {
                numThumbnails = 0;
            }

        }

    }

    private String getFileName(String filePath) {
        String filename = null;
        String separator = "/";
        int pos = filePath.lastIndexOf(separator);
        filename = filePath.substring(pos + 1);
        return filename;
    }

    private void updateSelection(String tag) {
        if (selectedTags.contains(tag)) {
            selectedTags.remove(tag);
            selectedView.remove(tag);
        } else {
            selectedTags.add(tag);
            selectedView.put(tag, MyApp.thumbNailMap.get(tag));
        }
    }

    private void setImageViewListener(final ImageView imageView, final CheckBox checkBox, final FileX fileX) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(!checkBox.isChecked());
                String imageViewTag = view.getTag().toString();
                updateSelection(imageViewTag);
            }
        });
    }

    private void setCheckBoxListener( final CheckBox checkBox, final FileX fileX) {
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(!checkBox.isChecked());
                String imageViewTag = view.getTag().toString();
                updateSelection(imageViewTag);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
