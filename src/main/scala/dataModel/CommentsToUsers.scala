package dataModel

import slick.driver.MySQLDriver.api._

case class CommentToUser(trackId:Int,commid:Int)

class CommentsToUsers(tag: Tag) extends Table[CommentToUser](tag, "Comments")  {
  def trackid = column[Int]("trackid")
  def commid = column[Int]("commid")

  def * = (trackid,commid)<>(CommentToUser.tupled,CommentToUser.unapply)
}
