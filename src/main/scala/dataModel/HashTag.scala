package dataModel

import slick.driver.MySQLDriver.api._

/**
  * Created by katakonst on 3/9/16.
  */
class HashTag(tag: Tag) extends Table[(Int, String)](tag, "HashTag") {
  def id = column[Int]("id",O.PrimaryKey,O.AutoInc)
  def description = column[String]("description")
  def * = (id,description);

}
