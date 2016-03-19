package JsonModels

import spray.json.DefaultJsonProtocol

/**
  * Created by katakonst on 3/10/16.
  */
case class User(id: Int,nume: String)

object UserJson extends DefaultJsonProtocol {
  implicit val trackFormat = jsonFormat2(User.apply)
}

