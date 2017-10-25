package net.riperion.rodent.controller;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import net.riperion.rodent.R;
import net.riperion.rodent.model.ListWrapper;
import net.riperion.rodent.model.RatSighting;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This activity is the controller for the view that hosts the Map view of the sightings.
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, Callback<ListWrapper<RatSighting>> {
    GoogleMap map;

    EditText startDateEdit;
    EditText endDateEdit;
    boolean queryInProgress;

    /**
     * The date format we display and parse from the date fields
     */
    public static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Calendar c = Calendar.getInstance();
        Date endDate = c.getTime();

        c.add(Calendar.MONTH, -1);
        Date startDate = c.getTime();

        startDateEdit = (EditText) findViewById(R.id.edit_startdate);
        endDateEdit = (EditText) findViewById(R.id.edit_enddate);

        startDateEdit.setText(dateFormat.format(startDate));
        endDateEdit.setText(dateFormat.format(endDate));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        queryInProgress = false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        this.map = googleMap;

        final MapActivity thisActivity = this;

        this.map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(thisActivity);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(thisActivity);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(thisActivity);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

        onGoPressed(null);
    }

    /**
     * The button press handler for the Go / Search button
     * @param view the button that was pressed (always equals the Go button)
     */
    public void onGoPressed(View view) {
        try {
            // Validate the date
            String startDateString = startDateEdit.getText().toString();
            Date startDate = dateFormat.parse(startDateString);

            String endDateString = endDateEdit.getText().toString();
            Date endDate = dateFormat.parse(endDateString);

            requestData(startDate, endDate);
        } catch (ParseException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Invalid dates entered - dates should be in YYYY-MM-DD format.").setTitle("Oops!");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    /**
     * Requests the data corresponding to the entered date range from the API handler
     * @param startDate start of date range (hours / minutes / seconds not taken into account)
     * @param endDate end of date range (hours / minutes / seconds not taken into account)
     */
    private void requestData(Date startDate, Date endDate) {
        if (!this.queryInProgress) {
            this.queryInProgress = true;
            RatSighting.asyncGetRatSightingByDateRange(startDate, endDate, this);
        }
    }

    @Override
    public void onResponse(Call<ListWrapper<RatSighting>> call, Response<ListWrapper<RatSighting>> response) {
        List<RatSighting> rats = response.body().getResults();

        if (rats.size() > 0) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (RatSighting rat : rats) {
                LatLng ratPos = new LatLng(rat.getLatitude().doubleValue(), rat.getLongitude().doubleValue());
                MarkerOptions ratMarker = new MarkerOptions().position(ratPos).title("Sighting #" + rat.getId()).snippet(rat.getDetails());
                map.addMarker(ratMarker);
                builder.include(ratMarker.getPosition());
            }

            LatLngBounds bounds = builder.build();

            int padding = 20; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

            map.moveCamera(cu);
            map.setLatLngBoundsForCameraTarget(bounds); // Cant get out anymore
        }

        this.queryInProgress = false;
    }

    @Override
    public void onFailure(Call<ListWrapper<RatSighting>> call, Throwable t) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Something went wrong trying to get data from the server :(").setTitle("Oops!");
        AlertDialog dialog = builder.create();
        dialog.show();

        this.queryInProgress = false;
    }
}
