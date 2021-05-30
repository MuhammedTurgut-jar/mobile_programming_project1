package com.example.odev;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;


public class CreateQuizActivity extends AppCompatActivity implements MyRecyclerViewAdapter2.ItemClickListener {

    MyRecyclerViewAdapter2 adapter2;
    DatabaseHelper db;
    ArrayList<Question> mailQuestions,questions;
    String userName;
    LocalDataManager localDataManager;
    String timeSetting, scoreSetting;
    Integer diffSetting;
    EditText time, score;
    Button createQuiz, mainMenu;
    public final Context context=this;
    String quizString, answerKeys;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        userName=getIntent().getExtras().getString("userName");
        user =new User();
        questions=new ArrayList<>();
        mailQuestions=new ArrayList<>();
        localDataManager=new LocalDataManager();
        db = new DatabaseHelper(this);

        defineVariable();
        defineListeners();
    }


    @Override
    public void onItemClick(View view, int position) {
        int question_id = adapter2.getItem(position);
        //Toast.makeText(CreateQuizActivity.this,"Id="+adapter2.getItem(position)+"question added Question List", Toast.LENGTH_SHORT).show();
        mailQuestions.add(db.getQuestion(question_id));
        Toast.makeText(CreateQuizActivity.this,"Quiz question count="+mailQuestions.size(), Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onItemClick2(View view, int position) {
        int question_id = adapter2.getItem(position);
        //Toast.makeText(CreateQuizActivity.this,"Id="+adapter2.getItem(position)+"question removed Question List", Toast.LENGTH_SHORT).show();
        for(int i=0; i<mailQuestions.size();i++)
        {
            if(mailQuestions.get(i).getId()==question_id)
            {
                mailQuestions.remove(i);
            }

        }
        Toast.makeText(CreateQuizActivity.this,"Quiz question count="+mailQuestions.size(), Toast.LENGTH_SHORT).show();
    }

    public void savePrivately(String quizString) {

        File folder = getExternalFilesDir("Quiz");

        File file = new File(folder, "quiz.txt");

        writeTextData(file, quizString);

    }

    public void sendMail(File fileLocation ) {

        Uri uri = FileProvider.getUriForFile(CreateQuizActivity.this, BuildConfig.APPLICATION_ID + ".provider",fileLocation);

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


        emailIntent .setType("vnd.android.cursor.dir/email");
        String to[] = {"muhammed.turgut@outlook.com.tr"};
        emailIntent .putExtra(Intent.EXTRA_EMAIL, to);

        emailIntent .putExtra(Intent.EXTRA_STREAM, uri);

        emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Quiz");
        startActivity(Intent.createChooser(emailIntent , "Send email..."));
    }


    private void writeTextData(File file, String data) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(data.getBytes());
           // Toast.makeText(this, "Done" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        sendMail(file);
    }


    public void defineVariable(){

        user=db.getUser(userName);

        createQuiz = (Button) findViewById(R.id.createQuiz);
        mainMenu = (Button) findViewById(R.id.bMenu);
        time = (EditText) findViewById(R.id.editTextTime);
        score = (EditText) findViewById(R.id.editTextScore);

        timeSetting=localDataManager.getSharedPreference(getApplicationContext(),"TimeVal","");
        scoreSetting=localDataManager.getSharedPreference(getApplicationContext(),"ScoreVal","");
        diffSetting=Integer.parseInt(localDataManager.getSharedPreference(getApplicationContext(),"DiffVal",""));

        time.setText(timeSetting);
        score.setText(scoreSetting);

        questions=db.getQuizQuestions(diffSetting);
        RecyclerView recyclerView2 = findViewById(R.id.recycler2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        adapter2 = new MyRecyclerViewAdapter2(this, questions);
        adapter2.setClickListener(this);
        recyclerView2.setAdapter(adapter2);

    }

    public void defineListeners(){

        mainMenu.setOnClickListener((v) -> {
            Intent intent;
            intent = new Intent(v.getContext(), MenuActivity.class);
            intent.putExtra("userName",userName);
            startActivity(intent);
        });
        createQuiz.setOnClickListener((v) -> {


            if (!time.getText().toString().equals("") && !score.getText().toString().equals("")&& mailQuestions.size()!=0) {

                Date nowDate = new Date();
                System.out.println(nowDate.toString());
                quizString="Quiz created by "+ user.getName()+" "+ user.getSurname()+"\t Created Date="+nowDate.toString()+"\t\t Exam time="+ time.getText().toString() +" Minutes" +"\n\n";

                for(int i=0; i<mailQuestions.size();i++)
                {
                    String questionNumber=String.valueOf(i+1);
                    quizString+="Question "+ questionNumber +") "+mailQuestions.get(i).getQuestion()+"\t("+score.getText().toString()+" Points"+")\n"+
                            "A)"+mailQuestions.get(i).getA()+"\n"+
                            "B)"+mailQuestions.get(i).getB()+"\n"+
                            "C)"+mailQuestions.get(i).getC()+"\n"+
                            "D)"+mailQuestions.get(i).getD()+"\n\n";
                }
                answerKeys="\n\n\n Answer Keys: ";
                for(int i=0; i<mailQuestions.size();i++)
                {
                    String questionNumber=String.valueOf(i+1);
                    
                    answerKeys+=questionNumber +") "+mailQuestions.get(i).getAnswer()+"| ";
                }

                quizString+=answerKeys;

                savePrivately(quizString);

            }

            else{ Toast.makeText(CreateQuizActivity.this,"Quiz Time, Question Score and Question List are cannot be null!", Toast.LENGTH_SHORT).show(); }


        });
    }

}