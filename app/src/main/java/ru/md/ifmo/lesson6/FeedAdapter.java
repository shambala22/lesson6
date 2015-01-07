package ru.md.ifmo.lesson6;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by 107476 on 05.01.2015.
 */
public class FeedAdapter extends ArrayAdapter<FeedMaster> {
    Context context;
    ArrayList<FeedMaster> items;

    public FeedAdapter(Context context, ArrayList<FeedMaster> items) {
        super(context, R.layout.list_element, items);

        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_element, parent, false);
        final TextView title = (TextView) convertView.findViewById(R.id.feed_title);
        TextView link = (TextView) convertView.findViewById(R.id.feed_link);
        ImageView delete = (ImageView) convertView.findViewById(R.id.imageButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle(title.getText());
                dialog.setMessage("Do you want to delete this channel?");
                dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int id = items.get(position).id;
                        Uri uri = Uri.parse(MyContentProvider.FEED_CONTENT_URI + "/" + id);
                        context.getContentResolver().delete(uri, null, null);
                        items.remove(position);
                        notifyDataSetChanged();
                    }
                });
                dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();

            }
        });
        title.setText(items.get(position).getTitle());
        link.setText(items.get(position).getLink());
        return convertView;
    }
}
