package ch.ocram.tcptap.monitor

import java.io.OutputStream
import java.io.InputStream
import ch.ocram.tcptap.model.Data

class Pipe(val inputStream: InputStream, val outputStream: OutputStream, val data : Data) extends Runnable {

  def run() {
    val buffer = new Array[Byte](1024);

    var bytes = 0;
    do {
      bytes = inputStream.read(buffer);
      if (bytes > 0) {
        outputStream.write(buffer, 0, bytes);
        data.add(buffer, bytes)
      }
    } while (bytes > 0);
  }
}