package dataModel

import slick.driver.MySQLDriver.api._

/**
  * Created by katakonst on 3/3/16.
  */
case class TrackDb(id:Int, name:String, link:String, photo:String, vizualizari:Int, UploaderId:Int)

class Tracks(tag: Tag) extends Table[TrackDb](tag, "Tracks") {
  def id = column[Int]("id",O.PrimaryKey,O.AutoInc)
  def name = column[String]("nume")
  def link = column[String]("link")
  def photo = column[String]("Photo")
  def Vizualizari = column[Int]("Vizualizari")
  def UploaderId = column[Int]("uploader_id")

  def * = (id,name, link,photo,Vizualizari,UploaderId)<>(TrackDb.tupled,TrackDb.unapply)
}
