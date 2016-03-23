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
              track.vizualizari)))

        }
      }
    }
  }
  def getTracksRoute=tracks


}
