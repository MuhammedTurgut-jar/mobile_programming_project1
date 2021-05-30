package com.example.odev;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class QuestionListActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener
 {

    MyRecyclerViewAdapter adapter;
    DatabaseHelper db;
    ArrayList<Question> questions;
    String userName;
    Button mainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);
        userName=getIntent().getExtras().getString("userName");
        questions=new ArrayList<>();
        db = new DatabaseHelper(this);
        questions=db.getAllQuestion();
        mainMenu=(Button) findViewById(R.id.backMenu);
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, questions);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        defineListeners();
    }


     @Override
     public void onItemClick(View view, int position) {
        int question_id = adapter.getItem(position);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

         alertDialogBuilder.setTitle("Warning!");

         alertDialogBuilder
                 .setMessage("Are you sure delete this question?")
                 .setCancelable(false)
                 .setIcon(R.mipmap.ic_launcher_round)
                 .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         dialog.dismiss();
                         db.deleteQuestion(question_id);
                         questions.remove(position);
                         adapter.notifyItemRemoved(position);
                     }
                 })
                 .setNegativeButton("No", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         dialog.dismiss();
                     }
                 });

         AlertDialog alertDialog = alertDialogBuilder.create();
         alertDialog.show();

     }
     public void onItemClick2(View view, int position) {
         int question_id = adapter.getItem(position);

         Intent intent;
         intent = new Intent(view.getContext(), UpsertQuestionActivity.class);
         intent.putExtra("userName",userName);
         intent.putExtra("question",question_id);
         startActivity(intent);

     }
     public void defineListeners(){

         mainMenu.setOnClickListener((v) -> {
             Intent intent;
             intent = new Intent(v.getContext(), MenuActivity.class);
             intent.putExtra("userName",userName);
             startActivity(intent);

         });

     }


}
