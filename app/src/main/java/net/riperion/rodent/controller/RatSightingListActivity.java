package net.riperion.rodent.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.riperion.rodent.R;
import net.riperion.rodent.model.ListWrapper;
import net.riperion.rodent.model.RatSighting;
import net.riperion.rodent.model.RatSightingProvider;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An activity representing a list of Rat Sightings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RatSightingDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RatSightingListActivity extends AppCompatActivity implements Callback<ListWrapper<RatSighting>>  {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private RecyclerView mRecyclerView;

    private boolean requestPresent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratsighting_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RatSightingListActivity.this, ReportActivity.class);
                startActivity(intent);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.ratsighting_list);
        assert mRecyclerView != null;
        setupRecyclerView(mRecyclerView);

        if (findViewById(R.id.ratsighting_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    getMoreRatSightings();
                }
            }
        });

        requestPresent = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((RatSightingRecyclerViewAdapter) mRecyclerView.getAdapter()).mValues.clear();
        getMoreRatSightings();
    }

    /**
     * Sets up the recycler view and requests for data to populate it with
     * @param recyclerView the recycler view to set the adapter on
     */
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        RatSightingRecyclerViewAdapter adapter = new RatSightingRecyclerViewAdapter(new ArrayList<RatSighting>());
        recyclerView.setAdapter(adapter);
        getMoreRatSightings();
    }

    /**
     * Queries the server for more Rat Sightings to populate the list with
     */
    private void getMoreRatSightings() {
        if (!requestPresent) {
            int size = ((RatSightingRecyclerViewAdapter) mRecyclerView.getAdapter()).mValues.size();
            RatSightingProvider.asyncGetRatSightings(this, size);
            requestPresent = true;
        }
    }

    @Override
    public void onResponse(@NonNull Call<ListWrapper<RatSighting>> call, @NonNull Response<ListWrapper<RatSighting>> response) {
        ListWrapper<RatSighting> body = response.body();
        assert body != null;
        ((RatSightingRecyclerViewAdapter) mRecyclerView.getAdapter()).mValues.addAll(body.getResults());
        mRecyclerView.getAdapter().notifyDataSetChanged();

        requestPresent = false;
    }

    @Override
    public void onFailure(@NonNull Call<ListWrapper<RatSighting>> call, @NonNull Throwable t) {
        requestPresent = false;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("An error occurred trying to fetch rat sighting reports.").setTitle("Oops!");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * An adapter class that populates recycler view cells with corresponding Rat Sightings
     */
    public class RatSightingRecyclerViewAdapter
            extends RecyclerView.Adapter<RatSightingRecyclerViewAdapter.ViewHolder> {

        private final List<RatSighting> mValues;

        /**
         * Instantiates an adapter to use on a RecyclerView
         * @param items the list of items to populate this RecyclerView with
         */
        public RatSightingRecyclerViewAdapter(List<RatSighting> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ratsighting_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText("" + mValues.get(position).getId());
            holder.mContentView.setText(mValues.get(position).getAddress());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putInt(RatSightingDetailFragment.ARG_ITEM_ID, holder.mItem.getId());
                        RatSightingDetailFragment fragment = new RatSightingDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.ratsighting_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, RatSightingDetailActivity.class);
                        intent.putExtra(RatSightingDetailFragment.ARG_ITEM_ID, holder.mItem.getId());

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        /**
         * Represents individual cell in the recycler view holding the rat sighting's ID and Address
         */
        public class ViewHolder extends RecyclerView.ViewHolder {
            final View mView;
            final TextView mIdView;
            final TextView mContentView;
            RatSighting mItem;

            /**
             * Instantiates a view holder - this is done by the Android UI backend
             * @param view the view this holder is linked to
             */
            ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = view.findViewById(R.id.id);
                mContentView = view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
