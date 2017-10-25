package net.riperion.rodent.controller;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.riperion.rodent.R;
import net.riperion.rodent.model.RatSighting;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A fragment representing a single Rat Sighting detail screen.
 * This fragment is either contained in a {@link RatSightingListActivity}
 * in two-pane mode (on tablets) or a {@link RatSightingDetailActivity}
 * on handsets.
 */
public class RatSightingDetailFragment extends Fragment implements Callback<RatSighting> {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private RatSighting mItem;

    private boolean viewCreated;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RatSightingDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = null; // For now!
            RatSighting.asyncGetRatSightingByKey(getArguments().getInt(ARG_ITEM_ID), this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ratsighting_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.ratsighting_detail)).setText(mItem.getDetails());
        }

        viewCreated = true;

        return rootView;
    }

    @Override
    public void onResponse(Call<RatSighting> call, Response<RatSighting> response) {
        if (viewCreated) {
            mItem = response.body();

            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) this.getActivity().findViewById(R.id.toolbar_layout);

            if (appBarLayout != null) {
                appBarLayout.setTitle("" + mItem.getId());
            }

            View rootView = this.getView();
            ((TextView) rootView.findViewById(R.id.ratsighting_detail)).setText(mItem.getDetails());
        }
    }

    @Override
    public void onFailure(Call<RatSighting> call, Throwable t) {
        // TODO: What to do here?
        t.printStackTrace();
    }
}
