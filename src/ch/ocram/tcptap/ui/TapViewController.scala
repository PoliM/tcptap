package ch.ocram.tcptap.ui

import javafx.{ event => jfxe }
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
import scalafx.scene.control.ComboBox

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
  private var fxmlFormat: jfxsc.ComboBox[String] = null
  private var format: ComboBox[String] = _

  @jfxf.FXML
  private var fxmlTableConnections: jfxsc.TableView[ConnectionRecord] = null
  private var tableConnections: TableView[ConnectionRecord] = _

  @jfxf.FXML
  private var fxmlColId: jfxsc.TableColumn[ConnectionRecord, String] = null
  private var columnId: TableColumn[ConnectionRecord, String] = _

  @jfxf.FXML
  private var fxmlColTime: jfxsc.TableColumn[ConnectionRecord, String] = null
  private var columnTime: TableColumn[ConnectionRecord, String] = _

  @jfxf.FXML
  private var fxmlColActivity: jfxsc.TableColumn[ConnectionRecord, String] = null
  private var columnActivity: TableColumn[ConnectionRecord, String] = _

  @jfxf.FXML
  private var fxmlColRemoteHost: jfxsc.TableColumn[ConnectionRecord, String] = null
  private var columnRemoteHost: TableColumn[ConnectionRecord, String] = _

  @jfxf.FXML
  private var fxmlColInfo: jfxsc.TableColumn[ConnectionRecord, String] = null
  private var columnInfo: TableColumn[ConnectionRecord, String] = _

  @jfxf.FXML
  private var fxmlRequest: jfxsc.TextArea = null
  private var textRequest: TextArea = _

  @jfxf.FXML
  private var fxmlResponse: jfxsc.TextArea = null
  private var textResponse: TextArea = _

  private val connectionList = new ObservableBuffer[ConnectionRecord]()

  private val connectionListener = new ConnectListener(this.connectionList, srcPort, host, trgPort)

  private var view: Parent = null

  private var currentRecord: ConnectionRecord = null;

  val fxmlLoader = new FXMLLoader(getClass.getResource("TapView.fxml"));
  fxmlLoader.setController(this);
  try {
    view = fxmlLoader.load().asInstanceOf[Parent]
  } catch {
    case ex: IOException => throw new RuntimeException(ex);
  }

  def getView = view

  @jfxf.FXML
  def onClose(event: jfxe.ActionEvent) {

  }

  @jfxf.FXML
  def onRemoveSelected(event: jfxe.ActionEvent) {
    val record = this.tableConnections.selectionModel().selectedItem()
    if (record != null) {

    }
  }

  @jfxf.FXML
  def onRemoveAll(event: jfxe.ActionEvent) {

  }

  def initialize(url: URL, rb: util.ResourceBundle) {
    this.listeningPort = new TextField(this.fxmlListeningPort)
    this.targetHost = new TextField(this.fxmlTargetHost)
    this.targetPort = new TextField(this.fxmlTargetPort)
    this.format = new ComboBox(this.fxmlFormat)
    this.tableConnections = new TableView(this.fxmlTableConnections)
    this.columnId = new TableColumn(this.fxmlColId)
    this.columnTime = new TableColumn(this.fxmlColTime)
    this.columnActivity = new TableColumn(this.fxmlColActivity)
    this.columnRemoteHost = new TableColumn(this.fxmlColRemoteHost)
    this.columnInfo = new TableColumn(this.fxmlColInfo)
    this.textRequest = new TextArea(this.fxmlRequest)
    this.textResponse = new TextArea(this.fxmlResponse)

    this.listeningPort.text = srcPort.toString()
    this.targetHost.text = host
    this.targetPort.text = trgPort.toString()

    this.format.items().clear()
    this.format += "String"
    this.format += "XML"
    this.format += "HEX"
    this.format.selectionModel().select(0);
    this.format.selectionModel().selectedItem.onChange((_, _, _) => this.showRecord())

    this.tableConnections.items = this.connectionList
    val selmodel = this.tableConnections.selectionModel()
    selmodel.selectionMode = SelectionMode.SINGLE
    selmodel.cellSelectionEnabled = false
    selmodel.selectedItem.onChange((_, _, newitem) => {
      this.currentRecord = newitem;
      this.showRecord()
    })

    this.columnId.cellValueFactory = { _.value.id }
    this.columnTime.cellValueFactory = { _.value.time }
    this.columnActivity.cellValueFactory = { _.value.activity }
    this.columnRemoteHost.cellValueFactory = { _.value.remoteHost }
    this.columnInfo.cellValueFactory = { _.value.info }

    val textStyle = "-fx-font-family: 'Consolas', 'Envy Code R', 'monospace';"
    this.textRequest.style = textStyle
    this.textResponse.style = textStyle

    new Thread(this.connectionListener).start();
  }

  def showRecord() {
    if (this.currentRecord == null) {
      this.textRequest.text = ""
      this.textResponse.text = ""
    } else {
      val fmt = this.format.selectionModel().selectedItem()
      this.textRequest.text = this.currentRecord.client2Target.getFormatedString(fmt)
      this.textResponse.text = this.currentRecord.target2Client.getFormatedString(fmt)
    }
  }
}