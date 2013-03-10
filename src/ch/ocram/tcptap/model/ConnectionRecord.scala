package ch.ocram.tcptap.model

import scalafx.beans.property.StringProperty
import scalafx.beans.property.IntegerProperty
import java.util.Locale
import java.text.SimpleDateFormat
import java.util.Date

class ConnectionRecord(var id_ : Int, var remoteHost_ : String) {
  val id = new StringProperty(this, "id", String.format(Locale.getDefault(), "%1$04d", new Integer(id_.intValue)))
  val time = new StringProperty(this, "time", currentTime())
  val remoteHost = new StringProperty(this, "remoteHost", remoteHost_)
  val activity = new StringProperty(this, "activity", "active")
  val info = new StringProperty(this, "info", "")

  val client2Target = new Data()
  
  val target2Client = new Data()
  
  def currentTime() ={
    new SimpleDateFormat("yyyy.MM.dd hh:mm:ss,SSS").format(new Date());
  }
}