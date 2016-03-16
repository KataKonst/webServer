package dataModel

import slick.driver.MySQLDriver.api._

/**
  * Created by katakonst on 3/10/16.
  */
class CommentsToUsers(tag: Tag) extends Table[(Int,Int)](tag, "Comments")  {
  def trackid = column[Int]("trackid")
  def commid = column[Int]("commid")

  def * = (trackid,commid);
}
