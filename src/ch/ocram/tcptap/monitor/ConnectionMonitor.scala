package ch.ocram.tcptap.monitor

import java.net.ServerSocket
import java.net.Socket
import ch.ocram.tcptap.model.ConnectionRecord

class ConnectionMonitor(val sourceSocket: Socket, val targetSocket: Socket, val index: Int, val connectionRecord: ConnectionRecord)
  extends Runnable {

  def run() {
    val th1 = new Thread(new Pipe(sourceSocket.getInputStream(), targetSocket.getOutputStream(), connectionRecord.client2Target));
    th1.start();

    val th2 = new Thread(new Pipe(targetSocket.getInputStream(), sourceSocket.getOutputStream(), connectionRecord.target2Client));
    th2.start();

    th1.join();
    th2.join();

    sourceSocket.close();
    targetSocket.close();
  }
}