package routes

import DataAcces.LikeData
import JsonModels._
import spray.routing._

/**
  * Created by katakonst on 3/17/16.
  */
trait LikeRoute  extends HttpService {

  implicit def executionContext = actorRefFactory.dispatcher
  import spray.httpx.SprayJsonSupport._


  val getTrackLikes:Route=path("getTrackLikes")
  {
    import LikeJson._
    parameters('trackId) { (trackId)=>
    onSuccess(LikeData.getDb().getTrackLikes(Integer.parseInt(trackId))) {
          case (likes) =>
                     complete( likes.map(x=>Like(x._1,x._2,x._3)))
               }
        }

   }

  val likeTrack:Route=path("likeTrack")
  {
    import LikeJson._

    parameters('trackId,'userId) { (trackId,userId)=>
      onSuccess(LikeData.getDb().likeTrack(Integer.parseInt(trackId),Integer.parseInt(userId))) {
        case (nothing) => complete("succes")
      }
    }
  }

  val getUserLikedTracks:Route=path("getUserLikedTracks")
  {
import TrackJson._
    parameters('userId) { (userId)=>
      onSuccess(LikeData.getDb().getUsersTracksLikes(Integer.parseInt(userId))) {
        case (tracks) =>
          complete(tracks.map(x => Track(x._1, x._2,x._3,x._4,x._5)))

      }
    }
  }

  val getUserTrackLiked:Route=path("getUserTrackLiked") {
    import UserJson._
    parameters('trackId) { (trackId) =>
      onSuccess(LikeData.getDb().getUsersLikedTrack(Integer.parseInt(trackId))) {
        case (users) =>
          complete(users.map(x => User(x._1, x._2)))
      }
    }
  }

  val getTrackLikesNr:Route=path("getTrackNrLikes")
  {
    import LikesNrJson._

    parameters('trackId) { (trackId) =>
      onSuccess(LikeData.getDb().getTrackLikesNr(Integer.parseInt(trackId))) {
        case (number) =>
          complete(LikesNr(number))
      }
    }


  }

  def getUserLikedTracksRoute=getUserLikedTracks
  def getLikeTrackRoute=likeTrack
  def getTrackLikesRoute=getTrackLikes
  def getUserTracksLikedRoute=getUserTrackLiked

}
