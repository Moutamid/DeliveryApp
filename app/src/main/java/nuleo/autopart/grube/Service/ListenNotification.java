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
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

import nuleo.autopart.grube.MainActivity;
import nuleo.autopart.grube.R;

public class ListenNotification extends Service implements ChildEventListener {
    DatabaseReference requests;

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
        if (firebaseUser != null) {
            requests = FirebaseDatabase.getInstance().getReference("Notifications").child(firebaseUser.getUid());
        }
     //   notificationArrayList.clear();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        requests.addChildEventListener(this);
        //notificationArrayList.clear();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
      //  for (DataSnapshot snapshot : dataSnapshot.getChildren()){
            nuleo.autopart.grube.Model.Notification notification =
                    dataSnapshot.getValue(nuleo.autopart.grube.Model.Notification.class);
        showNotification(notification);


    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }
    PendingIntent notifyPendingIntent;
    private void showNotification(nuleo.autopart.grube.Model.Notification info) {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);

        //  intent.putExtra("status",true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notifyPendingIntent = PendingIntent.getActivity(getBaseContext(),
                    0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        }else {
            notifyPendingIntent = PendingIntent.getActivity(getBaseContext(),
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        Uri soundUri = Uri.parse("android.resource://" + getBaseContext().getPackageName() + "/" +
                R.raw.notify);
        String channel_id = createNotificationChannel(this);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channel_id)
                .setContentTitle(info.getPostid())
                .setContentText(info.getText())
                /*.setLargeIcon(largeIcon)*/
                .setSmallIcon(R.mipmap.ic_launcher_round) //needs white icon with transparent BG (For all platforms)
                .setContentIntent(notifyPendingIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .setOngoing(true)
                .setSound(soundUri)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(( int ) System. currentTimeMillis () , notificationBuilder.build());
        /*NotificationCompat.Builder notify = new NotificationCompat.Builder(this);
        notify.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker(info.getPostid())
                .setContentTitle(info.getText())
                .setPriority(Notification.PRIORITY_MAX)
              //  .setContentText("Order # "+key+" is updated to " + Common.codeConverter(info.getStatus()))
                .setContentIntent(notifyPendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher_round);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channel_id,
                    "Default channel", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(1, notify.build());*/

    }

    public static String createNotificationChannel(Context context) {

        // NotificationChannels are required for Notifications on O (API 26) and above.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // The id of the channel.
            String channelId = "Channel_id";

            // The user-visible name of the channel.
            CharSequence channelName = "Grube";
            // The user-visible description of the channel.
            String channelDescription = "Grubu Alert";
            int channelImportance = NotificationManager.IMPORTANCE_HIGH;
            boolean channelEnableVibrate = true;
            Uri soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" +
                    R.raw.notify);
//            int channelLockscreenVisibility = Notification.;
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            // Initializes NotificationChannel.
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, channelImportance);
            notificationChannel.setDescription(channelDescription);
            notificationChannel.enableVibration(channelEnableVibrate);
            notificationChannel.setSound(soundUri,audioAttributes);
//            notificationChannel.setLockscreenVisibility(channelLockscreenVisibility);

            // Adds NotificationChannel to system. Attempting to create an existing notification
            // channel with its original values performs no operation, so it's safe to perform the
            // below sequence.
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);

            return channelId;
        } else {
            // Returns null for pre-O (26) devices.
            return null;
        }
    }

    @Override

    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
