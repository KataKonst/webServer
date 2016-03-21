package JsonModels

import JsonModels.PlayListJson._
import spray.json.DefaultJsonProtocol

/**
  * Created by katakonst on 3/21/16.
  */
case class LikesNr(nr:Int)

object LikesNrJson extends DefaultJsonProtocol {
  implicit val trackFormat = jsonFormat1(LikesNr.apply)
}

