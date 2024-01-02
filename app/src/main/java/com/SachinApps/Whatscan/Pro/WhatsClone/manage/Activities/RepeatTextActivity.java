package com.SachinApps.Whatscan.Pro.WhatsClone.manage.Activities;

import static com.SachinApps.Whatscan.Pro.WhatsClone.manage.GoogleAds.AdsDecalration.loadNative;
import static com.SachinApps.Whatscan.Pro.WhatsClone.manage.GoogleAds.AdsDecalration.mNativeAd;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.ads.nativetemplates.TemplateView;
import com.SachinApps.Whatscan.Pro.WhatsClone.R;

public class RepeatTextActivity extends AppCompatActivity {

    String Maintext;
    int NoofRepeat;
    String RepeatText;
    Button clearTxtBtn;
    Button convertButton;
    EditText convertedText;
    Button btnCopy;
    EditText emojeeText;
    ImageView imNewLine;
    EditText txtInput;
    boolean isNewLine = false;
    String no;
    ProgressDialog pDialog;
    Button btnShare;
    TextView txtNewLine;

    TemplateView templateView;


    //Click event of Button Convert
    private class btnConverListner implements View.OnClickListener {
        public void onClick(View view) {
            RepeatTextActivity.this.convertedText.setText("");
            RepeatTextActivity.this.RepeatText = RepeatTextActivity.this.txtInput.getText().toString();
            RepeatTextActivity.this.no = RepeatTextActivity.this.emojeeText.getText().toString();
            try {
                RepeatTextActivity.this.NoofRepeat = Integer.parseInt(RepeatTextActivity.this.no);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
            if (RepeatTextActivity.this.txtInput.getText().toString().isEmpty()) {
                Toast.makeText(RepeatTextActivity.this.getApplicationContext(), "Enter Repeat Text", Toast.LENGTH_SHORT).show();
            } else if (RepeatTextActivity.this.emojeeText.getText().toString().isEmpty()) {
                Toast.makeText(RepeatTextActivity.this.getApplicationContext(), "Enter Number of Repeat Text", Toast.LENGTH_SHORT).show();
            } else if (RepeatTextActivity.this.NoofRepeat <= 10000) {
                new CreateRepeateText().execute();
            } else {
                Toast.makeText(RepeatTextActivity.this.getApplicationContext(), "Number of Repeter Text Limited Please Enter Limited Number", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Click event of Button Clear
    private class btnClearTextListner implements View.OnClickListener {
        public void onClick(View view) {
            RepeatTextActivity.this.convertedText.setText("");
        }
    }


    //Listener of while converted text
    private class btnConvertedTexListner implements View.OnClickListener {
        @SuppressLint({"WrongConstant"})
        public void onClick(View view) {
            if (!RepeatTextActivity.this.convertedText.getText().toString().isEmpty()) {
                ((ClipboardManager) RepeatTextActivity.this.getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText(RepeatTextActivity.this.txtInput.getText().toString(), RepeatTextActivity.this.convertedText.getText().toString()));
                Toast.makeText(RepeatTextActivity.this.getApplicationContext(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Click event of Button Copy
    private class btnCopyListner implements View.OnClickListener {
        @SuppressLint({"WrongConstant"})
        public void onClick(View view) {
            if (RepeatTextActivity.this.convertedText.getText().toString().isEmpty()) {
                Toast.makeText(RepeatTextActivity.this.getApplicationContext(), "Convert text before copy", Toast.LENGTH_SHORT).show();
                return;
            }
            ((ClipboardManager) RepeatTextActivity.this.getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText(RepeatTextActivity.this.txtInput.getText().toString(), RepeatTextActivity.this.convertedText.getText().toString()));
            Toast.makeText(RepeatTextActivity.this.getApplicationContext(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
        }
    }


    //Click event of Button Share
    private class btnShareListner implements View.OnClickListener {
        public void onClick(View view) {
            if (RepeatTextActivity.this.convertedText.getText().toString().isEmpty()) {
                Toast.makeText(RepeatTextActivity.this.getApplicationContext(), "Please Convert text to share", Toast.LENGTH_LONG).show();
                return;
            }
            Intent shareIntent = new Intent();
            shareIntent.setAction("android.intent.action.SEND");
            shareIntent.setPackage("com.whatsapp");
            shareIntent.putExtra("android.intent.extra.TEXT", RepeatTextActivity.this.convertedText.getText().toString());
            shareIntent.setType("text/plain");
            RepeatTextActivity.this.startActivity(Intent.createChooser(shareIntent, "Select an app to share"));
        }
    }

    //Create repeat text in background
    private class CreateRepeateText extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            RepeatTextActivity.this.pDialog.setMessage("Please Wait...");
            RepeatTextActivity.this.pDialog.setProgressStyle(0);
            RepeatTextActivity.this.pDialog.setCancelable(false);
            RepeatTextActivity.this.pDialog.show();
        }

        public String doInBackground(String... strings) {
            int i;
            if (RepeatTextActivity.this.isNewLine) {
                for (i = 1; i <= RepeatTextActivity.this.NoofRepeat; i++) {
                    if (i == 1) {
                        RepeatTextActivity.this.Maintext = RepeatTextActivity.this.RepeatText;
                    } else {
                        RepeatTextActivity.this.Maintext += "\n" + RepeatTextActivity.this.RepeatText;
                    }
                }
            } else {
                for (i = 1; i <= RepeatTextActivity.this.NoofRepeat; i++) {
                    if (i == 1) {
                        RepeatTextActivity.this.Maintext = RepeatTextActivity.this.RepeatText;
                    } else {
                        RepeatTextActivity.this.Maintext += "\t" + RepeatTextActivity.this.RepeatText;
                    }
                }
            }
            return null;
        }

        @SuppressLint({"LongLogTag"})
        public void onPostExecute(String result) {
            RepeatTextActivity.this.pDialog.dismiss();
            RepeatTextActivity.this.convertedText.setText(RepeatTextActivity.this.Maintext);
        }
    }

    //Switch of New line is on or off
    private class newLineClick implements View.OnClickListener {

        public void onClick(View v) {
            if (RepeatTextActivity.this.isNewLine) {
                RepeatTextActivity.this.isNewLine = false;
                RepeatTextActivity.this.txtNewLine.setText("New Line Off");
                RepeatTextActivity.this.imNewLine.setImageResource(R.drawable.baseline_toggle_off_24);
                return;
            }
            RepeatTextActivity.this.isNewLine = true;
            RepeatTextActivity.this.txtNewLine.setText("New Line On");
            RepeatTextActivity.this.imNewLine.setImageResource(R.drawable.ons);
        }
    }
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_text_repeater);
        this.pDialog = new ProgressDialog(this);
        this.txtNewLine = findViewById(R.id.txtNewLine);
        this.imNewLine = findViewById(R.id.btnNewLine);
        if (this.isNewLine) {
            this.txtNewLine.setText("New Line On");
            this.imNewLine.setImageResource(R.drawable.ons);
        } else {
            this.txtNewLine.setText("New Line Off");
            this.imNewLine.setImageResource(R.drawable.baseline_toggle_off_24);
        }

        templateView = findViewById(R.id.TemplateView);

        loadNative(this);

        if (mNativeAd != null){
            templateView.setVisibility(View.VISIBLE);
            templateView.setNativeAd(mNativeAd);
        }

        this.imNewLine.setOnClickListener(new newLineClick());
        this.txtInput = findViewById(R.id.inputText);
        this.emojeeText = findViewById(R.id.emojeeTxt);
        this.convertedText = findViewById(R.id.convertedEmojeeTxt);
        this.convertButton = findViewById(R.id.convertEmojeeBtn);
        this.btnCopy = findViewById(R.id.copyTxtBtn);
        this.btnShare = findViewById(R.id.shareTxtBtn);
        this.clearTxtBtn = findViewById(R.id.clearTxtBtn);
        this.convertButton.setOnClickListener(new btnConverListner());
        this.clearTxtBtn.setOnClickListener(new btnClearTextListner());
        this.convertedText.setOnClickListener(new btnConvertedTexListner());
        this.btnCopy.setOnClickListener(new btnCopyListner());
        this.btnShare.setOnClickListener(new btnShareListner());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
