package com.example.pocketlibrary.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketlibrary.MainActivity;
import com.example.pocketlibrary.NetworkUtils;
import com.example.pocketlibrary.R;
import com.example.pocketlibrary.adapters.CustomFinishedItemAdapter;
import com.example.pocketlibrary.data.Book;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private Button buttonLogOut;
    private ArrayList<Book> favourites;
    private RecyclerView rvFavourite;
    private CustomFinishedItemAdapter customFinishedItemAdapter;
    private RecyclerView.LayoutManager rvManager;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        favourites = new ArrayList<>();

        rvFavourite = view.findViewById(R.id.rvFavourite);
        rvFavourite.setHasFixedSize(true);
        rvManager = new LinearLayoutManager(this.getContext());
        rvFavourite.setLayoutManager(rvManager);
        customFinishedItemAdapter = new CustomFinishedItemAdapter(favourites);
        rvFavourite.setAdapter(customFinishedItemAdapter);

        customFinishedItemAdapter.setOnItemClickListener(new CustomFinishedItemAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClicked(int position) {
                Book b = favourites.get(position);
                Toast.makeText(getContext(), "Book deleted", Toast.LENGTH_SHORT).show();
                databaseReference.child("users").child(NetworkUtils.sessionId).child("favourite").child(b.getId()).removeValue();
                favourites.remove(position);
                customFinishedItemAdapter.notifyItemRemoved(position);
            }
        });
          return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        ((MainActivity) getActivity()).updateNavDraw(true);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            Fragment f = new LoginFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, f, "findThisFragment")
                    .addToBackStack(null)
                    .commit();
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();
        NetworkUtils.sessionId = user.getUid();

        buttonLogOut = getView().findViewById(R.id.buttonLogOut);
        buttonLogOut.setOnClickListener(this);

        if (user != null) {
            callback.getUserEmail(user.getEmail());
        }
        loadDb();

    }


    @Override
    public void onClick(View view) {
        if (view == buttonLogOut) {
            firebaseAuth.signOut();
            Fragment f = new LoginFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, f, "findThisFragment")
                    .addToBackStack(null)
                    .commit();
            callback.getUserEmail("");
            NetworkUtils.sessionId = "";
        }
    }

    onLoggedUser callback;

    public void setOnLoggedUserListener(onLoggedUser callback) {
        this.callback = callback;
    }

    public interface onLoggedUser {
        void getUserEmail(String email);
    }

    public void loadDb() {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mReferenceUsers = mDatabase.getReference("users/"+NetworkUtils.sessionId+"/favourite");

        mReferenceUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                favourites.clear();
                List<String> keys = new ArrayList<>();

                for (DataSnapshot keynode : dataSnapshot.getChildren()) {
                    keys.add(keynode.getKey());
                    Book book= keynode.getValue(Book.class);
                    favourites.add(book);
                }
                customFinishedItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
