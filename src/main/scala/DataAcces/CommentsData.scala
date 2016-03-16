package DataAcces

/**
  * Created by katakonst on 3/10/16.
  */

import java.sql.Date

import dataModel.{CommentsToUsers, Tracks, Comments}
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
  val tracksToComments=TableQuery[CommentsToUsers]
  val db= Database.forURL("jdbc:mysql://localhost:3306/test", driver="com.mysql.jdbc.Driver", user="root", password="");

  def addComment(userid:Int,date:String,trackid:Int,text:String):Future[Unit]=db.run(DBIO.seq(comments+=(0,userid,trackid,date,text)))
  def getCommentsOfTracks(id: Int): Future[Seq[(Int,Int,Int,String, String)]] = db.run(comments.filter(_.trackid===id).result)

}
