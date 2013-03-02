package ch.ocram.tcptap

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.scene.paint.Color.sfxColor2jfx

import java.io.IOException
import javafx.{ fxml => jfxf }
import javafx.{ scene => jfxs }
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene

object TcpTap extends JFXApp {
  val resource = getClass.getResource("MainView.fxml")
  if (resource == null) {
    throw new IOException("Cannot load resource: MainView.fxml")
  }

  // NOTE: ScalaFX doe not yet provide a wrapper fro FXMLLoader (2012.11.12)
  // We load here FXML content using JavaFX directly.
  // It is important to provide type for the element loaded,
  // though it can be a generic, here use `javafx.scene.parent`.
  val root: jfxs.Parent = jfxf.FXMLLoader.load(resource)

  stage = new PrimaryStage() {
    title = "TCP Tap"
    scene = new Scene(root)
  }
}