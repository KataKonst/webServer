package routes

import DataAcces.{TracksData, UserData}
import JsonModels.{TrackInPlayListJson, TrackInPlayList, User}
import com.rockymadden.stringmetric.similarity.LevenshteinMetric
import dataModel.{TrackDb, UserDb}
import matcher.Constants
import spray.http.{MediaTypes, MultipartContent}
import spray.routing.{Route, HttpService}

import scala.reflect.io.File

/**
  * Created by katakonst on 3/24/16.
  */
trait UserRoute extends HttpService {

  implicit def executionContext = actorRefFactory.dispatcher

  val searchedUsers:Route=path("searchUser"){
    import spray.httpx.SprayJsonSupport._
    import JsonModels.UserJson._
    parameters('searchText) { searchText =>
      onSuccess(UserData.getDb.getAllUsers(searchText)){

        case (users)=>
          def comp(e1: (UserDb), e2: (UserDb)):Boolean = LevenshteinMetric.compare(searchText,e1.username).getOrElse(0) < LevenshteinMetric.compare(searchText, e2.username).getOrElse(0)
                complete(users.sortWith(comp).map((user)=>User(user.id,user.username,user.photoLink)))

      }

    }


  }



  val getUserById:Route=path("getUserById")
  {
    import spray.httpx.SprayJsonSupport._
    import JsonModels.UserJson._
    parameters('userId) { userId =>
      onSuccess(UserData.getDb.getUserById(Integer.parseInt(userId))){

        case (users)=>
          complete(users.map((user)=>User(user.id,user.username,user.photoLink)))

      }

    }

  }

  val addPhotoToUser:Route=path("addPhotoToUser")
  {
     entity(as[MultipartContent]) { emailData =>
      val id= emailData.parts.head.entity.data.asString
      File("/home/katakonst/licenta/playserver/images/" + emailData.parts.apply(1).filename.get).writeBytes(emailData.parts.apply(1).entity.data.toByteArray)
      onSuccess(UserData.getDb.addPhotoToUser(Integer.parseInt(id),emailData.parts.apply(1).filename.get)) {
        case (test)=>
          respondWithMediaType(MediaTypes.`text/html`) {

            complete("<META http-equiv=\"refresh\" content=\"0;URL=http://"+Constants.ip+"\">");
          }      }
    }


  }


  val followUser:Route=path("followUser") {
    parameters('userId, 'followUser) {(userId,followUser)=>


      onSuccess(UserData.getDb.followUser(Integer.parseInt(userId),Integer.parseInt(followUser)))
      {
        case (succes)=>complete("ssa")
      }

    }
  }

  val unFollowUser:Route=path("unfollowUser")
  {
    parameters('userId, 'followUser) {(userId,followUser)=>


      onSuccess(UserData.getDb.unfollowUser(Integer.parseInt(userId),Integer.parseInt(followUser)))
      {
        case (succes)=>complete("ssa")
      }

    }

  }

  val isUserFollowing:Route=path("isUserFollowing")
  {
    import spray.httpx.SprayJsonSupport._

    parameters('userId, 'followUser){(userId,followUser)=>

      import TrackInPlayListJson._

      onSuccess(UserData.getDb.isUserFollowing(Integer.parseInt(userId),Integer.parseInt(followUser)))
      {
        case (0) =>
          complete(TrackInPlayList(false))
        case _=>
          complete(TrackInPlayList(true))      }

    }
  }




  val  getFollowers:Route=path("getFollowers")
  {
    import spray.httpx.SprayJsonSupport._
    import JsonModels.UserJson._

    parameter('userId){userId=>
      onSuccess(UserData.getDb.getFollowers(Integer.parseInt(userId))){
        case (users)=>
          complete(users.map((user)=>User(user.id,user.username,user.photoLink)))
      }


    }
  }
  val getFollowing:Route=path("getFollowing") {
    import spray.httpx.SprayJsonSupport._
    import JsonModels.UserJson._

    parameter('userId) {
      { userId =>
        onSuccess(UserData.getDb.getFollowing(Integer.parseInt(userId))) {
          case (users) =>
            complete(users.map((user) => User(user.id, user.username, user.photoLink)))
        }

      }

    }
  }


  def getSearchedUsersRoute=searchedUsers
  def getAddPhotoToUserRoute=addPhotoToUser
  def getSearchByIdRoute=getUserById
  def getIsUserFollowingRoute=isUserFollowing
  def getFollowUserRoute=followUser
  def getUnfollowUserRoute=unFollowUser
  def getFollowersRoute=getFollowers
  def getFollowingRoute=getFollowing



}
