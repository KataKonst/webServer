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

  def addHashTags(description:String):Future[Int]=db.run((hash returning hash.map(_.id))+=HashTagDb(0,description))
  def addHashTagtoTrack(trackId:Int,HashTagId:Int):Future[Unit]=db.run(DBIO.seq(hashToTracks+=HashTagToTrackDb(trackId,HashTagId)))
  def getHashTagId(description:String):Future[Option[Int]]=db.run(hash.filter(lHash=>
    lHash.description===description).
    map(x=>x.id)
    .result.headOption)
  def getTracksOnHashTag(id: Int): Future[Seq[TrackDb]] = {

    val query = for {
      (tracks, hash) <- tracks join hashToTracks on ((lTrack,lHash)=>lTrack.id === lHash.trackId)
        if hash.hashId === id
      }
      yield tracks

    db.run(query.result)
  }
  def getHashTagOnTracks(trackId:Int):Future[Seq[HashTagDb]]={

    val query = for {
      (hashs, hashsToTracks) <- hash join hashToTracks on ((hashs,hashsToTracks)=>hashs.id === hashsToTracks.hashId)
      if hashsToTracks.trackId === trackId
    }
      yield hashs

    db.run(query.result)

  }

  def getHashTags:Future[Seq[HashTagDb]]=db.run(hash.result)

  def deleteTrack(trackId:Int):Future[Int]=db.run(hashToTracks.filter((pHash)=>pHash.trackId===trackId).delete)



}


