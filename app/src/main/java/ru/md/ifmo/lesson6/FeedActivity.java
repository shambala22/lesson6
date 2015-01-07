package ru.md.ifmo.lesson6;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;



/**
 * Created by 107476 on 05.01.2015.
 */
public class FeedActivity extends ListActivity implements LoaderManager.LoaderCallbacks<ArrayList<FeedMaster>> {

    Button add;
    EditText title;
    EditText link;
    ListView listView;

    FeedAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        listView = (ListView) findViewById(android.R.id.list);
        title = (EditText) findViewById(R.id.title2);
        link = (EditText) findViewById(R.id.link2);
        add = (Button) findViewById(R.id.add2);
        adapter = new FeedAdapter(this, new ArrayList<FeedMaster>());
        setListAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("HelloListView", "You clicked Item: " + id + " at position:" + position);
                FeedMaster item = adapter.getItem(position);
                int feedId = item.id;
                String title = item.getTitle();
                String url = item.getLink();
                Intent intent = new Intent(getBaseContext(), MyActivity.class);
                intent.putExtra("id", feedId);
                intent.putExtra("title", title);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!title.getText().toString().equals("") && !link.getText().toString().equals("")) {
                    Intent intent = new Intent(getBaseContext(), Updater.class);
                    intent.putExtra("url", link.getText().toString());
                    intent.putExtra("title", title.getText().toString());
                    link.setText("");
                    title.setText("");
                    onUpdate();
                    startService(intent);
                    onUpdate();

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please, input title and link", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        getLoaderManager().initLoader(0, null, this);

    }

    @Override
    public Loader<ArrayList<FeedMaster>> onCreateLoader(int id, Bundle args) {
        return new FeedLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<FeedMaster>> loader, ArrayList<FeedMaster> data) {
        adapter = new FeedAdapter(this, data);
        setListAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<FeedMaster>> loader) {
        adapter = new FeedAdapter(this, new ArrayList<FeedMaster>());
        setListAdapter(adapter);
    }

    void onUpdate() {
        getLoaderManager().restartLoader(0, null, this);
    }


}
