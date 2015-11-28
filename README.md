# SImpleAlarmManager-Android
Simple Alarm Manager is the library which can be used to create, cancel and to handle onBoot, OnTimeChange and OnTimeZone cases to avoid 100 lines of code.

# Instructions

Intialize Simple Alarm Manager.

```java
 new SimpleAlarmManager(MainActivity.this).setup(SimpleAlarmManager.INTERVAL_DAY, 8, 0, 0).register(0).start();
 new SimpleAlarmManager(MainActivity.this).setup(-1, 12, 0, 0).register(1).start();
```
