package net.riperion.rodent.controller;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import net.riperion.rodent.R;
import net.riperion.rodent.model.DateCountPair;
import net.riperion.rodent.model.DateFloatSerializer;
import net.riperion.rodent.model.RatSightingProvider;

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
public class GraphActivity extends AppCompatActivity implements Callback<List<DateCountPair>> {
    private static final int MAXIMUM_VISIBLE_VALUE_COUNT = 50;

    private BarChart chart;

    private EditText startDateEdit;
    private EditText endDateEdit;
    private boolean queryInProgress;

    private Date startDate;
    private Date endDate;

    /**
     * The date format we display and parse from the date fields
     */
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        Calendar c = Calendar.getInstance();
        endDate = c.getTime();

        c.add(Calendar.YEAR, -1);
        startDate = c.getTime();

        startDateEdit = (EditText) findViewById(R.id.edit_startdate);
        endDateEdit = (EditText) findViewById(R.id.edit_enddate);

        startDateEdit.setText(dateFormat.format(startDate));
        endDateEdit.setText(dateFormat.format(endDate));

        chart = (BarChart) findViewById(R.id.chart);

        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);

        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);


        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(MAXIMUM_VISIBLE_VALUE_COUNT);

        // Disable all zooming
        chart.setPinchZoom(false);
        chart.setTouchEnabled(false);
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setDoubleTapToZoomEnabled(false);

        chart.setDrawGridBackground(false);

        IAxisValueFormatter xAxisFormatter = new MonthAxisValueFormatter();

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 month
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        chart.getAxisRight().setEnabled(false);

        this.queryInProgress = true;
        RatSightingProvider.asyncGetMonthlyRatSightingCountByMonth(this);
    }

    /**
     * The button press handler for the Go / Search button
     * @param view the button that was pressed (always equals the Go button)
     */
    public void onGoPressed(@SuppressWarnings("UnusedParameters") View view) {
        try {
            // Validate the date
            String startDateString = startDateEdit.getText().toString();
            Date startDate = dateFormat.parse(startDateString);

            String endDateString = endDateEdit.getText().toString();
            Date endDate = dateFormat.parse(endDateString);

            this.startDate = startDate;
            this.endDate = endDate;

            displayData();
        } catch (ParseException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Invalid dates entered - dates should be in YYYY-MM format.").setTitle("Oops!");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    /**
     * Displays the data corresponding to the entered date range
     */
    private void displayData() {
        if (!this.queryInProgress) {
            float start = DateFloatSerializer.getFloatFromDate(startDate);
            float diff = DateFloatSerializer.getFloatFromDate(endDate) - start;
            chart.fitScreen();
            chart.setVisibleXRange(diff, diff);
            chart.moveViewToX(start);
            chart.invalidate();
        }
    }

    @Override
    public void onResponse(@NonNull Call<List<DateCountPair>> call, @NonNull Response<List<DateCountPair>> response) {
        List<DateCountPair> items = response.body();
        assert items != null;

        List<BarEntry> entries = new ArrayList<>();
        for (DateCountPair d: items) {
            entries.add(new BarEntry(DateFloatSerializer.getFloatFromDate(d.getMonth()), d.getCount()));
        }

        BarDataSet set = new BarDataSet(entries, "BarDataSet");

        BarData data = new BarData(set);
        chart.setData(data);

        this.queryInProgress = false;

        displayData();
    }

    @Override
    public void onFailure(@NonNull Call<List<DateCountPair>> call, @NonNull Throwable t) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Something went wrong trying to get data from the server :(").setTitle("Oops!");
        AlertDialog dialog = builder.create();
        dialog.show();

        this.queryInProgress = false;
    }

    /**
     * An axis label formatter class that uses the Date deserializer to get a date string for the X-axis.
     */
    private class MonthAxisValueFormatter implements IAxisValueFormatter {
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return DateFloatSerializer.getDateStringFromFloat(value);
        }
    }
}
