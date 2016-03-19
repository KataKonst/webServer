package dataModel

import slick.driver.MySQLDriver.api._

/**
  * Created by katakonst on 3/16/16.
  */
class Like(tag: Tag) extends Table[(Int, Int,Int)](tag, "LikeToTrack") {

  def like_id = column[Int]("like_id",O.PrimaryKey,O.AutoInc)
  def user_id = column[Int]("user_id")
  def track_id = column[Int]("track_id")
  def * = (like_id,user_id, track_id )

}

