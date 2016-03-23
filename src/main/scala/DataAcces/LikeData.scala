package DataAcces

import dataModel._

/**
  * Created by katakonst on 3/16/16.
  */
import slick.driver.MySQLDriver.api._
import scala.concurrent.Future


object LikeData {
  val db=new LikeData()
  def getDb:LikeData=db
}

class LikeData {
  val  like = TableQuery[Likes]
  val  tracks = TableQuery[Tracks]
  val  user = TableQuery[Users]
  val  db= Database.forURL("jdbc:mysql://localhost:3306/test", driver="com.mysql.jdbc.Driver", user="root", password="")

  def likeTrack(trackId:Int,userId:Int):Future[Unit]=db.run(DBIO.seq(like +=LikeDb(0, userId,trackId)))
  def getTrackLikes(trackId:Int): Future[Seq[LikeDb]]=db.run(like.filter(_.track_id===trackId).result)
  def getUsersTracksLikes(userId:Int):Future[Seq[TrackDb]]={
    val query = for {
      (track, like) <- tracks join like on (_.id===_.track_id )
      if like.user_id === userId
    }
      yield track

    db.run(query.result)
  }

  def getUsersLikedTrack(trackId:Int):Future[Seq[UserDb]]={
    val query = for {
      (user, like) <- user join like on (_.id===_.user_id )
      if like.track_id === trackId
    }
      yield user

    db.run(query.result)
  }

  def getTrackLikesNr(trackId:Int):Future[Int]=db.run(like.filter(_.track_id===trackId).length.result)



}



