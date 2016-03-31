package DataAcces

import dataModel.{UserDb, Users}
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

  val db= Database.forURL("jdbc:mysql://localhost:3306/test", driver="com.mysql.jdbc.Driver", user="root", password="")

  def register(name:String,md5:String):Future[Unit]=  db.run(DBIO.seq(users +=UserDb(1, name, md5,"S")))
  def getMd5Pass(name:String):Future[Seq[UserDb]]= db.run(users.filter(lUser=>lUser.username === name).result)
  def getAllUsers(name:String):Future[Seq[UserDb]]=db.run(users.result)
  def addPhotoToUser(id:Int,photo:String):Future[Int]=db.run(users.filter
      (lUser=>lUser.id===id).
    map(lUser=>lUser.photoLink).update(photo))
  def getUserById(userId:Int):Future[Seq[UserDb]]=db.run(users.filter((user)=>user.id===userId).result)


}
