package ch.ocram.tcptap.monitor

import java.net.ServerSocket
import java.net.Socket
import scalafx.collections.ObservableBuffer
import ch.ocram.tcptap.model.ConnectionRecord
import java.net.SocketException
import scalafx.application.Platform

class ConnectListener(val model: ObservableBuffer[ConnectionMonitor], val listenPort: Int, val targetHost: String, val targetPort: Int) extends Runnable {

  private var abort = false;

  private var serverSocket: ServerSocket = null

  def abortListening() {
    this.abort = true
    this.serverSocket.close()
  }

  def run() {
    this.serverSocket = new ServerSocket(listenPort);

    var count = 0;
    while (!abort) {

      try {
        val sourceSocket = serverSocket.accept();
        val connectionRecord = new ConnectionRecord(count, sourceSocket.getRemoteSocketAddress().toString())
        val targetSocket = new Socket(targetHost, targetPort);
        val monitor = new ConnectionMonitor(sourceSocket, targetSocket, count, connectionRecord)

        Platform.runLater(new Runnable {
          def run() {
            model += monitor
          }
        })

        new Thread(monitor).start();
      } catch {
        case sex: SocketException => {
          // nothing yet
        }
      }
      count += 1

    }

    serverSocket.close();
  }
}