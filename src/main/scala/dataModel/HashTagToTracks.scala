package dataModel


import slick.driver.MySQLDriver.api._

/**
  * Created by katakonst on 3/10/16.
  */


 class HashTagToTrack (tag: Tag) extends Table[(Int, Int)](tag, "HashTagtoTracks") {

  def trackId = column[Int]("tracksId")
  def hashId = column[Int]("hashId")

  def * = (trackId, hashId)
}




