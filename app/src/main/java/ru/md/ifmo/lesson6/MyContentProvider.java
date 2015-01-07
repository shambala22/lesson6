package ru.md.ifmo.lesson6;

/**
 * Created by 107476 on 23.12.2014.
 */
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;


public class MyContentProvider extends ContentProvider {


    static final String DB_NAME = "mydb";
    static final int DB_VERSION = 3;

    private static final int FEEDS = 0;
    private static final int FEED_ID = 1;
    private static final int POSTS = 2;


    static final String FEEDS_TABLE = "feeds";
    static final String POSTS_TABLE = "posts";


    static final String FIELD_ID ="id";
    static final String FIELD_TITLE = "title";
    static final String FIELD_DESCRIPTION = "description";
    static final String FIELD_URL = "url";
    static final String FEED_ID_FIELD = "feed_id";

    static final String AUTHORITY = "ru.ifmo.md.lesson6";

    static final String FEEDS_PATH = "feeds";
    static final String POSTS_PATH = "posts";


    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        matcher.addURI(AUTHORITY, FEEDS_PATH, FEEDS);
        matcher.addURI(AUTHORITY, POSTS_PATH, POSTS);
         matcher.addURI(AUTHORITY, FEEDS_PATH + "/#", FEED_ID);
    }


    public static final Uri FEED_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + FEEDS_PATH);

    public static final Uri POST_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + POSTS_PATH);

    static final String FEEDS_TABLE_CREATE = "create table " + FEEDS_TABLE + "("
            + FIELD_ID + " integer primary key autoincrement, "
            + FIELD_TITLE + " text, " + FIELD_URL + " text" + ");";

    static final String POSTS_TABLE_CREATE = "create table " + POSTS_TABLE + "("
            + FIELD_ID + " integer primary key autoincrement, "
            + FIELD_TITLE + " text, " + FIELD_DESCRIPTION + " text, " + FIELD_URL + " text, "
            + FEED_ID_FIELD + " integer" + ");";





    DBHelper dbHelper;
    SQLiteDatabase db;

    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch (matcher.match(uri)) {
            case FEEDS:
                queryBuilder.setTables(FEEDS_TABLE);
                break;
           case FEED_ID:
                queryBuilder.setTables(FEEDS_TABLE);
                queryBuilder.appendWhere("id=" + uri.getLastPathSegment());
                break;
            case POSTS:
                queryBuilder.setTables(POSTS_TABLE);

                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),
                uri);
        return cursor;
    }

    public Uri insert(Uri uri, ContentValues values) {
        db = dbHelper.getWritableDatabase();
        long rowID;
        switch (matcher.match(uri)) {
            case FEEDS:
                rowID = db.insert(FEEDS_TABLE, null, values);
                break;
            case POSTS:
                rowID = db.insert(POSTS_TABLE, null, values);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        Uri resultUri = Uri.withAppendedPath(uri, ""+rowID);
        getContext().getContentResolver().notifyChange(uri, null);
        return resultUri;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        db = dbHelper.getWritableDatabase();
        int deleted = 0;
        switch (matcher.match(uri)) {
            case FEED_ID:
                String id = uri.getLastPathSegment();
                deleted = db.delete(FEEDS_TABLE, FIELD_ID + "=" + id, null);
                break;
            case POSTS:
                deleted = db.delete(POSTS_TABLE, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return deleted;
    }

    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        db = dbHelper.getWritableDatabase();
        int updated = 0;
        if (matcher.match(uri) == FEED_ID) {
            String id = uri.getLastPathSegment();
            updated = db.update(FEEDS_TABLE, values, FIELD_ID + "=" + id, null);
        } else {
            throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return updated;
    }

    public String getType(Uri uri) {
        return Integer.toString(matcher.match(uri));
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL("PRAGMA foreign_keys=ON;");
            db.execSQL(FEEDS_TABLE_CREATE);
            db.execSQL(POSTS_TABLE_CREATE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists posts");
            db.execSQL("drop table if exists feeds");
            onCreate(db);
        }
    }
}