package net.vjdv.cvlmaker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Jassiel
 */
public class Main extends Application {
  public static Stage stage;
  @Override
  public void start(Stage stage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("Principal.fxml"));
    Scene scene = new Scene(root);
    stage.setTitle("CVL Maker");
    stage.setScene(scene);
    Main.stage = stage;
    stage.show();
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }
}