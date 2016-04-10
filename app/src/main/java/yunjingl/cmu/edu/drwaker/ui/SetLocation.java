package yunjingl.cmu.edu.drwaker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import yunjingl.cmu.edu.drwaker.R;

public class SetLocation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);
        Button save=(Button)findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewParam) {
                Intent intent=new Intent(getApplicationContext(),Settings.class);
                startActivity(intent);
            }
        });
    }

}
