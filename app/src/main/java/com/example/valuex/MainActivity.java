package com.example.valuex;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private EditText salaryInput, priceInput, hoursInput, balanceInput;
    private TextView timeResultText, balanceResultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        salaryInput = findViewById(R.id.salaryInput);
        priceInput = findViewById(R.id.priceInput);
        hoursInput = findViewById(R.id.hoursInput);
        balanceInput = findViewById(R.id.balanceInput);
        Button calculateButton = findViewById(R.id.calculateButton);
        timeResultText = findViewById(R.id.timeResultText);
        balanceResultText = findViewById(R.id.balanceResultText);

        // Set up calculate button click listener
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateProductValue();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void calculateProductValue() {
        try {
            // Get values from input fields
            String salaryStr = salaryInput.getText().toString();
            String priceStr = priceInput.getText().toString();
            String hoursStr = hoursInput.getText().toString();
            String balanceStr = balanceInput.getText().toString();

            // Check if required fields are filled
            if (salaryStr.isEmpty() || priceStr.isEmpty()) {
                timeResultText.setText("Please enter both salary and product price");
                balanceResultText.setText("");
                return;
            }

            // Convert to numbers
            double monthlySalary = Double.parseDouble(salaryStr);
            double productPrice = Double.parseDouble(priceStr);
            double monthlyHours = hoursStr.isEmpty() ? 160 : Double.parseDouble(hoursStr);

            // Calculate hourly rate and time cost
            double hourlyRate = monthlySalary / monthlyHours;
            double hoursForProduct = productPrice / hourlyRate;

            // Format time result
            String timeResult = formatTimeResult(hoursForProduct, hourlyRate);
            timeResultText.setText(timeResult);
            timeResultText.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_light));

            // Calculate and display balance percentage if balance is provided
            if (!balanceStr.isEmpty()) {
                double balance = Double.parseDouble(balanceStr);
                double percentage = (productPrice / balance) * 100;

                String balanceResult = formatBalanceResult(percentage, productPrice, balance);
                balanceResultText.setText(balanceResult);

                // Set text color based on percentage
                if (percentage > 10) {
                    balanceResultText.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_light));
                } else if (percentage > 5) {
                    balanceResultText.setTextColor(ContextCompat.getColor(this, android.R.color.holo_orange_light));
                } else {
                    balanceResultText.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_light));
                }
            } else {
                balanceResultText.setText("💡 Add your savings balance to see percentage impact");
                balanceResultText.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));
            }

        } catch (NumberFormatException e) {
            timeResultText.setText("❌ Please enter valid numbers");
            timeResultText.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_light));
            balanceResultText.setText("");
        } catch (Exception e) {
            timeResultText.setText("❌ Error in calculation");
            timeResultText.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_light));
            balanceResultText.setText("");
        }
    }

    @SuppressLint("DefaultLocale")
    private String formatTimeResult(double hoursForProduct, double hourlyRate) {
        StringBuilder result = new StringBuilder();

        result.append("⏰ Time Cost:\n");

        if (hoursForProduct < 1) {
            int minutes = (int) (hoursForProduct * 60);
            if (minutes < 1) {
                result.append(String.format("Only %d seconds of work", (int)(hoursForProduct * 3600)));
            } else {
                result.append(String.format("%d minutes of work", minutes));
            }
        } else if (hoursForProduct == 1) {
            result.append("1 hour of work");
        } else if (hoursForProduct < 8) {
            result.append(String.format("%.1f hours of work", hoursForProduct));
        } else {
            int days = (int) (hoursForProduct / 8);
            double remainingHours = hoursForProduct % 8;
            if (remainingHours > 0) {
                result.append(String.format("%d days %.1f hours of work", days, remainingHours));
            } else {
                result.append(String.format("%d days of work", days));
            }
        }

        result.append(String.format("\n💰 Hourly rate: %.2f TND/hour", hourlyRate));
        return result.toString();
    }

    @SuppressLint("DefaultLocale")
    private String formatBalanceResult(double percentage, double productPrice, double balance) {
        StringBuilder result = new StringBuilder();

        result.append("🏦 Savings Impact:\n");
        result.append(String.format("%.1f%% of your savings", percentage));

        if (percentage > 50) {
            result.append("\n🚨 Very significant purchase!");
        } else if (percentage > 25) {
            result.append("\n⚠️ Consider carefully");
        } else if (percentage > 10) {
            result.append("\n🔶 Moderate impact");
        } else if (percentage > 5) {
            result.append("\n🔸 Small impact");
        } else {
            result.append("\n✅ Minimal impact");
        }

        result.append(String.format("\n💵 Price: %.0f TND | Balance: %.0f TND", productPrice, balance));

        return result.toString();
    }
}