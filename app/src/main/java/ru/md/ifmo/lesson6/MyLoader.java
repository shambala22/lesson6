package ru.md.ifmo.lesson6;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import ru.md.ifmo.lesson6.ItemMaster;
import ru.md.ifmo.lesson6.MyContentProvider;

/**
 * Created by 107476 on 05.01.2015.
 */
public class MyLoader extends AsyncTaskLoader<ArrayList<ItemMaster>> {

    private String selection;
    private String[] projection = {"id", "title", "description", "url"};

    public MyLoader(Context context, int id) {
        super(context);
        selection = MyContentProvider.FEED_ID_FIELD + "=" + id;
    }


    @Override
    public ArrayList<ItemMaster> loadInBackground() {
        ArrayList<ItemMaster> items = new ArrayList<ItemMaster>();

        Cursor cursor = getContext().getContentResolver().query(MyContentProvider.POST_CONTENT_URI, projection, selection, null, null);
        cursor.moveToFirst();
        if (cursor.isAfterLast()) {

        }
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String description = cursor.getString(2);
            String url = cursor.getString(3);

            items.add(new ItemMaster(id,title, description, url, ""));
            cursor.moveToNext();
        }
        cursor.close();
        return items;
    }
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }
}
