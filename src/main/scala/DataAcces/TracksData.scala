package DataAcces
import dataModel.{Tracks, Users}
import slick.driver.MySQLDriver.api._
import scala.concurrent.Future



object  TracksData {
  def getDb():TracksData=new TracksData();;

}

/**
  * Created by katakonst on 3/5/16.
  */
class TracksData {
  val db= Database.forURL("jdbc:mysql://localhost:3306/test", driver="com.mysql.jdbc.Driver", user="root", password="")
  val tracks = TableQuery[Tracks]

  def addTrack(nume:String,photo:String,uploaderId:Int):Future[Unit]=db.run(DBIO.seq(tracks +=(1, nume, nume,photo,0,uploaderId)))

  def getTracks:Future[Seq[(Int,String,String,String,Int,Int)]]=db.run(tracks.result)

  def getVizs(id:Int):Future[Option[Int]]=db.run(tracks.filter(_.id===id).map(_.Vizualizari).result.headOption)

  def addVis(id:Int,viz:Int):Future[Int]=db.run(tracks.filter(_.id===id).map(x=>x.Vizualizari).update(viz+1))

  def addPhoto(nume:String,photo:String):Future[Int]=db.run(tracks.filter(_.name===nume).map(x=>x.photo).update(photo))
}
