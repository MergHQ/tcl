package com.twitchconlistservice.app.twitch

import scala.concurrent.{ExecutionContext, Future}

class TwitchService(twitchCient: TwitchClient) {
  var cachedResults: Option[Seq[TwitchProfile]] = None

  def fetchTwitchconProfiles()(implicit executionContext: ExecutionContext): Future[Seq[TwitchProfile]] =
    cachedResults match {
      case None =>
        for {
          twitchConAttendees <- twitchCient.getTwitchConAttendees
          twitchProfiles <- Future.sequence(
            twitchConAttendees
              .map(_.id)
              .grouped(100)
              .toSeq
              .map(twitchCient.getUserProfiles)
          )
        } yield {
          cachedResults = Some(twitchProfiles.flatten)
          twitchProfiles.flatten
        }
      case Some(results) => Future.successful(results)
    }
}
