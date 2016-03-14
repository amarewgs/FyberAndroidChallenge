package com.fyber.android.challenge;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Created by amare on 3/12/16.
 */
@RunWith(RobolectricGradleTestRunner.class)
public class OfferAdapterTest {

    OfferAdapater offerAdapater;
    private Context context;

    @Before
    public void setUp() {
        context = RuntimeEnvironment.application;

        List<Offer> offers = new ArrayList<>();

        Offer offer = new Offer("title1", "teaser1", "thumbnail1", "payout1");
        offers.add(offer);

        offer = new Offer("title2", "teaser2", "thumbnail2", "payout2");
        offers.add(offer);

        offerAdapater = new OfferAdapater(context, R.layout.list_item, offers);
    }

    @Test
    public void testGetItem() {
        assertEquals("title1 was expected.", "title1",
                offerAdapater.getItem(0).getTitle());
    }

    @Test
    public void testGetCount() {
        assertEquals(2, offerAdapater.getCount());
    }

    // I have 4 views on my adapter, title, teaser, payout and thumbnail
    @Test
    public void testGetView() {
        View view = offerAdapater.getView(0, null, null);

        TextView title = (TextView) view
                .findViewById(R.id.item_title);

        TextView teaser = (TextView) view
                .findViewById(R.id.item_teaser);

        TextView payout = (TextView) view
                .findViewById(R.id.item_payout);

        ImageView thumbnail = (ImageView) view
                .findViewById(R.id.item_thumbnail);

        //On this part you will have to test it with your own views/data
        assertNotNull("View is null. ", view);
        assertNotNull("title TextView is null. ", title);
        assertNotNull("teaser TextView is null. ", teaser);
        assertNotNull("payout TextView is null. ", payout);
        assertNotNull("thumbnail ImageView is null. ", thumbnail);

        assertEquals("titles doesn't match.", "title1", title.getText());
        assertEquals("teasers doesn't match.", "teaser1",
                teaser.getText());
    }
}
