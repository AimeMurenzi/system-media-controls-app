/**
 * @Author: Aimé
 * @Date:   2022-10-29 17:54:23
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-12-08 23:18:44
 */
package be.freeaime.main;

import be.freeaime.systemmediacontrol.launcher.FxEntryPoint;
import javafx.application.Platform;

public class MainApp {
    public static void main(String[] args) {
        Platform.startup(() -> {
        });
        FxEntryPoint.main(args);
    }
}
