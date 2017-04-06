package com.example.litan.myapplication;

import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private ImageButton loginbutton;
    private MarqueueText Marqueuetext;
    private Button change;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginbutton = (ImageButton) findViewById(R.id.button_1);
        Marqueuetext = (MarqueueText) findViewById(R.id.TextView2);
        change = (Button) findViewById(R.id.change_button);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,page.class);
                startActivity(i);
            }
        });
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Marqueuetext.setSingleLine();
                Toast.makeText(MainActivity.this, "button_1 clicked",Toast.LENGTH_SHORT).show();
            }
        });


    }

}
