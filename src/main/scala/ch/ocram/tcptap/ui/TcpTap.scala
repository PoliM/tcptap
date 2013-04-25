package ch.ocram.tcptap.ui

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.application.Platform
import scalafx.stage.WindowEvent

object TcpTap extends JFXApp {
 
  Platform.implicitExit = true
  
  val mainViewController = new MainViewController()
  
  stage = new PrimaryStage() {
    title = "TCP Tap"
    scene = new Scene(mainViewController.getView)
  }
  
  stage.onCloseRequest = { e:WindowEvent => System.exit(0) }
}