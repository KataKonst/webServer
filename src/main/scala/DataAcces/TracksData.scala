package DataAcces

import dataModel.dataModel.FollowersToUsers
import dataModel.{TrackDb, Tracks, Users}
import slick.driver.MySQLDriver.api._
import scala.concurrent.Future



object  TracksData {
  val tracks = TableQuery[Tracks]
  var followers=TableQuery[FollowersToUsers]

  def addTrack(nume:String,photo:String,uploaderId:Int,tracklink:String):Future[Int]=DatabaseConn.getConnection.run(
    (tracks returning tracks.map(_.id)) +=TrackDb(1, nume, tracklink,photo,0,uploaderId))

  def getTracks:Future[Seq[TrackDb]]=DatabaseConn.getConnection.run(tracks.result)

  def getVizs(id:Int):Future[Option[Int]]=DatabaseConn.getConnection.run(tracks.filter( lTrack=>lTrack.id===id)
    .map((lTrack)=>lTrack.Vizualizari)
    .result.headOption)

  def addVis(id:Int,viz:Int):Future[Int]=DatabaseConn.getConnection.run(tracks.filter(lTrack=>lTrack.id===id).map(x=>x.Vizualizari).update(viz+1))

  def addPhoto(nume:String,photo:String):Future[Int]=DatabaseConn.getConnection.run(tracks.filter
  (lTrack=>lTrack.name===nume).
    map(lTrack=>lTrack.photo).update(photo))

  def getTracksUploadedByUser(userId:Int):Future[Seq[TrackDb]]=DatabaseConn.getConnection.run(
    tracks.filter(lTrack=>lTrack.UploaderId===userId).result
  )

  def deleteTrack(trackId:Int):Future[Int]= {
    import scala.concurrent.ExecutionContext.Implicits.global
    val deleteTracks = for {
      res1 <- LikeData.deleteTrack(trackId)
      res2 <- PlayListData.deleteTrack(trackId)
      res3 <- HashTagData.deleteTrack(trackId)
      res4 <- CommentsData.deleteTracks(trackId)

    } yield res1+res2+res3+res4

    deleteTracks.flatMap {
      rez => {
        DatabaseConn.getConnection.run(tracks.filter(lTrack => lTrack.id === trackId).delete)
      }
    }
  }
  def getFollowingUsersTracks(userId:Int):Future[Seq[TrackDb]]={
    val query = for {
      (track, userFollowing) <-  followers.filter((fol)=>fol.user_id===userId) join tracks on (
        (lFollow,lTrack)=>lTrack.UploaderId === lFollow.follow_user)

    }
      yield userFollowing
    DatabaseConn.getConnection.run(query.result)
  }

  def getTrackById(trackId:Int):Future[Seq[TrackDb]]=DatabaseConn.getConnection.run(tracks.filter(lTrack => lTrack.id === trackId).result)
  def getTrackByLink(trackLink:String):Future[Seq[TrackDb]]=DatabaseConn.getConnection.run(tracks.filter(lTrack => lTrack.link === trackLink).result)


}

/**
  * Created by katakonst on 3/5/16.
  */
class TracksData {
}
