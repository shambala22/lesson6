package ru.md.ifmo.lesson6;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import ru.md.ifmo.lesson6.ItemMaster;
import ru.md.ifmo.lesson6.MyContentProvider;

/**
 * Created by 107476 on 05.01.2015.
 */
public class Updater extends IntentService {
    private int id;
    private String title;
    private Uri uri;

    public static boolean running = false;

    public Updater() {
        super("Updater");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String url;
        running = true;
        id = intent.getIntExtra("id", -1);
        title = intent.getStringExtra("title");
        if (id==-1) {
            url = intent.getStringExtra("url");
            String escapedUrl = DatabaseUtils.sqlEscapeString(url);
            Cursor cursor = getContentResolver().query(MyContentProvider.FEED_CONTENT_URI, null, "url="+escapedUrl, null, null);
            if (cursor.getCount() != 0) {
                Toast.makeText(getBaseContext(),"This channel already exists", Toast.LENGTH_SHORT).show();
                return;
            }
            ContentValues values = new ContentValues();
            values.put("title", title);
            values.put("url", url);

            uri = getContentResolver().insert(MyContentProvider.FEED_CONTENT_URI, values);
            id = Integer.parseInt(uri.getLastPathSegment());

        } else {
            uri = Uri.withAppendedPath(MyContentProvider.FEED_CONTENT_URI, "" + id);
            Cursor cursor = getContentResolver().query(uri, new String[]{"title", "url"}, null, null, null);
            cursor.moveToFirst();
            title = cursor.getString(cursor.getColumnIndex("title"));
            url = cursor.getString(cursor.getColumnIndex("url"));

            cursor.close();
        }

        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();

            ItemXMLHandler myXMLHandler = new ItemXMLHandler();
            xmlReader.setContentHandler(myXMLHandler);

            StringBuilder builder = new StringBuilder();
            URLConnection connection = new URL(url).openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine())!=null) {
                builder.append(line+"\n");
            }
            reader.close();
            InputSource inStream = new InputSource(new StringReader(builder.toString()));
            xmlReader.parse(inStream);
        }  catch (Exception e) {
            Toast toastt = Toast.makeText(getApplicationContext(),"Oops", Toast.LENGTH_SHORT);
            toastt.show();
        }
    }

    @Override
    public void onDestroy() {
        running = false;
        super.onDestroy();
    }

    public class ItemXMLHandler extends DefaultHandler {
        boolean currentElement = false;
        String currentValue = "";
        ItemMaster item = null;
        boolean openItem = false;
        ContentValues itemsList;

        @Override
        public void startDocument() {
            Log.wtf("aaa", "1f1fjf");
            getContentResolver().delete(MyContentProvider.POST_CONTENT_URI, MyContentProvider.FEED_ID_FIELD + "=" + id, null);
         }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            currentElement = true;
            currentValue = "";
            if (localName.equals("item")) {
                itemsList = new ContentValues();
                item = new ItemMaster();
                openItem = true;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws  SAXException {
            currentElement = false;
            if (localName.equalsIgnoreCase("item")) {
                openItem = false;
                itemsList.put("feed_id", id);
                getContentResolver().insert(MyContentProvider.POST_CONTENT_URI, itemsList);
            }
            else if (openItem) {

                if  (localName.equalsIgnoreCase("title")) {
                    Spanned text = Html.fromHtml(currentValue);
                    itemsList.put("title", text.toString());
                }
                else if (localName.equalsIgnoreCase("description")) {
                    Spanned text = Html.fromHtml(currentValue);
                    itemsList.put("description", text.toString());
                }

                else if(localName.equalsIgnoreCase("link"))
                    itemsList.put("url", currentValue);


            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (currentElement) {
                currentValue = currentValue + new String(ch, start, length);
            }
        }
    }
}
