package com.twitchconlistservice.app.twitch

import scala.concurrent.{ExecutionContext, Future}
import awscala._
import com.twitchconlistservice.app.AwsCreds
import dynamodbv2._

class TwitchService(twitchClient: TwitchClient, awsConfig: AwsCreds) {
  private implicit val dynamoDb = DynamoDB(awsConfig.keyId, awsConfig.secretKey)(Region.Frankfurt)

  def fetchTwitchconProfiles()(implicit executionContext: ExecutionContext): Future[Seq[TwitchProfile]] = Future {
    val tclTable = dynamoDb.table("tcl").get
    val results: Seq[Item] = tclTable.scan(
      Seq("broadcaster_type" -> cond.eq("partner"))
    )

    results
      .map(
        _.attributes
          map (attr =>
          attr.name -> attr.value.s.getOrElse(attr.value.getN))
          toMap)
      .map(item => TwitchProfile(
        item("login"),
        item("display_name"),
        item.getOrElse("type", ""),
        item("broadcaster_type"),
        item.getOrElse("description", ""),
        item.getOrElse("profile_image_url", ""),
        item.getOrElse("offline_image_url", ""),
        item.getOrElse("view_count", "-1").toInt
      ))
  }

  def updateDatabase()(implicit ec: ExecutionContext) = Future {
    val table = dynamoDb.table("tcl").get
    for {
      oldAttendees <- fetchTwitchconProfiles()
      tcAttendees <- twitchClient.getTwitchConAttendees()
      profiles <- Future.sequence(
        tcAttendees
          .grouped(100)
          .map(tcAttendeeGroup =>
            twitchClient.getUserProfiles(tcAttendeeGroup.map(_.id))
          )
      )
    } yield {
      profiles
        .flatten
        .toSeq
        .filter(attendee => !oldAttendees.exists(_.login == attendee.login))
        .foreach { profile =>
          println(s"Inserting ${profile.login} to database.")
          table.put(
            profile.login,
            "display_name" -> profile.display_name,
            "type" -> resolveEmptyString(profile.`type`),
            "broadcaster_type" -> profile.broadcaster_type,
            "description" -> resolveEmptyString(profile.description),
            "profile_image_url" -> resolveEmptyString(profile.profile_image_url),
            "offline_image_url" -> resolveEmptyString(profile.offline_image_url),
            "view_count" -> profile.view_count
          )
        }
    }
  }

  private def resolveEmptyString(str: String) =
    if (str.isEmpty)
      null
    else
      str
}
