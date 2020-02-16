package com.example.app

import com.twitchconlistservice.app.Application
import org.scalatra.test.scalatest._

class ApplicationTests extends ScalatraFunSuite {

  addServlet(classOf[Application], "/*")

  test("GET / on MyScalatraServlet should return status 200") {
    get("/") {
      status should equal (200)
    }
  }

}
