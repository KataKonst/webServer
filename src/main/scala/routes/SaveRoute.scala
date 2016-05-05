package routes

import java.io.PrintStream
import java.net.{InetAddress, Socket}

import DataAcces.{HashTagData, TracksData}
import matcher.Constants
import org.apache.commons.codec.digest.DigestUtils
import spray.http.{MediaTypes, MultipartContent}
import spray.routing._

import scala.concurrent.Future
import scala.io.BufferedSource
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


      val md52 = DigestUtils.md5Hex(emailData.parts.apply(2).entity.data.toByteArray)
      val md53 = DigestUtils.md5Hex(emailData.parts.apply(3).entity.data.toByteArray)


      File(Constants.musicPath+ md53+".mp3").writeBytes(emailData.parts.apply(3).entity.data.toByteArray)
      File(Constants.imagePath + md52).writeBytes(emailData.parts.apply(2).entity.data.toByteArray)

      val s = new Socket(InetAddress.getByName("localhost"), 50006)
      lazy val in = new BufferedSource(s.getInputStream).getLines
      val out = new PrintStream(s.getOutputStream)

      out.println(Constants.musicPath + md53+".mp3"+"|"+emailData.parts.apply(3).filename.get)
      out.flush()

      onSuccess( TracksData.addTrack(emailData.parts.apply(3).filename.get,md52,Integer.parseInt(id), md53+".mp3")) {
        case (test)=>

        val a= boss.distinct.map(hash=>
              HashTagData.getHashTagId(hash) flatMap {
                case Some(x:Int) => Future.successful(x)
                case None =>  HashTagData.addHashTags(hash)
              } flatMap( x=>HashTagData.addHashTagtoTrack(test,x)))


          onSuccess(Future.sequence(a.toList)) {

case o=>
  respondWithMediaType(MediaTypes.`text/html`) {

    complete("<META http-equiv=\"refresh\" content=\"0;URL=http://"+Constants.ip+"\">")
  }
}



        }
    }
  }


  def getSaveRoute=saveRoute



}
