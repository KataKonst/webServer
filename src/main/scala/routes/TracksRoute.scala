package routes

import java.io.PrintStream
import java.net.{InetAddress, Socket}

import DataAcces.TracksData
import JsonModels.{Track, TrackJson}
import akka.dispatch.Futures
import com.rockymadden.stringmetric.similarity.LevenshteinMetric
import matcher.Constants
import spray.http.MediaTypes
import spray.routing._

import scala.concurrent.Future
import scala.io.BufferedSource

/**
  * Created by katakonst on 3/7/16.
  */
trait TracksRoute extends HttpService {

  implicit def executionContext = actorRefFactory.dispatcher
  val tracks: Route = path("tracks")
  {
    import spray.httpx.SprayJsonSupport._

    get {
      respondWithMediaType(MediaTypes.`text/plain`) {
        import TrackJson._
        onSuccess( TracksData.getTracks) {
          case (tracksList) =>
            complete(tracksList.map(track => Track(track.id,
              track.name,
              track.link,
              track.photo,
              track.vizualizari,
              track.UploaderId)))
        }
      }
    }
  }

  val userUploadedTracks: Route = path("userUploadedTracks") {
    import spray.httpx.SprayJsonSupport._

    get {
      parameters('userId) { userId => {

        respondWithMediaType(MediaTypes.`text/plain`) {
          import TrackJson._
          onSuccess(TracksData.getTracksUploadedByUser(Integer.parseInt(userId))) {
            case (tracksList) =>
              complete(tracksList.map(track => Track(track.id,
                track.name,
                track.link,
                track.photo,
                track.vizualizari,
                track.UploaderId)))
          }
        }
      }
      }
    }
  }

  val deleteTrack:Route=path("deleteTrack") {
    get {
      parameters('trackId) { trackId => {
        respondWithMediaType(MediaTypes.`text/plain`) {
          onSuccess(TracksData.deleteTrack(Integer.parseInt(trackId))) {
            case _ => complete("succes")
          }

        }

      }

      }


    }
  }

  val FollowingUsersTracks:Route=path("getUserFollowingTracks")
  {
    import spray.httpx.SprayJsonSupport._

    parameters('userId){userId=>{
      respondWithMediaType(MediaTypes.`text/plain`) {
        import TrackJson._

        onSuccess(TracksData.getFollowingUsersTracks(Integer.parseInt(userId))) {
          case tracksList => complete(tracksList.map(track =>  Track(track.id,
            track.name,
            track.link,
            track.photo,
            track.vizualizari,
            track.UploaderId)))
        }

      }

    }

    }

  }


  val matchTracks:Route=path("matchTrack")
  {
    import spray.httpx.SprayJsonSupport._

    parameters('trackId){trackID=>{
      respondWithMediaType(MediaTypes.`text/plain`) {
        import TrackJson._

        val s = new Socket(InetAddress.getByName("localhost"), 50006)
        val out = new PrintStream(s.getOutputStream)
        val in = s.getInputStream

        onSuccess(TracksData.getTrackById(Integer.parseInt(trackID))) {

          case track =>
          out.println(Constants.musicPath+track.head.link)
          out.flush()
          val o = new Array[Byte](1000)
          val legth = in.read(o)
          new String(o, 0, legth).split("\\|").map(x => x.replace(Constants.musicPath, ""))

          val trackList = new String(o, 0, legth).split("\\|").
            filter(x => x.trim != "").
            map(x => TracksData.getTrackByLink(x.replace(Constants.musicPath, "")))


            onSuccess(Future.sequence(trackList.toList)) {

            case trackss =>
              complete(trackss.map(track => Track(track.head.id,
                track.head.name,
                track.head.link,
                track.head.photo,
                track.head.vizualizari,
                track.head.UploaderId)))

          }
        }
      }
    }
    }

  }





  def getFollowingUserTracksRoute=FollowingUsersTracks
  def getTracksRoute=tracks
  def getUserUploadedTracksRoute=userUploadedTracks
  def getDeleteTrackRoute=deleteTrack



  }
