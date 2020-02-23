package com.twitchconlistservice.app

import com.twitchconlistservice.app.twitch.{TwitchClient, TwitchService}
import _root_.akka.dispatch._
import akka.actor.ActorSystem
import org.scalatra._
import pureconfig._
import pureconfig.backend.ConfigFactoryWrapper
import com.typesafe.config._
import play.api.libs.json._
import pureconfig.generic.auto._
import org.scalatra.json._
import org.json4s.{DefaultFormats, Formats}
import org.json4s.JsonDSL._
import scala.concurrent.{ExecutionContext, Future}

class Application(system: ActorSystem) extends ScalatraServlet with FutureSupport {
  protected implicit lazy val jsonFormats: Formats = DefaultFormats
  protected implicit def executor: ExecutionContext = system.dispatcher

  val localConf = ConfigFactoryWrapper
    .parseResources("application.conf")
    .getOrElse(ConfigFactory.empty())

  val conf: TclConfig = ConfigSource.fromConfig(localConf).load[TclConfig] match {
    case Left(errors) =>
        throw new Exception(s"Failed loading config $errors")
    case Right(tclConf) =>
      tclConf
  }

  val twitchClient = new TwitchClient(conf.twitch.twitchApiToken, conf.twitch.twitchconApiUrl)
  val twitchService = new TwitchService(twitchClient, conf.aws)

  before() {
    contentType = "application/json"
    response.setHeader("Access-Control-Allow-Origin", "*")
  }

  get("/users") {
    new AsyncResult() {
      override val is = twitchService
        .fetchTwitchconProfiles
        .map { value =>
          Json.stringify(
            Json.toJson(value)
          )
        }
    }
  }

  get("/update") {
    new AsyncResult() {
      override val is: Future[_] = twitchService.updateDatabase()
    }
  }
}
