package com.example.pocketlibrary.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketlibrary.NetworkUtils;
import com.example.pocketlibrary.R;
import com.example.pocketlibrary.adapters.CustomSearchAdapter;
import com.example.pocketlibrary.adapters.CustomToReadItemAdapter;
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

public class ToReadFragment extends Fragment {

    private AutoCompleteTextView acToRead;
    private ArrayList<Book> books;
    private CustomSearchAdapter customSearchAdapter;
    private CustomToReadItemAdapter customToReadItemAdapter;
    private RecyclerView rvToRead;
    private RecyclerView.LayoutManager rvManager;
    private ArrayList<Book> toRead;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to_read, container, false);

        toRead = new ArrayList<>();

        loadDb();

        rvToRead = view.findViewById(R.id.rvToRead);
        rvToRead.setHasFixedSize(true);
        rvManager = new LinearLayoutManager(this.getContext());
        rvToRead.setLayoutManager(rvManager);
        customToReadItemAdapter = new CustomToReadItemAdapter(toRead);
        rvToRead.setAdapter(customToReadItemAdapter);

        customToReadItemAdapter.setOnItemClickListener(new CustomToReadItemAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClicked(int position) {
                Book b = toRead.get(position);
                Toast.makeText(getContext(), "Book deleted", Toast.LENGTH_SHORT).show();
                databaseReference.child("users").child(NetworkUtils.sessionId).child("toread").child(b.getId()).removeValue();
                toRead.remove(position);
                customToReadItemAdapter.notifyItemRemoved(position);
            }
            @Override
            public void onFinishedClicked(int position) {
                Book b = toRead.get(position);
                Toast.makeText(getContext(), "Book added to finished section", Toast.LENGTH_SHORT).show();
                toRead.remove(position);
                databaseReference.child("users").child(NetworkUtils.sessionId).child("toread").child(b.getId()).removeValue();
                databaseReference.child("users").child(NetworkUtils.sessionId).child("finished").child(b.getId()).setValue(b);
                customToReadItemAdapter.notifyDataSetChanged();
            }
        });
        acToRead = view.findViewById(R.id.acToRead);
        acToRead.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Book b = (Book) customSearchAdapter.getItem(position);
                toRead.add(b);

                databaseReference.child("users").child(NetworkUtils.sessionId).child("toread").child(b.getId()).setValue(b);
                acToRead.setText("");
                acToRead.setHint("Add books...");
                customToReadItemAdapter.notifyDataSetChanged();
            }
        });
        acToRead.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchBooks(acToRead.getText().toString());
            }
        });


        return view;
    }

    public void searchBooks(String searchKeyword) {

        books = new ArrayList<>();

        Call<BooksObject> call = NetworkUtils.booksAPI.findBooks(searchKeyword, NetworkUtils.apiKey, 5);
        call.enqueue(new Callback<BooksObject>() {
            @Override
            public void onResponse(Call<BooksObject> call, Response<BooksObject> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                BooksObject booksObject = response.body();
                ArrayList<Book> bookList = booksObject.getItems();
                for (Book b : bookList) {
                    String title = b.getVolumeInfo().getTitle();
                    b.getVolumeInfo().setTitle(title);
                    books.add(b);
                }

                customSearchAdapter = new CustomSearchAdapter(getContext(), books);
                acToRead.setAdapter(customSearchAdapter);
                customSearchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<BooksObject> call, Throwable t) {
            }
        });
    }

    public void loadDb() {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mReferenceUsers = mDatabase.getReference("users/"+NetworkUtils.sessionId+"/toread");

        mReferenceUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                toRead.clear();
                List<String> keys = new ArrayList<>();

                for (DataSnapshot keynode : dataSnapshot.getChildren()) {
                    keys.add(keynode.getKey());
                    Book book= keynode.getValue(Book.class);
                    toRead.add(book);
                }
                customToReadItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
