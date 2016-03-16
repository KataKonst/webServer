package dataModel

import java.sql.Date

import slick.driver.MySQLDriver.api._


/**
  * Created by katakonst on 3/10/16.
  */
class Comments(tag: Tag) extends Table[(Int,Int,Int,String, String)](tag, "Comments") {

  def id = column[Int]("id",O.PrimaryKey,O.AutoInc)
  def author_id =  column[Int]("author_id")
  def trackid=column[Int]("trackid")

  def date=column[String]("date")

  def text=column[String]("Text")

  def * = (id,author_id,trackid,date,text)



}
