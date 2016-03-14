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

    public static final String TITLE = "title";
    public static final String TEASER = "teaser";
    public static final String PAYOUT = "payout";
    public static final String THUMBNAIL_URL = "thumbnail";
    public static final String LOWERS = "lowres";

    public static final String OFFERS = "offers";

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

                //read error from the connection
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

        JSONObject jsonResponse;

        try {

            if(response != null) {

                jsonResponse = new JSONObject(response);

                JSONArray offersArray = jsonResponse.optJSONArray(OFFERS);

                List<Offer> offers = new ArrayList<>();

                if(offersArray != null) {

                    int lengthJsonArr = offersArray.length();

                    for (int i = 0; i < lengthJsonArr; i++) {

                        JSONObject jsonChildNode = offersArray.getJSONObject(i);

                        String title = jsonChildNode.optString(TITLE);
                        String teaser = jsonChildNode.optString(TEASER);
                        String payout = jsonChildNode.optString(PAYOUT);
                        String thumbnailUrl = jsonChildNode.getJSONObject(THUMBNAIL_URL).optString(LOWERS);

                        Offer offer = new Offer();
                        offer.setPayout(payout);
                        offer.setTitle(title);
                        offer.setTeaser(teaser);
                        offer.setThumbnailUrl(thumbnailUrl);

                        offers.add(offer);
                    }
                }

                offersLoadedCallback.setOffers(offers);
            } else {
                //reset with empty offers
                offersLoadedCallback.setOffers(new ArrayList<Offer>());
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
