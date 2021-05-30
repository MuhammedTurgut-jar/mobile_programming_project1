package com.example.odev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {

    Button login, sign;
    ImageView imageView;
    EditText userName, passWord;
    Integer counter;
    Boolean status;
    DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);
        defineVariable();
        defineListeners();

    }

    private void cleanText(){
        userName.setText("");
        passWord.setText("");
    }



    public void defineVariable(){
        counter=0;

        userName=(EditText) findViewById(R.id.User);
        passWord =(EditText) findViewById(R.id.Pass);
        login=(Button) findViewById(R.id.login);
        sign =(Button) findViewById(R.id.signup);
        imageView =(ImageView) findViewById(R.id.imageUser);


    }

    public void defineListeners(){
        login.setOnClickListener((v) -> {
        if(userName.getText().toString().equals("") || passWord.getText().toString().equals("") )
        {Toast.makeText(MainActivity.this,"Username and Password are cannot be null!", Toast.LENGTH_SHORT).show();}

        else {

                status = db.checkUser(userName.getText().toString(), passWord.getText().toString());

                if (status) {

                    Intent intent;
                    intent = new Intent(v.getContext(), MenuActivity.class);
                    intent.putExtra("userName",userName.getText().toString());
                    startActivity(intent);


                } else {

                    cleanText();
                    counter += 1;
                    Toast.makeText(MainActivity.this, "Username and/or Password is incorrect!", Toast.LENGTH_SHORT).show();

                    if (counter >= 3) {

                         Intent setIntent = new Intent(Intent.ACTION_MAIN);
                        setIntent.addCategory(Intent.CATEGORY_HOME);
                        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(setIntent);


                    }
                }

        }
        });

        sign.setOnClickListener((v) -> {
            cleanText();
            Intent intent;
            intent = new Intent(v.getContext(), SignUpActivity.class);
            startActivity(intent);
        });

    }
}