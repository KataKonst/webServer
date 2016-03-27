package JsonModels

import spray.json.DefaultJsonProtocol

/**
  * Created by katakonst on 3/27/16.
  */
case class UserLikedTrack(result:Boolean)

object UserLikedTrackJson extends DefaultJsonProtocol {
  implicit val trackFormat = jsonFormat1(UserLikedTrack.apply)
}

