package com.sportskeeda_test_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sportskeeda_test_app.models.FeedModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<FeedModel> sourceDataArray;
    private List<FeedModel> filteredList;
    private FeedDataAdapter mAdapter;
    private List<String> spinnerItemArray;
    private JSONArray resultsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);

        initViews();
    }

    private void initViews() {

        sourceDataArray = new ArrayList<>();
        filteredList = new ArrayList<>();
        spinnerItemArray = new ArrayList<>();
        spinnerItemArray.add("All");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new FeedDataAdapter(MainActivity.this,
                filteredList);
        recyclerView.setAdapter(mAdapter);

        LoadInputFeedDate();

    }

    private void LoadInputFeedDate() {

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                APIClass.BASE_URL + APIClass.API_INPUT_VALUE, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                progressDialog.dismiss();

                try {

                    resultsArray = response.getJSONArray("feed");

                    populateResults("All");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

                APIClass.VolleyExceptions(MainActivity.this, error);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(jsonObjectRequest);

    }

    private void populateResults(String sortIdentifier) {
        sourceDataArray = new ArrayList<>();
        filteredList = new ArrayList<>();

        try {

            if (resultsArray.length() > 0) {

                for (int i = 0; i < resultsArray.length(); i++) {

                    JSONObject resultsObject = resultsArray.getJSONObject(i);

                    List<String> post_tagArray = new ArrayList<>();

                    for (int j = 0; j < resultsObject.getJSONArray("post_tag").length(); j++) {

                        post_tagArray.add(resultsObject.getJSONArray("post_tag").getString(j));
                    }

                    FeedModel dataItem = new FeedModel(
                            resultsObject.getString("thumbnail"),
                            resultsObject.getInt("word_count"),
                            resultsObject.getJSONObject("author").toString(),
                            resultsObject.getString("name"),
                            resultsObject.getInt("id"),
                            resultsObject.getString("title"),
                            resultsObject.getString("modified_date"),
                            resultsObject.getString("permalink"),
                            resultsObject.getString("published_date"),
                            resultsObject.getString("read_count"),
                            resultsObject.getInt("comment_count"),
                            resultsObject.getInt("live_traffic"),
                            resultsObject.getInt("rank"),
                            post_tagArray,
                            resultsObject.getJSONObject("algo_meta").toString(),
                            resultsObject.getInt("index"),
                            resultsObject.getString("type"),
                            resultsObject.getString("excerpt")
                    );

                    if (sortIdentifier.equals("All")) {

                        if (!resultsObject.getString("type").equals("")
                                && !spinnerItemArray.contains(resultsObject.getString("type"))) {

                            spinnerItemArray.add(resultsObject.getString("type"));
                        }

                        filteredList.add(dataItem);
                        sourceDataArray.add(dataItem);

                    } else {

                        if (resultsObject.getString("type").equals(sortIdentifier)) {

                            filteredList.add(dataItem);
                            sourceDataArray.add(dataItem);

                        }
                    }

                }

                if (filteredList != null) {

                    mAdapter = new FeedDataAdapter(MainActivity.this,
                            filteredList);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();

                } else {

                    APIClass.ShowToastMessage(getApplicationContext(), "No data found!");
                }

            } else {

                APIClass.ShowToastMessage(getApplicationContext(), "No data found!");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public class FeedDataAdapter extends RecyclerView.Adapter<FeedDataAdapter.MyViewHolder> implements Filterable {

        private final List<FeedModel> dataList;
        private final Activity parentActivity;
        private final CustomFilter mFilter;

        @Override
        public Filter getFilter() {
            return mFilter;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            final TextView titleTV;
            final TextView excerpt;
            final TextView authorTV;
            private final TextView dateTV;
            private final ImageView sourceImage;
            final LinearLayout itemViewLL;

            MyViewHolder(View view) {
                super(view);
                titleTV = view.findViewById(R.id.titleTV);
                excerpt = view.findViewById(R.id.descriptionTV);
                authorTV = view.findViewById(R.id.authorTV);
                dateTV = view.findViewById(R.id.dateTV);
                sourceImage = view.findViewById(R.id.sourceImage);
                itemViewLL = view.findViewById(R.id.itemViewLL);
            }
        }


        FeedDataAdapter(Activity parentActivity,
                        List<FeedModel> dataList) {
            this.dataList = dataList;
            this.parentActivity = parentActivity;
            mFilter = new CustomFilter(FeedDataAdapter.this);
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_items, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            final FeedModel dataItem = dataList.get(position);

            holder.titleTV.setText(dataItem.getTitle());
            holder.excerpt.setText(dataItem.getExcerpt());
            holder.dateTV.setText(dataItem.getPublished_date());
            try {
                JSONObject authorObject = new JSONObject(dataItem.getAuthor());
                String contentText = "Author: " + authorObject.getString("name");
                holder.authorTV.setText(contentText);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (dataItem.getThumbnail() != null && !dataItem.getThumbnail().equals("")) {

                Picasso.get().load(dataItem.getThumbnail()).placeholder(R.mipmap.ic_launcher).into(holder.sourceImage);

            }

            holder.itemViewLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent detailsActivity = new Intent(parentActivity, ItemDetailsActivity.class);
                    detailsActivity.putExtra("sourceDataItem", dataItem);
                    parentActivity.startActivity(detailsActivity);

                }
            });
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        class CustomFilter extends Filter {
            private final FeedDataAdapter mAdapter;

            private CustomFilter(FeedDataAdapter mAdapter) {
                super();
                this.mAdapter = mAdapter;
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                filteredList.clear();
                final FilterResults results = new FilterResults();
                if (constraint.length() == 0) {
                    filteredList.addAll(sourceDataArray);
                } else {
                    final String filterPattern = constraint.toString().toLowerCase().trim();
                    for (final FeedModel mWords : sourceDataArray) {
                        if (mWords.getTitle().toLowerCase().contains(filterPattern)
                                || mWords.getExcerpt().toLowerCase().contains(filterPattern)
                                || mWords.getAuthor().toLowerCase().contains(filterPattern)
                                || mWords.getPublished_date().toLowerCase().contains(filterPattern)) {
                            filteredList.add(mWords);
                        }
                    }
                }
                results.values = filteredList;
                results.count = filteredList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                this.mAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_items, menu);

        MenuItem item = menu.findItem(R.id.spinner);
        MenuItem searchItem = menu.findItem(R.id.search);
        Spinner spinner = (Spinner) item.getActionView();
        SearchView searchView = (SearchView) searchItem.getActionView();

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item,
                        spinnerItemArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (sourceDataArray.size() > 0) {
                    String selectedItem = spinnerItemArray.get(i);
                    populateResults(selectedItem);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String queryText) {

                mAdapter.getFilter().filter(queryText);

                return false;
            }
        });

        return true;
    }

}