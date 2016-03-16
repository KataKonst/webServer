package matcher

import spray.json.DefaultJsonProtocol

case class Login(id: Int, data: String,md5Hash: String)

object LoginJSon extends DefaultJsonProtocol {
  implicit val stuffFormat = jsonFormat3(Login.apply)
}

