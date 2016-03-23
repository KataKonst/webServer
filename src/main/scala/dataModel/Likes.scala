package dataModel

import slick.driver.MySQLDriver.api._

case class LikeDb(like_id:Int, user_id:Int, track_id:Int)

class Likes(tag: Tag) extends Table[LikeDb](tag, "LikeToTrack") {

  def like_id = column[Int]("like_id",O.PrimaryKey,O.AutoInc)
  def user_id = column[Int]("user_id")
  def track_id = column[Int]("track_id")
  def * = (like_id,user_id, track_id )<>(LikeDb.tupled,LikeDb.unapply)

}

