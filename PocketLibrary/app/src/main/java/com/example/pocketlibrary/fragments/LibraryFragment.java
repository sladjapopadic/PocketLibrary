package com.example.pocketlibrary.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketlibrary.NetworkUtils;
import com.example.pocketlibrary.R;
import com.example.pocketlibrary.adapters.CustomLibraryItemAdapter;
import com.example.pocketlibrary.adapters.CustomSearchAdapter;
import com.example.pocketlibrary.data.Book;
import com.example.pocketlibrary.data.BooksObject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LibraryFragment extends Fragment {

    private AutoCompleteTextView acLibrary;
    private ArrayList<Book> books;
    private CustomSearchAdapter customSearchAdapter;
    private CustomLibraryItemAdapter customLibraryItemAdapter;
    private RecyclerView rvLibrary;
    private RecyclerView.LayoutManager rvManager;
    private ArrayList<Book> library;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private SearchView svLibrary;

    public static String TAG = "test";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, null);

        library = new ArrayList<>();

        loadDb();

        rvLibrary = view.findViewById(R.id.rvLibrary);
        rvLibrary.setHasFixedSize(true);
        rvManager = new LinearLayoutManager(this.getContext());
        rvLibrary.setLayoutManager(rvManager);
        customLibraryItemAdapter = new CustomLibraryItemAdapter(library);
        rvLibrary.setAdapter(customLibraryItemAdapter);

        customLibraryItemAdapter.setOnItemClickListener(new CustomLibraryItemAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClicked(int position) {
                Book b = library.get(position);
                databaseReference.child("users").child(NetworkUtils.sessionId).child("library").child(b.getId()).removeValue();
                if (customLibraryItemAdapter.getLibraryFull().contains(b))
                    customLibraryItemAdapter.getLibraryFull().remove(position);
                library.remove(position);
                Toast.makeText(getContext(), "Book deleted", Toast.LENGTH_SHORT).show();
                customLibraryItemAdapter.notifyItemRemoved(position);
            }

            @Override
            public void onFavouriteClicked(int position) {
                Book b = library.get(position);
                Toast.makeText(getContext(), "Book added to your favourites", Toast.LENGTH_SHORT).show();
                databaseReference.child("users").child(NetworkUtils.sessionId).child("favourite").child(b.getId()).setValue(b);
            }

            @Override
            public void onToReadClicked(int position) {
                Book b = library.get(position);
                Toast.makeText(getContext(), "Book added to To read section", Toast.LENGTH_SHORT).show();
                databaseReference.child("users").child(NetworkUtils.sessionId).child("toread").child(b.getId()).setValue(b);
            }

            @Override
            public void onFinishedClicked(int position) {
                Book b = library.get(position);
                Toast.makeText(getContext(), "Book added to finished section", Toast.LENGTH_SHORT).show();
                databaseReference.child("users").child(NetworkUtils.sessionId).child("toread").child(b.getId()).removeValue();
                databaseReference.child("users").child(NetworkUtils.sessionId).child("finished").child(b.getId()).setValue(b);
            }
        });

        acLibrary = view.findViewById(R.id.acLibrary);
        acLibrary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Book b = (Book) customSearchAdapter.getItem(position);

                library.add(b);
                customLibraryItemAdapter.notifyDataSetChanged();
                databaseReference.child("users").child(NetworkUtils.sessionId).child("library").child(b.getId()).setValue(b);
                acLibrary.setText("");
                acLibrary.setHint("Add books...");
            }
        });
        acLibrary.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (svLibrary.getQuery().toString().length() > 0) {
                    svLibrary.setQuery("", false);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchBooks(acLibrary.getText().toString());
            }
        });

        svLibrary = view.findViewById(R.id.svLibrary);
        svLibrary.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                customLibraryItemAdapter.setLibrary(library);
                customLibraryItemAdapter.getFilter().filter(s);
                customLibraryItemAdapter.notifyDataSetChanged();
                return false;
            }
        });
        return view;
    }

    public void searchBooks(String searchKeyword) {
//        ArrayList<String> authors = new ArrayList<>();
//        authors.add("david");
//        authors.add("sladja");
//        authors.add("zuza");
//        authors.add("seka");
//        ImageLinks imageLinks = new ImageLinks();
//        imageLinks.setSmallThumbnail("https://www.avasflowers.net/img/prod_img/avasflowers-dreaming-of-tuscany-bouquet.jpg");
//        books = new ArrayList<>();
//        Book b1 = new Book();
//        BookInfo b1info = new BookInfo();
//        b1info.setTitle("Mali., princ.");
//        String title = b1info.getTitle().replaceAll("[^a-zA-Z, ]", "");
//        b1info.setTitle(title);
//        b1info.setAuthors(authors);
//        b1info.setLanguage("sr");
//        b1info.setPageCount(55);
//        b1info.setImageLinks(new ImageLinks());
//        b1info.setImageLinks(imageLinks);
//        b1.setVolumeInfo(b1info);
//
//        Book b2 = new Book();
//        BookInfo b2info = new BookInfo();
//        b2info.setTitle("Lovac na zmajeve");
//        b2info.setAuthors(authors);
//        b2info.setLanguage("hr");
//        b2info.setPageCount(145);
//
//        b2info.setImageLinks(imageLinks);
//        b2.setVolumeInfo(b2info);
//
//        books.add(b1);
//        books.add(b2);
//
//        customSearchAdapter = new CustomSearchAdapter(getContext(), books);
//        acLibrary.setAdapter(customSearchAdapter);
//        customSearchAdapter.notifyDataSetChanged();


        books = new ArrayList<>();
        Call<BooksObject> call = NetworkUtils.booksAPI.findBooks(searchKeyword, NetworkUtils.apiKey, 5);
        call.enqueue(new Callback<BooksObject>() {
            @Override
            public void onResponse(Call<BooksObject> call, Response<BooksObject> response) {
                if (!response.isSuccessful()) {
                    Log.i(TAG, "" + response.code());
                    return;
                }

                BooksObject booksObject = response.body();
                ArrayList<Book> bookList = booksObject.getItems();
                for (Book b : bookList) {
                    String id = b.getId();
                    boolean exists = false;
                    for (Book bo : library) {
                        if (id.equals(bo.getId())) {
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) {
                        books.add(b);
                    }
                }

                customSearchAdapter = new CustomSearchAdapter(getContext(), books);
                acLibrary.setAdapter(customSearchAdapter);
                customSearchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<BooksObject> call, Throwable t) {
                Log.i(TAG, t.getMessage());
            }
        });

    }

    public void loadDb() {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mReferenceUsers = mDatabase.getReference("users/"+NetworkUtils.sessionId+"/library");

        mReferenceUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                library.clear();
                List<String> keys = new ArrayList<>();

                for (DataSnapshot keynode : dataSnapshot.getChildren()) {
                    keys.add(keynode.getKey());
                    Book book= keynode.getValue(Book.class);
                    library.add(book);
                }
                customLibraryItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
