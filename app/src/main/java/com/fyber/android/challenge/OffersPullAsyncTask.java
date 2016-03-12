package com.fyber.android.challenge;

/**
 * Created by amare on 3/12/16.
 */

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OffersPullAsyncTask extends AsyncTask<String, Void, Void> {

    String urlSpec;

    Map<String, Object> params;

    String response;

    public OffersPullAsyncTask(Map<String, Object> params, String urlSpec) {

        this.urlSpec = urlSpec;
        this.params = params;
    }


    private String buildUri() {
        Uri.Builder builder = Uri.parse(this.urlSpec)
                .buildUpon();

        for(String param : params.keySet()) {
            builder.appendQueryParameter(param, params.get(param).toString());
        }
        return builder.build().toString();
    }

    @Override
    protected Void doInBackground(String... params) {
        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(buildUri());
            connection = (HttpURLConnection) url.openConnection();

            int responsecode = connection.getResponseCode();

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            if(responsecode >= 400 && responsecode <= 499) {

                InputStream inputStream = connection.getErrorStream();

                int bytesRead = 0;
                byte[] buffer = new byte[1024];

                while ((bytesRead = inputStream.read(buffer)) > 0) {
                    out.write(buffer, 0, bytesRead);
                }

                out.close();

                String errorResponse = new String(out.toByteArray());

                throw new Exception("Bad authentication, status: " + responsecode);
            } else if(responsecode == 500) {

                throw new Exception("Internal Server Error (Error on the Fyber server), status:" + responsecode);
            } else if (responsecode == 502) {
                throw new Exception("Bad Gateway (Error on the Fyber server), status:" + responsecode);
            } else if(responsecode == 200) {

                InputStream in = connection.getInputStream();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    throw new IOException(connection.getResponseMessage() +
                            ": with " +
                            urlSpec);
                }

                int bytesRead = 0;
                byte[] buffer = new byte[1024];

                while ((bytesRead = in.read(buffer)) > 0) {
                    out.write(buffer, 0, bytesRead);
                }
                out.close();

                response = new String(out.toByteArray());
            } else {
                //TODO: add a logic here if any of those above conditions doesn't meet
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }

        return null;
    }
    protected void onPostExecute(Void unused) {
        // NOTE: You can call UI Element here.

        String OutputData = "";
        JSONObject jsonResponse;


        try {

            /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/

            if(response != null) {

                jsonResponse = new JSONObject(response);

                JSONArray offersArray = jsonResponse.optJSONArray("offers");

                /*********** Process each JSON Node ************/

                int lengthJsonArr = offersArray.length();

                List<Offer> offers = new ArrayList<>();

                for (int i = 0; i < lengthJsonArr; i++) {
                    /****** Get Object for each JSON node.***********/
                    JSONObject jsonChildNode = offersArray.getJSONObject(i);

                    /******* Fetch offer values **********/
                    String title = jsonChildNode.optString("title").toString();
                    String teaser = jsonChildNode.optString("teaser").toString();
                    String payout = jsonChildNode.optString("payout").toString();
                    String thumbnail = jsonChildNode.getJSONObject("thumbnail").optString("lowres").toString();

                    Offer offer = new Offer();
                    offer.setPayout(payout);
                    offer.setTitle(title);
                    offer.setTeaser(teaser);
                    offer.setThumbnail(thumbnail);

                    offers.add(offer);
                }

                offersLoadedCallback.setOffers(offers);
            }

        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    private OffersLoadedCallback offersLoadedCallback;

    public void setOffersLoadedCallback(OffersLoadedCallback offersLoadedCallback) {
        this.offersLoadedCallback = offersLoadedCallback;
    }

    public interface OffersLoadedCallback {
        public void setOffers(List<Offer> offers);
    }
}
