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

public class CustomToReadItemAdapter extends RecyclerView.Adapter<CustomToReadItemAdapter.ToReadItemViewHolder> {

    public ArrayList<Book> toRead;
    private CustomToReadItemAdapter.OnItemClickListener bookListener;

    public CustomToReadItemAdapter(ArrayList<Book> toRead) {
        this.toRead = toRead;
    }

    public void setOnItemClickListener(CustomToReadItemAdapter.OnItemClickListener listener) {
        bookListener = listener;
    }

    public interface OnItemClickListener {
        void onDeleteClicked(int position);
        void onFinishedClicked(int position);
    }

    @NonNull
    @Override
    public CustomToReadItemAdapter.ToReadItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.to_read_item, parent, false);
        CustomToReadItemAdapter.ToReadItemViewHolder toReadItemViewHolder = new CustomToReadItemAdapter.ToReadItemViewHolder(view, bookListener);
        return toReadItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomToReadItemAdapter.ToReadItemViewHolder holder, int position) {

        Book book = toRead.get(position);
        if (book.getVolumeInfo().getImageLinks() != null) {
            ImageLinks imageLink = book.getVolumeInfo().getImageLinks();
            if (!imageLink.getThumbnail().equals(null)) {
                Picasso.get().load(book.getVolumeInfo().getImageLinks().getThumbnail()).into(holder.trImage);
            } else if (!(imageLink.getSmallThumbnail().equals(null))) {
                Picasso.get().load(book.getVolumeInfo().getImageLinks().getSmallThumbnail()).into(holder.trImage);
            } else if (!(imageLink.getSmall().equals(null))) {
                Picasso.get().load(book.getVolumeInfo().getImageLinks().getSmall()).into(holder.trImage);
            } else if (!(imageLink.getMedium().equals(null))) {
                Picasso.get().load(book.getVolumeInfo().getImageLinks().getMedium()).into(holder.trImage);
            } else if (!(imageLink.getLarge().equals(null))) {
                Picasso.get().load(book.getVolumeInfo().getImageLinks().getLarge()).into(holder.trImage);
            } else if (!!imageLink.getExtraLarge().equals(null)) {
                Picasso.get().load(book.getVolumeInfo().getImageLinks().getExtraLarge()).into(holder.trImage);
            }
        }

        String title = book.getVolumeInfo().getTitle();
        if (title.length() < 30) {
            holder.trTitle.setText(title);
        } else if (title.length() > 30) {
            holder.trTitle.setText(title.substring(0, 27) + "...");
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
            holder.trAuthors.setText(authors);
        } else if (authors.length() > 35) {
            holder.trAuthors.setText(authors.substring(0, 30) + "...");
        }
        holder.trLanguage.setText("Language: " + book.getVolumeInfo().getLanguage());
        int pageNumber = book.getVolumeInfo().getPageCount();
        if (pageNumber == 0) {
            holder.trPageNumber.setText("");
        } else {
            holder.trPageNumber.setText("Number of pages: " + pageNumber);
        }

    }

    @Override
    public int getItemCount() {
        if (toRead == null)
            return 0;
        else
            return toRead.size();
    }

    public static class ToReadItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView trImage;
        public TextView trTitle;
        public TextView trAuthors;
        public TextView trPageNumber;
        public TextView trLanguage;
        public Button btnTrDelete;
        public Button btnTrFinished;

        public ToReadItemViewHolder(@NonNull View itemView, final CustomToReadItemAdapter.OnItemClickListener listener) {
            super(itemView);

            trLanguage = itemView.findViewById(R.id.fLanguage);
            trImage = itemView.findViewById(R.id.fImage);
            trTitle = itemView.findViewById(R.id.fTitle);
            trAuthors = itemView.findViewById(R.id.fAuthors);
            trPageNumber = itemView.findViewById(R.id.fPageNumber);
            btnTrDelete = itemView.findViewById(R.id.btnFDelete);
            btnTrFinished = itemView.findViewById(R.id.btnFFinished);

            btnTrDelete.setOnClickListener(new View.OnClickListener() {
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
            btnTrFinished.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onFinishedClicked(position);
                        }
                    }
                }
            });
        }
    }
}
