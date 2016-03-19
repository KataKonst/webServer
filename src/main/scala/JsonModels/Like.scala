package JsonModels

import spray.json.DefaultJsonProtocol

case class Like(id: Int, authorid: Int,trackId:Int)

object LikeJson extends DefaultJsonProtocol {
  implicit val trackFormat = jsonFormat3(Like.apply)
}

