package com.example.odev;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerViewAdapter2 extends RecyclerView.Adapter<MyRecyclerViewAdapter2.ViewHolder> {

    private ArrayList<Question> mData;
    private LayoutInflater mInflater;
    private MyRecyclerViewAdapter2.ItemClickListener mClickListener;



    MyRecyclerViewAdapter2(Context context, ArrayList<Question> data) {
        this.mInflater = LayoutInflater.from(context);

        this.mData = data;

    }


    @Override
    public MyRecyclerViewAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view2 = mInflater.inflate(R.layout.single_question_2, parent, false);
        return new ViewHolder(view2);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyRecyclerViewAdapter2.ViewHolder holder, int position) {

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
        Switch sb;

        ViewHolder(View itemView) {
            super(itemView);

            question = itemView.findViewById(R.id.tv2Question);
            A = itemView.findViewById(R.id.tv2A);
            B = itemView.findViewById(R.id.tv2B);
            C = itemView.findViewById(R.id.tv2C);
            D = itemView.findViewById(R.id.tv2D);
            answer = itemView.findViewById(R.id.tv2Answer);
            diff = itemView.findViewById(R.id.tv2Diff);
            sb = itemView.findViewById(R.id.tvSw);
            sb.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Boolean switchState = sb.isChecked();

            if (switchState) {
                if (mClickListener != null)
                    mClickListener.onItemClick(view, getAbsoluteAdapterPosition());
            } else  {
                if (mClickListener != null)
                    mClickListener.onItemClick2(view, getAbsoluteAdapterPosition());
            }
        }

    }

    int getItem(int id) {
        return mData.get(id).getId();
    }

    void setClickListener(MyRecyclerViewAdapter2.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
        void onItemClick2(View view, int position);
    }



}
