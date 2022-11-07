package nuleo.autopart.grube;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class OpcionesActivity extends AppCompatActivity {


    ImageView imageView, imageView2, imageView3;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference first = databaseReference.child("image");

    private FirebaseDatabase firebaseDatabase2 = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference2 = firebaseDatabase.getReference();
    private DatabaseReference first2 = databaseReference.child("image2");

    private FirebaseDatabase firebaseDatabase3 = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference3 = firebaseDatabase.getReference();
    private DatabaseReference first3 = databaseReference.child("image3");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones);

        imageView = findViewById(R.id.ingf);
        imageView2 = findViewById(R.id.ingf2);
        imageView3 = findViewById(R.id.ingf3);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Servicio a cliente");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    @Override
    protected void onStart () {
        super.onStart();
        first.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String link = snapshot.getValue(String.class);
                Picasso.get().load(link).into(imageView);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        first2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String link2 = snapshot.getValue(String.class);
                Picasso.get().load(link2).into(imageView2);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        first3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String link3 = snapshot.getValue(String.class);
                Picasso.get().load(link3).into(imageView3);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}