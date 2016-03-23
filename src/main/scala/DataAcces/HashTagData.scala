package DataAcces

import dataModel._

import scala.concurrent.Future

/**
  * Created by katakonst on 3/10/16.
  */

import slick.driver.MySQLDriver.api._

import scala.concurrent.Future
object  HashTagData{
  def getDb:HashTagData=new HashTagData()
}

class HashTagData {
  val hash = TableQuery[HashTags]
  val  tracks = TableQuery[Tracks]
  val  hashToTracks = TableQuery[HashTagsToTracks]

  val db= Database.forURL("jdbc:mysql://localhost:3306/test", driver="com.mysql.jdbc.Driver", user="root", password="")

  def addHashTags(description:String):Future[Unit]=db.run(DBIO.seq(hash+=HashTagDb(0,description)))
  def addHashTagtoTrack(description:String):Future[Unit]=db.run(DBIO.seq(hash+=HashTagDb(0,description)))
  def getHashTagId(description:String):Future[Option[Int]]=db.run(hash.filter(_.description===description).map(x=>x.id).result.headOption)
  def getTracksOnHashTag(id: Int): Future[Seq[TrackDb]] = {

    val query = for {
      (tracks, hash) <- tracks join hashToTracks on (_.id === _.trackId)
        if hash.hashId === id
      }
      yield tracks

    db.run(query.result)
  }


  }


