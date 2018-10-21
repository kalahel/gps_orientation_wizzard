package com.example.mat.gps_orientation_wizzard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button computeButton;
    private EditText wpLatitudeText, wpLongitudeText;
    private TextView resultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.computeButton = (Button) findViewById(R.id.computeButton);
        this.wpLatitudeText = (EditText) findViewById(R.id.wpLatitudeText);
        this.wpLongitudeText = (EditText) findViewById(R.id.wpLongitudeText);
        this.resultView = (TextView) findViewById(R.id.resultView);
    }

    public void computeVector(View view) {
        // TODO REMPLACE FIRST TWO VALUE BY ACTUAL GPS VALUES
        double resultVector[] = VectorGenerator.computeVector(
                2.1547299000000066,
                49.022697,
                Double.valueOf(this.wpLongitudeText.getText().toString()),
                Double.valueOf(this.wpLatitudeText.getText().toString())
        );
        String displayedString = "Longitude : " + resultVector[0] + "\nLatitude :" + resultVector[1];
        this.resultView.setText(displayedString);
    }
}
