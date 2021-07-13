package com.example.businesscardscanner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private ArrayList<CardItem> mCardList;

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public TextView mTextView4;
        public TextView mTextView5;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.textView1);
            mTextView2 = itemView.findViewById(R.id.textView2);
            mTextView3 = itemView.findViewById(R.id.textView3);
            mTextView4 = itemView.findViewById(R.id.textView4);
            mTextView5 = itemView.findViewById(R.id.textView5);

        }
    }

    public CardAdapter(ArrayList<CardItem> cardList) {
        mCardList = cardList;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        CardViewHolder cvh = new CardViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CardItem currentItem = mCardList.get(position);

        holder.mTextView1.setText(currentItem.getName());
        holder.mTextView2.setText(currentItem.getPhone());
        holder.mTextView3.setText(currentItem.getAddress());
        holder.mTextView4.setText(currentItem.getCity());
        holder.mTextView5.setText(currentItem.getEmail());
    }

    @Override
    public int getItemCount() {
        return mCardList.size();
    }
}
