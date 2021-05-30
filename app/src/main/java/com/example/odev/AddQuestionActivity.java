package com.example.odev;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddQuestionActivity extends AppCompatActivity {

    Button saveQuestion;
    ImageView imageViewUser;
    TextView userId,name,surname,birthDate,gsm ;
    EditText questionText,A,B,C,D;
    Spinner answer,diff;
    String userName;
    DatabaseHelper db;
    User user;
    Image image;
    Question question;
    String spinnerAnswer, spinnerDiff;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        db = new DatabaseHelper(this);
        user= new User();
        image= new Image();
        userName=getIntent().getExtras().getString("userName");
        user=db.getUser(userName);
        image=db.getImage(user);

        defineVariable();
        defineListeners();
    }

    public boolean textCheck()
    {
        if(questionText.getText().toString().equals("") || A.getText().toString().equals("") || B.getText().toString().equals("") || C.getText().toString().equals("") || D.getText().toString().equals("") || spinnerAnswer.equals("")|| spinnerDiff.equals("") )
            return false;

        else
            return true;

    }
    @SuppressLint("SetTextI18n")
    public void defineVariable() {
        saveQuestion = (Button) findViewById(R.id.addQuestion);
        questionText = (EditText) findViewById(R.id.questionTextAdd);
        A = (EditText) findViewById(R.id.AAdd);
        B = (EditText) findViewById(R.id.BAdd);
        C = (EditText) findViewById(R.id.CAdd);
        D = (EditText) findViewById(R.id.DAdd);
        answer = (Spinner) findViewById(R.id.spinnerAnswerAdd);
        diff = (Spinner) findViewById(R.id.spinnerDifficultyAdd);
        imageViewUser=(ImageView) findViewById(R.id.imageViewUserAdd);
        userId = (TextView) findViewById(R.id.userIdAdd);
        name = (TextView) findViewById(R.id.nameAdd);
        surname = (TextView) findViewById(R.id.surnameAdd);
        birthDate = (TextView) findViewById(R.id.birthdateAdd);
        surname = (TextView) findViewById(R.id.surnameAdd);
        gsm = (TextView) findViewById(R.id.gsmAdd);

        userId.setText("User ID= "+user.getId());
        name.setText("Name= "+user.getName());
        surname.setText("Surname= "+user.getSurname());
        birthDate.setText("Birthdate= "+user.getBirth());
        gsm.setText("Phone= "+user.getPhone());
        imageViewUser.setImageBitmap(image.getImage());

        String[] items = new String[]{"A", "B", "C", "D"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item,items);
        answer.setAdapter(adapter);

        String[] items2 = new String[]{"1", "2", "3", "4","5"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.spinner_item,items2);
        diff.setAdapter(adapter2);


    }

    public void defineListeners(){
        saveQuestion.setOnClickListener((v) -> {
            spinnerAnswer = answer.getSelectedItem().toString();
            spinnerDiff = diff.getSelectedItem().toString();

            boolean status = textCheck();

            if (status)
            {
                question=new Question();
                question.setQuestion(questionText.getText().toString());
                question.setA(A.getText().toString());
                question.setB(B.getText().toString());
                question.setC(C.getText().toString());
                question.setD(D.getText().toString());
                question.setAnswer(spinnerAnswer);
                question.setLevel(spinnerDiff);
                db.addQuestion(question);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setTitle("Done!");

                alertDialogBuilder
                        .setMessage("Do you want add another question?")
                        .setCancelable(false)
                        .setIcon(R.mipmap.ic_launcher_round)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent;
                                intent = new Intent(v.getContext(), AddQuestionActivity.class);
                                intent.putExtra("userName",userName);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent;
                                intent = new Intent(v.getContext(), MenuActivity.class);
                                intent.putExtra("userName",userName);
                                startActivity(intent);
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }

            else{
                Toast.makeText(AddQuestionActivity.this, "All fields must be filled!", Toast.LENGTH_SHORT).show();
            }



        });

    }
}