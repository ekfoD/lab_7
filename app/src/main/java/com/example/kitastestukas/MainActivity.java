package com.example.kitastestukas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kitastestukas.Models.GraphType;
import com.example.kitastestukas.Models.UserInput;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private EditText yearField, monthField, amountField, annualPercentField;
    private Spinner graphChoiceSpinner;
    private Button goToNextPageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.input_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        yearField = findViewById(R.id.editYearAmount);
        monthField = findViewById(R.id.editMonthAmount);
        amountField = findViewById(R.id.editAmount);
        annualPercentField = findViewById(R.id.editAnnualPercent);
        graphChoiceSpinner = findViewById(R.id.graphSelection);

        // button listeneris
        goToNextPageButton = findViewById(R.id.submit_data);
        goToNextPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yearField.getText().toString().isEmpty() || monthField.getText().toString().isEmpty() ||
                        amountField.getText().toString().isEmpty() || annualPercentField.getText().toString().isEmpty() ||
                        graphChoiceSpinner.getSelectedItem().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Užpildykite visus laukus!", Toast.LENGTH_SHORT).show();
                }
                else {
                    int year = Integer.parseInt(yearField.getText().toString());
                    int month = Integer.parseInt(monthField.getText().toString());
                    double amount = Double.parseDouble(amountField.getText().toString());
                    double annualPercent = Double.parseDouble(annualPercentField.getText().toString());
                    GraphType graphType = Objects.equals((String) graphChoiceSpinner.getSelectedItem(), "Linijinis") ? GraphType.Linijinis : GraphType.Anuiteto;

                    SingletonUserData.getInstance().setUserInput(new UserInput(
                            amount, annualPercent, year, month, graphType));

                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                    startActivity(intent);
                }
            }
        });
    }
}