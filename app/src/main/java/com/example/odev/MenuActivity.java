package com.example.odev;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {
    Button addQuestion, listQuestion, createExam, examSettings;
    ImageView imageViewUser;
    TextView userId,name,surname,birthDate,gsm ;
    String userName;
    DatabaseHelper db;
    User user;
    Image image;
    LocalDataManager localDataManager;
    String timeSetting,scoreSetting,diffSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        localDataManager=new LocalDataManager();
        db = new DatabaseHelper(this);
        user= new User();
        image= new Image();
        userName=getIntent().getExtras().getString("userName");
        user=db.getUser(userName);
        image=db.getImage(user);
        defineVariable();
        defineListeners();

    }

    @SuppressLint("SetTextI18n")
    public void defineVariable() {
        addQuestion = (Button) findViewById(R.id.questionAdd);
        listQuestion = (Button) findViewById(R.id.questionList);
        createExam = (Button) findViewById(R.id.examCreate);
        examSettings = (Button) findViewById(R.id.examSettings);
        imageViewUser=(ImageView) findViewById(R.id.imageViewUserMenu);
        userId = (TextView) findViewById(R.id.userIdMenu);
        name = (TextView) findViewById(R.id.nameMenu);
        surname = (TextView) findViewById(R.id.surnameMenu);
        birthDate = (TextView) findViewById(R.id.birthDateMenu);
        gsm = (TextView) findViewById(R.id.gsmMenu);
        
        userId.setText("User ID= "+user.getId());
        name.setText("Name= "+user.getName());
        surname.setText("Surname= "+user.getSurname());
        birthDate.setText("Birthdate= "+user.getBirth());
        gsm.setText("Phone= "+user.getPhone());
        imageViewUser.setImageBitmap(image.getImage());

        timeSetting=localDataManager.getSharedPreference(getApplicationContext(),"TimeVal","");
        scoreSetting=localDataManager.getSharedPreference(getApplicationContext(),"ScoreVal","");
        diffSetting=localDataManager.getSharedPreference(getApplicationContext(),"DiffVal","");

        //Toast.makeText(MenuActivity.this,timeSetting, Toast.LENGTH_SHORT).show();
    }


    public void defineListeners(){

        addQuestion.setOnClickListener((v) -> {
            Intent intent;
            intent = new Intent(v.getContext(), AddQuestionActivity.class);
            intent.putExtra("userName",userName);
            startActivity(intent);
        });
        listQuestion.setOnClickListener((v) -> {
            Intent intent;
            intent = new Intent(v.getContext(), QuestionListActivity.class);
            intent.putExtra("userName",userName);
            startActivity(intent);
        });
        createExam.setOnClickListener((v) -> {
            if (timeSetting.equals("") || scoreSetting.equals("") || diffSetting.equals("")) {

                Toast.makeText(MenuActivity.this,"Quiz Settings values cannot be null!", Toast.LENGTH_SHORT).show();
            }

            else {

                Intent intent;
                intent = new Intent(v.getContext(), CreateQuizActivity.class);
                intent.putExtra("userName", userName);
                startActivity(intent);

            }
        });
        examSettings.setOnClickListener((v) -> {
            Intent intent;
            intent = new Intent(v.getContext(), QuizSettingsActivity.class);
            intent.putExtra("userName",userName);
            startActivity(intent);
        });

    }
}