package my.app.browser.utility;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.util.Date;

/**
 * Created by Rajiv on 9/8/13.
 */
public class FileUtil {



    public static Cursor dumpVideos(Context context) {
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Video.VideoColumns._ID,MediaStore.Video.VideoColumns.DISPLAY_NAME, MediaStore.Video.VideoColumns.DATA,MediaStore.Video.VideoColumns.DATE_ADDED };
        Cursor c = context.getContentResolver().query(uri, projection, null, null, null);
        return c;
    }


    public static Cursor dumpFiles(Context context){
        ContentResolver cr = context.getContentResolver();
        @SuppressLint("NewApi") Uri uri = MediaStore.Files.getContentUri("external");

        String[] projection = {MediaStore.Files.FileColumns._ID,MediaStore.Files.FileColumns.DISPLAY_NAME, MediaStore.Files.FileColumns.DATA,MediaStore.Files.FileColumns.DATE_ADDED};
        String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf");
        String[] selectionArgsPdf = new String[]{ mimeType };
        String sortOrder = null; // unordered
        Cursor c = cr.query(uri, projection, selectionMimeType, selectionArgsPdf, sortOrder);
        int vidsCount = 0;
        Log.d("VIDEO", "Total count of FILES: " + vidsCount);
        return c;
    }

    public static Cursor dumpImages(Context context) {
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns._ID,MediaStore.Images.ImageColumns.DISPLAY_NAME, MediaStore.Images.ImageColumns.DATA,MediaStore.Images.ImageColumns.DATE_ADDED};
        Cursor c = context.getContentResolver().query(uri, projection, null, null, MediaStore.Images.ImageColumns.DATE_ADDED + " DESC");
        Log.d("IMAGE", "Total count of Images: " + c.getCount());
        return c;
    }


    private static Bitmap getThumbnail(int id, Uri thumbUri,String [] mProjection,Context context) {
        Uri uri = thumbUri;
        String[] projection = mProjection;
        String selection = projection[1] + "=" + id;
        Cursor thumbCursor = context.getContentResolver().query(uri, projection, selection, null, null);
        Bitmap thumbBitmap = null;
        if (thumbCursor.moveToFirst()) {
            int thCulumnIndex = thumbCursor.getColumnIndex(projection[0]);

            String thumbPath = thumbCursor.getString(thCulumnIndex);

            thumbBitmap = BitmapFactory.decodeFile(thumbPath);

        }
        return thumbBitmap;
    }

}
