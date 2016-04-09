package routes

import DataAcces.TracksData
import JsonModels.{Track, TrackJson}
import com.rockymadden.stringmetric.similarity.LevenshteinMetric
import spray.http.MediaTypes
import spray.routing._

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
        onSuccess( TracksData.getDb.getTracks) {
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
          onSuccess(TracksData.getDb.getTracksUploadedByUser(Integer.parseInt(userId))) {
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
          onSuccess(TracksData.getDb.deleteTrack(Integer.parseInt(trackId))) {
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

        onSuccess(TracksData.getDb.getFollowingUsersTracks(Integer.parseInt(userId))) {
          case tracks => complete(tracks.map(track =>  Track(track.id,
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


  def getFollowingUserTracksRoute=FollowingUsersTracks
  def getTracksRoute=tracks
  def getUserUploadedTracksRoute=userUploadedTracks
  def getDeleteTrackRoute=deleteTrack



  }
