package com.fyber.android.challenge;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowListView;
import org.robolectric.util.ActivityController;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by amare on 3/12/16.
 */
@RunWith(RobolectricGradleTestRunner.class)
public class MainActivityTest {

    private ActivityController<MainActivity> controller;
    private MainActivity activity;

    private ListView offersLV;
    EditText uidET, appidET, apikeyET, pub0ET;
    Button getOfferButton;

    @Before
    public void setUp() {
        activity = Robolectric.setupActivity(MainActivity.class);

        offersLV = (ListView) activity.findViewById(R.id.offers_listview);

        uidET = (EditText) activity.findViewById(R.id.uidET);
        appidET = (EditText) activity.findViewById(R.id.appIdET);
        apikeyET = (EditText) activity.findViewById(R.id.apiKeyET);
        pub0ET = (EditText) activity.findViewById(R.id.pub0ET);

        getOfferButton = (Button) activity.findViewById(R.id.load_offers_button);
    }

    @After
    public void tearDown() {
        controller.destroy();
    }


    @Test
    public void testLoadOffer() {

        ShadowListView shadowListView = Shadows.shadowOf(offersLV); //we need to shadow the list view
        shadowListView.populateItems();// will populate the with empty list adapter

        //make sure offers adapter is not null
        assertNotNull("Offer Adapter not null", activity.offerAdapater);

        //check the adapters are same
        assertEquals(activity.offerAdapater, offersLV.getAdapter());

        uidET.setText(activity.UID_PARAM_TEST);
        appidET.setText(activity.APPID_PARAM_TEST);
        apikeyET.setText(activity.API_KEY_PARAM_TEST);
        pub0ET.setText("campaign");

        getOfferButton.performClick();

        // Validation - wait for background tasks to finish (i.e. AsyncTask)
        ShadowApplication.runBackgroundTasks();

        shadowListView.populateItems();

        String response = activity.offersPullAsyncTask.response;

        if(response != null) {

            JSONObject jsonResponse = null;

            try {
                jsonResponse = new JSONObject(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONArray offersArray = jsonResponse.optJSONArray("offers");

            assertEquals(offersArray != null ? offersArray.length() : 0, offersLV.getAdapter().getCount());
        } else {
            assertEquals(0, offersLV.getAdapter().getCount());
        }

    }
}
