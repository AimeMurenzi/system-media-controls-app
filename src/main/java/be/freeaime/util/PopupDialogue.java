/**
 * @Author: Aimé
 * @Date:   2022-08-24 20:30:03
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-12-08 23:48:40
 */
package be.freeaime.util;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PopupDialogue {
    // private final GridPane titleGridPane = new GridPane();
    private final Label popupTitleLabel = new Label();
    private final Button popupCloseButton = new Button("X");
    private final VBox popupContentVBox = new VBox();
    private final Button cancelButton = new Button("Cancel");
    private final Button confirmButton = new Button("Confirm");
    private final HBox titleBarHBox = new HBox(//
            popupCloseButton, //
            popupTitleLabel//
    );
    private final VBox popupInnerContainer = new VBox( // /* Node */
            titleBarHBox, //
            popupContentVBox,
            new VBox(//
                    new HBox(//
                            cancelButton, //
                            confirmButton//
                    )//
            )//
    );

    private final GridPane popupOuterContainer = new GridPane();

    private void initUI() {
        // border-bottom
        popupOuterContainer.getChildren().add(popupInnerContainer);

        HBox.setHgrow(popupTitleLabel, Priority.ALWAYS);
        HBox.setHgrow(cancelButton, Priority.ALWAYS);
        HBox.setHgrow(confirmButton, Priority.ALWAYS);
        VBox.setVgrow(popupContentVBox, Priority.ALWAYS);
        confirmButton.setMaxWidth(Double.MAX_VALUE);
        cancelButton.setMaxWidth(Double.MAX_VALUE);
        popupOuterContainer.setAlignment(Pos.CENTER);

        titleBarHBox.getStyleClass().add("border-bottom");
        popupInnerContainer.getStyleClass().add("popup-inner-container");
        popupOuterContainer.getStyleClass().add("popup-outer-container"); 

        popupCloseButton.setOnAction(event -> popupOuterContainer.toBack());
    }

    private PopupDialogue() {
        initUI();
    }

    /**
     *
     * @param content
     * @param title
     * @param callBack
     */
    public static void setContent(Node content, String title,
            IPopupCallback callBack) {
        setContent(content, title, "Confirm", "Cancel", callBack);
    }
    /**
     * 
     * @param title
     * @param message
     */
    public static void setContent(String title, String message) {
        setContent(new Label(message), title, () -> {
        });
    }
    private static void clear() {
        getInstance().popupContentVBox.getChildren().clear();
    }

    private static void add(Node node) {
        getInstance().popupContentVBox.getChildren().add(node);
    }

    /**
     * 
     * @param content
     * @param title
     * @param confirmButtonText
     * @param cancelButtonText
     * @param callBack
     */
    public static void setContent(Node content, String title, String confirmButtonText, String cancelButtonText,
            IPopupCallback callBack) {
        clear();
        add(content);
        setTitle(title);

        setConfirmButtonLabel(confirmButtonText);
        setConfirmButtonCallBack(callBack);

        setCancelButtonLabel(cancelButtonText);
        setCancelButtonAction();

        thisPopupDialogue().toFront();
    }

    private static void setCancelButtonAction() {
        getInstance().cancelButton.setOnAction(event -> thisPopupDialogue().toBack());
    }

    private static void setConfirmButtonCallBack(IPopupCallback callBack) {
        getInstance().confirmButton.setOnAction(event -> {
            thisPopupDialogue().toBack();
            callBack.triggerAction();
        });
    }

    private static void setCancelButtonLabel(String cancelButtonText) {
        getInstance().cancelButton.setText(cancelButtonText);
    }

    private static void setConfirmButtonLabel(String confirmButtonText) {
        getInstance().confirmButton.setText(confirmButtonText);
    }

    private static void setTitle(String title) {
        getInstance().popupTitleLabel.setText(title);
    } 
    private static class HoldInstance {
        private static final PopupDialogue INSTANCE = new PopupDialogue();
    }

    public static PopupDialogue getInstance() {
        return HoldInstance.INSTANCE;
    }

    public static Node thisPopupDialogue() {
        return getInstance().popupOuterContainer;
    }
}