package com.fyber.android.challenge;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by amare on 3/12/16.
 */
public class OfferAdapater extends ArrayAdapter<Offer> {
    Context context;
    List<Offer> offers;
    int layoutResourceId;

    public OfferAdapater(Context context, int resourceId, List<Offer> offers) {

        super(context, resourceId, offers);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.offers = offers;
        this.layoutResourceId = resourceId;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return offers.size();
    }

    @Override
    public Offer getItem(int position) {
        // TODO Auto-generated method stub
        return offers.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            vi = inflater.inflate(layoutResourceId, parent, false);
        }

        Offer offer = offers.get(position);

        TextView text = (TextView) vi.findViewById(R.id.item_title);
        text.setText(offer.getTitle());

        text = (TextView) vi.findViewById(R.id.item_teaser);
        text.setText(offer.getTeaser());

        text = (TextView) vi.findViewById(R.id.item_payout);
        text.setText(offer.getPayout());

        ImageView imageView = (ImageView) vi.findViewById(R.id.item_thumbnail);

        new DownloadImageTask(imageView)
                .execute(offer.getThumbnail());

        return vi;
    }
}