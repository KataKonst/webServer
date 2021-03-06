package routes

import DataAcces.{PlayListData, TracksData}
import JsonModels._
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

      onSuccess( PlayListData.getPlaylsts) {
        case (playList) =>
          complete( playList.map(playList=>
            PlayList(playList.id,
              playList.authorid,
              playList.nume,
              playList.date)))


      }


  }

  val addToPlayList:Route=path("addToPlayList")
  {
    import spray.httpx.SprayJsonSupport._
    import PlayListJson._
    parameters('playId,'trackId) {(playId,trackId)=>
      onSuccess(PlayListData.addToPlayList(Integer.parseInt(playId),Integer.parseInt(trackId))) {
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
      onSuccess(PlayListData.createPlayList(Integer.parseInt(userId), "1-1-2001", nume)) {
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

      onSuccess(PlayListData.getTrackPlayList(Integer.valueOf(playId))) {

        case (tracks) =>
          complete(tracks.map(track => Track(track.id, track.name, track.link, track.photo, track.vizualizari,track.UploaderId)))
      }
    }
  }

  val getUserPlayList:Route=path("userPlayLists")
  {
    import spray.httpx.SprayJsonSupport._

    parameter('userId ) {(userId)=>
      import PlayListJson._

      onSuccess(PlayListData.getUserPlaylists(Integer.valueOf(userId))) {

        case (playLists) =>
          complete(
            playLists.map(playList=>
            PlayList(playList.id,
              playList.authorid,
              playList.nume,
              playList.date))
          )}
    }
  }

  val deletePlayList:Route=path("deletePlayList")
  {
    parameter('playId ){
      (playId)=>
                onSuccess(PlayListData.deleteFromTrackToPlayLis(Integer.parseInt(playId))) {
                  case (succes)=>
                  onSuccess(PlayListData.deletePlayList(Integer.parseInt(playId))) {
                    case (result) => complete("")

                  }
                }

    }

  }

  val deleteTrackFromPlayList:Route=path("deleteTrackFromPlayList")
  {

    parameter('playId, 'trackId ){(playId,trackId)=>
      {
        onSuccess(PlayListData.deleteTrackFromPlayList(Integer.parseInt(trackId),Integer.parseInt(playId)))
        {
          case (succes)=>
                complete("succes")
        }
      }

    }

  }

  val checkTrackFromPlayList:Route=path("checkTrackFromPlayList")
  {
    import spray.httpx.SprayJsonSupport._

    parameter('playId, 'trackId ){(playId,trackId)=>
    {
      import TrackInPlayListJson._

      onSuccess(PlayListData.checkTrackPlayList(Integer.parseInt(trackId),Integer.parseInt(playId)))
      {

        case (0) =>
          complete(TrackInPlayList(false))
        case _=>
          complete(TrackInPlayList(true))
      }
    }

    }

  }





  def getPlayListRoutes=createPlayList~addToPlayList~getPlayLists~tracksOfPlayList~getUserPlayList~deletePlayList~deleteTrackFromPlayList~checkTrackFromPlayList




}
