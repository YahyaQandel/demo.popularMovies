package com.udacity.geekless.popularmovies;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TrailerAdapter extends ArrayAdapter<Trailer> {

    private Activity activity;
    private List<Trailer> items;
    private Trailer objBean;
    private int row;

    public TrailerAdapter(Activity act, int resource, List<Trailer> arrayList) {
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

        holder.name = (TextView) view.findViewById(R.id.trailer_name_txtview);
        holder.site = (TextView) view.findViewById(R.id.trailer_site_txtview);
        holder.size = (TextView) view.findViewById(R.id.trailer_size_txtview);
        holder.imgView = (ImageView) view.findViewById(R.id.trailer_imgview);

        if (holder.name != null && null != objBean.getName()
                && objBean.getName().trim().length() > 0) {
            holder.name.setText(objBean.getName());
        }
        if (holder.site != null && null != objBean.getSite()
                && objBean.getSite().trim().length() > 0) {
            holder.site.setText(objBean.getSite());
        }
        if (holder.size != null && null != objBean.getSize()
                && objBean.getSize().trim().length() > 0) {
            holder.size.setText(objBean.getSize());
        }
        holder.imgView.setImageResource(R.drawable.video);

        return view;
    }

    public class ViewHolder {

        public TextView  name, size,site;
        private ImageView imgView;

    }

}