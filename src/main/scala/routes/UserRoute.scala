package routes

import DataAcces.{TracksData, UserData}
import JsonModels.User
import com.rockymadden.stringmetric.similarity.LevenshteinMetric
import dataModel.{TrackDb, UserDb}
import spray.http.MultipartContent
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
          complete("succes")
      }
    }


  }






  def getSearchedUsersRoute=searchedUsers
  def getAddPhotoToUserRoute=addPhotoToUser
  def getSearchByIdRoute=getUserById



}
