package DataAcces

import dataModel.Users
import slick.driver.MySQLDriver.api._

import scala.concurrent.Future


/**
  * Created by katakonst on 3/5/16.
  */
class UserData {
  val users = TableQuery[Users]

  val db= Database.forURL("jdbc:mysql://localhost:3306/test", driver="com.mysql.jdbc.Driver", user="root", password="");

  def register(name:String,md5:String):Future[Unit]=  db.run(DBIO.seq(users +=(1, name, md5)));
  def getMd5Pass(name:String):Future[Seq[(Int,String,String)]]= db.run(users.filter(_.username === name).result)


}
