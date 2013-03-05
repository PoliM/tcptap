package ch.ocram.tcptap.model

import scalafx.beans.property.StringProperty
import scalafx.beans.property.IntegerProperty
import java.util.Locale

class ConnectionRecord(var id_ : Int, var remoteHost_ : String) {
  val id = new StringProperty(this, "id", String.format(Locale.getDefault(), "%1$04d", new Integer(id_.intValue)))
  val remoteHost = new StringProperty(this, "remoteHost", remoteHost_)
  
  val client2Target = new Data()
  
  val target2Client = new Data()
}