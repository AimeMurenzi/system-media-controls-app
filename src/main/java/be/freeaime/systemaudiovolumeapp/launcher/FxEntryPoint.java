/**
 * @Author: Aimé
 * @Date:   2022-08-18 21:41:45
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-10-29 18:10:02
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.freeaime.systemaudiovolumeapp.launcher;

import static java.lang.System.exit;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author legacy
 */
public class FxEntryPoint extends Application {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    // private final static TreeLocationGridPane changeLeaf = new
    // TreeLocationGridPane();
    private static final ListView<String> mixerList = new ListView<>();
    private static final ListView<String> lineList = new ListView<>();
    private static final SplitPane SPLIT_PANE = new SplitPane(
            new VBox(new Label("Mixers"),
                    mixerList),
            new VBox(new Label("Lines"), lineList)

    );

    @Override
    public void start(Stage primaryStage) throws IOException {

        final Scene scene = new Scene(SPLIT_PANE, 800, 600);
        // final URL cssUrl = FxEntryPoint.class.getResource("/css/main.css");
        // final String css = cssUrl.toString();
        // scene.getStylesheets().add(css);
        // final String icon =
        // getClass().getResource("/images/iconfinder_icon-leaf_211852.png").toString();
        // primaryStage.getIcons().add(new Image(icon));
        primaryStage.setScene(scene);

        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                exit(0);
            }
        });
        new Thread(() -> {
            while (true) {
                try {
                    long heapSize = (long) ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())
                            / 1024 / 1024);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            primaryStage.setTitle("Leaf Player - " + heapSize + "MB / "
                                    + (Runtime.getRuntime().totalMemory() / 1024 / 1024));
                        }
                    });
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FxEntryPoint.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }

}
