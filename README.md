# SImpleAlarmManager-Android
Simple Alarm Manager is the library which can be used to create, cancel and to handle onBoot, OnTimeChange and OnTimeZone cases to avoid 100 lines of code.

# Instructions

**Initialize Simple Alarm Manager.**
```java
 new SimpleAlarmManager(context).setup(intever, hour_of_day, minute_of_day, seconds_of_day).register(id).start();
```
Resgistration Id should be unique for each alarm. Registering an alarm with same registration id will cancel the prevous alarm and will register a new one again. Use id as -1 to trigger the alarm only once.

Example:-

```java
 new SimpleAlarmManager(MainActivity.this).setup(SimpleAlarmManager.INTERVAL_DAY, 8, 0, 0).register(0).start();
 new SimpleAlarmManager(MainActivity.this).setup(-1, 12, 0, 0).register(1).start();
```

**Handle on Boot/Restart, onTimeChange and onTimeZoneChange case **

Create onBoot, OnTimeChange and OnTimeZoneChange broadcast receiver and add it on Manifest as given:

*1) OnBootReceiver*
```java
public class OnBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // To get set of all registered ids on boot.
        Set<String> set = SimpleAlarmManager.getAllRegistrationIds(context);
        for ( Iterator<String> it = set.iterator(); it.hasNext(); ) {
            int id = Integer.parseInt(it.next());
            //to initialize with registreation id
            SimpleAlarmManager.initWithId(context, id).start();
        }
    }
 }
```
Register OnBootReceiver on Manifest as follows.

``` xml
  <receiver
     android:name=".OnBootReceiver"
     android:exported="false">
         <intent-filter>
             <action android:name="android.intent.action.BOOT_COMPLETED"/>
          </intent-filter>
  </receiver>
```

Add onBoot Permission
```xml
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>
```

*2) OnTimeChangeReceiver*
```java
public class OnTimeChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // To get set of all registered ids on boot.
        Set<String> set = SimpleAlarmManager.getAllRegistrationIds(context);
        for ( Iterator<String> it = set.iterator(); it.hasNext(); ) {
            int id = Integer.parseInt(it.next());
            //to initialize with registreation id
            SimpleAlarmManager.initWithId(context, id).start();
        }
    }
 }
```
Register OnTimeChangeReceiver on Manifest as follows.

``` xml
 <receiver
            android:name=".OnTimeChangeReceiver"
            android:exported="false">
            <intent-filter android:priority="2">
                <action android:name="android.intent.action.TIME_SET"/>
            </intent-filter>
        </receiver>
```
*1) OnTimeZoneChangeReceiver*
```java
public class OnTimeChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // To get set of all registered ids on boot.
        Set<String> set = SimpleAlarmManager.getAllRegistrationIds(context);
        for ( Iterator<String> it = set.iterator(); it.hasNext(); ) {
            int id = Integer.parseInt(it.next());
            //to initialize with registreation id
            SimpleAlarmManager.initWithId(context, id).start();
        }
    }
 }
```
Register OnTimeZoneChangeReceiver on Manifest as follows.

``` xml
<receiver
  android:name=".OnTimeZoneChangeReceiver"
  android:exported="false">
    <intent-filter >
       <action android:name="android.intent.action.TIMEZONE_CHANGED"/>
    </intent-filter>
 </receiver>
```

To cancel an alarm:

```java
new SimpleAlarmManager(context).cancel(id);
```

