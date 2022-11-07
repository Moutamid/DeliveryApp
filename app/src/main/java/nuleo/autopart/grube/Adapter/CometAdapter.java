package nuleo.autopart.grube.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import nuleo.autopart.grube.ComActivity;
import nuleo.autopart.grube.Model.Comet;
import nuleo.autopart.grube.Model.User;
import nuleo.autopart.grube.R;

public class CometAdapter extends RecyclerView.Adapter<CometAdapter.ViewHolder> {


    private Context nContext;
    private List<Comet> nComent;
    private String commentid;

    private FirebaseUser firebaseUser;

    public CometAdapter(Context nContext, List<Comet> nComent, String commentid) {
        this.nContext = nContext;
        this.nComent = nComent;
        this.commentid = commentid;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(nContext).inflate(R.layout.comet_iten, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Comet comet = nComent.get(position);

        holder.comentario.setText(comet.getComet());
        getUserInfa(holder.imgenspro, holder.usuarioo, comet.getPubli());
        isLikesLogT(comet.getComid(), holder.LikeII);
        nr2LikesT(holder.LikeTT, comet.getComid());
        getCommentsTT(comet.getComid(), holder.commentTT);

        holder.LikeII.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.LikeII.getTag().equals("like")) {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(comet.getComid())
                            .child(firebaseUser.getUid()).setValue(true);
                    addNotifications(comet.getPubli(), comet.getPostid());
                }else {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(comet.getComid())
                            .child(firebaseUser.getUid()).removeValue();


                }
            }

        });

        holder.TedComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(nContext, ComActivity.class);
                intent.putExtra("comid", comet.getComid());
                intent.putExtra("publi", comet.getPubli());
                nContext.startActivity(intent);

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (comet.getPubli().equals(firebaseUser.getUid())) {

                    AlertDialog alertDialog = new AlertDialog.Builder(nContext).create();
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
                                    FirebaseDatabase.getInstance().getReference("Commentss")
                                            .child(commentid).child(comet.getComid())
                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(nContext, "Deleted!", Toast.LENGTH_SHORT).show();
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
        return nComent.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public ImageView imgenspro, TedComment, LikeII;
        public TextView usuarioo, comentario, LikeTT, commentTT;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgenspro = itemView.findViewById(R.id.imagenprofila);
            usuarioo = itemView.findViewById(R.id.usuarioa);
            comentario = itemView.findViewById(R.id.comentar);
            commentTT = itemView.findViewById(R.id.commentsTT);

            TedComment = itemView.findViewById(R.id.Tedcomment);

            LikeII = itemView.findViewById(R.id.likeII);
            LikeTT = itemView.findViewById(R.id.likeTT);
        }
    }

    private  void getCommentsTT(final String comid, final TextView comments){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Commentsss").child(comid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comments.setText("" +dataSnapshot.getChildrenCount() + " ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void isLikesLogT (String comid, final ImageView imageView){
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Likes")
                .child(comid);

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
        hashMap.put("text", "Le gusto tu respuesta.");
        hashMap.put("postid", comid);

        reference.push().setValue(hashMap);


    }

    private void nr2LikesT(final TextView likes, String comid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes")
                .child(comid);

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


    private void getUserInfa (final ImageView imageView, final TextView usuario, String publishid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(publishid);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Glide.with(nContext).load(user.getImageurl()).into(imageView);
                usuario.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}
