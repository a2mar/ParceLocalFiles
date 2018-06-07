package com.undisclosed123.parselocalfiles;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class BrowseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        addFakeFileList();
    }

    private void addFakeFileList() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for(int i = 0; i<3; i++) {
            View newView = inflater.inflate(R.layout.file_entry, null);
            LinearLayout innerParentLayout = findViewById(R.id.parent_f);

            innerParentLayout.addView(newView, innerParentLayout.getChildCount());
        }
    }
}
