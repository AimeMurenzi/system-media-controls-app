/**
 * @Author: Aimé
 * @Date:   2022-10-29 00:52:53
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-12-07 18:15:23
 */
package be.freeaime.systemmediacontrol.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import be.freeaime.systemmediacontrol.model.CMixer;
import be.freeaime.systemmediacontrol.service.AudioService;
import be.freeaime.systemmediacontrol.service.MediaKey;

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
               return AudioService.getLineVolumeStatus(lineName, mixer); 
            }
        }
        return 0f;
    }
    @GetMapping("/state/mixer/main/line")
    public Float stateMainMixerLine() { 
        return AudioService.getMasterLineStatus();
    }

    @GetMapping("/volume/decrease/mixer/{mixerName}/line/{lineName}")
    public Float decreaseAdvanced(@PathVariable("mixerName") String mixerName, @PathVariable("lineName") String lineName) {
        if (StringUtils.isNotBlank(mixerName) && StringUtils.isNotBlank(lineName)) {
            return AudioService.decreaseVolumeBy(mixerName, lineName, 0.05f);
        }
        return 0f;
    }

    @GetMapping("/volume/increase/mixer/{mixerName}/line/{lineName}")
    public Float increaseAdvanced(@PathVariable("mixerName") String mixerName, @PathVariable("lineName") String lineName) {
        if (StringUtils.isNotBlank(mixerName) && StringUtils.isNotBlank(lineName)) {
            return AudioService.increaseVolumeBy(mixerName, lineName, 0.05f);
        }
        return 0f;
    }
    @GetMapping("/volume/increase")
    public Float increase() {
        MediaKey.raiseVolume(); 
        return AudioService.getMasterLineStatus();
    }
    @GetMapping("/volume/decrease")
    public Float decrease() {
        MediaKey.lowerVolume(); 
        return AudioService.getMasterLineStatus();
    }
    @GetMapping("/volume/mute")
    public Float mute() {
        MediaKey.mute(); 
        return AudioService.getMasterLineStatus();
    } 
    @GetMapping("/media/next")
    public void next() {
        MediaKey.next();  
    }
    @GetMapping("/media/previous")
    public void previous() {
        MediaKey.previous();  
    }
    @GetMapping("/media/play")
    public void play() {
        MediaKey.play();  
    }
}
