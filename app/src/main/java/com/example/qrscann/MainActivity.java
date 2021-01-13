package com.example.qrscann;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton =findViewById(R.id.startButton);
    }

    @Override
    public void onClick(View v) {
        scanCode();
    }

    private  void  scanCode(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(Capture.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int request, int outcome, Intent data) {
        final IntentResult result = IntentIntegrator.parseActivityResult(request, outcome, data);
        if (result != null) {
           if (result.getContents() != null){
               AlertDialog.Builder builder = new AlertDialog.Builder(this);
               builder.setMessage(result.getContents());
               builder.setTitle("Result");
               builder.setPositiveButton("Again", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       scanCode();
                   }
               }).setNegativeButton("Search", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(result.toString()));
                       startActivity(browserIntent);
                   }
               });
               AlertDialog dialog = builder.create();
               dialog.show();
           }
           else {
               Toast.makeText(this, "No results", Toast.LENGTH_LONG).show();
           }
        }
        else {
            super.onActivityResult(outcome, request, data);
        }
    }
}