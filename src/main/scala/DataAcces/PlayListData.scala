package DataAcces

/**
  * Created by katakonst on 3/10/16.
  */

import dataModel._
import slick.driver.MySQLDriver.api._
import scala.concurrent.Future



object PlayListData {
  val db=TableQuery[PlayLists]
  def getDb:PlayListData=new PlayListData()
}


class PlayListData {

  val playList=TableQuery[PlayLists]
  val trackPlaylist=TableQuery[TrackToPlayLists]
  val tracks=TableQuery[Tracks]
  val db= Database.forURL("jdbc:mysql://localhost:3306/test", driver="com.mysql.jdbc.Driver", user="root", password="")
  def createPlayList(userId:Int,date:String,nume:String):Future[Unit]=db.run(DBIO.seq(playList +=PlayListDb(0, userId,date,nume)))
  def addToPlayList(playId:Int,trackId:Int):Future[Unit]=db.run(DBIO.seq(trackPlaylist +=TrackToPlayListDb(0, trackId,playId)))
  def getTrackPlayList(playId:Int): Future[Seq[TrackDb]] = {

    val query = for {
      (track, tracktoplaylist) <- tracks join trackPlaylist on (_.id === _.trackid)
      if tracktoplaylist.playid === playId
    }
      yield track

    db.run(query.result)
  }

  def getPlayLists:Future[Seq[PlayListDb]]=db.run(playList.result)



}
