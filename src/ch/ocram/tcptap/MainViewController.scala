package ch.ocram.tcptap

import java.net.URL
import java.util
import javafx.scene.{layout => jfxsl}
import javafx.{event => jfxe}
import javafx.{fxml => jfxf}
import scalafx.scene.layout.GridPane
import javafx.scene.{control => jfxsc }
import scalafx.scene.control.TextField

class MainViewController extends jfxf.Initializable {

  @jfxf.FXML
  private var txtAddress: jfxsc.TextField = null
  private var address: TextField = _


  @jfxf.FXML
  private def onStartMonitoring(event: jfxe.ActionEvent) {
    println(address.text);
  }


  def initialize(url: URL, rb: util.ResourceBundle) {
    address = new TextField(txtAddress)
  }
}