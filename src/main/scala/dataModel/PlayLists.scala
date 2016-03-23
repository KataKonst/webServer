package dataModel

/**
  * Created by katakonst on 3/10/16.
  */
import slick.driver.MySQLDriver.api._
case class PlayListDb(id:Int, authorid:Int, date:String, nume:String)

class PlayLists(tag: Tag) extends Table[PlayListDb](tag, "PlayList") {

  def id = column[Int]("id",O.PrimaryKey,O.AutoInc)
  def authorid = column[Int]("author_id")
  def date = column[String]("date")
  def nume = column[String]("nume")

  def * = (id,authorid, date,nume)<>(PlayListDb.tupled,PlayListDb.unapply)
}



