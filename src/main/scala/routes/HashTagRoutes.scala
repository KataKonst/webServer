package routes

import DataAcces.{HashTagData, TracksData}
import JsonModels.{Track, TrackJson}
import spray.http.MediaTypes
import spray.routing._

/**
  * Created by katakonst on 3/10/16.
  */

trait HashTagRoutes extends HttpService  {

  implicit def executionContext = actorRefFactory.dispatcher

  val hashRoutes: Route = path("getTracksHash") {
    import spray.httpx.SprayJsonSupport._

    get {
      respondWithMediaType(MediaTypes.`text/plain`) {
        import TrackJson._
        parameters('id) { id =>

          onSuccess(HashTagData.getDb.getTracksOnHashTag(Integer.parseInt(id))){
            case (name) =>
              complete(name.map(track => Track(track.id,
                track.name,
                track.link,
                track.photo,
                track.vizualizari)))

          }

      }
    }
   }
  }
  def getHashRoutes=hashRoutes

}
