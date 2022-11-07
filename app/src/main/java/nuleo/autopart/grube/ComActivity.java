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

import nuleo.autopart.grube.Adapter.ComAdapter;
import nuleo.autopart.grube.Model.Com;
import nuleo.autopart.grube.Model.User;

public class ComActivity extends AppCompatActivity {

    private RecyclerView recyclerViewH;
    private ComAdapter comAdapter;
    private List<Com> comList;

    EditText addCom;
    ImageView imagenPeofi;
    TextView nPOSt;

    String comid;
    String publi;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_com);

        Toolbar tulbar = findViewById(R.id.tulbar);
        setSupportActionBar(tulbar);
        getSupportActionBar().setTitle("Comentarios");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tulbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        comid = intent.getStringExtra("comid");
        publi = intent.getStringExtra("publid");


        recyclerViewH = findViewById(R.id.recyclerviewH);
        recyclerViewH.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewH.setLayoutManager(linearLayoutManager);
        comList = new ArrayList<>();
        comAdapter = new ComAdapter(this, comList, comid);
        recyclerViewH.setAdapter(comAdapter);


        addCom = findViewById(R.id.addcom);
        imagenPeofi = findViewById(R.id.imagenPrefil);
        nPOSt = findViewById(R.id.POSt);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        nPOSt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addCom.getText().toString().equals("")) {
                    Toast.makeText(ComActivity.this, "empaty", Toast.LENGTH_SHORT).show();
                } else {
                    addCom();
                }
            }
        });

        ketImagen();
        readComentariodo();


    }

    private void addCom() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Commentsss").child(comid);

        String comidd = reference.push().getKey();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("comett", addCom.getText().toString());
        hashMap.put("publii", firebaseUser.getUid());
        hashMap.put("comidd", comidd);

        reference.child(comidd).setValue(hashMap);
        addCom.setText("");
    }





    private void ketImagen() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Glide.with(getApplicationContext()).load(user.getImageurl()).into(imagenPeofi);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readComentariodo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Commentsss").child(comid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Com com = snapshot.getValue(Com.class);
                    comList.add(com);
                }
                comAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}