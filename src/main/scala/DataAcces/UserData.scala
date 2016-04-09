package DataAcces

import dataModel.dataModel.{FollowersToUsersDb, FollowersToUsers}
import dataModel.{Tracks, UserDb, Users}
import slick.driver.MySQLDriver.api._

import scala.concurrent.Future


/**
  * Created by katakonst on 3/5/16.
  */
object  UserData{

  val db=new UserData()
  def getDb=db
}

class UserData {
  val users = TableQuery[Users]
  var follow=TableQuery[FollowersToUsers]



  val db= Database.forURL("jdbc:mysql://localhost:3306/test", driver="com.mysql.jdbc.Driver", user="root", password="")

  def register(name:String,md5:String):Future[Unit]=  db.run(DBIO.seq(users +=UserDb(0, name, md5,"S")))
  def getMd5Pass(name:String):Future[Seq[UserDb]]= db.run(users.filter(lUser=>lUser.username === name).result)
  def getAllUsers(name:String):Future[Seq[UserDb]]=db.run(users.result)
  def addPhotoToUser(id:Int,photo:String):Future[Int]=db.run(users.filter
      (lUser=>lUser.id===id).
    map(lUser=>lUser.photoLink).update(photo))
  def getUserById(userId:Int):Future[Seq[UserDb]]=db.run(users.filter((user)=>user.id===userId).result)

  def followUser(userId:Int,follow_id:Int):Future[Unit]=db.run(DBIO.seq(follow +=FollowersToUsersDb(1, userId,follow_id)))

  def unfollowUser(userId:Int,follow_id:Int):Future[Int]=db.run(follow.filter((fol)=>fol.user_id===userId&&fol.follow_user===follow_id).delete)

  def isUserFollowing(userId:Int,follow_id:Int):Future[Int]=db.run(follow.filter(fol=>fol.user_id===userId&&fol.follow_user===follow_id).length.result)

  def getFollowing(userId:Int):Future[Seq[UserDb]]={

    val query = for {
      (follow, user) <-  follow.filter((fol)=>fol.user_id===userId)  join users on (
        (lFollow,lUser)=>lUser.id === lFollow.follow_user)

    }
      yield user

    db.run(query.result)

  }

  def getFollowers(userId:Int):Future[Seq[UserDb]]={

    val query = for {
      (follow, user) <-  follow.filter((fol)=>fol.user_id===userId)  join users on (
        (lFollow,lUser)=>lUser.id === lFollow.user_id)

    }
      yield user

    db.run(query.result)

  }

}
