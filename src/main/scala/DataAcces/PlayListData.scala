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
      (track, tracktoplaylist) <- tracks join trackPlaylist on (
                (lTrack,lTrackToPlayList)=>lTrack.id === lTrackToPlayList.trackid)
      if tracktoplaylist.playid === playId
    }
      yield track

    db.run(query.result)
  }
  def getPlaylsts=db.run(playList.result)


  def getUserPlaylists(userId:Int):Future[Seq[PlayListDb]]=db.run(playList.filter(playlist=>playlist.authorid===userId).result)
  def deletePlayList(playId:Int)=db.run(playList.filter((playList)=>playList.id===playId).delete)
  def deleteFromTrackToPlayLis(playId:Int):Future[Int]=db.run(trackPlaylist.filter(ptrackPlaylist=>ptrackPlaylist.playid===playId).delete)

  def checkTrackPlayList(trackid:Int,playId:Int):Future[Int]=db.run(trackPlaylist.filter(
    (pTrackPlayList)=>pTrackPlayList.playid===playId&&pTrackPlayList.trackid===trackid)
    .length
    .result)

  def deleteTrackFromPlayList(trackId:Int,playId:Int):Future[Int]=db.run(trackPlaylist.filter(
    (pTrackPlayList)=>pTrackPlayList.playid===playId&&pTrackPlayList.trackid===trackId).delete
  )

}
