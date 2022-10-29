package be.freeaime.systemaudiovolumeapp.model;

import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;

import be.freeaime.systemaudiovolumeapp.service.AudioService;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class CMixer {
    private String name;
    private final Mixer mixer;
    private int duplicateNameCounter = 2;
    private final Map<String, Line> lineMap = new LinkedHashMap<>();

    public CMixer(Mixer mixer) {
        this.name = mixer.getMixerInfo().toString();
        this.mixer = mixer;
        AudioService.getOutputLines(mixer).forEach(line -> {
            final String lineName = line.getLineInfo().toString();
            final String key = lineMap.containsKey(lineName) ? lineName + " " + duplicateNameCounter++ : lineName;
            lineMap.put(key, line);
        });
    }

    public Mixer getMixer() {
        return mixer;
    }

    public String getName() {
        return name;
    }

    public Set<String> getLines() {
        return lineMap.keySet();
    }

    public Line getLine(String toStringName) {
        return lineMap.get(toStringName);
    }
}
