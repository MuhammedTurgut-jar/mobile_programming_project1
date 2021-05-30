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

public class UpsertQuestionActivity extends AppCompatActivity {
    Button updateQuestion;
    ImageView imageViewUser;
    TextView userId,name,surname,birthDate,gsm ;
    EditText questionText,A,B,C,D;
    Spinner answer,diff;
    String userName;
    int question_id;
    DatabaseHelper db;
    User user;
    Image image;
    Question question;
    String spinnerAnswer, spinnerDiff;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upsert_question);
        db = new DatabaseHelper(this);
        user= new User();
        image= new Image();
        question=new Question();
        userName=getIntent().getExtras().getString("userName");
        question_id=getIntent().getExtras().getInt("question");

        user=db.getUser(userName);
        image=db.getImage(user);
        question=db.getQuestion(question_id);

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
        updateQuestion = (Button) findViewById(R.id.updateQuestion);
        questionText = (EditText) findViewById(R.id.questionTextUp);
        A = (EditText) findViewById(R.id.AUp);
        B = (EditText) findViewById(R.id.BUp);
        C = (EditText) findViewById(R.id.CUp);
        D = (EditText) findViewById(R.id.DUp);
        answer = (Spinner) findViewById(R.id.spinnerAnswerUp);
        diff = (Spinner) findViewById(R.id.spinnerDifficultyUp);
        imageViewUser=(ImageView) findViewById(R.id.imageViewUserUp);
        userId = (TextView) findViewById(R.id.userIdUp);
        name = (TextView) findViewById(R.id.nameUp);
        surname = (TextView) findViewById(R.id.surnameUp);
        birthDate = (TextView) findViewById(R.id.birthDateUp);
        surname = (TextView) findViewById(R.id.surnameUp);
        gsm = (TextView) findViewById(R.id.gsmUp);

        userId.setText("User ID= "+user.getId());
        name.setText("Name= "+user.getName());
        surname.setText("Surname= "+user.getSurname());
        birthDate.setText("Birthdate= "+user.getBirth());
        gsm.setText("Phone= "+user.getPhone());
        imageViewUser.setImageBitmap(image.getImage());

        questionText.setText(question.getQuestion());
        A.setText(question.getA());
        B.setText(question.getB());
        C.setText(question.getC());
        D.setText(question.getD());


        String[] items = new String[]{"A", "B", "C", "D"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item,items);
        answer.setAdapter(adapter);
        answer.setSelection(adapter.getPosition(question.getAnswer()));
        String[] items2 = new String[]{"1", "2", "3", "4","5"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.spinner_item,items2);
        diff.setAdapter(adapter2);
        diff.setSelection(adapter2.getPosition(question.getLevel()));


    }

    public void defineListeners(){
        updateQuestion.setOnClickListener((v) -> {
            spinnerAnswer = answer.getSelectedItem().toString();
            spinnerDiff = diff.getSelectedItem().toString();

            boolean status = textCheck();

            if (status)
            {
                question=new Question();
                question.setId(question_id);
                question.setQuestion(questionText.getText().toString());
                question.setA(A.getText().toString());
                question.setB(B.getText().toString());
                question.setC(C.getText().toString());
                question.setD(D.getText().toString());
                question.setAnswer(spinnerAnswer);
                question.setLevel(spinnerDiff);
                db.updateQuestion(question);

                Intent intent;
                intent = new Intent(v.getContext(), QuestionListActivity.class);
                intent.putExtra("userName",userName);
                startActivity(intent);

            }

            else{
                Toast.makeText(UpsertQuestionActivity.this, "All fields must be filled!", Toast.LENGTH_SHORT).show();
            }

        });

    }
}

