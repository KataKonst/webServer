package dataModel

import slick.driver.MySQLDriver.api._

/**
  * Created by katakonst on 3/3/16.
  */
class Tracks(tag: Tag) extends Table[(Int, String,String,String,Int,Int)](tag, "Tracks") {
  def id = column[Int]("id",O.PrimaryKey,O.AutoInc)
  def name = column[String]("nume")
  def link = column[String]("link")
  def photo = column[String]("Photo")
  def Vizualizari = column[Int]("Vizualizari")
  def UploaderId = column[Int]("uploader_id")

  def * = (id,name, link,photo,Vizualizari,UploaderId);
}
