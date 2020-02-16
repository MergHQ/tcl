package com.twitchconlistservice.app.twitch

import play.api.libs.json.Json

import scala.concurrent.Future
import sttp.client._
import sttp.client.asynchttpclient.future.AsyncHttpClientFutureBackend
import sttp.client.playJson._

import scala.concurrent.ExecutionContext.Implicits.global


case class TwitchUserResponse(data: Seq[TwitchProfile])

object TwitchUserResponse {
  implicit val reads = Json.format[TwitchUserResponse]
  implicit val writes = Json.writes[TwitchUserResponse]
}

class TwitchClient(token: String, twitchConUrl: String) {
  implicit val sttpBackend = AsyncHttpClientFutureBackend()

  def getUserProfiles(userIds: Seq[String]): Future[Seq[TwitchProfile]] = {
    val request = basicRequest
      .get(uri"https://api.twitch.tv/helix/users?${userIds.map("id" -> _)}")
      .header("Client-ID", token)
      .response(asJson[TwitchUserResponse])
      .send()

    request
      .flatMap {
        _.body match {
          case Left(error) =>
            println(s"Failed to fetch users from twitch ${error.body}")
            Future.failed(error)
          case Right(response) =>
            Future.successful(response.data)
        }
      }
  }

  def getTwitchConAttendees() = {
    val request = basicRequest
      .get(uri"$twitchConUrl")
      .response(asJson[Seq[TwitchConProfile]])
      .send()

    request
      .flatMap {
        _.body match {
          case Left(error) =>
            println(s"Failed to fetch twitchcon attendees ${error.body}")
            Future.failed(error)
          case Right(response) =>
            Future.successful(response)
        }
      }
  }
}