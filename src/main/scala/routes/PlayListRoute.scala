package routes

import DataAcces.{PlayListData, TracksData}
import JsonModels.{PlayList, PlayListJson, Track, TrackJson}
import com.rockymadden.stringmetric.similarity.LevenshteinMetric
import spray.routing._

/**
  * Created by katakonst on 3/10/16.
  */
trait PlayListRoute extends HttpService {

  implicit def executionContext = actorRefFactory.dispatcher


  val getPlayLists:Route=path("playlist")
  {
     import spray.httpx.SprayJsonSupport._
     import PlayListJson._

      onSuccess( PlayListData.getDb().getPlayLists) {
        case (name) =>
          complete( name.map(x=>PlayList(x._1,x._2,x._3,x._4)))
      }

  }

  val addToPlayList:Route=path("addToPlayList")
  {
    import spray.httpx.SprayJsonSupport._
    import PlayListJson._
    parameters('playId,'trackId) {(playId,trackId)=>
      onSuccess(PlayListData.getDb().addToPlayList(Integer.parseInt(playId),Integer.parseInt(trackId))) {
        case (name) =>
          complete("succes")
      }
    }

  }
  val createPlayList:Route=path("createPlayList")
  {
    import spray.httpx.SprayJsonSupport._
    import PlayListJson._
    parameters('userId,'nume) { (userId, nume) =>
      onSuccess(PlayListData.getDb().createPlayList(Integer.parseInt(userId), "1-1-2001", nume)) {
        case (name) =>
          complete("succes")
      }
    }
  }

  val tracksOfPlayList:Route=path("tracksofPlayList")
  {
    import spray.httpx.SprayJsonSupport._

    parameter('playId ) {(playId)=>
      import TrackJson._

      onSuccess(PlayListData.getDb().getTrackPlayList(Integer.valueOf(playId))) {

        case (name) =>
          complete(name.map(x => Track(x._1, x._2, x._3, x._4, x._5)))
      }
    }
  }

  def getCreatePLayListRoute=createPlayList
  def getAddToPlayListRoute=addToPlayList
  def getPlayListsRoute=getPlayLists
  def getTracksOfPlaylistRoute=tracksOfPlayList

}
