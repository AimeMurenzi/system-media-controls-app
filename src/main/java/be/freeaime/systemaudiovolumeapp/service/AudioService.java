/**
 * @Author: Aimé
 * @Date:   2022-10-24 22:17:56
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-10-29 17:56:59
 */
package be.freeaime.systemaudiovolumeapp.service;

import java.util.*;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.CompoundControl;
import javax.sound.sampled.Control;
import javax.sound.sampled.Control.Type;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;

import be.freeaime.systemaudiovolumeapp.model.CMixer;

public class AudioService {
    public static CMixer getMixer(String mixerName) {
        return getInstance().getMixerByToStringName(mixerName);
    }

    public static Float increaseVolumeBy(String mixerName, String lineName, float incrementValue) {
        return getInstance().increase(mixerName, lineName, incrementValue);
    }

    public static Float decreaseVolumeBy(String mixerName, String lineName, float decrementValue) {
        return getInstance().decrease(mixerName, lineName, decrementValue);
    }

    private Float increase(String mixerName, String lineName, float incrementValue) {
        CMixer mixer = AudioService.getInstance().getMixerByToStringName(mixerName);
        if (mixer != null) {
            Line line = AudioService.getInstance().getLineByToStringName(lineName, mixer);
            if (line != null) {
                boolean opened = AudioService.open(line);
                try {
                    FloatControl volumeControl = AudioService.getVolumeControl(line);
                    if (volumeControl != null) {
                        float oldValue = volumeControl.getValue();
                        float newValue = oldValue + incrementValue;
                        return setVolume(newValue, volumeControl);
                    }
                } finally {
                    if (opened)
                        line.close();
                }
            }
        }
        return 0f;
    }

    private Float decrease(String mixerName, String lineName, float decrementValue) {
        CMixer mixer = AudioService.getInstance().getMixerByToStringName(mixerName);
        if (mixer != null) {
            Line line = AudioService.getInstance().getLineByToStringName(lineName, mixer);
            if (line != null) {
                boolean opened = AudioService.open(line);
                try {
                    FloatControl volumeControl = AudioService.getVolumeControl(line);
                    if (volumeControl != null) {
                        float oldValue = volumeControl.getValue();
                        float newValue = oldValue - decrementValue;
                        return setVolume(newValue, volumeControl);
                    }
                } finally {
                    if (opened)
                        line.close();
                }
            }
        }
        return 0f;
    }

    private static float setVolume(float value, FloatControl volumeControl) {
        value = value > 1 ? 1 : value;
        value = value < 0 ? 0 : value;
        volumeControl.setValue(value);
        return volumeControl.getValue();
    }

    private static final class InstanceHolder {
        private static final AudioService INSTANCE = new AudioService();
    }

    public static AudioService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private final List<CMixer> mixers = new ArrayList<>();
    private final Map<String, CMixer> mixerMap = new LinkedHashMap<>();

    private AudioService() {
        loadMixers();
    }

    private void loadMixers() {
        this.mixers.clear();
        final Info[] mixerInfoArray = AudioSystem.getMixerInfo();
        for (final Info mixerInfo : mixerInfoArray) {
            final Mixer mixer = AudioSystem.getMixer(mixerInfo);
            final CMixer cMixer = new CMixer(mixer);
            mixers.add(cMixer);
            mixerMap.put(cMixer.getName(), cMixer);
        }
    }

    public CMixer getMixerByToStringName(String toStringName) {
        return mixerMap.get(toStringName);
    }

    public Line getLineByToStringName(String toStringName, CMixer cMixer) {
        return cMixer.getLine(toStringName);
    }

    public static List<Line> getOutputLines(Mixer mixer) {
        return getLines(mixer, mixer.getTargetLineInfo());
    }

    private static List<Line> getLines(Mixer mixer, Line.Info[] lineInfos) {
        List<Line> lines = new ArrayList<Line>(lineInfos.length);
        for (Line.Info lineInfo : lineInfos) {
            Line line;
            line = getLine(mixer, lineInfo);
            if (line != null)
                lines.add(line);
        }
        return lines;
    }

    public static boolean open(Line line) {
        if (line.isOpen())
            return false;
        try {
            line.open();
        } catch (LineUnavailableException ex) {
            return false;
        }
        return true;
    }

    public static void setOutputVolume(int vol, Line line) {
        if (line == null)
            return;
        if (vol < 0 || vol > 100)
            return;
        float value = vol == 0 ? vol : vol / 100f;
        boolean opened = open(line);
        try {
            FloatControl control = getVolumeControl(line);
            if (control == null)
                return;

            control.setValue(value);
        } finally {
            if (opened)
                line.close();
        }
    }

    public static FloatControl getVolumeControl(Line line) {
        if (!line.isOpen())
            return null;
        return (FloatControl) findControl(FloatControl.Type.VOLUME, line.getControls());
    }

    private static Control findControl(Type type, Control... controls) {
        if (controls == null || controls.length == 0)
            return null;
        for (Control control : controls) {
            if (control.getType().equals(type))
                return control;
            if (control instanceof CompoundControl) {
                CompoundControl compoundControl = (CompoundControl) control;
                Control member = findControl(type, compoundControl.getMemberControls());
                if (member != null)
                    return member;
            }
        }
        return null;
    }

    private static Line getLine(Mixer mixer, Line.Info lineInfo) {
        try {
            return mixer.getLine(lineInfo);
        } catch (LineUnavailableException ex) {
            return null;
        }
    }

    public static List<CMixer> getMixers() {
        return getInstance().mixers;
    }
}
