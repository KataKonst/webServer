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

          onSuccess(HashTagData.getDb().getTracksOnHashTag(Integer.parseInt(id))){
            case (name) =>
              complete(name.map(x => Track(x._1, x._2, x._3, x._4, x._5)))

          }

      }
    }
   }
  }
  def getHashRoutes=hashRoutes;

}
