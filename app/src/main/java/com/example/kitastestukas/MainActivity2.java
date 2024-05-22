package com.example.kitastestukas;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kitastestukas.Models.DateInterval;
import com.example.kitastestukas.Models.GraphType;
import com.example.kitastestukas.Models.Payment;
import com.example.kitastestukas.Models.UserInput;
import com.example.kitastestukas.Utilities.AnnuityPaymentCalculations;
import com.example.kitastestukas.Utilities.DateUtilities;
import com.example.kitastestukas.Utilities.ExportUtilities;
import com.example.kitastestukas.Utilities.LinnearPaymentCalculations;
import com.example.kitastestukas.Utilities.StringDateParser;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    public class MonthValueFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            long months = (long) value;
            LocalDate date = DateUtilities.fromMonthsSinceEpoch(months);
            return date.getDayOfMonth() + "-" + date.getMonth().getValue() + "-" + date.getYear(); // Format the date as needed
        }
    }
    private Button goToDataPage;
    private Button filter;
    private Button cancelFilter;
    private Button pickFilterStart;
    private Button pickFilterEnd;
    private TextView filterStartDateLabel;
    private TextView filterEndDateLabel;
    private TableLayout paymentsTable;
    private LineChart lineChart;
    private UserInput userInput;
    private Button applyDelay;
    private Button cancelDelay;
    private Button pickDelayStart;
    private Button pickDelayEnd;
    private TextView delayStartDateLabel;
    private TextView delayEndDateLabel;
    private Button exportToTxt;
    private List<Payment> paymentList;
    private LinnearPaymentCalculations linnearPaymentCalculations;
    private AnnuityPaymentCalculations annuityPaymentCalculations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.output_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userInput = SingletonUserData.getInstance().getUserInput();

        linnearPaymentCalculations = new LinnearPaymentCalculations(userInput);
        annuityPaymentCalculations = new AnnuityPaymentCalculations(userInput);

        paymentList = (userInput.getGraphType() == GraphType.Anuiteto) ? annuityPaymentCalculations.fillList(userInput) : linnearPaymentCalculations.fillList(userInput);

        goToDataPage = findViewById(R.id.goBack);
        filter = findViewById(R.id.applyFilter);
        cancelFilter = findViewById(R.id.cancelFilter);
        pickFilterStart = findViewById(R.id.chooseFilterStart);
        pickFilterEnd = findViewById(R.id.chooseFilterEnd);
        filterStartDateLabel = findViewById(R.id.filterStartDateLabel);
        filterEndDateLabel = findViewById(R.id.filterEndDateLabel);
        applyDelay = findViewById(R.id.applyDelay);
        cancelDelay = findViewById(R.id.cancelDelay);
        pickDelayStart = findViewById(R.id.delayStart);
        pickDelayEnd = findViewById(R.id.delayEnd);
        delayStartDateLabel = findViewById(R.id.delayStartDateLabel);
        delayEndDateLabel = findViewById(R.id.delayEndDateLabel);
        paymentsTable = findViewById(R.id.paymentsTable);
        lineChart = findViewById(R.id.paymentsChart);
        exportToTxt = findViewById(R.id.exportToTxt);

        // table
        createTable();

        // graph
        createGraph();

        // export (in .txt)
        exportToTxt.setOnClickListener(v -> {
            Context context = this.getApplicationContext();
            ExportUtilities.writeStringAsFile(paymentList, "data.txt" ,context);
            Toast.makeText(getApplicationContext(), "Eksportuojama", Toast.LENGTH_SHORT).show();
        });

        // atidejimas (update graph + table)
        setDateButtonListeners(pickDelayStart, delayStartDateLabel);
        setDateButtonListeners(pickDelayEnd, delayEndDateLabel);

        applyDelay.setOnClickListener(v -> {
            if (delayStartDateLabel.getText().toString().isEmpty() || delayEndDateLabel.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Užpildykite ir atidėjimo pradžią ir pabaigą!", Toast.LENGTH_SHORT).show();
                return;
            }
            DateInterval delayDateInterval = new DateInterval(StringDateParser.parseStringToDate(delayStartDateLabel.getText().toString()), StringDateParser.parseStringToDate(delayEndDateLabel.getText().toString()));
            paymentList = (userInput.getGraphType() == GraphType.Anuiteto) ? annuityPaymentCalculations.addDelayToList(delayDateInterval) : linnearPaymentCalculations.addDelayToList(delayDateInterval);
            updateGraphAndTable();
        });
        cancelDelay.setOnClickListener(v -> {
            paymentList = (userInput.getGraphType() == GraphType.Anuiteto) ? annuityPaymentCalculations.getPayments() : linnearPaymentCalculations.getPayments();
            updateGraphAndTable();
        });

        // filter buttons
        setDateButtonListeners(pickFilterStart, filterStartDateLabel);
        setDateButtonListeners(pickFilterEnd, filterEndDateLabel);

        filter.setOnClickListener(v -> {
            if (filterStartDateLabel.getText().toString().isEmpty() || filterEndDateLabel.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Užpildykite ir filtro pradžią ir pabaigą!", Toast.LENGTH_SHORT).show();
                return;
            }
            DateInterval filterDateInterval = new DateInterval(StringDateParser.parseStringToDate(filterStartDateLabel.getText().toString()), StringDateParser.parseStringToDate(filterEndDateLabel.getText().toString()));
            paymentList = (userInput.getGraphType() == GraphType.Anuiteto) ? annuityPaymentCalculations.filterList(filterDateInterval) : linnearPaymentCalculations.filterList(filterDateInterval);
            updateGraphAndTable();
        });

        cancelFilter.setOnClickListener(v -> {
            paymentList = (userInput.getGraphType() == GraphType.Anuiteto) ? annuityPaymentCalculations.getPayments() : linnearPaymentCalculations.getPayments();
            updateGraphAndTable();
        });


        // go back
        goToDataPage.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity2.this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void updateGraphAndTable() {
        createGraph();
        createTable();
    }

    private void setDateButtonListeners(Button btn, TextView label) {
        btn.setOnClickListener(v -> {
            label.setTextIsSelectable(true);
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    MainActivity2.this,
                    (view, year1, month1, dayOfMonth) -> label.setText(dayOfMonth + "-" + (month1 + 1) + "-" + year1),
                    year, month, day);
            datePickerDialog.show();
        });
    }

    private void createGraph() {
        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.setBorderWidth(20);
        lineChart.getDescription().setText("Mokėjimai atvaizduoti grafike");
        lineChart.getAxisRight().setEnabled(false);

        List<Entry> entries = new ArrayList<>();
        int temp = 0;
        for (Payment payment : paymentList) {
            temp += 1;
            entries.add(new Entry( (float) temp, (float) payment.getTotal()));
        }

        entries.sort(new EntryXComparator());

        LineDataSet dataSet = new LineDataSet(entries, "Mokėjimai"); // add entries to dataset
        dataSet.setColor(Color.DKGRAY);
        dataSet.setValueTextColor(Color.DKGRAY); // styling, ...

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new MonthValueFormatter());
        xAxis.setGranularity(1f); // set interval to 1 (one month)
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);


        lineChart.invalidate(); // refresh
    }

    private void createTable() {
        paymentsTable.removeAllViews();

        TableRow tbrow0 = new TableRow(this);

        addHeaderCell(tbrow0, "Liko");
        addHeaderCell(tbrow0, "Data");
        addHeaderCell(tbrow0, "Kreditas");
        addHeaderCell(tbrow0, "Palūkanos");
        addHeaderCell(tbrow0, "Bendrai");
        paymentsTable.addView(tbrow0);

        for (Payment payment : paymentList) {
            TableRow tbrow = new TableRow(this);

            // Alternating row color
            int backgroundColor = (paymentsTable.getChildCount() % 2 == 0) ? Color.LTGRAY : Color.WHITE;
            tbrow.setBackgroundColor(backgroundColor);

            addDataCell(tbrow, payment.getFormattedHowMuchLeftToPay());
            addDataCell(tbrow, payment.getFormattedPaymentDate());
            addDataCell(tbrow, payment.getFormattedPrincipalAmount());
            addDataCell(tbrow, payment.getFormattedInterestAmount());
            addDataCell(tbrow, payment.getFormattedTotal());
            paymentsTable.addView(tbrow);
        }
    }

    private void addHeaderCell(TableRow row, String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextColor(Color.WHITE);
        tv.setBackgroundColor(Color.DKGRAY);
        tv.setPadding(8, 8, 8, 8);
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        row.addView(tv);
    }

    private void addDataCell(TableRow row, String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextColor(Color.BLACK);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(8, 8, 8, 8);
        row.addView(tv);
    }


}