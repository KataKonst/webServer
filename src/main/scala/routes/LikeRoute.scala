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
    onSuccess(LikeData.getDb.getTrackLikes(Integer.parseInt(trackId))) {
          case (likes) =>
                     complete( likes.map(like=>Like(like.like_id,like.user_id,like.track_id)))
               }
        }

   }

  val likeTrack:Route=path("likeTrack")
  {
    import LikeJson._

    parameters('trackId,'userId) { (trackId,userId)=>
      onSuccess(LikeData.getDb.likeTrack(Integer.parseInt(trackId),Integer.parseInt(userId))) {
        case (nothing) => complete("succes")
      }
    }
  }

  val getUserLikedTracks:Route=path("getUserLikedTracks")
  {
import TrackJson._
    parameters('userId) { (userId)=>
      onSuccess(LikeData.getDb.getUsersTracksLikes(Integer.parseInt(userId))) {
        case (tracks) =>
          complete(tracks.map(track =>  Track(track.id,
            track.name,
            track.link,
            track.photo,
            track.vizualizari)))

      }
    }
  }

  val getUserTrackLiked:Route=path("getUserTrackLiked") {
    import UserJson._
    parameters('trackId) { (trackId) =>
      onSuccess(LikeData.getDb.getUsersLikedTrack(Integer.parseInt(trackId))) {
        case (users) =>
          complete(users.map(user => User(user.id, user.username,user.photoLink)))
      }
    }
  }

  val getTrackLikesNr:Route=path("getTrackNrLikes") {
    import LikesNrJson._

    parameters('trackId) { (trackId) =>
      onSuccess(LikeData.getDb.getTrackLikesNr(Integer.parseInt(trackId))) {
        case (number) =>
          complete(LikesNr(number))
      }
    }
  }


    val unlike:Route=path("unlike") {
      import LikesNrJson._

      parameters('trackId, 'userId) { (trackId, userId) =>
        onSuccess(LikeData.getDb.unLike(Integer.parseInt(trackId), Integer.parseInt(userId))) {
          case (number) =>
            complete("succes")
        }
      }
    }

      val checkUserLikedTrack:Route=path("checkUserLikedTrack")
      {
        import UserLikedTrackJson._

        parameters('trackId ,'userId) { (trackId,userId) =>
          onSuccess(LikeData.getDb.checkUserLikedTrack(Integer.parseInt(trackId),Integer.parseInt(userId))) {
            case (0) =>
              complete(UserLikedTrack(false))
            case _=>
              complete(UserLikedTrack(true))

          }
        }



    }

  def getUserLikedTracksRoute=getUserLikedTracks
  def getLikeTrackRoute=likeTrack
  def getTrackLikesRoute=getTrackLikes
  def getUserTracksLikedRoute=getUserTrackLiked
  def getCheckUserLikedTrackRoute=checkUserLikedTrack
  def getUnLikeRoute=unlike



}
