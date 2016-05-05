package DataAcces

import dataModel._

/**
  * Created by katakonst on 3/16/16.
  */
import slick.driver.MySQLDriver.api._
import scala.concurrent.Future


object LikeData {
  val  like = TableQuery[Likes]
  val  tracks = TableQuery[Tracks]
  val  user = TableQuery[Users]

  def likeTrack(trackId:Int,userId:Int):Future[Unit]=DatabaseConn.getConnection.run(DBIO.seq(like +=LikeDb(0, userId,trackId)))
  def getTrackLikes(trackId:Int): Future[Seq[LikeDb]]=DatabaseConn.getConnection.run(like.filter((lTrack)=>lTrack.track_id===trackId).result)
  def getUsersTracksLikes(userId:Int):Future[Seq[TrackDb]]={
    val query = for {
      (track, like) <- tracks join like on ((lTrack,lLike)=>lTrack.id===lLike.track_id )
      if like.user_id === userId
    }
      yield track

    DatabaseConn.getConnection.run(query.result)
  }

  def getUsersLikedTrack(trackId:Int):Future[Seq[UserDb]]={
    val query = for {
      (user, like) <- user join like on ((lUser,lLike)=>lUser.id===lLike.user_id )
      if like.track_id === trackId
    }
      yield user

    DatabaseConn.getConnection.run(query.result)
  }

  def getTrackLikesNr(trackId:Int):Future[Int]=DatabaseConn.getConnection.run(like.filter((lLike)=>lLike.track_id===trackId).length.result)

  def checkUserLikedTrack(trackid:Int,userId:Int):Future[Int]=DatabaseConn.getConnection.run(like.filter(
    (pLike)=>pLike.track_id===trackid&&pLike.user_id===userId)
    .length
    .result)

  def unLike(trackid:Int,userId:Int):Future[Int]=DatabaseConn.getConnection.run(like.filter((pLike)=>pLike.track_id===trackid&&pLike.user_id===userId).delete)

  def deleteTrack(trackId:Int):Future[Int]=DatabaseConn.getConnection.run(like.filter((pLike)=>pLike.track_id===trackId).delete)

}

class LikeData {

}



