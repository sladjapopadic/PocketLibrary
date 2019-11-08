package com.example.pocketlibrary.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pocketlibrary.R;
import com.example.pocketlibrary.data.Book;
import com.example.pocketlibrary.data.ImageLinks;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomSearchAdapter extends ArrayAdapter {


    public CustomSearchAdapter(@NonNull Context context, ArrayList<Book> books) {
        super(context, R.layout.search_book_item, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.search_book_item, parent, false);

        Book book = (Book) getItem(position);

        ImageView bookImage = view.findViewById(R.id.bookImage);
        TextView bookTitle = view.findViewById(R.id.bookTitle);
        TextView bookAuthor = view.findViewById(R.id.bookAuthor);

        if (book.getVolumeInfo().getImageLinks() != null) {
            ImageLinks imageLink = book.getVolumeInfo().getImageLinks();
            if (!imageLink.getThumbnail().equals(null)) {
                Picasso.get().load(book.getVolumeInfo().getImageLinks().getThumbnail()).into(bookImage);
            } else if (!(imageLink.getSmallThumbnail().equals(null))) {
                Picasso.get().load(book.getVolumeInfo().getImageLinks().getSmallThumbnail()).into(bookImage);
            } else if (!(imageLink.getSmall().equals(null))) {
                Picasso.get().load(book.getVolumeInfo().getImageLinks().getSmall()).into(bookImage);
            } else if (!(imageLink.getMedium().equals(null))) {
                Picasso.get().load(book.getVolumeInfo().getImageLinks().getMedium()).into(bookImage);
            } else if (!(imageLink.getLarge().equals(null))) {
                Picasso.get().load(book.getVolumeInfo().getImageLinks().getLarge()).into(bookImage);
            } else if (!!imageLink.getExtraLarge().equals(null)) {
                Picasso.get().load(book.getVolumeInfo().getImageLinks().getExtraLarge()).into(bookImage);
            }
        }

        bookTitle.setText(book.getVolumeInfo().getTitle());

        String authors = "";
        ArrayList<String> authorList = book.getVolumeInfo().getAuthors();

        for (String s : authorList) {
            authors += s;
            if (!(authorList.indexOf(s) == authorList.size() - 1)) {
                authors += ", ";
            }
        }
        bookAuthor.setText(authors);

        return view;
    }
}
