/**
 * @Author: Aimé
 * @Date:   2022-08-18 21:41:45
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-12-09 00:06:28
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.freeaime.systemmediacontrol.launcher;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import be.freeaime.main.MainApp;
import be.freeaime.systemmediacontrol.SystemAudioVolumeApp;
import be.freeaime.util.Settings;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.converter.IntegerStringConverter;

/**
 *
 * @author legacy
 */
public class FxEntryPoint extends Application {
    private ConfigurableApplicationContext context = null;

    private void restart() {
        restart(portStringProperty.get());
    }

    private boolean portAvailableCheck(String port) throws IllegalStateException {
        try (Socket ignored = new Socket("localhost", Integer.parseInt(port))) {
            return false;
        } catch (IOException e) {
            return true;
        }
    }

    private void restart(String port) {

        Thread thread = new Thread(() -> {
            if (!portAvailableCheck(port)) {
                Platform.runLater(() -> {
                    mainViewStateLabel.setText("Error: Port " + port + " Already In Use");
                    if (!mainViewStateLabel.getStyleClass().contains("error"))
                        mainViewStateLabel.getStyleClass().add("error");
                    showMainScreen();
                }); 
                return;
            }
            
            initMediaLib();
            SpringApplication app = new SpringApplication(SystemAudioVolumeApp.class);
            app.setDefaultProperties(Collections.singletonMap("server.port", port));
            app.setHeadless(false);
            if (context != null && context.isActive()) {
                ApplicationArguments args = context.getBean(ApplicationArguments.class);
                context.close(); 
                context = app.run(args.getSourceArgs());
            }
            if (context == null || !context.isActive()) {
                context = app.run();
            }
            Platform.runLater(() -> {
                mainViewStateLabel.setText("OK");
                mainViewStateLabel.getStyleClass().remove("error");
                mainViewAddressLabel.setText("http://" + ipAddressString + ":" + port);
                showMainScreen();
            });

        });
        thread.setDaemon(false);
        thread.start();
    } 

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    } 
    private String ipAddressString = "unknown";
    private static final Button changePortViewRestart = new Button("Restart");
    private static final Button changePortViewBack = new Button("Back");
    private static final HBox changePortViewBackButton = new HBox(
            changePortViewRestart,
            changePortViewBack);
    private static final TextField changePortViewTextField = new TextField("8080");
    private static final Label changePortViewLabel = new Label();
    private static final Node[] changePortView = new Node[] {
            new HBox(new Label("CURRENT PORT: "), changePortViewLabel),
            changePortViewTextField,
            changePortViewBackButton
    };
    private static final Button mainViewChangePortButton = new Button("Change Port");
    private static final Button mainViewRestartButton = new Button("Restart");
    private static final Label mainViewStateLabel = new Label("---");
    private static final HBox mainViewSettingButtonsHBox = new HBox(
            mainViewChangePortButton,
            mainViewRestartButton);
    private static final StringProperty portStringProperty = new SimpleStringProperty(Settings.getServerPort());
    private static final Label mainViewPortLabel = new Label();
    private static final Label mainViewAddressLabel = new Label();
    private static final Node[] mainView = new Node[] {
            new HBox(new Label("PORT: "), mainViewPortLabel),
            new HBox(new Label("PORT STATE: "), mainViewStateLabel),
            new HBox(new Label("ADDRESS: "), mainViewAddressLabel),
            mainViewSettingButtonsHBox
    };
    private static final VBox innerContainer = new VBox(mainView);

    private static final GridPane outerContainer = new GridPane();

    private void showChangePortScreen() {
        innerContainer.getChildren().clear();
        innerContainer.getChildren().addAll(changePortView);
    }

    private void showMainScreen() {
        innerContainer.getChildren().clear();
        innerContainer.getChildren().addAll(mainView);

    }

    private boolean portInputNumber = false;

    private void changePort() {
        String port = changePortViewTextField.getText();
        if (StringUtils.isNotBlank(port) && portInputNumber) {
            portStringProperty.set(port);
            restart(port);
            Settings.saveServerPort(port);
        }
    }

    private String getLocalIpAddress() {
        try (final DatagramSocket datagramSocket = new DatagramSocket()) {
            datagramSocket.connect(InetAddress.getByName("8.8.8.8"), 12345);
            return datagramSocket.getLocalAddress().getHostAddress();
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }
        return "unknown";
    }

    private void initMediaLib() {
        final String libName = "media-key.so";
        final Path libPath = Paths.get(System.getProperty("user.dir"), libName);
        final String destinationAbsoluteString = libPath.toString();
        if (Files.exists(libPath)) {
            return;
        }
        final byte[] buffer = new byte[4096]; 
        int bytes_read;
        try (InputStream source = MainApp.class.getClassLoader().getResourceAsStream("lib/media-key.so");
                FileOutputStream destination = new FileOutputStream(destinationAbsoluteString);) {
            while (((bytes_read = source.read(buffer)) > 0)) {
                destination.write(buffer, 0, bytes_read);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        outerContainer.getChildren().add(innerContainer);
        outerContainer.setAlignment(Pos.CENTER);
        outerContainer.setPadding(new Insets(10));
        innerContainer.setSpacing(10);
        changePortViewBackButton.setSpacing(10);
        mainViewSettingButtonsHBox.setSpacing(10);
        changePortViewRestart.setMaxWidth(Double.MAX_VALUE);
        changePortViewBack.setMaxWidth(Double.MAX_VALUE);
        mainViewChangePortButton.setMaxWidth(Double.MAX_VALUE);
        mainViewRestartButton.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(changePortViewRestart, Priority.ALWAYS);
        HBox.setHgrow(changePortViewBack, Priority.ALWAYS);
        HBox.setHgrow(mainViewChangePortButton, Priority.ALWAYS);
        HBox.setHgrow(mainViewRestartButton, Priority.ALWAYS);

        mainViewChangePortButton.setOnAction(event -> showChangePortScreen());
        changePortViewBack.setOnAction(event -> showMainScreen());
        changePortViewRestart.setOnAction(event -> changePort());
        mainViewRestartButton.setOnAction(event -> restart(portStringProperty.get()));

        changePortViewLabel.textProperty().bind(portStringProperty);
        mainViewPortLabel.textProperty().bind(portStringProperty);

        changePortViewTextField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
        changePortViewTextField.textProperty().addListener((ob, ov, nv) -> {
            try {
                changePortViewTextField.getTextFormatter().getValueConverter().fromString(nv);
                changePortViewTextField.setBorder(null);
                changePortViewTextField.getStyleClass().remove("error");
                portInputNumber = true;
            } catch (NumberFormatException e) {
                portInputNumber = false;
                changePortViewTextField.getStyleClass().add("error");
            }
        });
        final URL cssUrl = FxEntryPoint.class.getResource("/fxml/css/main.css");
        final String css = cssUrl.toString();
        final String icon = getClass().getResource("/fxml/images/icon.png").toString();
        final Scene scene = new Scene(outerContainer, 600, 300);
        scene.getStylesheets().add(css);
        primaryStage.getIcons().add(new Image(icon));
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
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
                            primaryStage.setTitle("System Media Controls - " + heapSize + "MB / "
                                    + (Runtime.getRuntime().totalMemory() / 1024 / 1024));
                        }
                    });
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FxEntryPoint.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
        mainViewStateLabel.setText(portStringProperty.get());
        ipAddressString = getLocalIpAddress();
        restart();

    }

}
