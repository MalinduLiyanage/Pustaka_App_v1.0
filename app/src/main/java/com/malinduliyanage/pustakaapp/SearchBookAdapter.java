package com.malinduliyanage.pustakaapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class SearchBookAdapter extends RecyclerView.Adapter<SearchBookAdapter.ViewHolder> {

    private List<SearchBook> searchbookList;
    private Context context;

    public SearchBookAdapter(List<SearchBook> searchbookList, Context context) {
        this.searchbookList = searchbookList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_search_a_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchBook searchbook = searchbookList.get(position);
        holder.titleTextView.setText(searchbook.getTitle());
        holder.authorTextView.setText(searchbook.getAuthor());
        holder.descTextView.setText(searchbook.getDescription());

        String imageUrlOrFilePath = searchbook.getbookThumb();

        Picasso.get()
                .load(imageUrlOrFilePath)
                .resize(80, 80)
                .placeholder(R.drawable.pustaka_logo)
                .into(holder.searchbookThumb, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        loadFromFile(holder.searchbookThumb, imageUrlOrFilePath);
                    }
                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ViewBookActivity.class);
                intent.putExtra("book_id", searchbook.getBookId());
                intent.putExtra("title", searchbook.getTitle());
                intent.putExtra("author", searchbook.getAuthor());
                intent.putExtra("desc", searchbook.getDescription());
                intent.putExtra("thumb", searchbook.getbookThumb());
                intent.putExtra("isbn",searchbook.getIsbn());
                intent.putExtra("pages",searchbook.getPages());
                intent.putExtra("publisher",searchbook.getPublisher());
                context.startActivity(intent);

            }
        });
    }

    private void loadFromFile(ImageView imageView, String filePath) {
        File imageFile = new File(filePath);
        Picasso.get()
                .load(imageFile)
                .resize(80, 80)
                .placeholder(R.drawable.pustaka_logo)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return searchbookList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView authorTextView;
        TextView descTextView;
        ImageView searchbookThumb;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.book_title);
            authorTextView = itemView.findViewById(R.id.book_author);
            descTextView = itemView.findViewById(R.id.book_desc);
            searchbookThumb = itemView.findViewById(R.id.search_book_Thumb);
        }
    }
}
