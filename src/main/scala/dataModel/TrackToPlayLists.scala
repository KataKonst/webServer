package dataModel


/**
  * Created by katakonst on 3/10/16.
  */
import slick.driver.MySQLDriver.api._

case class TrackToPlayListDb(id:Int, trackId: Int, playId:Int)

class TrackToPlayLists(tag: Tag) extends Table[TrackToPlayListDb](tag, "tracksToPlayList") {

  def id = column[Int]("id",O.PrimaryKey,O.AutoInc)
  def trackid = column[Int]("track_id")
  def playid = column[Int]("play_id")
  def * = (id,trackid, playid)<>(TrackToPlayListDb.tupled,TrackToPlayListDb.unapply)
}
