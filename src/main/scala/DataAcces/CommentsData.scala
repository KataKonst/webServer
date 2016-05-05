package DataAcces

/**
  * Created by katakonst on 3/10/16.
  */

import java.sql.Date

import dataModel._
import slick.driver.MySQLDriver.api._

import scala.concurrent.Future

object CommentsData
{

  val comments=TableQuery[Comments]
  val tracks=TableQuery[Tracks]
  val users=TableQuery[Users]

  val tracksToComments=TableQuery[CommentsToUsers]

  def addComment(userid:Int,date:String,trackid:Int,text:String):Future[Unit]=DatabaseConn.getConnection.run(DBIO.seq(comments+=CommentDb(0,userid,trackid,date,text)))
  def getCommentsOfTracks(id: Int): Future[Seq[(CommentDb,String,String)]] = {

    val query = for {
      (comments, users) <- comments.filter(
        lComment=>lComment.trackid===id).join(users).on(
        (lComment,lUser)=>lComment.author_id===lUser.id)

    }
      yield (comments,users.username,users.photoLink)

    DatabaseConn.getConnection.run(query.result)
  }
  def deleteComment(id:Int):Future[Int]=DatabaseConn.getConnection.run(comments.filter((pComments)=>pComments.id===id).delete)
  def deleteTracks(trackId:Int):Future[Int]=DatabaseConn.getConnection.run(comments.filter(pComments=>pComments.trackid===trackId).delete)



}

class CommentsData {
}
