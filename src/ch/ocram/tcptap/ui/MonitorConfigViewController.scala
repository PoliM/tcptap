package ch.ocram.tcptap.ui

import scalafx.Includes._
import java.io.IOException
import java.net.URL
import java.util
import javafx.{ event => jfxe }
import javafx.{ fxml => jfxf }
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.{ control => jfxsc }
import scalafx.scene.control.TextField
import scalafx.event.{ Event, ActionEvent }
import javafx.{ fxml => jfxf }
import javafx.scene.{ control => jfxsc }
import scalafx.stage.Popup
import scalafx.scene.layout.StackPane
import scalafx.scene.shape.Rectangle
import scalafx.scene.paint.Color
import scalafx.scene.layout.BorderPane
import scalafx.scene.control.Label
import scalafx.scene.control.Button
import scalafx.geometry.Pos
import scalafx.geometry.Insets
import scalafx.stage.Stage

class MonitorConfigViewController(val mainViewController: MainViewController) extends jfxf.Initializable {

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

    try {
      val srcPort = listenPort.text.value.toInt
      val trgPort = targetPort.text.value.toInt
      val host = targetHost.text.value.trim();
      if (host.isEmpty()) {
        this.invalidEntry("Missing host");
      } else {
        this.mainViewController.startNewMonitor(srcPort, host, trgPort);
      }
    } catch {
      case ex: NumberFormatException => this.invalidEntry("Port number invalid");
    }
  }

  def initialize(url: URL, rb: util.ResourceBundle) {
    listenPort = new TextField(txtListenPort)
    targetHost = new TextField(txtTargetHost)
    targetPort = new TextField(txtTargetPort)
  }

  private def invalidEntry(msg: String) {
    val popup = new Popup {
      inner =>
      content.add(new StackPane {
        content = List(
          new Rectangle {
            width = 300
            height = 200
            arcWidth = 20
            arcHeight = 20
            fill = Color.LIGHTBLUE
            stroke = Color.GRAY
            strokeWidth = 2
          },
          new BorderPane {
            center = new Label {
              text = msg
              wrapText = true
              maxWidth = 280
              maxHeight = 140
            }
            bottom = new Button("OK") {
              onAction = (_: ActionEvent) => inner.hide
              alignmentInParent = Pos.CENTER
              margin = Insets(10, 0, 10, 0)
            }
          })
      }.delegate)
    }

    popup.show(TcpTap.stage, (TcpTap.stage.width() - popup.width()) / 2.0 + TcpTap.stage.x(),
      (TcpTap.stage.height() - popup.height()) / 2.0 + TcpTap.stage.y())
  }
}