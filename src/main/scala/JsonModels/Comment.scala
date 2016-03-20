package JsonModels

/**
  * Created by katakonst on 3/10/16.
  */
import spray.json.DefaultJsonProtocol

case class Comment(id: Int, author_id: Int,date: String, Text:String,authorName:String)

object CommentJson extends DefaultJsonProtocol {
  implicit val trackFormat = jsonFormat5(Comment.apply)
}

