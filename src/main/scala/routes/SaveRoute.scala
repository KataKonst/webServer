package routes

import DataAcces.{HashTagData, TracksData}
import spray.http.MultipartContent
import spray.routing._

import scala.concurrent.Future
import scala.reflect.io.File


/**
  * Created by katakonst on 3/7/16.
  */
trait SaveRoute extends HttpService  {

  implicit def executionContext = actorRefFactory.dispatcher

  val saveRoute:Route=path("save") {
    entity(as[MultipartContent]) { emailData =>
      val boss= emailData.parts.head.entity.data.asString.split("#").map(x=>"#"+x.trim)

      print( boss)
     File("/home/katakonst/licenta/playserver/music/" + emailData.parts.apply(1).filename.get).writeBytes(emailData.parts.apply(1).entity.data.toByteArray)
      File("/home/katakonst/licenta/playserver/images/" + emailData.parts.apply(2).filename.get).writeBytes(emailData.parts.apply(2).entity.data.toByteArray)
      onSuccess( TracksData.getDb.addTrack(emailData.parts.apply(1).filename.get,emailData.parts.apply(2).filename.get,0)) {
        case (test)=>

        val a= boss.map(hash=>
              HashTagData.getDb.getHashTagId(hash).map(
              hashTag=>HashTagData.getDb.addHashTagtoTrack(test,hashTag.getOrElse(
               HashTagData.getDb.addHashTags(hash)).##)))

          onSuccess(Future.sequence(a.toList)) {
            case o=>
            complete("succes");
          }


        }
    }
  }


  def getSaveRoute=saveRoute



}
