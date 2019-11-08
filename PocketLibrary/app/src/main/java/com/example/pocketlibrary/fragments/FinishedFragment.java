package com.example.pocketlibrary.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketlibrary.NetworkUtils;
import com.example.pocketlibrary.R;
import com.example.pocketlibrary.adapters.CustomFinishedItemAdapter;
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

public class FinishedFragment extends Fragment {

    private AutoCompleteTextView acFinished;
    private ArrayList<Book> books;
    private CustomSearchAdapter customSearchAdapter;
    private CustomFinishedItemAdapter customFinishedItemAdapter;
    private RecyclerView rvFinished;
    private RecyclerView.LayoutManager rvManager;
    private ArrayList<Book> finished;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finished, container, false);

        finished = new ArrayList<>();

        loadDb();

        rvFinished = view.findViewById(R.id.rvFinished);
        rvFinished.setHasFixedSize(true);
        rvManager = new LinearLayoutManager(this.getContext());
        rvFinished.setLayoutManager(rvManager);
        customFinishedItemAdapter = new CustomFinishedItemAdapter(finished);
        rvFinished.setAdapter(customFinishedItemAdapter);

        customFinishedItemAdapter.setOnItemClickListener(new CustomFinishedItemAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClicked(int position) {
                Book b = finished.get(position);
                databaseReference.child("users").child(NetworkUtils.sessionId).child("finished").child(b.getId()).removeValue();
                finished.remove(position);
                customFinishedItemAdapter.notifyItemRemoved(position);
            }
        });
        acFinished = view.findViewById(R.id.acFinished);
        acFinished.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Book b = (Book) customSearchAdapter.getItem(position);
                finished.add(b);

                databaseReference.child("users").child(NetworkUtils.sessionId).child("finished").child(b.getId()).setValue(b);
                acFinished.setText("");
                acFinished.setHint("Add books...");
                customFinishedItemAdapter.notifyDataSetChanged();
            }
        });
        acFinished.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchBooks(acFinished.getText().toString());
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
                acFinished.setAdapter(customSearchAdapter);
                customSearchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<BooksObject> call, Throwable t) {
            }
        });
    }
    public void loadDb() {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mReferenceUsers = mDatabase.getReference("users/"+NetworkUtils.sessionId+"/finished");

        mReferenceUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                finished.clear();
                List<String> keys = new ArrayList<>();

                for (DataSnapshot keynode : dataSnapshot.getChildren()) {
                    keys.add(keynode.getKey());
                    Book book= keynode.getValue(Book.class);
                    finished.add(book);
                }
                customFinishedItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
