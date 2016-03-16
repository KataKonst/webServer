package routes

import java.util.Calendar

import DataAcces.{CommentsData, HashTagData}
import JsonModels.{CommentJson, Comment, Track, TrackJson}
import spray.http.MediaTypes
import spray.routing._

/**
  * Created by katakonst on 3/10/16.
  */
trait CommentsRoute extends HttpService {
  implicit def executionContext = actorRefFactory.dispatcher

  import spray.httpx.SprayJsonSupport._
  val addComment: Route = path("addComent") {
    import spray.httpx.SprayJsonSupport._

    get {
      respondWithMediaType(MediaTypes.`text/plain`) {
        import TrackJson._
        parameters('userid, 'text, 'trackId) { (userid,text,trackid) =>
            onSuccess(CommentsData.getDb.addComment(Integer.parseInt(userid),
              "1:2:2015",Integer.parseInt(trackid),text)) {
            case (name) =>
            complete("succes")

          }

        }
      }
    }
  }


  val getTracksComments: Route = path("getComments") {
    import spray.httpx.SprayJsonSupport._

    get {
      respondWithMediaType(MediaTypes.`text/plain`) {
        import CommentJson._

        parameters('trackId) { (trackid) =>

          onSuccess(CommentsData.getDb.getCommentsOfTracks(Integer.parseInt(trackid))) {
          case (name) =>
              complete(name.map(x=>Comment(x._1,x._2,x._4.toString,x._5)))
           // complete(name.map(x=>))
        }

        }
      }
    }
  }

  def getAddCommentRoute=addComment;


}
