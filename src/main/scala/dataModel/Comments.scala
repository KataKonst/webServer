package dataModel

import java.sql.Date

import slick.driver.MySQLDriver.api._


case class CommentDb(id: Int, author_id:Int, trackid:Int, date:String, text:String)

class Comments(tag: Tag) extends Table[CommentDb](tag, "Comments") {

  def id = column[Int]("id",O.PrimaryKey,O.AutoInc)
  def author_id =  column[Int]("author_id")
  def trackid=column[Int]("trackid")

  def date=column[String]("date")

  def text=column[String]("Text")

  def * = (id,author_id,trackid,date,text)<>(CommentDb.tupled, CommentDb.unapply)



}
