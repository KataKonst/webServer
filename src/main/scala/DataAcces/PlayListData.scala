package DataAcces

/**
  * Created by katakonst on 3/10/16.
  */

import dataModel._
import slick.driver.MySQLDriver.api._
import scala.concurrent.Future



object PlayListData {
  val playList=TableQuery[PlayLists]
  val trackPlaylist=TableQuery[TrackToPlayLists]
  val tracks=TableQuery[Tracks]
  def createPlayList(userId:Int,date:String,nume:String):Future[Unit]=DatabaseConn.getConnection.run(DBIO.seq(playList +=PlayListDb(0, userId,date,nume)))
  def addToPlayList(playId:Int,trackId:Int):Future[Unit]=DatabaseConn.getConnection.run(DBIO.seq(trackPlaylist +=TrackToPlayListDb(0, trackId,playId)))
  def getTrackPlayList(playId:Int): Future[Seq[TrackDb]] = {

    val query = for {
      (track, tracktoplaylist) <- tracks join trackPlaylist on (
        (lTrack,lTrackToPlayList)=>lTrack.id === lTrackToPlayList.trackid)
      if tracktoplaylist.playid === playId
    }
      yield track

    DatabaseConn.getConnection.run(query.result)
  }
  def getPlaylsts=DatabaseConn.getConnection.run(playList.result)


  def getUserPlaylists(userId:Int):Future[Seq[PlayListDb]]=DatabaseConn.getConnection.run(playList.filter(playlist=>playlist.authorid===userId).result)
  def deletePlayList(playId:Int)=DatabaseConn.getConnection.run(playList.filter((playList)=>playList.id===playId).delete)
  def deleteFromTrackToPlayLis(playId:Int):Future[Int]=DatabaseConn.getConnection.run(trackPlaylist.filter(ptrackPlaylist=>ptrackPlaylist.playid===playId).delete)

  def checkTrackPlayList(trackid:Int,playId:Int):Future[Int]=DatabaseConn.getConnection.run(trackPlaylist.filter(
    (pTrackPlayList)=>pTrackPlayList.playid===playId&&pTrackPlayList.trackid===trackid)
    .length
    .result)

  def deleteTrackFromPlayList(trackId:Int,playId:Int):Future[Int]=DatabaseConn.getConnection.run(trackPlaylist.filter(
    (pTrackPlayList)=>pTrackPlayList.playid===playId&&pTrackPlayList.trackid===trackId).delete
  )

  def deleteTrack(trackId:Int):Future[Int]=DatabaseConn.getConnection.run(trackPlaylist.filter((ptrp)=>ptrp.trackid===trackId).delete)


}


class PlayListData {


}
