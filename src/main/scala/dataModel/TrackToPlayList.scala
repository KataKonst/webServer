package dataModel


/**
  * Created by katakonst on 3/10/16.
  */
import slick.driver.MySQLDriver.api._


class TrackToPlayList(tag: Tag) extends Table[(Int, Int,Int)](tag, "tracksToPlayList") {

  def id = column[Int]("id",O.PrimaryKey,O.AutoInc)
  def trackid = column[Int]("track_id")
  def playid = column[Int]("play_id")
  def * = (id,trackid, playid)
}
