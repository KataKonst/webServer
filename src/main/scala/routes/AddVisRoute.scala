package routes

import DataAcces.TracksData
import spray.http.MultipartContent
import spray.routing._

import scala.reflect.io.File

/**
  * Created by katakonst on 3/9/16.
  */
trait AddVisRoute  extends HttpService {

  implicit def executionContext = actorRefFactory.dispatcher

  val addVisRoute:Route=path("addVis") {
    parameter('id) { (id) =>
      onSuccess( TracksData.getVizs(Integer.parseInt(id))) {
        case (test)=>
           onSuccess(TracksData.addVis(Integer.parseInt(id),test.getOrElse(0)))
                  {
                          case(track)=>complete("succes")

                  }
      }
    }
  }

  val trackVis:Route=path("getVis")
  {
    parameter('id){id=>

      onSuccess( TracksData.getVizs(Integer.parseInt(id))) {
        case(viz)=> complete(String.valueOf(viz.get))
      }

    }


  }
  def getVisRoute=addVisRoute
  def getTrackVisRoute=trackVis




}
