package com.example.odev;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Question> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;


    MyRecyclerViewAdapter(Context context, ArrayList<Question> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_question, parent, false);
        return new ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String question = mData.get(position).getQuestion();
        String A = mData.get(position).getA();
        String B = mData.get(position).getB();
        String C = mData.get(position).getC();
        String D = mData.get(position).getD();
        String answer = mData.get(position).getAnswer();
        String diff = mData.get(position).getLevel();
        holder.question.setText(question);
        holder.A.setText("A) "+A);
        holder.B.setText("B) "+B);
        holder.C.setText("C) "+C);
        holder.D.setText("D) "+D);
        holder.answer.setText("Correct choice= "+answer);
        holder.diff.setText("Difficulty level= "+diff);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
            {
        TextView question,A,B,C,D,answer,diff;
        Button update,delete;

        ViewHolder(View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.tvQuestion);
            A = itemView.findViewById(R.id.tvA);
            B = itemView.findViewById(R.id.tvB);
            C = itemView.findViewById(R.id.tvC);
            D = itemView.findViewById(R.id.tvD);
            answer = itemView.findViewById(R.id.tvAnswer);
            diff = itemView.findViewById(R.id.tvDiff);
            update = itemView.findViewById(R.id.update);
            delete = itemView.findViewById(R.id.delete);

            update.setOnClickListener(this);
            delete.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.delete:
                    if (mClickListener != null) mClickListener.onItemClick(view, getAbsoluteAdapterPosition());
                    break;
                case R.id.update:
                    if (mClickListener != null) mClickListener.onItemClick2(view, getAbsoluteAdapterPosition());
                    break;
                default:
                    break;
            }
        }




    }

    int getItem(int id) {
       return mData.get(id).getId();
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
        void onItemClick2(View view, int position);
    }




}
