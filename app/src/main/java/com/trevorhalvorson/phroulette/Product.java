package com.trevorhalvorson.phroulette;

import com.google.gson.JsonObject;

/**
 * Created by Trevor on 9/5/2015.
 */
public class Product {

    private int id;
    private String name;
    private String tagline;
    private String day;
    private String created_at;
    private boolean featured;
    private int comments_count;
    private int votes_count;
    private String discussion_url;
    private String redirect_url;
    private boolean maker_inside;


    public Product(int id, String name, String tagline, String day, String created_at,
                   boolean featured, int comments_count, int votes_count, String discussion_url,
                   String redirect_url, boolean maker_inside) {
        this.id = id;
        this.name = name;
        this.tagline = tagline;
        this.day = day;
        this.created_at = created_at;
        this.featured = featured;
        this.comments_count = comments_count;
        this.votes_count = votes_count;
        this.discussion_url = discussion_url;
        this.redirect_url = redirect_url;
        this.maker_inside = maker_inside;
    }

    public Product(JsonObject obj) {
        this(obj.get("id").getAsInt(),
                obj.get("name").getAsString(),
                obj.get("tagline").getAsString(),
                obj.get("day").getAsString(),
                obj.get("created_at").getAsString(),
                obj.get("featured").getAsBoolean(),
                obj.get("comments_count").getAsInt(),
                obj.get("votes_count").getAsInt(),
                obj.get("discussion_url").getAsString(),
                obj.get("redirect_url").getAsString(),
                obj.get("maker_inside").getAsBoolean());
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTagline() {
        return tagline;
    }

    public String getDay() {
        return day;
    }

    public String getCreated_at() {
        return created_at;
    }

    public boolean isFeatured() {
        return featured;
    }

    public int getComments_count() {
        return comments_count;
    }

    public int getVotes_count() {
        return votes_count;
    }

    public String getDiscussion_url() {
        return discussion_url;
    }

    public String getRedirect_url() {
        return redirect_url;
    }

    public boolean getMaker_inside() {
        return maker_inside;
    }
}
