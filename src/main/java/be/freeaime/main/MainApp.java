/**
 * @Author: Aimé
 * @Date:   2022-10-29 17:54:23
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-10-29 18:09:34
 */
package be.freeaime.main;

import org.springframework.boot.SpringApplication;

import be.freeaime.systemaudiovolumeapp.SystemAudioVolumeApp;

public class MainApp {
    public static void main(String[] args) {
        SpringApplication.run(SystemAudioVolumeApp.class, args);
    }
}
