package dataModel


 import slick.driver.MySQLDriver.api._

/**
  * Created by katakonst on 3/10/16.
  */

 case class HashTagToTrackDb(trackId:Int, hashId:Int)

 class HashTagsToTracks(tag: Tag) extends Table[HashTagToTrackDb](tag, "HashTagtoTracks") {

  def trackId = column[Int]("tracksId")
  def hashId = column[Int]("hashId")

  def * = (trackId, hashId)<>(HashTagToTrackDb.tupled,HashTagToTrackDb.unapply)
}




