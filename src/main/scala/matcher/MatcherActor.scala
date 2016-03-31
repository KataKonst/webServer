package matcher

import java.util.Collections

import DataAcces.{TracksData, UserData}
import JsonModels.{TrackJson, Track}
import akka.actor.{ Props, Actor }
import com.rockymadden.stringmetric.similarity.LevenshteinMetric
import dataModel.{UserDb, Tracks, Users}
import routes._

import shazamapi.{ResultTrack, Similiarity}
import spray.http._
import spray.routing._
import java.io._
import slick.driver.MySQLDriver.api._

import scala.reflect.io.File

class MatcherActor extends Actor
  with MatchService
  with SaveRoute
  with SearchRoute
  with TracksRoute
  with  AddVisRoute
  with HashTagRoutes
  with CommentsRoute
  with PlayListRoute
  with LikeRoute
  with UserRoute{

  def actorRefFactory = context
  override implicit def executionContext = actorRefFactory.dispatcher


  def receive = runRoute(login~
    register~
    this.getSaveRoute~
    this.getSearchRoute~
    this.getTracksRoute~
    matcher~
    initMAtcher~
    this.getVisRoute~
    this.getTrackVisRoute~
    this.getAddCommentRoute~
    this.getTracksComments~
    this.getPlayLists~
    this.getAddToPlayListRoute~
    this.getCreatePLayListRoute~
    this.getTracksOfPlaylistRoute~
    this.getTrackLikesRoute~
    this.getLikeTrackRoute~
    this.getUserLikedTracksRoute~
    this.getUserTracksLikedRoute~
    this.getTrackLikesNr~
    this.getSearchedUsersRoute~
    this.getUserPlayLists~
    this.getDeletePlayListRoute~
    this.getUnLikeRoute~
    this.getCheckUserLikedTrackRoute~
     this.getDeleteTrackFromPlayListRoute~
    this.getCheckTrackFromPlayListRoute~
    this.getDeleteComRoute~
    this.getHashOfTrackRoute~
    this.getTracksOfHash~
    this.getAllHashTagsRoute~
    this.getAddPhotoToUserRoute~
    this.getUserById)
}

trait MatchService extends HttpService {

  implicit def executionContext = actorRefFactory.dispatcher

  import spray.httpx.SprayJsonSupport._

  val simMatcher=new Similiarity()
  val userDataAcces=new UserData()
  val tracksDataAcces=new TracksData()




  val register: Route = path("register")
{
  get {
    respondWithMediaType(MediaTypes.`text/plain`) {
      val users = TableQuery[Users]
      val db= Database.forURL("jdbc:mysql://localhost:3306/test", driver="com.mysql.jdbc.Driver", user="root", password="")
      parameters('name,'md5) { (name,md5) =>
        val a= DBIO.seq(users +=UserDb(1, name, md5,""))
        onSuccess( db.run(a)) {
          case (test)=>
          complete("succes")
        }
      }
    }
   }

}


  val login:Route=path("login") {
    get {
      respondWithMediaType(MediaTypes.`application/json`) {
        parameters('nume, 'opt.?) { (nume, opt) =>
          import LoginJSon._
          onSuccess(userDataAcces.getMd5Pass(nume)) {
            case (name) =>
              complete(name.map(x => Login(x.id, x.username,x.md5Hash)));
          }
        }
      }
    }
  }





  val initMAtcher:Route=path("initMatcher") {
    get {
      val path="/home/katakonst/licenta/playserver/music/"
      respondWithMediaType(MediaTypes.`text/html`) {
        onSuccess(tracksDataAcces.getTracks) {
          case (name) =>
            name.foreach(x =>
              simMatcher.addTrack(new java.io.File(path + x.name)))
              complete("sada")

        }
      }
    }
  }








  val matcher:Route=path("match")
  {
    parameters('nume){(nume)=>
      val tracks = TableQuery[Tracks]
      val path="/home/katakonst/licenta/playserver/music/"
      val	lsTrack=simMatcher.matchTrack(new java.io.File(path+nume))

      respondWithMediaType(MediaTypes.`application/json`) {
        Collections.sort(lsTrack)
        import TrackJson._
        complete(lsTrack.toArray().map(x=>
              Track(0,
                x.asInstanceOf[ResultTrack].getName,
                x.asInstanceOf[ResultTrack].getName,
                "",0)))

      }
    }

  }

}