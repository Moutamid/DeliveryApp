package nuleo.autopart.grube.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import nuleo.autopart.grube.Model.Com;
import nuleo.autopart.grube.Model.User;
import nuleo.autopart.grube.R;

public class ComAdapter extends RecyclerView.Adapter<ComAdapter.ViewHolder> {

    private Context zContext;
    private List<Com> zComent;
    private String comid;


    private FirebaseUser firebaseUser;

    public ComAdapter(Context zContext, List<Com> zComent, String comid) {
        this.zContext = zContext;
        this.zComent = zComent;
        this.comid = comid;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(zContext).inflate(R.layout.com_iter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Com com = zComent.get(position);

        holder.comentary.setText(com.getComett());
        getUserInfz(holder.imaprofiler, holder.usuarinombre, com.getPublii());
        isLikesLogZ(com.getComidd(), holder.LikeIII);
        nr2LikesZ(holder.LikeTTT, com.getComidd());

        holder.LikeIII.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.LikeIII.getTag().equals("like")) {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(com.getComidd())
                            .child(firebaseUser.getUid()).setValue(true);
                    addNotifications(com.getPublii(), com.getPostid());
                }else {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(com.getComidd())
                            .child(firebaseUser.getUid()).removeValue();


                }
            }

        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (com.getPublii().equals(firebaseUser.getUid())) {

                    AlertDialog alertDialog = new AlertDialog.Builder(zContext).create();
                    alertDialog.setTitle("Desea eleiminar este comentario?");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "No",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    dialogInterface.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Si",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseDatabase.getInstance().getReference("Commentsss")
                                            .child(comid).child(com.getComidd())
                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(zContext, "Eliminado!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return zComent.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imaprofiler, LikeIII;
        public TextView usuarinombre, comentary, LikeTTT;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imaprofiler = itemView.findViewById(R.id.imagenprofi);
            usuarinombre = itemView.findViewById(R.id.usualiti);
            comentary = itemView.findViewById(R.id.comenatry);

            LikeIII = itemView.findViewById(R.id.likeIII);
            LikeTTT = itemView.findViewById(R.id.likeTTT);
        }
    }


    private void isLikesLogZ (String comidd, final ImageView imageView){
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Likes")
                .child(comidd);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(firebaseUser.getUid()).exists()){
                    imageView.setImageResource(R.drawable.ic_favoriton);
                    imageView.setTag("liked");
                }else {
                    imageView.setImageResource(R.drawable.ic_favoritan);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void addNotifications(String userid, String comid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", firebaseUser.getUid());
        hashMap.put("text", "Le gusto tu sub-respuesta.");
        hashMap.put("postid", comid);

        reference.push().setValue(hashMap);


    }
    private void nr2LikesZ(final TextView likes, String comidd){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes")
                .child(comidd);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                likes.setText(dataSnapshot.getChildrenCount()+" ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUserInfz (ImageView imageVim, TextView usuariom, String publi){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(publi);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Glide.with(zContext).load(user.getImageurl()).into(imageVim);
                usuariom.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
