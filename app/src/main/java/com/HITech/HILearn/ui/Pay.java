package com.HITech.HILearn.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.HITech.HILearn.R;
import com.HITech.HILearn.utils.Constant;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Calendar;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.exceptions.ExpiredAccessCodeException;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

public class Pay extends AppCompatActivity {
    private EditText etCardNumber, etExpiryMonth, etExpiryYear, etCVC;

    private Charge charge;
    private Transaction transaction;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constant.setDefaultLanguage(this);
        setContentView(R.layout.activity_pay);
        PaystackSdk.initialize(getApplicationContext());
        etCardNumber = findViewById(R.id.card_number);
        etExpiryMonth = findViewById(R.id.month);
        etExpiryYear = findViewById(R.id.year);
        etCVC = findViewById(R.id.cvc);
        Button pay = findViewById(R.id.pay);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay();
            }
        });



    }
    public void pay() {
        int expiryMonth = 0;
        int expiryYear = 0;

        String cvv = etCVC.getText().toString().trim();
        String cardNumber = etCardNumber.getText().toString().trim();
        if (cardNumber.isEmpty()) {
            etCardNumber.setError("enter your card number");
            return;
        } else if (!TextUtils.isDigitsOnly(cardNumber)) {
            etCardNumber.setError("card number should be digits");
            return;
        } else if (TextUtils.isEmpty(cvv)) {
            etCardNumber.setError("enter your cvv is located at the back of you card");
            return;
        } else if (!TextUtils.isDigitsOnly(cardNumber)) {
            etCardNumber.setError("cvv should be digits");
            return;
        }else if (etExpiryMonth.getText().toString().trim().isEmpty()) {
            etExpiryMonth.setError("enter expiry month");
            return;
        }else if (etExpiryYear.getText().toString().trim().isEmpty()) {
            etExpiryYear.setError("enter expiry year");
            return;
        }
        else {
            expiryMonth = Integer.parseInt(etExpiryMonth.getText().toString());
            expiryYear = Integer.parseInt(etExpiryYear.getText().toString());
            Card card = new Card(cardNumber, expiryMonth, expiryYear, cvv);
            if (card.isValid()) {
                charge = new Charge();
                charge.setCard(card);

                dialog = new ProgressDialog(getApplicationContext());
                dialog.setCancelable(false);
                dialog.setTitle("Performing transaction... please wait");
                dialog.show();

                charge.setAmount(1000);
                charge.setEmail("nnadihenry92@gmail.com");
                charge.setReference("ChargedFromAndroid_" + Calendar.getInstance().getTimeInMillis());
                try {
                    charge.putCustomField("Charged From", "Android SDK");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                chargeCard();
                //Function to Charge user here
            } else {
                Toast.makeText(getApplicationContext(), "Invalid card details", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void dismissDialog() {
        if ((dialog != null) && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void chargeCard() {
        transaction = null;
        PaystackSdk.chargeCard(Pay.this, charge, new Paystack.TransactionCallback() {
            // This is called only after transaction is successful
            @Override
            public void onSuccess(Transaction transaction) {
                dismissDialog();

                Pay.this.transaction = transaction;
                Toast.makeText(getApplicationContext(), transaction.getReference(), Toast.LENGTH_LONG).show();
                new verifyOnServer().execute(transaction.getReference());
            }

            // This is called only before requesting OTP
            // Save reference so you may send to server if
            // error occurs with OTP
            // No need to dismiss dialog
            @Override
            public void beforeValidate(Transaction transaction) {
                Pay.this.transaction = transaction;
                Toast.makeText(Pay.this, transaction.getReference(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable error, Transaction transaction) {
                // If an access code has expired, simply ask your server for a new one
                // and restart the charge instead of displaying error
                Pay.this.transaction = transaction;
                if (error instanceof ExpiredAccessCodeException) {
                    Pay.this.chargeCard();
                    return;
                }

                dismissDialog();

                if (transaction.getReference() != null) {
                    Toast.makeText(getApplicationContext(), transaction.getReference() + " concluded with error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    new verifyOnServer().execute(transaction.getReference());
                } else {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private class verifyOnServer extends AsyncTask<String, Void, String> {
        private String reference;
        private String error;

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                Toast.makeText(getApplicationContext(), String.format("Gateway response: %s", result), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), String.format("There was a problem verifying %s on the backend: %s ", this.reference, error), Toast.LENGTH_LONG).show();
                dismissDialog();
            }
        }

        @Override
        protected String doInBackground(String... reference) {
            try {
                this.reference = reference[0];
                String json = String.format("{\"reference\":\"%s\"}", this.reference);
                String url1 = "https://www.serverdomain.com/app/verify.php?details=" + json;
                URL url = new URL(url1);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                url.openStream()));

                String inputLine;
                inputLine = in.readLine();
                in.close();
                return inputLine;
            } catch (Exception e) {
                error = e.getClass().getSimpleName() + ": " + e.getMessage();
            }
            return null;
        }
    }

}