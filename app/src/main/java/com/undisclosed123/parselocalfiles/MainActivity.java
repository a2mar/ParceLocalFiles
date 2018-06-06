package com.undisclosed123.parselocalfiles;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.widget.Button;
import android.widget.TextView;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static String sOutPut = "WritMe.xml";
    private File mOutFile;
    private ArrayList<String> mListWords;
    private int[] priority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mOutFile = new File(getFileStreamPath(sOutPut).getPath());
        mOutFile = new File(getFilesDir(),sOutPut);     //same effect, but I UNDERSTAND IT :)

        TextView tv = findViewById(R.id.title_1);
        tv.setText("this text was changed");

        createWords();
        createPriority();
        writeXML();


    }

    private void createPriority() {
        priority = new int[11];

        for(int i = 0; i<= 10; i++){
            priority[i]= i*2;
        }

    }

    private void writeXML() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(mOutFile);
            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();

            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("UTF-8", true);
            xmlSerializer.text("\n");
            xmlSerializer.startTag(null, "doc");

            String vocabulary = "vocabulary";
            String word = "word";
            String prio = "priority";
            String currentWord;

            try {
                for (int i = 0; i <= 10; i++) {
                    currentWord = mListWords.get(i);

                    xmlSerializer.text("\n");
                    xmlSerializer.text("\n    ");
                    xmlSerializer.startTag(null, vocabulary);
                    xmlSerializer.attribute(null, word, currentWord);

                    xmlSerializer.text("\n        ");
                    xmlSerializer.startTag(null, prio);
                    xmlSerializer.text(String.valueOf(priority[i]));
                    xmlSerializer.endTag(null, prio);
                    xmlSerializer.text("\n    ");

                    xmlSerializer.endTag(null, vocabulary);

                }
            }
            catch (IOException e) {
                e.printStackTrace();

            }

            xmlSerializer.text("\n");

            xmlSerializer.endTag(null, "doc");
            xmlSerializer.endDocument();
            xmlSerializer.flush();
            String dataWrite = writer.toString();
            fileOutputStream.write(dataWrite.getBytes());
            fileOutputStream.close();

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }




    }

    private void createWords() {

        mListWords = new ArrayList<String>(11);

        for(int i = 0; i<=10; i++){
            mListWords.add("word"+String.valueOf(i));
        }
    }
}
