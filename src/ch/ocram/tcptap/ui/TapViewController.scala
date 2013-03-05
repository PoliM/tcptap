package ch.ocram.tcptap.ui

import java.io.IOException
import java.net.URL
import java.util
import javafx.{ fxml => jfxf }
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.{ control => jfxsc }
import scalafx.scene.control.Tab
import scalafx.scene.control.Tab.sfxTab2jfx
import scalafx.scene.control.TabPane
import javafx.{ fxml => jfxf }
import javafx.scene.{ control => jfxsc }
import scalafx.scene.control.TextField

class TapViewController(val srcPort: Int, val host: String, val trgPort: Int) extends jfxf.Initializable {

  @jfxf.FXML
  private var fxmlListeningPort: jfxsc.TextField = null
  private var listeningPort: TextField = _

  @jfxf.FXML
  private var fxmlTargetHost: jfxsc.TextField = null
  private var targetHost: TextField = _

  @jfxf.FXML
  private var fxmlTargetPort: jfxsc.TextField = null
  private var targetPort: TextField = _

  private var view: Parent = null

  val fxmlLoader = new FXMLLoader(getClass.getResource("TapView.fxml"));
  fxmlLoader.setController(this);
  try {
    view = fxmlLoader.load().asInstanceOf[Parent]
  } catch {
    case ex: IOException => throw new RuntimeException(ex);
  }

  def getView = view

  def initialize(url: URL, rb: util.ResourceBundle) {
    this.listeningPort = new TextField(this.fxmlListeningPort)
    this.targetHost = new TextField(this.fxmlTargetHost)
    this.targetPort = new TextField(this.fxmlTargetPort)

    this.listeningPort.setText(srcPort.toString())
    this.targetHost.setText(host)
    this.targetPort.setText(trgPort.toString())

  }
}