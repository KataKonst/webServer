package routes

import DataAcces.TracksData
import spray.http.MultipartContent
import spray.routing._

import scala.reflect.io.File


/**
  * Created by katakonst on 3/7/16.
  */
trait SaveRoute extends HttpService  {

  implicit def executionContext = actorRefFactory.dispatcher

  val saveRoute:Route=path("save") {
    entity(as[MultipartContent]) { emailData =>
      File("/home/katakonst/licenta/playserver/music/" + emailData.parts.head.filename.get).writeBytes(emailData.parts.head.entity.data.toByteArray)
      File("/home/katakonst/licenta/playserver/images/" + emailData.parts.apply(1).filename.get).writeBytes(emailData.parts.apply(1).entity.data.toByteArray)

      onSuccess( TracksData.getDb.addTrack(emailData.parts.head.filename.get,emailData.parts.apply(1).filename.get,0)) {
        case (test)=>
          complete("succes")
      }
    }
  }


  def getSaveRoute=saveRoute



}
