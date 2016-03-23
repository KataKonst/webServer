package JsonModels

import spray.json.DefaultJsonProtocol

/**
  * Created by katakonst on 3/10/16.
  */


case class PlayList(id: Int, authorid: Int,nume: String, date:String)

object PlayListJson extends DefaultJsonProtocol {
  implicit val trackFormat = jsonFormat4(PlayList.apply)
}

