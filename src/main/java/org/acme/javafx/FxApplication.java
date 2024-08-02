package org.acme.javafx;

import javafx.application.Application;
import javafx.stage.Stage;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.util.AnnotationLiteral;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import com.pi4j.Pi4J;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.provider.Providers;
import com.pi4j.util.Console;
import com.pi4j.context.Context;

public class FxApplication extends Application {

    private static final int PIN_LED_RED = 14; // PIN 08 = BCM 14
    private static final int PIN_LED_YELLOW = 15; // PIN 10 = BCM 15
    private static final int PIN_LED_GREEN = 18; // PIN 12 = BCM 18

    public static void printProviders(Console console, Context pi4j) {
      Providers providers = pi4j.providers();
      console.box("Pi4J PROVIDERS");
      console.println();
      providers.describe().print(System.out);
      console.println();
    }

    @Override
    public void start(Stage primaryStage) {

      final var console = new Console();
      console.title("<-- The Pi4J Project -->", "Traffic Light");
      var pi4j = Pi4J.newAutoContext();
      printProviders(console, pi4j);

      var ledConfigRed = DigitalOutput.newConfigBuilder(pi4j)
        .id("led01")
	.name("LED Red")
	.address(PIN_LED_RED)
	.shutdown(DigitalState.LOW)
	.initial(DigitalState.LOW)
	.provider("pigpio-digital-output");

      var ledConfigYellow = DigitalOutput.newConfigBuilder(pi4j)
	.id("led02")
	.name("LED Yellow")
	.address(PIN_LED_YELLOW)
	.shutdown(DigitalState.LOW)
	.initial(DigitalState.LOW)
	.provider("pigpio-digital-output");

      var ledConfigGreen = DigitalOutput.newConfigBuilder(pi4j)
	.id("led03")
	.name("LED Green")
	.address(PIN_LED_GREEN)
	.shutdown(DigitalState.LOW)
	.initial(DigitalState.LOW)
	.provider("pigpio-digital-output");

      var ledRed = pi4j.create(ledConfigRed);
      var ledYellow = pi4j.create(ledConfigYellow);
      var ledGreen = pi4j.create(ledConfigGreen);


      Pane lightPane = new Pane();
      Rectangle tLight = new Rectangle(0, 0, 100 / 3.0, 100);
      Circle redLight = new Circle(100 / 3.0 / 2, 100 / 4.0, 10);
      Circle yellowLight = new Circle(100 / 3.0 / 2, 100 / 2.0, 10);
      Circle greenLight = new Circle(100 / 3.0 / 2, 100 / 4.0 * 3, 10);
      tLight.setFill(Color.WHITE);
      tLight.setStroke(Color.BLACK);
      tLight.xProperty().bind(lightPane.widthProperty().divide(2).subtract(tLight.getWidth() / 2));
      tLight.yProperty().bind(lightPane.heightProperty().divide(2).subtract(tLight.getHeight() / 2));
      redLight.setFill(Color.WHITE);
      redLight.setStroke(Color.BLACK);
      redLight.centerXProperty().bind(lightPane.widthProperty().divide(2));
      redLight.centerYProperty().bind(tLight.yProperty().add(20));
      yellowLight.setFill(Color.WHITE);
      yellowLight.setStroke(Color.BLACK);
      yellowLight.centerXProperty().bind(lightPane.widthProperty().divide(2));
      yellowLight.centerYProperty().bind(tLight.yProperty().add(50));
      greenLight.setFill(Color.WHITE);
      greenLight.setStroke(Color.BLACK);
      greenLight.centerXProperty().bind(lightPane.widthProperty().divide(2));
      greenLight.centerYProperty().bind(tLight.yProperty().add(80));

      lightPane.getChildren().addAll(tLight, redLight, yellowLight, greenLight);

      RadioButton rbRed = new RadioButton("Red");
      RadioButton rbYellow = new RadioButton("Yellow");
      RadioButton rbGreen = new RadioButton("Green");

      ToggleGroup tg = new ToggleGroup();
      rbRed.setToggleGroup(tg);
      rbYellow.setToggleGroup(tg);
      rbGreen.setToggleGroup(tg);

      HBox controlPane = new HBox(10);
      controlPane.getChildren().addAll(rbRed, rbYellow, rbGreen);
      controlPane.setAlignment(Pos.CENTER);

      BorderPane pane = new BorderPane();
      pane.setCenter(lightPane);
      pane.setBottom(controlPane);

      rbRed.setOnAction(e -> {

         ledRed.high();
         ledGreen.low();
         ledYellow.low();

         redLight.setFill(Color.RED);
         yellowLight.setFill(Color.WHITE);
         greenLight.setFill(Color.WHITE);
      });

      rbYellow.setOnAction(e -> {

         ledYellow.high();
         ledGreen.low();
         ledRed.low();

         redLight.setFill(Color.WHITE);
         yellowLight.setFill(Color.YELLOW);
         greenLight.setFill(Color.WHITE);
      });

      rbGreen.setOnAction(e -> {

         ledGreen.high();         
         ledRed.low();
         ledYellow.low();

         redLight.setFill(Color.WHITE);
         yellowLight.setFill(Color.WHITE);
         greenLight.setFill(Color.GREEN);
      });

      Scene scene = new Scene(pane, 300, 150);
      primaryStage.setTitle("igfasouza");
      primaryStage.setScene(scene);
      primaryStage.show();

      //pi4j.shutdown();

    }

}
