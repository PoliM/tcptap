package ch.ocram.tcptap.monitor

import java.net.ServerSocket
import java.net.Socket
import ch.ocram.tcptap.model.ConnectionRecord

class ConnectionMonitor(val sourceSocket: Socket, val targetSocket: Socket, val index: Int, val connectionRecord: ConnectionRecord)
  extends Runnable {

  var pipe1: Pipe = null
  var pipe2: Pipe = null

  def run() {
    pipe1 = new Pipe(sourceSocket.getInputStream(), targetSocket.getOutputStream(), connectionRecord.client2Target)
    //    val th1 = new Thread(pipe1);
    //    th1.start();

    pipe2 = new Pipe(targetSocket.getInputStream(), sourceSocket.getOutputStream(), connectionRecord.target2Client)
    val th2 = new Thread(pipe2);
    th2.start();

    pipe1.run();
    //th1.join();
    th2.join();

    this.connectionRecord.activity.set("closed")

    sourceSocket.close();
    targetSocket.close();
  }

  def abort() {
    pipe1.abort()
    pipe2.abort()

    if (!sourceSocket.isClosed()) {
      sourceSocket.close()
    }
    
    if (!targetSocket.isClosed()) {
      targetSocket.close()
    }
  }
}