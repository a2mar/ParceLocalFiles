package com.undisclosed123.parselocalfiles;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;



public class BrowseActivity extends AppCompatActivity {

    private String path;
    private TextView tv;
    private File file;
    private boolean hasParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        //addFakeFileList();

        file = Environment.getExternalStorageDirectory();
       // hasParent = true;   //makeshift statement, if statement needed
        while (minTwoSlash()){
            file = file.getParentFile();
            minTwoSlash();
        }




//        while(file.getParent()!= null){
//            file = file.getParentFile();
//        }

        //file = new File("/storage");

        showFiles();

        String path = file.getAbsolutePath();
        tv = findViewById(R.id.path_test);
        tv.setText(path);
        
    }

    private boolean minTwoSlash() {
        int count = 0;
        String filePath = file.getAbsolutePath();
        int index = filePath.indexOf("/");
        count++;

        while(index != -1) {
            filePath = filePath.substring(index + 1);
            index = filePath.indexOf("/");
            count++;
        }
        if(count >= 3){
            return true;
        }
        else{
            return false;
        }
    }

    private void showFiles() {
        File[] currentDir = file.listFiles();

        TextView tv2 = findViewById(R.id.path_list);
        String pathes = "";
        pathes = file.getAbsolutePath();
        tv2.setText(pathes);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        for(File mFile:currentDir){
//            TextView newView = (TextView) inflater.inflate(R.layout.file_entry, null);
//
//            String entry = mFile.getAbsolutePath();
//            newView.setText(entry);
//            LinearLayout innerParentLayout = findViewById(R.id.parent_f);
//            innerParentLayout.addView(newView, innerParentLayout.getChildCount());
//        }

    }

    private void addFakeFileList() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for(int i = 0; i<20; i++) {
            View newView = inflater.inflate(R.layout.file_entry, null);
            LinearLayout innerParentLayout = findViewById(R.id.parent_f);

            innerParentLayout.addView(newView, innerParentLayout.getChildCount());
        }
    }

    public void getParentDir(View view) {
//        int ind = path.lastIndexOf("/");
//        String path2 = path.substring(0, ind);

       if (!file.getAbsolutePath().equals("/")) {
            File file2 = file.getParentFile();

           tv.setText(file2.getAbsolutePath());
            file = file2;
       }
       else{
           Toast hint = new Toast(this);

           hint.makeText(this, "no parent Directory", Toast.LENGTH_LONG).show();
       }
    }

    public void getFileList(View view) {
        File file_X = new File("/storage");

        File[] fileList =  file_X.listFiles();
        String allFiles = "";

        for(File mFile:fileList){
            allFiles = allFiles+mFile.getAbsolutePath()+"\n";
        }

        //int debug = fileList.length;

        TextView tv2 = findViewById(R.id.path_list);
        tv2.setText(allFiles);
        String testString = (String) tv2.getText();
        tv2.setText(testString+testString);
        //tv2.setText(String.valueOf(debug));

    }

    public void goParentFile(View view) {
    }
}
