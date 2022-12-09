/**
 * @Author: Aimé
 * @Date:   2022-12-04 14:14:53
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-12-08 22:58:30
 */
package be.freeaime.systemmediacontrol.service;

import java.nio.file.Paths;

public class MediaKey {  
    static { 
        final String libName = "media-key.so"; 
        final String lib =Paths.get(System.getProperty("user.dir"), libName).toString();
        System.load(lib);
    } 
    public static native void mute(); 
    public static native void play(); 
    public static native void pause(); 
    public static native void stop(); 
    public static native void next(); 
    public static native void previous();
    public static native void lowerVolume();
    public static native void raiseVolume(); 
}