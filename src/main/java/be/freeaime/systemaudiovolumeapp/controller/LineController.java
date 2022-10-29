/**
 * @Author: Aimé
 * @Date:   2022-10-29 00:52:53
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-10-29 18:09:43
 */
package be.freeaime.systemaudiovolumeapp.controller;

import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import be.freeaime.systemaudiovolumeapp.model.CMixer;
import be.freeaime.systemaudiovolumeapp.service.AudioService;

@RestController
@RequestMapping("/api")
public class LineController {
    @GetMapping
    public String api() {
        return "system audio volume app api v 0.1.0";
    }

    @GetMapping("/state/mixer/{mixerName}/line/{lineName}")
    public Float state(@PathVariable("mixerName") String mixerName, @PathVariable("lineName") String lineName) {
        if (StringUtils.isNotBlank(mixerName) && StringUtils.isNotBlank(lineName)) {
            CMixer mixer = AudioService.getInstance().getMixerByToStringName(mixerName);
            if (mixer != null) {
                Line line = AudioService.getInstance().getLineByToStringName(lineName, mixer);
                if (line != null) {
                    boolean opened = AudioService.open(line);
                    try {
                        FloatControl volumeControl = AudioService.getVolumeControl(line);
                        if (volumeControl != null) {
                            return volumeControl.getValue();
                        }
                    } finally {
                        if (opened)
                            line.close();
                    }
                }
            }
        }
        return 0f;
    }

    @GetMapping("/volume/decrease/mixer/{mixerName}/line/{lineName}")
    public Float decrease(@PathVariable("mixerName") String mixerName, @PathVariable("lineName") String lineName) {
        if (StringUtils.isNotBlank(mixerName) && StringUtils.isNotBlank(lineName)) {
            return AudioService.decreaseVolumeBy(mixerName, lineName, 0.05f);
        }
        return 0f;
    }

    @GetMapping("/volume/increase/mixer/{mixerName}/line/{lineName}")
    public Float increase(@PathVariable("mixerName") String mixerName, @PathVariable("lineName") String lineName) {
        if (StringUtils.isNotBlank(mixerName) && StringUtils.isNotBlank(lineName)) {
            return AudioService.increaseVolumeBy(mixerName, lineName, 0.05f);
        }
        return 0f;
    }
}
