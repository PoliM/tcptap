package ch.ocram.tcptap.model

class Data {

  private var buffer = new Array[Byte](1024)
  private var currentIndex = 0;

  def add(bytes: Array[Byte], length: Int) {
    this.synchronized {
      if (currentIndex + length >= buffer.length) {
        val tmpBuf = new Array[Byte](Math.max(currentIndex + length, buffer.length * 2))
        System.arraycopy(buffer, 0, tmpBuf, 0, currentIndex);
        buffer = tmpBuf
      }

      System.arraycopy(bytes, 0, buffer, currentIndex, length);
      currentIndex += length;
    }
  }
  
  def getRawString() = {
    new String(buffer, 0, currentIndex)
  }
}