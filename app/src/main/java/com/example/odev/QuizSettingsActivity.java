package com.example.odev;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class QuizSettingsActivity extends AppCompatActivity {
    Button update,save;
    ImageView imageViewUser;
    TextView userId,name,surname,birthDate,gsm ;
    EditText timeSet, scoreSet;
    String userName,spinnerDiff;
    DatabaseHelper db;
    User user;
    Image image;
    Spinner diff;
    LocalDataManager localDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_settings);
        db = new DatabaseHelper(this);
        localDataManager=new LocalDataManager();
        user= new User();
        image=new Image();
        userName=getIntent().getExtras().getString("userName");
        user=db.getUser(userName);
        image=db.getImage(user);

        defineVariable();
        defineListeners();
    }

    @SuppressLint("SetTextI18n")
    public void defineVariable() {
        save = (Button) findViewById(R.id.saveSet);
        update = (Button) findViewById(R.id.updateSet);

        imageViewUser=(ImageView) findViewById(R.id.imageViewUserSet);
        userId = (TextView) findViewById(R.id.userIdSet);
        name = (TextView) findViewById(R.id.nameSet);
        surname = (TextView) findViewById(R.id.surnameSet);
        birthDate = (TextView) findViewById(R.id.birthDateSet);
        gsm = (TextView) findViewById(R.id.gsmSet);
        diff = (Spinner) findViewById(R.id.spinnerDifficultySet);
        timeSet = (EditText) findViewById(R.id.timeSet);
        scoreSet = (EditText) findViewById(R.id.scoreSet);

        userId.setText("User ID= "+user.getId());
        name.setText("Name= "+user.getName());
        surname.setText("Surname= "+user.getSurname());
        birthDate.setText("Birthdate= "+user.getBirth());
        gsm.setText("Phone= "+user.getPhone());
        imageViewUser.setImageBitmap(image.getImage());

        timeSet.setText(localDataManager.getSharedPreference(getApplicationContext(),"TimeVal",""));
        scoreSet.setText(localDataManager.getSharedPreference(getApplicationContext(),"ScoreVal",""));

        String[] items = new String[]{"1", "2", "3", "4","5"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item,items);
        diff.setAdapter(adapter);
        diff.setSelection(adapter.getPosition(localDataManager.getSharedPreference(getApplicationContext(),"DiffVal","")));

        save.setEnabled(false);
        update.setEnabled(true);
        timeSet.setEnabled(false);
        scoreSet.setEnabled(false);
        diff.setEnabled(false);

    }

    public void defineListeners(){
        save.setOnClickListener((v) -> {
            spinnerDiff = diff.getSelectedItem().toString();
            /*
            Toast.makeText(QuizSettingsActivity.this,timeSet.getText().toString(), Toast.LENGTH_SHORT).show();
            Toast.makeText(QuizSettingsActivity.this,scoreSet.getText().toString(), Toast.LENGTH_SHORT).show();
            Toast.makeText(QuizSettingsActivity.this,spinnerDiff, Toast.LENGTH_SHORT).show();
            */
            if (timeSet.getText().toString().equals("") || scoreSet.getText().toString().equals("") || spinnerDiff.equals("")) {
                Toast.makeText(QuizSettingsActivity.this,"Quiz Settings values cannot be null!", Toast.LENGTH_SHORT).show();
            }

            else{
                
                localDataManager.setSharedPreference(getApplicationContext(), "TimeVal", timeSet.getText().toString());
                localDataManager.setSharedPreference(getApplicationContext(), "ScoreVal", scoreSet.getText().toString());
                localDataManager.setSharedPreference(getApplicationContext(), "DiffVal", spinnerDiff);

                update.setEnabled(true);
                save.setEnabled(false);
                timeSet.setEnabled(false);
                scoreSet.setEnabled(false);
                diff.setEnabled(false);

                Toast.makeText(QuizSettingsActivity.this,"Quiz Settings values updated!", Toast.LENGTH_SHORT).show();

                Intent intent;
                intent = new Intent(v.getContext(), MenuActivity.class);
                intent.putExtra("userName",userName);
                startActivity(intent);


            }
        });
        update.setOnClickListener((v) -> {

            save.setEnabled(true);
            update.setEnabled(false);
            timeSet.setEnabled(true);
            scoreSet.setEnabled(true);
            diff.setEnabled(true);

        });



    }
}