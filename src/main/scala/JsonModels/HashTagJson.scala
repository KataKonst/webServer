package JsonModels

import spray.json.DefaultJsonProtocol

case class HashTag(id: Int, description: String)

object HashTagJson extends DefaultJsonProtocol {
  implicit val trackFormat = jsonFormat2(HashTag.apply)
}

