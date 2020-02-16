package com.twitchconlistservice.app.twitch

import play.api.libs.json.{Json, OFormat}

case class TwitchProfile(
 login: String,
 display_name: String,
 `type`: String,
 broadcaster_type: String,
 description: String,
 profile_image_url: String,
 offline_image_url: String,
 view_count: Int
)

object TwitchProfile {
  implicit val json: OFormat[TwitchProfile] = Json.format[TwitchProfile]
}

case class TwitchConProfile(
 id: String,
 display_name: String,
 broadcaster_type: String,
 profile_image_url: String,
 channel_url: String
)

object TwitchConProfile {
  implicit val json: OFormat[TwitchConProfile] = Json.format[TwitchConProfile]
}