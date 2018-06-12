package com.undisclosed123.parselocalfiles;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.widget.TextView;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
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
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;



public class ParseActivity extends AppCompatActivity {

    private static String sOutPut = "WritMe.xml";
    private File mOutFile;
    private ArrayList<String> mListWords;
    private int[] priority;
    private String passedPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parse);
        Intent intent = getIntent();
        passedPath = intent.getStringExtra("path");

        //mOutFile = new File(getFileStreamPath(sOutPut).getPath());
        mOutFile = new File(getFilesDir(),sOutPut);     //same effect, but I UNDERSTAND IT :)

        TextView tv = findViewById(R.id.title_1);
        tv.setText("this text was changed");

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

    private void writeXML() {

        try {
            File userText = new File(passedPath);
            Scanner scn = new Scanner(new FileInputStream(userText), "UTF-8");
            String separate = ";";

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            //root element
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Collection");
            doc.appendChild(rootElement);

            //List element
            Element listElement = doc.createElement("List");
            rootElement.appendChild(listElement);

            //set Attributes to listElement
            Attr attr = doc.createAttribute("name");
            String fullLine = scn.nextLine();
            int index = fullLine.indexOf(separate);
            String nameOfList = fullLine.substring(0,index);
            attr.setValue(nameOfList);
            listElement.setAttributeNode(attr);

            //inside while loop
            while(scn.hasNext()) {
                String line = scn.nextLine();
                String[] parts = line.split(separate);

                //Scanner-ZeichenTest (mit ZeichenText.txt)
//                TextView tv = findViewById(R.id.title_1);
//                tv.setText(parts[0]+parts[1]);

                //vocabulary element
                Element vocElement = doc.createElement("vocabulary");
                listElement.appendChild(vocElement);

                //native Word Element
                Element natWord = doc.createElement("native");
                natWord.appendChild(doc.createTextNode(parts[0]));
                vocElement.appendChild(natWord);

                //foreign Word Element
                Element forWord = doc.createElement("foreign");
                forWord.appendChild(doc.createTextNode(parts[1]));
                vocElement.appendChild(forWord);

                //Priority Element
                Element priorityElement = doc.createElement("priority");
                priorityElement.appendChild(doc.createTextNode(parts[2]));
                vocElement.appendChild(priorityElement);

                //learn status Element
                Element learnStatus = doc.createElement("learningStat");
                learnStatus.appendChild(doc.createTextNode("0"));
                vocElement.appendChild(learnStatus);
            }
            //write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(mOutFile);

            transformer.transform(source, result);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
