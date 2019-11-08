package com.example.pocketlibrary.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketlibrary.R;
import com.example.pocketlibrary.data.Book;
import com.example.pocketlibrary.data.ImageLinks;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomFinishedItemAdapter extends RecyclerView.Adapter<CustomFinishedItemAdapter.FinishedItemViewHolder> {

    public ArrayList<Book> finished;
    private CustomFinishedItemAdapter.OnItemClickListener bookListener;

    public CustomFinishedItemAdapter(ArrayList<Book> finished) {
        this.finished = finished;
    }

    public void setOnItemClickListener(CustomFinishedItemAdapter.OnItemClickListener listener) {
        bookListener = listener;
    }

    public interface OnItemClickListener {
        void onDeleteClicked(int position);
    }

    @NonNull
    @Override
    public CustomFinishedItemAdapter.FinishedItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.finished_item, parent, false);
        CustomFinishedItemAdapter.FinishedItemViewHolder finishedItemViewHolder = new CustomFinishedItemAdapter.FinishedItemViewHolder(view, bookListener);
        return finishedItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomFinishedItemAdapter.FinishedItemViewHolder holder, int position) {

        Book book = finished.get(position);
        if (book.getVolumeInfo().getImageLinks() != null) {
            ImageLinks imageLink = book.getVolumeInfo().getImageLinks();
            if (!imageLink.getThumbnail().equals(null)) {
                Picasso.get().load(book.getVolumeInfo().getImageLinks().getThumbnail()).into(holder.fImage);
            } else if (!(imageLink.getSmallThumbnail().equals(null))) {
                Picasso.get().load(book.getVolumeInfo().getImageLinks().getSmallThumbnail()).into(holder.fImage);
            } else if (!(imageLink.getSmall().equals(null))) {
                Picasso.get().load(book.getVolumeInfo().getImageLinks().getSmall()).into(holder.fImage);
            } else if (!(imageLink.getMedium().equals(null))) {
                Picasso.get().load(book.getVolumeInfo().getImageLinks().getMedium()).into(holder.fImage);
            } else if (!(imageLink.getLarge().equals(null))) {
                Picasso.get().load(book.getVolumeInfo().getImageLinks().getLarge()).into(holder.fImage);
            } else if (!!imageLink.getExtraLarge().equals(null)) {
                Picasso.get().load(book.getVolumeInfo().getImageLinks().getExtraLarge()).into(holder.fImage);
            }
        }

        String title = book.getVolumeInfo().getTitle();
        if (title.length() < 30) {
            holder.fTitle.setText(title);
        } else if (title.length() > 30) {
            holder.fTitle.setText(title.substring(0, 27) + "...");
        }
        String authors = "";
        ArrayList<String> authorList = book.getVolumeInfo().getAuthors();

        for (String s : authorList) {
            authors += s;
            if (!(authorList.indexOf(s) == authorList.size() - 1)) {
                authors += ", ";
            }
        }
        if (authors.length() < 35) {
            holder.fAuthors.setText(authors);
        } else if (authors.length() > 35) {
            holder.fAuthors.setText(authors.substring(0, 30) + "...");
        }
        holder.fLanguage.setText("Language: " + book.getVolumeInfo().getLanguage());
        int pageNumber = book.getVolumeInfo().getPageCount();
        if (pageNumber == 0) {
            holder.fPageNumber.setText("");
        } else {
            holder.fPageNumber.setText("Number of pages: " + pageNumber);
        }

    }

    @Override
    public int getItemCount() {
        if (finished == null)
            return 0;
        else
            return finished.size();
    }

    public static class FinishedItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView fImage;
        public TextView fTitle;
        public TextView fAuthors;
        public TextView fPageNumber;
        public TextView fLanguage;
        public Button btnFDelete;

        public FinishedItemViewHolder(@NonNull View itemView, final CustomFinishedItemAdapter.OnItemClickListener listener) {
            super(itemView);

            fLanguage = itemView.findViewById(R.id.fLanguage);
            fImage = itemView.findViewById(R.id.fImage);
            fTitle = itemView.findViewById(R.id.fTitle);
            fAuthors = itemView.findViewById(R.id.fAuthors);
            fPageNumber = itemView.findViewById(R.id.fPageNumber);
            btnFDelete = itemView.findViewById(R.id.btnFDelete);

            btnFDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClicked(position);
                        }
                    }
                }
            });
        }
    }
}
