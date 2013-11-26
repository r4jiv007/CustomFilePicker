package my.app.browser.utility;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by Rajiv on 10/3/13.
 */
public class FileX {


    private String mAbsolutePath;
    private Uri mFileUri;
    private Long mDate;
    private Bitmap mBitmap;
    private String mName;

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public FileX(String name,String path,Uri uri,Long date,Bitmap bitmap){
        this.mAbsolutePath=path;
        this.mFileUri=uri;
        this.mDate=date;
        this.mBitmap=bitmap;
        this.mName=name;
    }

    public String getAbsolutePath() {
        return mAbsolutePath;
    }

    public void setAbsolutePath(String mAbsolutePath) {
        this.mAbsolutePath = mAbsolutePath;
    }

    public Uri getFileUri() {
        return mFileUri;
    }

    public void setFileUri(Uri mFileUri) {
        this.mFileUri = mFileUri;
    }

    public Long getDate() {
        return mDate;
    }

    public void setDate(Long mDate) {
        this.mDate = mDate;
    }

    public void setBitmap(Bitmap bitmap){
        this.mBitmap=bitmap;
    }

    public Bitmap getBitmap(){
        return mBitmap;
    }
}
