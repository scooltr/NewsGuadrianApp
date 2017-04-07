package com.example.scooltr.guardianmaxnewsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Making adapter for news
 */

public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(Context context, List<News> newsConstructor) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, newsConstructor);
    }
    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position The position in the list of data that should be displayed in the
     *                 list item view.
     * @param convertView The recycled view to populate.
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View currentView = convertView;
        if (currentView == null) {
            currentView = LayoutInflater.from(getContext()).inflate(R.layout.news_item, parent, false);
        }
        // Find the news at the given position in the list of news
        News currentNews = getItem(position);

        TextView titleTextView = (TextView) currentView.findViewById(R.id.title);
        titleTextView.setText(currentNews.getTitle());

        TextView sectionTextView = (TextView) currentView.findViewById(R.id.section);
        sectionTextView.setText(currentNews.getSection());

        TextView dateTextView = (TextView) currentView.findViewById(R.id.date);
        dateTextView.setText(currentNews.getDatePublished());

        return currentView;
    }
}
