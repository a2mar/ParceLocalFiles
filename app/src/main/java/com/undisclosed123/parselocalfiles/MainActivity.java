package com.undisclosed123.parselocalfiles;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.widget.Button;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
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


        readXML();



    }

    private void readXML() {
        try {

            FileInputStream fis = new FileInputStream(mOutFile);
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(fis, null);
            parser.nextTag();

            extractXML(parser);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }





    }

    private void extractXML(XmlPullParser parser) {
        TextView tv = findViewById(R.id.textview_2);
        //StringBuffer sb = new StringBuffer();
        String sTest = "";

        try {
            parser.require(XmlPullParser.START_TAG, null, "doc");

            while(parser.next() != XmlPullParser.END_TAG){
              //  sb.append(parser.getText());

            sTest = sTest + parser.getText();
            parser.nextTag();

            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //tv.setText(sb);
        tv.setText(sTest);      // Die resultierenden Zeienabstände kommen von der unsauberen  Formatierund des xml-Files, worin "xmlSerializer.text ="\n"; " verwendet wurde.
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
