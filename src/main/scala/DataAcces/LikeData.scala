package DataAcces

import dataModel._

/**
  * Created by katakonst on 3/16/16.
  */
import slick.driver.MySQLDriver.api._
import scala.concurrent.Future


object LikeData {
  val db=new LikeData()
  def getDb():LikeData=db
}

class LikeData {
  val  like = TableQuery[Like]
  val  tracks = TableQuery[Tracks]
  val  user = TableQuery[Users]
  val  db= Database.forURL("jdbc:mysql://localhost:3306/test", driver="com.mysql.jdbc.Driver", user="root", password="")

  def likeTrack(trackId:Int,userId:Int):Future[Unit]=db.run(DBIO.seq(like +=(0, userId,trackId)))
  def getTrackLikes(trackId:Int): Future[Seq[(Int,Int,Int)]]=db.run(like.filter(_.track_id===trackId).result)
  def getUsersTracksLikes(userId:Int):Future[Seq[(Int,String,String,String,Int,Int)]]={
    val query = for {
      (t, l) <- tracks join like on (_.id===_.track_id )
      if l.user_id === userId
    }
      yield t

    db.run(query.result)
  }

  def getUsersLikedTrack(trackId:Int):Future[Seq[(Int,String,String)]]={
    val query = for {
      (t, l) <- user join like on (_.id===_.user_id )
      if l.track_id === trackId
    }
      yield t

    db.run(query.result)
  }



}



