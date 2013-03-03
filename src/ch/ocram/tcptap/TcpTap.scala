package ch.ocram.tcptap

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