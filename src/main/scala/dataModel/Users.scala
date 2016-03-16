package dataModel

import slick.driver.MySQLDriver.api._

/**
  * Created by katakonst on 2/28/16.
  */
class Users(tag: Tag) extends Table[(Int, String,String)](tag, "users") {
  def id = column[Int]("id",O.PrimaryKey)
  def username = column[String]("username")
  def md5Hash = column[String]("md5HASH")

  def * = (id, username, md5Hash)
}
