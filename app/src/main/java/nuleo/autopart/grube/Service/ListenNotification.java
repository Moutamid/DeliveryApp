package nuleo.autopart.grube.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

import nuleo.autopart.grube.MainActivity;
import nuleo.autopart.grube.Model.Comment;
import nuleo.autopart.grube.Model.User;
import nuleo.autopart.grube.PostActivity;
import nuleo.autopart.grube.R;
import nuleo.autopart.grube.SharedPreferencesManager;

public class ListenNotification extends Service {
    DatabaseReference notificationDB;
    private Uri soundUri = null;
    private SharedPreferencesManager manager;
    String channelId = "Channel_id";

    public ListenNotification() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        manager = new SharedPreferencesManager(ListenNotification.this);
        if (firebaseUser != null) {
            notificationDB = FirebaseDatabase.getInstance().getReference("Notifications").child(firebaseUser.getUid());
        }
     //   notificationArrayList.clear();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                nuleo.autopart.grube.Model.Notification notification =
                        dataSnapshot.getValue(nuleo.autopart.grube.Model.Notification.class);
                String pushKey = dataSnapshot.getKey().toString();
                if (manager.retrieveBoolean(pushKey,false)){
                }else {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                            .child(notification.getUserid());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                User model = snapshot.getValue(User.class);
                                showNotification(notification,model.getFullname());
                                manager.storeBoolean(pushKey,true);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //notificationArrayList.clear();
        return super.onStartCommand(intent, flags, startId);
    }


    PendingIntent notifyPendingIntent;
    private void showNotification(nuleo.autopart.grube.Model.Notification info, String fullname) {

        Intent intent = new Intent(getBaseContext(), MainActivity.class);

        intent.putExtra("status",info.getPostid());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notifyPendingIntent = PendingIntent.getActivity(getBaseContext(),
                    0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        }else {
            notifyPendingIntent = PendingIntent.getActivity(getBaseContext(),
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        Toast.makeText(this, username, Toast.LENGTH_SHORT).show();
        Uri rawPathUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notify);
        Ringtone r = RingtoneManager.getRingtone(ListenNotification.this, rawPathUri);
        r.play();
        createNotificationChannel(this);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setContentTitle(fullname)
                .setContentText(info.getText())
                /*.setLargeIcon(largeIcon)*/
                .setSmallIcon(R.mipmap.ic_launcher_round) //needs white icon with transparent BG (For all platforms)
                .setContentIntent(notifyPendingIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .setNotificationSilent()
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(( int ) System. currentTimeMillis () , notificationBuilder.build());

    }
    String username = "";
    private String getUsername(String userid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(userid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    User model = snapshot.getValue(User.class);
                    username = model.getFullname();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return username;
    }

    public void createNotificationChannel(Context context) {

        // NotificationChannels are required for Notifications on O (API 26) and above.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // The id of the channel.
            // The user-visible name of the channel.
            CharSequence channelName = "Grube";
            // The user-visible description of the channel.
            String channelDescription = "Grubu Alert";
            int channelImportance = NotificationManager.IMPORTANCE_DEFAULT;
            boolean channelEnableVibrate = true;
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            // Initializes NotificationChannel.
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, channelImportance);
            notificationChannel.setDescription(channelDescription);
            notificationChannel.enableVibration(channelEnableVibrate);

//            notificationChannel.setSound(soundUri, audioAttributes);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

}
