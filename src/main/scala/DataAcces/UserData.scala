package DataAcces

import dataModel.dataModel.{FollowersToUsersDb, FollowersToUsers}
import dataModel.{Tracks, UserDb, Users}
import slick.driver.MySQLDriver.api._

import scala.concurrent.Future


/**
  * Created by katakonst on 3/5/16.
  */
object  UserData{

  val users = TableQuery[Users]
  var follow=TableQuery[FollowersToUsers]




  def register(name:String,md5:String):Future[Unit]=  DatabaseConn.getConnection.run(DBIO.seq(users +=UserDb(0, name, md5,"S")))
  def getMd5Pass(name:String):Future[Seq[UserDb]]= DatabaseConn.getConnection.run(users.filter(lUser=>lUser.username === name).result)
  def getAllUsers(name:String):Future[Seq[UserDb]]=DatabaseConn.getConnection.run(users.result)
  def addPhotoToUser(id:Int,photo:String):Future[Int]=DatabaseConn.getConnection.run(users.filter
  (lUser=>lUser.id===id).
    map(lUser=>lUser.photoLink).update(photo))
  def getUserById(userId:Int):Future[Seq[UserDb]]=DatabaseConn.getConnection.run(users.filter((user)=>user.id===userId).result)

  def followUser(userId:Int,follow_id:Int):Future[Unit]=DatabaseConn.getConnection.run(DBIO.seq(follow +=FollowersToUsersDb(1, userId,follow_id)))

  def unfollowUser(userId:Int,follow_id:Int):Future[Int]=DatabaseConn.getConnection.run(follow.filter((fol)=>fol.user_id===userId&&fol.follow_user===follow_id).delete)

  def isUserFollowing(userId:Int,follow_id:Int):Future[Int]=DatabaseConn.getConnection.run(follow.filter(fol=>fol.user_id===userId&&fol.follow_user===follow_id).length.result)

  def getFollowing(userId:Int):Future[Seq[UserDb]]={

    val query = for {
      (follow, user) <-  follow.filter((fol)=>fol.user_id===userId)  join users on (
        (lFollow,lUser)=>lUser.id === lFollow.follow_user)

    }
      yield user

    DatabaseConn.getConnection.run(query.result)

  }

  def getFollowers(userId:Int):Future[Seq[UserDb]]={

    val query = for {
      (follow, user) <-  follow.filter((fol)=>fol.user_id===userId)  join users on (
        (lFollow,lUser)=>lUser.id === lFollow.user_id)

    }
      yield user

    DatabaseConn.getConnection.run(query.result)

  }
}

class UserData {


}
