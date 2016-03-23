package dataModel

import slick.driver.MySQLDriver.api._

/**
  * Created by katakonst on 2/28/16.
  */
case class UserDb(id:Int, username:String, md5Hash:String, photoLink:String)

class Users(tag: Tag) extends Table[UserDb](tag, "users") {
  def id = column[Int]("id",O.PrimaryKey)
  def username = column[String]("username")
  def md5Hash = column[String]("md5HASH")
  def photoLink=column[String]("photoLink")

  def * = (id, username, md5Hash,photoLink)<>(UserDb.tupled,UserDb.unapply)
}
