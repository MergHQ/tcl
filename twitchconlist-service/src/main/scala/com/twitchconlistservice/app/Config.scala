package com.twitchconlistservice.app

case class TwitchConfig(twitchApiToken: String, twitchconApiUrl: String)
case class AwsCreds(keyId: String ,secretKey: String)

case class TclConfig(twitch: TwitchConfig, aws: AwsCreds)