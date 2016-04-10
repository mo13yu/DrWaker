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

public class MathActivity extends AppCompatActivity {
    private final String question="3+9=";
    private EditText answer;
    private String mathAnswer;
    private TextView topLine, mathQuestion;
    private int result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);
        topLine=(TextView)findViewById(R.id.textView);
        mathQuestion=(TextView)findViewById(R.id.textView3);
        mathQuestion.setText(question);
        Button okmath=(Button)findViewById(R.id.okmath);
        okmath.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewParam) {

                result=Integer.parseInt(((EditText) findViewById(R.id.editText)).getText().toString());
                if(result==12){
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);

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
