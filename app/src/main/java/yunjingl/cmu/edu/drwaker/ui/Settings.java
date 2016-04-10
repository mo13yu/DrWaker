package yunjingl.cmu.edu.drwaker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import yunjingl.cmu.edu.drwaker.R;

public class Settings extends AppCompatActivity {
    private List<String> list = new ArrayList<String>();
    private TextView myTextView;
    private Spinner mySpinner;
    private ArrayAdapter<CharSequence> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
//        mySpinner = (Spinner)findViewById(R.id.hour);
//        adapter=ArrayAdapter.createFromResource(this,R.array.hour, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mySpinner.setAdapter(adapter);
        Button newloc=(Button)findViewById(R.id.newlocbutton);
        newloc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewParam) {
                Intent intent=new Intent(getApplicationContext(),SetLocation.class);
                startActivity(intent);
            }
        });
        Button done=(Button)findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewParam) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        Button delete=(Button)findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewParam) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
