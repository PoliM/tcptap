package ch.ocram.tcptap.model

object Data {
  def getXmlString(buffer: Array[Byte]) = {
    val str = new StringBuilder()
    val tmpBuf = new StringBuilder()

    var indent = 0
    buffer.foreach({ b =>
      val ch = b.asInstanceOf[Char]

      if ("></?".indexOf(ch) >= 0) {
        tmpBuf.append(ch);
      } else {
        if (tmpBuf.size > 0) {
          val part = tmpBuf.toString()
          part match {
            case "/></" => {
              indent -= 1
              str.append("/>\n").append("  " * indent).append("</")
            }
            case "/><" => {
              indent += 1
              str.append("/>\n").append("  " * indent).append("<")
            }
            case "></" => {
              str.append(">\n").append("  " * indent).append("</")
              indent -= 1
            }
            case "><" => {
              indent += 1
              str.append(">\n").append("  " * indent).append("<")
            }
            case "</" => {
              indent -= 1
              str.append("</")
            }
            case "?></" => {
              str.append("?>\n").append("  " * indent).append("</")
            }
            case "?><" => {
              str.append("?>\n").append("  " * indent).append("<")
            }
            case _ => {
              str.append(part)
            }
          }
          tmpBuf.clear()
        }
        str.append(ch)
        indent = Math.max(0, indent)
      }
    })

    str.append(tmpBuf)
    str.toString()
  }
}

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
      try{
      fmt match {
        case "String" => new String(buffer, 0, currentIndex)
        case "XML" => Data.getXmlString(this.buffer)
        case "HEX" => this.getHexString()
        case _ => "unknown format"
      }
      }
      catch {
        case ex : Exception => ex.getStackTraceString
      }
    }
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