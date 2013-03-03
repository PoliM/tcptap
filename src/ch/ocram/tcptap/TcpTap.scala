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
 
  val mainViewController = new MainViewController()
  
  
  
  stage = new PrimaryStage() {
    title = "TCP Tap"
    scene = new Scene(mainViewController.getView)
  }
}