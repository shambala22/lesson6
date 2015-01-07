package ru.md.ifmo.lesson6;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


public class MyAdapter extends ArrayAdapter<ItemMaster> {
    Context context;
    ArrayList<ItemMaster> items;
    boolean browser;

    public MyAdapter(Context context, ArrayList<ItemMaster> items, boolean browser) {
        super(context, R.layout.list_element, items);

        this.context = context;
        this.items = items;
        this.browser = browser;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View elementView = inflater.inflate(R.layout.list_element, parent, false);
        TextView title = (TextView) elementView.findViewById(R.id.title);
        TextView date = (TextView) elementView.findViewById(R.id.date);
        TextView description = (TextView) elementView.findViewById(R.id.description);
        Button button = (Button) elementView.findViewById((R.id.more));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebView link = (WebView) elementView.findViewById(R.id.link);
                    Intent intent = new Intent(getContext(), WebViewActivity.class);
                    intent.putExtra("link", items.get(position).getLink());
                    context.startActivity(intent);

            }
        });
        title.setText(items.get(position).getTitle());
        date.setText(items.get(position).getPubDate());
        description.setText(items.get(position).getDescription());
        return elementView;
    }




}
