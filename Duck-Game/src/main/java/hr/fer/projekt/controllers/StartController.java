package hr.fer.projekt.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartController {

    @FXML
    private void playButton(ActionEvent event) throws Exception {

        startGame(false, event);

    }

    @FXML
    private void aiPlayButton(ActionEvent event) throws Exception {

        startGame(true, event);

    }

    private void startGame(boolean isAI, ActionEvent event) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hr/fer/projekt/application/view.fxml"));
        Scene gameScene = new Scene(fxmlLoader.load());
        gameScene.getRoot().requestFocus();

        Controller controller = fxmlLoader.getController();
        controller.setNeuralNetworkPlaying(isAI);

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        stage.setScene(gameScene);
    }
}
