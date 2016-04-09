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

  val db=new CommentsData()
  def getDb:CommentsData=db

}

class CommentsData {

  val comments=TableQuery[Comments]
  val tracks=TableQuery[Tracks]
  val users=TableQuery[Users]

  val tracksToComments=TableQuery[CommentsToUsers]
  val db= Database.forURL("jdbc:mysql://localhost:3306/test", driver="com.mysql.jdbc.Driver", user="root", password="")

  def addComment(userid:Int,date:String,trackid:Int,text:String):Future[Unit]=db.run(DBIO.seq(comments+=CommentDb(0,userid,trackid,date,text)))
  def getCommentsOfTracks(id: Int): Future[Seq[(CommentDb,String,String)]] = {

    val query = for {
      (comments, users) <- comments.filter(
        lComment=>lComment.trackid===id).join(users).on(
        (lComment,lUser)=>lComment.author_id===lUser.id)

    }
      yield (comments,users.username,users.photoLink)

    db.run(query.result)
  }
  def deleteComment(id:Int):Future[Int]=db.run(comments.filter((pComments)=>pComments.id===id).delete)
  def deleteTracks(trackId:Int):Future[Int]=db.run(comments.filter(pComments=>pComments.trackid===trackId).delete)

}
