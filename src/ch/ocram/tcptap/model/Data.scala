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

  def getFormatedString(fmt: String) = {
    this.synchronized {
      fmt match {
        case "String" => new String(buffer, 0, currentIndex)
        case "XML" => this.getXmlString()
        case "HEX" => this.getHexString()
        case _ => "unknown format"
      }
    }
  }

  private def getXmlString() = {
    val str = new StringBuilder()

    var before: Char = ' '
    this.buffer.foreach({ b =>
      val ch = b.asInstanceOf[Char]

      if (ch == '<' && before == '>') {
        str.append('\n')
      }
      str.append(ch)
      before = ch
    })

    str.toString()
  }

  private def getHexString() = {
    val hexStr = new StringBuilder()
    val txtStr = new StringBuilder()

    var idx = 0
    this.buffer.foreach({ b =>

      if (b < 32) {
        txtStr.append('.')
      } else {
        txtStr.append(b.asInstanceOf[Char])
      }
      hexStr.append("%02X" format b).append(" ")

      idx += 1
      if (idx % 16 == 0) {
        hexStr.append("  ").append(txtStr.toString()).append('\n')
        txtStr.clear()
      }

    })

    hexStr.toString()
  }
}