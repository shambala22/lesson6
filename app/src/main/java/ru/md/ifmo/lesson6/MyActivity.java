package ru.md.ifmo.lesson6;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;



public class MyActivity extends Activity implements LoaderManager.LoaderCallbacks<ArrayList<ItemMaster>>{
    int id;
    String title;
    String description;

    Button refresh;
    ListView listView;
    MyAdapter adapter;
    boolean browser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        /**/
        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
        title = intent.getStringExtra("title");
        description = intent.getStringExtra("description");


        refresh = (Button)findViewById(R.id.button2);
        listView =(ListView) findViewById(R.id.listView2);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpdate();
                updated();
            }
        });

        adapter = new MyAdapter(this, new ArrayList<ItemMaster>(), browser);
        listView.setAdapter(adapter);

        getLoaderManager().initLoader(0, null, this);
        updated();
    }



    @Override
    public Loader<ArrayList<ItemMaster>> onCreateLoader(int i, Bundle args) {
        return new MyLoader(this, id);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<ItemMaster>> loader, ArrayList<ItemMaster> data) {
        adapter = new MyAdapter(this, data, browser);
        listView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<ItemMaster>> loader) {
        adapter = new MyAdapter(this, new ArrayList<ItemMaster>(), browser);
        listView.setAdapter(adapter);
    }

    public void onUpdate() {
        if (Updater.running) {
            return;
        }
        Intent intent = new Intent(this, Updater.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        startService(intent);

    }

   public void updated() {
       getLoaderManager().restartLoader(0, null, this);
   }
}
