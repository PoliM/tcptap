package ch.ocram.tcptap

import java.io.IOException
import java.net.URL

import java.util
import javafx.{event => jfxe}
import javafx.{fxml => jfxf}
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.{control => jfxsc}
import scalafx.scene.control.TextField

class MonitorConfigViewController extends jfxf.Initializable {

  @jfxf.FXML
  private var txtListenPort: jfxsc.TextField = null
  private var listenPort: TextField = _

  @jfxf.FXML
  private var txtTargetHost: jfxsc.TextField = null
  private var targetHost: TextField = _

  @jfxf.FXML
  private var txtTargetPort: jfxsc.TextField = null
  private var targetPort: TextField = _

  private var view: Parent = null

  val fxmlLoader = new FXMLLoader(getClass.getResource("MonitorConfigView.fxml"));
  fxmlLoader.setController(this);
  try {
    view = fxmlLoader.load().asInstanceOf[Parent]
  } catch {
    case ex: IOException => throw new RuntimeException(ex);
  }

  def getView = view
  @jfxf.FXML
  private def onStartMonitoring(event: jfxe.ActionEvent) {
    println(s"${listenPort.text.value} ==> ${targetHost.text.value}:${targetPort.text.value}");
  }

  def initialize(url: URL, rb: util.ResourceBundle) {
    listenPort = new TextField(txtListenPort)
    targetHost = new TextField(txtTargetHost)
    targetPort = new TextField(txtTargetPort)
  }
}