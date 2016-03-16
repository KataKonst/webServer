package JsonModels
import spray.json.DefaultJsonProtocol

case class Track(id: Int, nume: String,link: String, photoLink:String,vizualizari:Int)

object TrackJson extends DefaultJsonProtocol {
  implicit val trackFormat = jsonFormat5(Track.apply)
}

