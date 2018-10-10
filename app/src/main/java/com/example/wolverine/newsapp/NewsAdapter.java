package com.example.wolverine.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by wolverine on 12/08/18.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, ArrayList<News> news) {
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        News newses_local = getItem(position);

        TextView sectiontxt = listItemView.findViewById(R.id.news_section);
        sectiontxt.setText(newses_local.getmSectionName());

        TextView typetxt = listItemView.findViewById(R.id.news_type);
        typetxt.setText(newses_local.getmType());

        TextView titletxt = listItemView.findViewById(R.id.news_title);
        titletxt.setText(newses_local.getmTitle());

        TextView authortxt = listItemView.findViewById(R.id.Author);
        authortxt.setText(newses_local.getmAuthor());

        TextView datetxt = listItemView.findViewById(R.id.news_date);
        datetxt.setText(formatDate(newses_local.getmTimeDate()));
        return listItemView;
    }

    private String formatDate(String dateObject) {
        Date date1 = new Date();
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(dateObject);
        } catch (ParseException e) {

        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd LLL yyyy");

        return dateFormat.format(date1).toString();
    }

}
