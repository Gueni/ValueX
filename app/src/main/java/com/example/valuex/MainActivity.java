
package com.example.valuex;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText salaryInput, priceInput, hoursInput;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        salaryInput = findViewById(R.id.salaryInput);
        priceInput = findViewById(R.id.priceInput);
        hoursInput = findViewById(R.id.hoursInput);
        Button calculateButton = findViewById(R.id.calculateButton);
        resultText = findViewById(R.id.resultText);

        // Set up calculate button click listener
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateProductValueInHours();
            }
        });
    }

    private void calculateProductValueInHours() {
        try {
            // Get values from input fields
            String salaryStr = salaryInput.getText().toString();
            String priceStr = priceInput.getText().toString();
            String hoursStr = hoursInput.getText().toString();

            // Check if required fields are filled
            if (salaryStr.isEmpty() || priceStr.isEmpty()) {
                resultText.setText("Please enter both salary and product price");
                return;
            }

            // Convert to numbers
            double monthlySalary = Double.parseDouble(salaryStr);
            double productPrice = Double.parseDouble(priceStr);
            double monthlyHours = hoursStr.isEmpty() ? 160 : Double.parseDouble(hoursStr);

            // Calculate hourly rate
            double hourlyRate = monthlySalary / monthlyHours;

            // Calculate how many hours the product costs
            double hoursForProduct = productPrice / hourlyRate;

            // Format the result
            String result;
            if (hoursForProduct < 1) {
                // Convert to minutes if less than 1 hour
                int minutes = (int) (hoursForProduct * 60);
                result = String.format("This product costs you: %d minutes", minutes);
            } else if (hoursForProduct == 1) {
                result = "This product costs you: 1 hour";
            } else {
                result = String.format("This product costs you: %.1f hours", hoursForProduct);
            }

            // Show result
            resultText.setText(result);

        } catch (NumberFormatException e) {
            resultText.setText("Please enter valid numbers");
        } catch (Exception e) {
            resultText.setText("Error in calculation");
        }
    }
}