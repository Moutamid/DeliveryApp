package nuleo.autopart.grube;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nuleo.autopart.grube.Adapter.CometAdapter;
import nuleo.autopart.grube.Model.Comet;
import nuleo.autopart.grube.Model.User;

public class CometActivity extends AppCompatActivity {


    private RecyclerView recyclerViewP;
    private CometAdapter cometAdapter;
    private List<Comet> cometList;

    EditText addComentr;
    ImageView imageProfilr;
    TextView Post;

    String commentid;
    String publisher;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comet);

        Toolbar tolbar = findViewById(R.id.tolbar);
        setSupportActionBar(tolbar);
        getSupportActionBar().setTitle("Comentarios");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        commentid = intent.getStringExtra("commentid");
        publisher = intent.getStringExtra("publisher");

        recyclerViewP = findViewById(R.id.recyclerviewP);
        recyclerViewP.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewP.setLayoutManager(linearLayoutManager);
        cometList = new ArrayList<>();
        cometAdapter = new CometAdapter(this, cometList, commentid);
        recyclerViewP.setAdapter(cometAdapter);

        addComentr = findViewById(R.id.addcomment);
        imageProfilr = findViewById(R.id.imageprofiles);
        Post = findViewById(R.id.PoSt);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addComentr.getText().toString().equals("")) {
                    Toast.makeText(CometActivity.this, "empaty", Toast.LENGTH_SHORT).show();
                } else {
                    addComentr();
                }
            }
        });

        fetImagen();
        readComentario();

    }

    private void addComentr() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Commentss").child(commentid);

        String comid = reference.push().getKey();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("comet", addComentr.getText().toString());
        hashMap.put("publi", firebaseUser.getUid());
        hashMap.put("comid", comid);

        reference.child(comid).setValue(hashMap);
        addNotifications();
        addComentr.setText("");
    }

    private void addNotifications () {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(publisher);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", firebaseUser.getUid());
        hashMap.put("text", "Respondio tu comentario: " + addComentr.getText().toString());
        hashMap.put("commentid", commentid);

        reference.push().setValue(hashMap);

    }



    private void fetImagen() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Glide.with(getApplicationContext()).load(user.getImageurl()).into(imageProfilr);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void readComentario() {
        DatabaseReference reference = FirebaseDatabase.getInstance().
                getReference("Commentss").child(commentid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cometList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Comet comet = snapshot.getValue(Comet.class);
                    cometList.add(comet);
                }
                cometAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}