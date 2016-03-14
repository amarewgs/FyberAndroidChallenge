package com.fyber.android.challenge;

/**
 * Created by amare on 3/12/16.
 */
public class Offer {
    private String title;
    private String teaser;
    private String thumbnailUrl;
    private String payout;


    public Offer() {

    }

    public Offer(String title, String teaser, String thumbnailUrl, String payout) {
        this.title = title;
        this.teaser = teaser;
        this.thumbnailUrl = thumbnailUrl;
        this.payout = payout;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTeaser() {
        return teaser;
    }

    public void setTeaser(String teaser) {
        this.teaser = teaser;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getPayout() {
        return payout;
    }

    public void setPayout(String payout) {
        this.payout = payout;
    }
}