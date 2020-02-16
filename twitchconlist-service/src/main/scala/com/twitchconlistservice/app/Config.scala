package com.twitchconlistservice.app

case class TwitchConfig(twitchApiToken: String, twitchconApiUrl: String)

case class TclConfig(twitch: TwitchConfig)