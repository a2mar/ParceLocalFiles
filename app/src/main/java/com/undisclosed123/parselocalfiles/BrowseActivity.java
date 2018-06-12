package com.undisclosed123.parselocalfiles;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
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
    private String firstPath;
    private static int slashes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        Intent intent = getIntent();

        if(intent.hasExtra("newPath")) {
            String pathDown = intent.getStringExtra("newPath");
            TextView tv_3 = findViewById(R.id.path_list);
            tv_3.setText(pathDown);
            file = new File(pathDown);
            firstPath = file.getAbsolutePath();

            TextView tv1 = findViewById(R.id.title_1);
            if(pathsLeft() == slashes-1) {
                tv1.setText("Local Storage");
            }
            else{
                int index = firstPath.lastIndexOf("/");
                String topTitle = firstPath.substring(index+1);
                tv1.setText(topTitle);
            }
        }
        else {

            //file = new File("/1/2/3/4/5/6/7/8/9");
            //file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            file = Environment.getExternalStorageDirectory();
            TextView tv_early = findViewById(R.id.path_list2);
            firstPath = file.getAbsolutePath();
            tv_early.setText(firstPath);

            TextView tv1 = findViewById(R.id.title_1);
            tv1.setText("Local Storage");

            slashes = pathsLeft()+1;

            testReadable();
            testPermission();
        }




//        while(file.getParent()!= null){
//            file = file.getParentFile();
//        }

        //file = new File("/storage");

        showFiles();

//        String path = file.getAbsolutePath();
//        tv = findViewById(R.id.path_test);
//        tv.setText(path);
        
    }

    private void testPermission() {
        TextView tv2 = findViewById(R.id.path_list);
        if (ContextCompat.checkSelfPermission(BrowseActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            tv2.setText("permission Granted");
        }
        else{
            tv2.setText("permission NOT Granted");
        }
    }

    private void testReadable() {
        tv = findViewById(R.id.path_test);

        if(file.canRead()){
            tv.setText("canRead() true");
        }
        else{
            tv.setText("canRead() false");
        }

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

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for(File mFile:currentDir){
            String fullPath = mFile.getAbsolutePath();
            if(mFile.isDirectory()|| fullPath.contains(".csv")|| fullPath.contains(".txt")|| fullPath.contains(".text")) {
                TextView newView = (TextView) inflater.inflate(R.layout.file_entry, null);

                String entry = fullPath.substring(firstPath.length() + 1);
                newView.setText(entry);
                LinearLayout innerParentLayout = findViewById(R.id.parent_f);
                innerParentLayout.addView(newView, innerParentLayout.getChildCount());
            }
        }

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

        if(pathsLeft()>= slashes) {
            int index = firstPath.lastIndexOf("/");
            String parentPath = firstPath.substring(0, index);

            Intent intent = new Intent(BrowseActivity.this, BrowseActivity.class);

            intent.putExtra("newPath", parentPath);
            startActivity(intent);
        }
        else{
            Toast toast = new Toast(this);
            toast.makeText(this,"No higher directory available.", Toast.LENGTH_LONG).show();
        }
    }

    private int pathsLeft() {
        String tSubject = firstPath;
        String subString;
        int counter = 0;
        while(tSubject.contains("/")) {
            int index = tSubject.lastIndexOf("/");
            subString = tSubject.substring(0, index);
            tSubject = subString;
            counter++;
        }
            return counter;
    }

    public void enterDirectory(View view) {
        TextView tv_1 = (TextView) view;
        final String temp = firstPath+"/"+(String)tv_1.getText();

        File mFile = new File(temp);

        if(mFile.isDirectory()) {
            Intent intent = new Intent(BrowseActivity.this, BrowseActivity.class);

            intent.putExtra("newPath", temp);
            startActivity(intent);
        }
        else{

                //TODO: promptMethod() which leads to ParseActivity
            AlertDialog.Builder builder = new AlertDialog.Builder(BrowseActivity.this);
            builder.setMessage("Do you want to parse this file?").setTitle("File Selected");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(BrowseActivity.this, ParseActivity.class);
                    intent.putExtra("path", temp);
                    startActivity(intent);
                }
            })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();



        }
    }
}
