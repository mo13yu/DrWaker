package yunjingl.cmu.edu.drwaker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import yunjingl.cmu.edu.drwaker.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addnew=(Button)findViewById(R.id.okmath);
        addnew.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewParam) {
                Intent intent=new Intent(getApplicationContext(),Settings.class);
                startActivity(intent);
            }
        });
        Button test=(Button)findViewById(R.id.map);
        test.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewParam) {
                Intent intent=new Intent(getApplicationContext(),SetLocation.class);
                startActivity(intent);
            }
        });
        Button math=(Button)findViewById(R.id.math);
        math.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewParam) {
                Intent intent=new Intent(getApplicationContext(),MathActivity.class);
                startActivity(intent);
            }
        });
        Button selfie=(Button)findViewById(R.id.selfie);
        selfie.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewParam) {
                Intent intent=new Intent(getApplicationContext(),SelfieActivity.class);
                startActivity(intent);
            }
        });
    }

    public void initialize(){

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
