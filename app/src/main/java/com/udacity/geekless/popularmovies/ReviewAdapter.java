package com.udacity.geekless.popularmovies;


import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class ReviewAdapter extends ArrayAdapter<Review> {

    private Activity activity;
    private List<Review> items;
    private Review objBean;
    private int row;

    public ReviewAdapter(Activity act, int resource, List<Review> arrayList) {
        super(act, resource, arrayList);
        this.activity = act;
        this.row = resource;
        this.items = arrayList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(row, null);

            holder = new ViewHolder();
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if ((items == null) || ((position + 1) > items.size()))
            return view;

        objBean = items.get(position);

        holder.title = (TextView) view.findViewById(R.id.reviewtitle_txtview);
        holder.desc = (TextView) view.findViewById(R.id.reviewdesc_txtview);
        holder.link = (TextView) view.findViewById(R.id.reviewlink_txtview);
        holder.imgView = (ImageView) view.findViewById(R.id.revierwe_imgview);

        if (holder.title != null && null != objBean.getTitle()
                && objBean.getTitle().trim().length() > 0) {
            holder.title.setText(objBean.getTitle());
        }
        if (holder.desc != null && null != objBean.getDesc()
                && objBean.getDesc().trim().length() > 0) {
            holder.desc.setText(objBean.getDesc());
        }
        if (holder.link != null && null != objBean.getLink()
                && objBean.getDesc().trim().length() > 0) {
            holder.link.setText(objBean.getLink());
        }
        holder.imgView.setImageResource(R.drawable.profile);

        return view;
    }

    public class ViewHolder {

        public TextView title, desc, link;
        private ImageView imgView;

    }

}