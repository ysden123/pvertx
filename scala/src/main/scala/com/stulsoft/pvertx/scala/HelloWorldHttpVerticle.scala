/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.scala


import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.Vertx
import org.slf4j.LoggerFactory

import scala.util.{Failure, Success}

/**
  * @author Yuriy Stul
  */
class HelloWorldHttpVerticle extends ScalaVerticle {
  private val logger = LoggerFactory.getLogger(HelloWorldHttpVerticle.getClass)

  override def start(): Unit = {
    logger.info("Staring")
//    throw new RuntimeException("test error")
    super.start()
  }

  override def stop(): Unit = {
    logger.info("Stopping")
    super.stop()
  }
}

object HelloWorldHttpVerticle extends App {
  private val logger = LoggerFactory.getLogger(HelloWorldHttpVerticle.getClass)
  var vertx = Vertx.vertx
  //  vertx.deployVerticle(s"scala:${classOf[HelloWorldHttpVerticle].getName}") // by name
  //  vertx.deployVerticle(ScalaVerticle.nameForVerticle[HelloWorldHttpVerticle]) // by name with helper

  import scala.concurrent.ExecutionContext.Implicits.global

  // Deploying with future
  vertx.deployVerticleFuture(ScalaVerticle.nameForVerticle[HelloWorldHttpVerticle])
    .onComplete {
      case Success(result) =>
        logger.info("Success, verticle ID is {}", result)
        vertx.setTimer(1000, l => vertx.close)
      case Failure(t) => logger.error("Failure {}", t.getMessage)
    }
}