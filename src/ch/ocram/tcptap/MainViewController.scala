package ch.ocram.tcptap

import java.io.IOException
import java.net.URL

import java.util
import javafx.{fxml => jfxf}
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.{control => jfxsc}
import scalafx.scene.control.Tab
import scalafx.scene.control.Tab.sfxTab2jfx
import scalafx.scene.control.TabPane

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