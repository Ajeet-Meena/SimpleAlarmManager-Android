package com.beta.android.beewise.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.beta.android.beewise.Service.AlarmReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Ajeet Kumar Meena on 29-10-2015.
 */
public class SimpleAlarmManager {
    private Context context;
    private Intent alarmIntent;
    private int hourOfDay;
    private int minuteOfDay;
    private int secondOfDay;
    private Calendar calendar;
    private int id;
    private long interval;
    public final static long INTERVAL_DAY = AlarmManager.INTERVAL_DAY;
    private static Boolean isInitWithId = Boolean.FALSE;
    private PendingIntent pendingIntent;



    public SimpleAlarmManager(Context context) {
        this.context = context;
        this.alarmIntent = new Intent(context, AlarmReceiver.class);
    }

    public SimpleAlarmManager register(int id) {
        this.id = id;
        alarmIntent.putExtra("id", id);
        SharedPreferences sharedPreferences = context.getSharedPreferences("alarm_manager", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if( sharedPreferences.contains("ids") ) {
            Set<String> set = sharedPreferences.getStringSet("ids", null);
            if( set != null && !set.isEmpty() ) {
                set.add(Integer.toString(id));
            }
        } else {
            Set<String> set = new HashSet<>();
            set.add(Integer.toString(id));
            editor.putStringSet("ids", set);
            editor.apply();
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("interval", this.interval);
            jsonObject.put("hourOfDay", this.hourOfDay);
            jsonObject.put("minuteOfDay", this.minuteOfDay);
            jsonObject.put("secondOfDay", this.secondOfDay);
            editor.putString("idExtra" + id, jsonObject.toString()).apply();
        } catch (JSONException e) {

        }
        return this;
    }

    public static SimpleAlarmManager initWithId(Context context, int id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("alarm_manager", Context.MODE_PRIVATE);
        String registrationExtra = sharedPreferences.getString("idExtra" + id, null);
        int interval, hourDay, minuteOfDay, secondOfDay;
        if( registrationExtra != null ) {
            try {
                JSONObject jsonObject = new JSONObject(registrationExtra);
                interval = jsonObject.getInt("interval");
                hourDay = jsonObject.getInt("hourOfDay");
                minuteOfDay = jsonObject.getInt("minuteOfDay");
                secondOfDay = jsonObject.getInt("secondOfDay");
                isInitWithId = Boolean.TRUE;
                return new SimpleAlarmManager(context).setup(interval, hourDay, minuteOfDay, secondOfDay).register(id);

            } catch (JSONException e) {

            }
        }
        return null;
    }

    public static Set<String> getAllRegistrationIds(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("alarm_manager", Context.MODE_PRIVATE);
        return sharedPreferences.getStringSet("ids", null);
    }

    public SimpleAlarmManager setup(long interval, int hourOfDay, int minuteOfDay, int secondOfDay) {
        this.hourOfDay = hourOfDay;
        this.secondOfDay = secondOfDay;
        this.minuteOfDay = minuteOfDay;
        this.interval = interval;
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minuteOfDay);
        calendar.set(Calendar.SECOND, secondOfDay);
        Calendar now = Calendar.getInstance();
        if( now.after(calendar) )
            calendar.add(Calendar.HOUR_OF_DAY, 24);
        return this;
    }

    public Intent getIntent() {
        return alarmIntent;
    }

    public SimpleAlarmManager start() {
        if( isInitWithId == Boolean.FALSE ) {
            if( PendingIntent.getBroadcast(context, id, alarmIntent, PendingIntent.FLAG_NO_CREATE) == null ) {
                pendingIntent = PendingIntent.getBroadcast(context, id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                if( pendingIntent != null ) {
                    AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    //manager.cancel(pendingIntent);
                    if( interval == -1 ) {
                        manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    } else {
                        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, pendingIntent);
                    }
                }
            }
        } else {
            pendingIntent = PendingIntent.getBroadcast(context, id, alarmIntent, PendingIntent.FLAG_NO_CREATE);
            if( pendingIntent != null ) {
                AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                manager.cancel(pendingIntent);
                if( interval == -1 ) {
                    manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                } else {
                    manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, pendingIntent);
                }
            }
        }
        return this;
    }

}
