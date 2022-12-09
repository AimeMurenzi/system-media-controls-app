/**
 * @Author: Aimé
 * @Date:   2022-08-27 01:40:28
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-08-27 01:40:44
 */
package be.freeaime.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Log {

    public static void normal(Object object, String message) {
        System.out.println(String.format(" [ %s ] %s", object.getClass().getName(), message));
        // Logger.getLogger(object.getClass().getName()).log(Level.INFO,
        // String.format("[ %s ] %s", object.getClass().getName(), message));

    }

    public static void info(String message) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HH-mm-ss");// 20xx/xx/xx 00:00:00
        Date date = new Date();
        String newMessage = String.format(" [ info     ] %s | %s", dateFormat.format(date), message);
        System.out.println(newMessage);
    }

    public static String database(String message) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HH-mm-ss");// 20xx/xx/xx 00:00:00
        Date date = new Date();
        String newMessage = String.format(" [ database ] %s | %s", dateFormat.format(date), message);
        System.out.println(newMessage);
        return newMessage;
    }

    public static void err(Object object, String message) {
        System.out.println(String.format(" [ %s ] %s", object.getClass().getName(), message));
        // Logger.getLogger(object.getClass().getName()).log(Level.SEVERE,
        // String.format("[ %s ] %s", object.getClass().getName(), message));
    }

    public static void duration(String message) {
        long endTime = System.nanoTime();
        double duration = (double) (endTime - startTime) / 1000000000;
        Log.info(message + "[ " + duration + "]");
    }

    private static long startTime = 0;

    public static void recordStartTime() {
        startTime = System.nanoTime();
    }

    private static final HashMap<String, Long> timeHashMap = new HashMap<>();

    public static void recordStartTime(String timerName) {
        timeHashMap.put(timerName, System.nanoTime());
    }

    public static void duration(String timerName, String message) {
        long endTime = System.nanoTime();
        Long startTime = timeHashMap.get(timerName);
        if (startTime == null) {
            return;
        }
        double duration = (double) (endTime - startTime) / 1000000000;
        Log.info(message + "[ " + duration + "]");
    }

}

class TimerInteger {
    public long time = 0;
}