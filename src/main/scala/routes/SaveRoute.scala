package routes

import DataAcces.{HashTagData, TracksData}
import matcher.Constants
import org.apache.commons.codec.digest.DigestUtils
import spray.http.{MediaTypes, MultipartContent}
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
      val id= emailData.parts.apply(1).entity.data.asString


      val md52 = DigestUtils.md5Hex(emailData.parts.apply(2).entity.data.toByteArray);
      val md53 = DigestUtils.md5Hex(emailData.parts.apply(3).entity.data.toByteArray);


      File("/home/katakonst/licenta/playserver/music/" + md53).writeBytes(emailData.parts.apply(3).entity.data.toByteArray)
      File("/home/katakonst/licenta/playserver/images/" + md52).writeBytes(emailData.parts.apply(2).entity.data.toByteArray)
      onSuccess( TracksData.getDb.addTrack(emailData.parts.apply(3).filename.get,md52,Integer.parseInt(id),md53)) {
        case (test)=>

        val a= boss.distinct.map(hash=>
              HashTagData.getDb.getHashTagId(hash) flatMap {
                case Some(x:Int) => Future.successful(x)
                case None =>  HashTagData.getDb.addHashTags(hash)
              } flatMap( x=>HashTagData.getDb.addHashTagtoTrack(test,x)))


          onSuccess(Future.sequence(a.toList)) {

case o=>
  respondWithMediaType(MediaTypes.`text/html`) {

    complete("<META http-equiv=\"refresh\" content=\"0;URL=http://"+Constants.ip+"\">");
  }
}



        }
    }
  }


  def getSaveRoute=saveRoute



}
