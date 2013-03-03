package ch.ocram.tcptap

import java.net.URL
import java.util
import javafx.scene.{ layout => jfxsl }
import javafx.{ event => jfxe }
import javafx.{ fxml => jfxf }
import scalafx.scene.layout.GridPane
import javafx.scene.{ control => jfxsc }
import scalafx.scene.control.TextField
import scalafx.scene.control.Tab
import scalafx.scene.control.TabPane
import java.io.IOException
import javafx.fxml.FXMLLoader
import javafx.scene.Parent

class MainViewController extends jfxf.Initializable {

  @jfxf.FXML
  private var tabMonitor: jfxsc.Tab = null
  private var monitorTab: Tab = _

  @jfxf.FXML
  private var tabsMain: jfxsc.TabPane = null
  private var tabs: TabPane = _

  private var view : Parent = null
  
  val fxmlLoader = new FXMLLoader(getClass.getResource("MainView.fxml"));
  fxmlLoader.setController(this);
  try {
    view = fxmlLoader.load().asInstanceOf[Parent]
  } catch {
    case ex: IOException => throw new RuntimeException(ex);
  }

  def getView = view
  
  def initialize(url: URL, rb: util.ResourceBundle) {
    monitorTab = new Tab(tabMonitor)
    tabs = new TabPane(tabsMain)

    val monitorConfigViewController = new MonitorConfigViewController()
    monitorTab.setContent(monitorConfigViewController.getView)
  }
}