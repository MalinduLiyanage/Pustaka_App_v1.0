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

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private List<Book> bookList;
    private Context context;
    public BookAdapter(List<Book> bookList, Context context) {
        this.bookList = bookList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.titleTextView.setText(book.getTitle());
        String imageUrlOrFilePath = book.getbookThumb();

        Picasso.get()
                .load(imageUrlOrFilePath)
                .resize(450, 590)
                .placeholder(R.drawable.pustaka_logo)
                .into(holder.bookThumb, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        loadFromFile(holder.bookThumb, imageUrlOrFilePath);
                    }
                });

        holder.authorTextView.setText(book.getAuthor());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch ViewBookActivity with book details
                Intent intent = new Intent(context, ViewBookActivity.class);
                intent.putExtra("book_id", book.getBookId());
                intent.putExtra("title", book.getTitle());
                intent.putExtra("author", book.getAuthor());
                intent.putExtra("desc", book.getDescription());
                intent.putExtra("thumb", book.getbookThumb());
                intent.putExtra("isbn",book.getIsbn());
                intent.putExtra("pages",book.getPages());
                intent.putExtra("publisher",book.getPublisher());
                intent.putExtra("copies",book.getCopiesCount());
                context.startActivity(intent);

            }
        });
    }

    private void loadFromFile(ImageView imageView, String filePath) {
        File imageFile = new File(filePath);
        Picasso.get()
                .load(imageFile)
                .resize(450, 590)
                .placeholder(R.drawable.pustaka_logo)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView authorTextView;
        ImageView bookThumb;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.book_title);
            bookThumb = itemView.findViewById(R.id.book_Thumb);
            authorTextView = itemView.findViewById(R.id.book_author);
        }
    }
}
