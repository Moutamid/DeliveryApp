package nuleo.autopart.grube.Service;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import nuleo.autopart.grube.Model.NotificationObject;
import nuleo.autopart.grube.SharedPreferencesManager;

public class NotificationService extends NotificationListenerService {
    Context context;
    String titleData="", textData="";
    SharedPreferencesManager prefs;
    List<NotificationObject> objectList;
    HashMap<String,Boolean> isNotificationExist;

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = new SharedPreferencesManager(this);
        context = getApplicationContext();
        objectList = new ArrayList<>();
        isNotificationExist = new HashMap<>();
    }
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String packageName = sbn.getPackageName();
        System.out.println("=====packageName---"+packageName);

        Bundle extras = sbn.getNotification().extras;
        if(extras.getString("android.title")!=null){
            titleData = extras.getString("android.title");
        }else{
            titleData = "";
        }
        if(extras.getCharSequence("android.text")!=null){
            textData = extras.getCharSequence("android.text").toString();
        }else{
            textData = "";
        }
        int id1 = extras.getInt(Notification.EXTRA_SMALL_ICON);
    //    Bitmap id = sbn.getNotification().largeIcon;
        int icon = sbn.getNotification().icon;

        Intent msgrcv = new Intent("Msg");
        msgrcv.putExtra("id", id1);
        msgrcv.putExtra("title", titleData);
        msgrcv.putExtra("text", textData);
        msgrcv.putExtra("icon", icon);

        NotificationObject model = new NotificationObject(titleData,textData,id1,icon);
        String uniqueKey = sbn.getKey();
        System.out.println("=====key---"+uniqueKey);
        // if unique key is not exist in hash map then its new notification add in list
        if(!isNotificationExist.containsKey(uniqueKey)){
            System.out.println("=====new notification");
            model.setUniqueKey(uniqueKey);
            isNotificationExist.put(uniqueKey,true);
            //objectList.add(model);
            //Gson gson = new Gson();
            //String jsonObject = gson.toJson(objectList);

            //prefs.storeString("notify",jsonObject);

            LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
        }
        else{
            System.out.println("=====old notification---");
        }

    }
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.d("Msg", "Notification Removed");
        //prefs.clear();
        // remove from hash map
        isNotificationExist.remove(sbn.getKey());
        Iterator<NotificationObject> iterator = objectList.iterator();
        while (iterator.hasNext()){
            NotificationObject object = iterator.next();
            if(object.getUniqueKey().equalsIgnoreCase(sbn.getKey())){
                iterator.remove();
            }
        }
        Intent msgrcv = new Intent("Msg");
        //Gson gson = new Gson();
        //String jsonObject = gson.toJson(objectList);

        //prefs.storeString("notify",jsonObject);

        LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
    }


    public String today() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm aa");
        return sdf.format(Calendar.getInstance().getTime());
    }

}