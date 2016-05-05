package DataAcces
import slick.driver.MySQLDriver.api._

/**
  * Created by katakonst on 5/4/16.
  */
object DatabaseConn {
  val dbConn= Database.forURL("jdbc:mysql://localhost:3306/test", driver="com.mysql.jdbc.Driver", user="root", password="")
  def getConnection=dbConn

}
