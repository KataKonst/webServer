package routes

import DataAcces.UserData
import JsonModels.User
import com.rockymadden.stringmetric.similarity.LevenshteinMetric
import dataModel.{TrackDb, UserDb}
import spray.http.MultipartContent
import spray.routing.{Route, HttpService}

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





  def getSearchedUsersRoute=searchedUsers



}
