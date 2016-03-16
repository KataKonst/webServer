package DataAcces

/**
  * Created by katakonst on 3/10/16.
  */

import dataModel.{Tracks, TrackToPlayList, PlayList}
import slick.driver.MySQLDriver.api._
import scala.concurrent.Future



object PlayListData {
  val db=TableQuery[PlayList];
  def getDb():PlayListData=new PlayListData()
}


class PlayListData {

  val playList=TableQuery[PlayList];
  val trackPlaylist=TableQuery[TrackToPlayList]
  val tracks=TableQuery[Tracks]
  val db= Database.forURL("jdbc:mysql://localhost:3306/test", driver="com.mysql.jdbc.Driver", user="root", password="");
  def createPlayList(userId:Int,date:String,nume:String):Future[Unit]=db.run(DBIO.seq(playList +=(0, userId,date,nume)))
  def addToPlayList(playId:Int,trackId:Int):Future[Unit]=db.run(DBIO.seq(trackPlaylist +=(0, trackId,playId)))
  def getTrackPlayList(playId:Int): Future[Seq[(Int, String,String,String,Int,Int)]] = {

    val query = for {
      (t, h) <- tracks join trackPlaylist on (_.id === _.trackid)
      if h.playid === playId
    }
      yield t

    db.run(query.result)
  }

  def getPlayLists:Future[Seq[(Int,Int,String,String)]]=db.run(playList.result)



}
