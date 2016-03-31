package DataAcces
import dataModel.{TrackDb, Tracks, Users}
import slick.driver.MySQLDriver.api._
import scala.concurrent.Future



object  TracksData {
  def getDb:TracksData=new TracksData()

}

/**
  * Created by katakonst on 3/5/16.
  */
class TracksData {
  val db= Database.forURL("jdbc:mysql://localhost:3306/test", driver="com.mysql.jdbc.Driver", user="root", password="")
  val tracks = TableQuery[Tracks]

  def addTrack(nume:String,photo:String,uploaderId:Int):Future[Int]=db.run(
    (tracks returning tracks.map(_.id)) +=TrackDb(1, nume, nume,photo,0,uploaderId))

  def getTracks:Future[Seq[TrackDb]]=db.run(tracks.result)

  def getVizs(id:Int):Future[Option[Int]]=db.run(tracks.filter( lTrack=>lTrack.id===id)
         .map((lTrack)=>lTrack.Vizualizari)
         .result.headOption)

  def addVis(id:Int,viz:Int):Future[Int]=db.run(tracks.filter(lTrack=>lTrack.id===id).map(x=>x.Vizualizari).update(viz+1))

  def addPhoto(nume:String,photo:String):Future[Int]=db.run(tracks.filter
        (lTrack=>lTrack.name===nume).
    map(lTrack=>lTrack.photo).update(photo))
}
