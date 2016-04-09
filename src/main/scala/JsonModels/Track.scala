package JsonModels
import spray.json.DefaultJsonProtocol

/**
  *
  * @param id  id of track
  * @param nume name of track
  * @param link  streaming link of track
  * @param photoLink static link of photo
  * @param vizualizari number of views
  */
case class Track(id: Int, nume: String,link: String, photoLink:String,vizualizari:Int,uploaderId:Int)

object TrackJson extends DefaultJsonProtocol {
  implicit val trackFormat = jsonFormat6(Track.apply)
}

