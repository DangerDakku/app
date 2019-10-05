package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editTextName;
    Button button;
    Spinner spinner;

    DatabaseReference databaseArtist;
    ListView listViewArtist;
    List<Artist> artistList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseArtist = FirebaseDatabase.getInstance().getReference("artists");

       editTextName =  findViewById(R.id.editTextName);
       button =  findViewById(R.id.buttonAdd);
       spinner =  findViewById(R.id.spinner);
       listViewArtist = findViewById(R.id.listViewArtists);
       artistList = new ArrayList<>();
       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               addartist();
           }
       });

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseArtist.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                  artistList.clear();
                for (DataSnapshot artistSnapshot : dataSnapshot.getChildren()) {
                    Artist artist = artistSnapshot.getValue(Artist.class);
                    artistList.add(artist);
                }
                ArtistList adapter = new ArtistList(MainActivity.this,artistList);
                listViewArtist.setAdapter(adapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addartist(){
        String name = editTextName.getText().toString().trim();
        String genre = spinner.getSelectedItem().toString();
        if(!TextUtils.isEmpty(name))
        {
           String id =  databaseArtist.push().getKey();

           Artist artist = new Artist(id, name, genre);
           databaseArtist.child(id).setValue(artist);

            Toast.makeText( this, "Artist added", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this,"you should enter a name",Toast.LENGTH_LONG).show();
        }
    }
}
