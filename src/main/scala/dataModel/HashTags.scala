package dataModel

import slick.driver.MySQLDriver.api._

/**
  * Created by katakonst on 3/9/16.
  */
case class HashTagDb(id:Int, description:String)

class HashTags(tag: Tag) extends Table[HashTagDb](tag, "HashTags") {
  def id = column[Int]("id",O.PrimaryKey,O.AutoInc)
  def description = column[String]("description")
  def * = (id,description)<>(HashTagDb.tupled,HashTagDb.unapply)

}
