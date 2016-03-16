package DataAcces

import dataModel.{HashTagToTrack, Tracks, HashTag, Users}

import scala.concurrent.Future

/**
  * Created by katakonst on 3/10/16.
  */

import slick.driver.MySQLDriver.api._

import scala.concurrent.Future
object  HashTagData{
  def getDb():HashTagData=new HashTagData()
}


class HashTagData {
  val hash = TableQuery[HashTag]
  val  tracks = TableQuery[Tracks]
  val  hashToTracks = TableQuery[HashTagToTrack]

  val db= Database.forURL("jdbc:mysql://localhost:3306/test", driver="com.mysql.jdbc.Driver", user="root", password="");

  def addHashTags(description:String):Future[Unit]=db.run(DBIO.seq(hash+=(0,description)))
  def addHashTagtoTrack(description:String):Future[Unit]=db.run(DBIO.seq(hash+=(0,description)))
  def getHashTagId(description:String):Future[Option[Int]]=db.run(hash.filter(_.description===description).map(x=>x.id).result.headOption)
  def getTracksOnHashTag(id: Int): Future[Seq[(Int, String,String,String,Int,Int)]] = {

    val query = for {
      (t, h) <- tracks join hashToTracks on (_.id === _.trackId)
        if h.hashId === id
      }
      yield t

    db.run(query.result)
  }


  }


