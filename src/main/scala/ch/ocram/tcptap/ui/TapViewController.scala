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
import javafx.scene.{layout => jfxsl }

class TapViewController(val srcPort: Int, val host: String, val trgPort: Int) extends jfxf.Initializable {

  @jfxf.FXML
  private var fxmlMainSplit : jfxsc.SplitPane = null
  
  @jfxf.FXML
  private var fxmlLeftPane : jfxsl.AnchorPane = null
  
  @jfxf.FXML
  private var fxmlListeningPort: jfxsc.TextField = null
  private var listeningPort: TextField = _

  @jfxf.FXML
  private var fxmlTargetHost: jfxsc.TextField = null
  private var targetHost: TextField = _

  @jfxf.FXML
  private var fxmlFormat: jfxsc.ComboBox[String] = null
  private var format: ComboBox[String] = _

  @jfxf.FXML
  private var fxmlTableConnections: jfxsc.TableView[ConnectionMonitor] = null
  private var tableConnections: TableView[ConnectionMonitor] = _

  @jfxf.FXML
  private var fxmlColId: jfxsc.TableColumn[ConnectionMonitor, String] = null
  private var columnId: TableColumn[ConnectionMonitor, String] = _

  @jfxf.FXML
  private var fxmlColTime: jfxsc.TableColumn[ConnectionMonitor, String] = null
  private var columnTime: TableColumn[ConnectionMonitor, String] = _

  @jfxf.FXML
  private var fxmlColActivity: jfxsc.TableColumn[ConnectionMonitor, String] = null
  private var columnActivity: TableColumn[ConnectionMonitor, String] = _

  @jfxf.FXML
  private var fxmlColRemoteHost: jfxsc.TableColumn[ConnectionMonitor, String] = null
  private var columnRemoteHost: TableColumn[ConnectionMonitor, String] = _

  @jfxf.FXML
  private var fxmlColInfo: jfxsc.TableColumn[ConnectionMonitor, String] = null
  private var columnInfo: TableColumn[ConnectionMonitor, String] = _

  @jfxf.FXML
  private var fxmlRequest: jfxsc.TextArea = null
  private var textRequest: TextArea = _

  @jfxf.FXML
  private var fxmlResponse: jfxsc.TextArea = null
  private var textResponse: TextArea = _

  private val connectionList = new ObservableBuffer[ConnectionMonitor]()

  private val connectionListener = new ConnectListener(this.connectionList, srcPort, host, trgPort)

  private var view: Parent = null

  private var currentMonitor: ConnectionMonitor = null;

  val fxmlLoader = new FXMLLoader(getClass.getResource("TapView.fxml"));
  fxmlLoader.setController(this);
  try {
    view = fxmlLoader.load().asInstanceOf[Parent]
  } catch {
    case ex: IOException => throw new RuntimeException(ex);
  }

  def getView = view

  def close() {
	  this.onRemoveAll(null)
	  this.connectionListener.abortListening()
  }
  
  @jfxf.FXML
  def onRemoveSelected(event: jfxe.ActionEvent) {
    if (this.currentMonitor != null){
      this.currentMonitor.abort()
    }
    
    this.connectionList -= this.currentMonitor
    this.currentMonitor = null
  }

  @jfxf.FXML
  def onRemoveAll(event: jfxe.ActionEvent) {
	  this.connectionList.foreach(mon => mon.abort())
	  this.connectionList.clear()
  }

  def initialize(url: URL, rb: util.ResourceBundle) {
    this.listeningPort = new TextField(this.fxmlListeningPort)
    this.targetHost = new TextField(this.fxmlTargetHost)
    this.format = new ComboBox(this.fxmlFormat)
    this.tableConnections = new TableView(this.fxmlTableConnections)
    this.columnId = new TableColumn(this.fxmlColId)
    this.columnTime = new TableColumn(this.fxmlColTime)
    this.columnActivity = new TableColumn(this.fxmlColActivity)
    this.columnRemoteHost = new TableColumn(this.fxmlColRemoteHost)
    this.columnInfo = new TableColumn(this.fxmlColInfo)
    this.textRequest = new TextArea(this.fxmlRequest)
    this.textResponse = new TextArea(this.fxmlResponse)

    jfxsc.SplitPane.setResizableWithParent(this.fxmlLeftPane, false)
    
    this.listeningPort.text = srcPort.toString()
    this.targetHost.text = host + ":" + trgPort.toString()

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
      this.currentMonitor = newitem;
      this.showRecord()
    })

    this.columnId.cellValueFactory = { _.value.connectionRecord.id }
    this.columnTime.cellValueFactory = { _.value.connectionRecord.time }
    this.columnActivity.cellValueFactory = { _.value.connectionRecord.activity }
    this.columnRemoteHost.cellValueFactory = { _.value.connectionRecord.remoteHost }
    this.columnInfo.cellValueFactory = { _.value.connectionRecord.info }

    val textStyle = "-fx-font-family: 'Consolas', 'Envy Code R', 'monospace';"
    this.textRequest.style = textStyle
    this.textResponse.style = textStyle

    new Thread(this.connectionListener).start();
  }

  def showRecord() {
    if (this.currentMonitor == null) {
      this.textRequest.text = ""
      this.textResponse.text = ""
    } else {
      val fmt = this.format.selectionModel().selectedItem()
      this.textRequest.text = this.currentMonitor.connectionRecord.client2Target.getFormatedString(fmt)
      this.textResponse.text = this.currentMonitor.connectionRecord.target2Client.getFormatedString(fmt)
    }
  }
}