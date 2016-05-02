package yunjingl.cmu.edu.drwaker.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import yunjingl.cmu.edu.drwaker.R;

/**
 * detect whether answer input by user is correct, if yes then stop alarm.
 */
public class MathActivity extends AppCompatActivity {
    private TextView topLine, mathQuestion;
    private int result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);

        final String question=(getIntent().getExtras()).getString("question");
        final String answer=(getIntent().getExtras()).getString("answer");

        topLine=(TextView)findViewById(R.id.textView);
        mathQuestion=(TextView)findViewById(R.id.textView3);
        mathQuestion.setText(question);
        Button okmath=(Button)findViewById(R.id.okmath);
        okmath.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewParam) {
                final Intent myIntent = new Intent(MathActivity.this, RingtonePlayingService.class);
                result=Integer.parseInt(((EditText) findViewById(R.id.editText)).getText().toString());
                if(result==Integer.parseInt(answer)){
                    myIntent.putExtra("extra", "off");
                    startService(myIntent);
                    finish();
                }
                else{
                    AlertDialog.Builder builder=new AlertDialog.Builder(MathActivity.this);
                    builder.setTitle("Attention");
                    builder.setMessage("Your answer didn't pass the detection, Please try again! ");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create();
                    builder.show();
                }
            }
        });

    }


}
