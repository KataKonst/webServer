package routes

import DataAcces.TracksData
import JsonModels.{Track, TrackJson}
import com.rockymadden.stringmetric.similarity.LevenshteinMetric
import dataModel.TrackDb
import spray.routing._

/**
  * Created by katakonst on 3/7/16.
  */
trait SearchRoute  extends HttpService {

  implicit def executionContext = actorRefFactory.dispatcher


  val searchRoute:Route=path("search")
  {
    import spray.httpx.SprayJsonSupport._



    parameters('nume){ nume=>
      import TrackJson._
      onSuccess( TracksData.getDb.getTracks) {
        case (tracks) =>
          def comp(e1: (TrackDb), e2: (TrackDb)):Boolean = LevenshteinMetric.compare(nume,e1.name).getOrElse(0) < LevenshteinMetric.compare(nume,e2.name).getOrElse(0)
          complete( tracks.sortWith(comp).map(track => Track(track.id,
             track.name,
             track.link,
             track.photo,
             track.vizualizari)))
      }
    }
  }
  def getSearchRoute=searchRoute

}
