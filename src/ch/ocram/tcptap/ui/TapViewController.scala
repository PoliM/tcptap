package ch.ocram.tcptap.ui

import scalafx.Includes._
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
import ch.ocram.tcptap.monitor.ConnectionMonitor
import ch.ocram.tcptap.monitor.ConnectListener
import scalafx.collections.ObservableBuffer
import ch.ocram.tcptap.model.ConnectionRecord
import scalafx.scene.control.TableColumn
import scalafx.scene.control.TableView
import scalafx.scene.control.TextArea
import scalafx.beans.value.ObservableValue
import scalafx.scene.control.SelectionMode

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

  @jfxf.FXML
  private var fxmlTableConnections: jfxsc.TableView[ConnectionRecord] = null
  private var tableConnections: TableView[ConnectionRecord] = _

  @jfxf.FXML
  private var fxmlColId: jfxsc.TableColumn[ConnectionRecord, String] = null
  private var columnId: TableColumn[ConnectionRecord, String] = _

  @jfxf.FXML
  private var fxmlColRemoteHost: jfxsc.TableColumn[ConnectionRecord, String] = null
  private var columnRemoteHost: TableColumn[ConnectionRecord, String] = _

  @jfxf.FXML
  private var fxmlRequest: jfxsc.TextArea = null
  private var textRequest: TextArea = _

  @jfxf.FXML
  private var fxmlResponse: jfxsc.TextArea = null
  private var textResponse: TextArea = _

  private val connectionList = new ObservableBuffer[ConnectionRecord]()

  private val connectionListener = new ConnectListener(this.connectionList, srcPort, host, trgPort)

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
    this.tableConnections = new TableView(this.fxmlTableConnections)
    this.columnId = new TableColumn(this.fxmlColId)
    this.columnRemoteHost = new TableColumn(this.fxmlColRemoteHost)
    this.textRequest = new TextArea(this.fxmlRequest)
    this.textResponse = new TextArea(this.fxmlResponse)

    this.tableConnections.items = this.connectionList
    val selmodel = this.tableConnections.selectionModel()
    selmodel.setSelectionMode(SelectionMode.SINGLE)
    selmodel.cellSelectionEnabled = false
    selmodel.selectedItem.onChange((_, _, newitem) => { this.showRecord(newitem) })

    this.columnId.cellValueFactory = { _.value.id }
    this.columnRemoteHost.cellValueFactory = { _.value.remoteHost }

    this.listeningPort.text = srcPort.toString()
    this.targetHost.text = host
    this.targetPort.text = trgPort.toString()

    new Thread(this.connectionListener).start();
  }

  def showRecord(record: ConnectionRecord) {
    if (record == null) {
      this.textRequest.text = ""
      this.textResponse.text = ""
    } else {
      this.textRequest.text = record.client2Target.getRawString()
      this.textResponse.text = record.target2Client.getRawString()
    }
  }
}