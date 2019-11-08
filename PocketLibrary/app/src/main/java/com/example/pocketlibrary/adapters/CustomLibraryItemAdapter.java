package com.example.pocketlibrary.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketlibrary.R;
import com.example.pocketlibrary.data.Book;
import com.example.pocketlibrary.data.ImageLinks;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CustomLibraryItemAdapter extends RecyclerView.Adapter<CustomLibraryItemAdapter.LibraryItemViewHolder> implements Filterable {

    public ArrayList<Book> library;
    private List<Book> libraryFull;
    private OnItemClickListener bookListener;

    public CustomLibraryItemAdapter(ArrayList<Book> library) {
        this.library = library;
        libraryFull = new ArrayList<>(library);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        bookListener = listener;
    }

    public interface OnItemClickListener {
        void onDeleteClicked(int position);

        void onToReadClicked(int position);

        void onFinishedClicked(int position);

        void onFavouriteClicked(int position);
    }

    @NonNull
    @Override
    public LibraryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.library_item, parent, false);
        LibraryItemViewHolder libraryItemViewHolder = new LibraryItemViewHolder(view, bookListener);
        return libraryItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LibraryItemViewHolder holder, int position) {

        Book book = library.get(position);
        if (book.getVolumeInfo().getImageLinks() != null) {
            ImageLinks imageLink = book.getVolumeInfo().getImageLinks();
            if (!imageLink.getThumbnail().equals(null)) {
                Picasso.get().load(book.getVolumeInfo().getImageLinks().getThumbnail()).into(holder.liImage);
            } else if (!(imageLink.getSmallThumbnail().equals(null))) {
                Picasso.get().load(book.getVolumeInfo().getImageLinks().getSmallThumbnail()).into(holder.liImage);
            } else if (!(imageLink.getSmall().equals(null))) {
                Picasso.get().load(book.getVolumeInfo().getImageLinks().getSmall()).into(holder.liImage);
            } else if (!(imageLink.getMedium().equals(null))) {
                Picasso.get().load(book.getVolumeInfo().getImageLinks().getMedium()).into(holder.liImage);
            } else if (!(imageLink.getLarge().equals(null))) {
                Picasso.get().load(book.getVolumeInfo().getImageLinks().getLarge()).into(holder.liImage);
            } else if (!!imageLink.getExtraLarge().equals(null)) {
                Picasso.get().load(book.getVolumeInfo().getImageLinks().getExtraLarge()).into(holder.liImage);
            }
        }

        String title = book.getVolumeInfo().getTitle();
        if (title.length() < 30) {
            holder.liTitle.setText(title);
        } else if (title.length() > 30) {
            holder.liTitle.setText(title.substring(0, 27) + "...");
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
            holder.liAuthors.setText(authors);
        } else if (authors.length() > 35) {
            holder.liAuthors.setText(authors.substring(0, 30) + "...");
        }
        holder.liLanguage.setText("Language: " + book.getVolumeInfo().getLanguage());
        int pageNumber = book.getVolumeInfo().getPageCount();
        if (pageNumber == 0) {
            holder.liPageNumber.setText("Number of pages: unknown");
        } else {
            holder.liPageNumber.setText("Number of pages: " + pageNumber);
        }

    }

    @Override
    public int getItemCount() {
        if (library == null)
            return 0;
        else
            return library.size();
    }

    public static class LibraryItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView liImage;
        public TextView liTitle;
        public TextView liAuthors;
        public TextView liPageNumber;
        public TextView liLanguage;
        public Button btnDelete;
        public Button btnToRead;
        public Button btnFinished;
        public Button btnFavourite;

        public LibraryItemViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            liLanguage = itemView.findViewById(R.id.fLanguage);
            liImage = itemView.findViewById(R.id.fImage);
            liTitle = itemView.findViewById(R.id.fTitle);
            liAuthors = itemView.findViewById(R.id.fAuthors);
            liPageNumber = itemView.findViewById(R.id.fPageNumber);
            btnDelete = itemView.findViewById(R.id.btnFDelete);
            btnFinished = itemView.findViewById(R.id.btnFFinished);
            btnToRead = itemView.findViewById(R.id.btnToRead);
            btnFavourite = itemView.findViewById(R.id.btnFavourite);

            btnFavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onFavouriteClicked(position);
                        }
                    }
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
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
            btnToRead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onToReadClicked(position);
                        }
                    }
                }
            });
            btnFinished.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public Filter getFilter() {
        return libraryFilter;
    }

    private Filter libraryFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            for (Book b : library) {
                if (!(libraryFull.contains(b)))
                    libraryFull.add(b);
            }
            List<Book> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(libraryFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Book b : libraryFull) {
                    if (b.getVolumeInfo().getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(b);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            library.clear();
            library.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    public void setLibrary(ArrayList<Book> library) {
        this.library = library;
    }

    public List<Book> getLibraryFull() {
        return libraryFull;
    }
}
