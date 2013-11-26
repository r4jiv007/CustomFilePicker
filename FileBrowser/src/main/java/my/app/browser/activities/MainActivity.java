package my.app.browser.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import my.app.browser.R;
import my.app.browser.application.MyApp;


public class MainActivity extends Activity {

    private Button button;
    private ListView listView;
    private Spinner spinner;

    private final int REQUEST_CODE = 1;
    private String STRING_IDENTIFIER = "result";
    private final int MODE_IMAGE = 0;
    private final int MODE_VIDEO = 1;
    private final int MODE_FILE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListeners();
    }

    private void initView() {
        button = (Button) findViewById(R.id.call_gallery);
        listView = (ListView) findViewById(R.id.pathList);
        spinner = (Spinner) findViewById(R.id.dropdown);
        setAdapter();
    }

    private void setAdapter() {
        String[] modes = {"IMAGE_MODE", "VIDEO_MODE", "FILE_MODE"};
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, modes);
        spinner.setAdapter(adapter);
    }

    private void initListeners() {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mode = 5;
                if(spinner.getSelectedItem().toString().equalsIgnoreCase("IMAGE_MODE")){
                    mode =0;

                }else if(spinner.getSelectedItem().toString().equalsIgnoreCase("VIDEO_MODE")){
                    mode =1;
                }else if(spinner.getSelectedItem().toString().equalsIgnoreCase("FILE_MODE")){
                    mode=2;
                }
                Intent intent = new Intent(getApplicationContext(), CustomGallery.class);
                intent.putExtra("mode", mode);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

/*
    @Override
    protected void onStop() {

        MyApp.ImagefilteredMap.clear();
        MyApp.VideofilteredMap.clear();
        MyApp.FilefilteredMap.clear();
        super.onStop();
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApp.clearListnMaps();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (REQUEST_CODE): {
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getBundleExtra("bundle");
                    ArrayList<String> paths = bundle.getStringArrayList(STRING_IDENTIFIER);
                    Toast.makeText(this, paths.size() + "", 2000).show();

                    ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.list_text, R.id.pathText, paths);
                    listView.setAdapter(arrayAdapter);
                    // TODO Update your TextView.
                }
                break;
            }
        }
    }
}