package routes

import DataAcces.TracksData
import JsonModels.{Track, TrackJson}
import com.rockymadden.stringmetric.similarity.LevenshteinMetric
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
      onSuccess( TracksData.getDb().getTracks) {
        case (name) =>
          def comp(e1: (Int,String,String,String,Int,Int), e2: (Int,String,String,String,Int,Int)):Boolean = LevenshteinMetric.compare(nume,e1._2).getOrElse(0) < LevenshteinMetric.compare(nume,e2._2).getOrElse(0)
          complete( name.sortWith(comp).map(x => Track(x._1, x._2,x._3,x._4,x._5)))
      }
    }
  }
  def getSearchRoute=searchRoute;

}
