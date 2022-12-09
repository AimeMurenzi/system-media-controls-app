/**
 * @Author: Aimé
 * @Date:   2022-11-03 14:44:02
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-12-04 22:44:26
 */
package be.freeaime.systemmediacontrol.model;

import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;

import be.freeaime.systemmediacontrol.service.AudioService;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class CMixer {
    private String name;
    private final Mixer mixer;
    private static int duplicateNameCounter = 2;
    private static final Map<String, Line> lineMap = new LinkedHashMap<>();
    private static Line masterLine=null;
    public CMixer(Mixer mixer) {
        this.name = mixer.getMixerInfo().toString();
        this.mixer = mixer;
        AudioService.getOutputLines(mixer).forEach(line -> {
            final String lineName = line.getLineInfo().toString();
            final String key = lineMap.containsKey(lineName) ? lineName + " " + duplicateNameCounter++ : lineName;
            lineMap.put(key, line);
            if(lineName.toLowerCase().contains("master"))
            masterLine=line;
        });
    }
    public static Line getMasterLine(){
        return masterLine;
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
