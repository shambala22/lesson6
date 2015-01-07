package ru.md.ifmo.lesson6;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

/**
 * Created by 107476 on 05.01.2015.
 */
public class FeedLoader extends AsyncTaskLoader<ArrayList<FeedMaster>> {
    public FeedLoader(Context context) {
        super(context);
    }

    @Override
    public ArrayList<FeedMaster> loadInBackground() {
        ArrayList<FeedMaster> list= new ArrayList<FeedMaster>();
        Cursor cursor = getContext().getContentResolver().query(MyContentProvider.FEED_CONTENT_URI, null, null ,null ,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String url = cursor.getString(2);
            list.add(new FeedMaster(id,title,url));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
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
