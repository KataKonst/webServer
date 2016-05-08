package routes

import DataAcces.{HashTagData, TracksData}
import JsonModels.{HashTag, HashTagJson, Track, TrackJson}
import spray.http.MediaTypes
import spray.routing._

/**
  * Created by katakonst on 3/10/16.
  */

trait HashTagRoutes extends HttpService  {

  implicit def executionContext = actorRefFactory.dispatcher

  val hashTracks: Route = path("getTracksOfHash") {
    import spray.httpx.SprayJsonSupport._

    get {
      respondWithMediaType(MediaTypes.`text/plain`) {
        import TrackJson._
        parameters('hashId) { id =>

          onSuccess(HashTagData.getTracksOnHashTag(Integer.parseInt(id))){
            case (name) =>
              complete(name.map(track => Track(track.id,
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
  val hashOfTracks:Route=path("getHashOfTrack")
  {
    import spray.httpx.SprayJsonSupport._
    get {
      respondWithMediaType(MediaTypes.`text/plain`) {
        import HashTagJson._
        parameters('trackId) { trackId =>

          onSuccess(HashTagData.getHashTagOnTracks(Integer.parseInt(trackId))){
            case (hashs) =>
              complete(hashs.map(hash =>HashTag(hash.id,hash.description)))

          }

        }
      }
    }


  }
  val getAllHashs:Route=path("getAllHashs") {
    get {
      import spray.httpx.SprayJsonSupport._

      respondWithMediaType(MediaTypes.`text/plain`) {
        import HashTagJson._


        onSuccess(HashTagData.getHashTags) {
          case (hashs) =>
            complete(hashs.map(hash => HashTag(hash.id, hash.description)))

           }
        }
      }
    }


  def getHashRoutes=hashOfTracks~hashTracks~getAllHashs

}
