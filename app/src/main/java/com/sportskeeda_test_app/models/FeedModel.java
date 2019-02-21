package com.sportskeeda_test_app.models;

import java.io.Serializable;
import java.util.List;

public class FeedModel implements Serializable {
    private final String thumbnail;
    private final int word_count;
    private final String author;
    private final String name;
    private final int id;
    private final String title;
    private final String modified_date;
    private final String permalink;
    private final String published_date;
    private final String read_count;
    private final int comment_count;
    private final int live_traffic;
    private final int rank;
    private final List<String> post_tagArray;
    private final String algo_meta;
    private final int index;
    private final String type;
    private final String excerpt;

    public FeedModel(String thumbnail, int word_count, String author, String name, int id, String title, String modified_date, String permalink, String published_date, String read_count, int comment_count, int live_traffic, int rank, List<String> post_tagArray, String algo_meta, int index, String type, String excerpt) {
        this.thumbnail = thumbnail;
        this.word_count = word_count;
        this.author = author;
        this.name = name;
        this.id = id;
        this.title = title;
        this.modified_date = modified_date;
        this.permalink = permalink;
        this.published_date = published_date;
        this.read_count = read_count;
        this.comment_count = comment_count;
        this.live_traffic = live_traffic;
        this.rank = rank;
        this.post_tagArray = post_tagArray;
        this.algo_meta = algo_meta;
        this.index = index;
        this.type = type;
        this.excerpt = excerpt;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public int getWord_count() {
        return word_count;
    }

    public String getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getModified_date() {
        return modified_date;
    }

    public String getPermalink() {
        return permalink;
    }

    public String getPublished_date() {
        return published_date;
    }

    public String getRead_count() {
        return read_count;
    }

    public int getComment_count() {
        return comment_count;
    }

    public int getLive_traffic() {
        return live_traffic;
    }

    public int getRank() {
        return rank;
    }

    public List<String> getPost_tagArray() {
        return post_tagArray;
    }

    public String getAlgo_meta() {
        return algo_meta;
    }

    public int getIndex() {
        return index;
    }

    public String getType() {
        return type;
    }

    public String getExcerpt() {
        return excerpt;
    }
}
