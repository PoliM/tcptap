package ch.ocram.tcptap.monitor

import java.net.ServerSocket
import java.net.Socket
import scalafx.collections.ObservableBuffer
import ch.ocram.tcptap.model.ConnectionRecord

class ConnectListener(val model: ObservableBuffer[ConnectionRecord], val listenPort: Int, val targetHost: String, val targetPort: Int) extends Runnable {

  private var abort = false;

  private var serverSocket: ServerSocket = null

  def abortListening() {
    this.abort = true
    this.serverSocket.close()
  }

  def run() {
    this.serverSocket = new ServerSocket(listenPort);

    var count = 0;
    while (!abort) { {
        
        val sourceSocket = serverSocket.accept();

        val connectionRecord = new ConnectionRecord(count, sourceSocket.getRemoteSocketAddress().toString())

        model += connectionRecord
        
        val targetSocket = new Socket(targetHost, targetPort);

        new Thread(new ConnectionMonitor(sourceSocket, targetSocket, count, connectionRecord)).start();

        count += 1
      }
    }

    serverSocket.close();
  }
}