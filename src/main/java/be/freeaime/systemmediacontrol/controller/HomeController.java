/**
 * @Author: Aimé
 * @Date:   2022-10-25 16:02:26
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-12-04 21:04:48
 */
package be.freeaime.systemmediacontrol.controller;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import be.freeaime.systemmediacontrol.model.CMixer;
import be.freeaime.systemmediacontrol.service.AudioService;

@Controller
public class HomeController {
    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("Audio", AudioService.getInstance());
        return "home";
    }
    @GetMapping("/advanced")
    public String advancedPage(Model model) {
        model.addAttribute("Audio", AudioService.getInstance());
        return "advanced";
    }

    @GetMapping("/mixer/{mixerName}")
    public String homePage(Model model, @PathVariable String mixerName) {
        model.addAttribute("Audio", AudioService.getInstance());
        model.addAttribute("mixerName", mixerName);

        if (StringUtils.isNotBlank(mixerName)) {
            CMixer mixer = AudioService.getMixer(mixerName);
            if (mixer != null) {
                Set<String> lines = mixer.getLines();
                model.addAttribute("lines", lines);
            }
        }
        return "mixer";
    }

    @GetMapping("/mixer/{mixerName}/line/{line}")
    public String line(Model model, @PathVariable String mixerName, @PathVariable String line) {
        model.addAttribute("Audio", AudioService.getInstance());
        model.addAttribute("mixerName", mixerName);
        model.addAttribute("line", line);
        return "line";
    }
}
