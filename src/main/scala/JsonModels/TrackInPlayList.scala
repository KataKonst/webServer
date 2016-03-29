package JsonModels

import spray.json.DefaultJsonProtocol

/**
  * Created by katakonst on 3/27/16.
  */
case class TrackInPlayList(result:Boolean)

object TrackInPlayListJson extends DefaultJsonProtocol {
  implicit val trackFormat = jsonFormat1(TrackInPlayList.apply)
}
