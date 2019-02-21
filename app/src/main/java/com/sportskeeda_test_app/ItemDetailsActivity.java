package com.sportskeeda_test_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sportskeeda_test_app.models.FeedModel;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class ItemDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        initViews();
    }

    private void initViews() {

        FeedModel oneFeedData = (FeedModel) getIntent().getSerializableExtra("sourceDataItem");

        View includedLayout = findViewById(R.id.oneFeedItemLayout);
        TextView titleTV = includedLayout.findViewById(R.id.titleTV);
        TextView excerpt = includedLayout.findViewById(R.id.descriptionTV);
        TextView authorTV = includedLayout.findViewById(R.id.authorTV);
        TextView dateTV = includedLayout.findViewById(R.id.dateTV);
        ImageView sourceImage = includedLayout.findViewById(R.id.sourceImage);

        titleTV.setText(oneFeedData.getTitle());
        excerpt.setText(oneFeedData.getExcerpt());
        dateTV.setText(oneFeedData.getPublished_date());
        try {
            JSONObject authorObject = new JSONObject(oneFeedData.getAuthor());
            String contentText = "Author: " + authorObject.getString("name");
            authorTV.setText(contentText);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (oneFeedData.getThumbnail() != null && !oneFeedData.getThumbnail().equals("")) {

            Picasso.get().load(oneFeedData.getThumbnail()).placeholder(R.mipmap.ic_launcher).into(sourceImage);

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
