/*
 * Copyright (c) 2018. Yuriy Stul
 */

package com.stulsoft.pvertx.scala

import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.Vertx
import org.slf4j.LoggerFactory

import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * @author Yuriy Stul
  */
class JobVerticle extends ScalaVerticle {
  private val logger = LoggerFactory.getLogger(classOf[JobVerticle])

  override def stopFuture(): Future[_] = {
    logger.info("Stopping")
    Future.successful()
  }

  override def startFuture(): Future[_] = {
    logger.info("Stopping")
    super.startFuture()
//    Future.failed(new RuntimeException("test error"))
  }
}

object JobVerticle extends App {
  private val logger = LoggerFactory.getLogger(classOf[JobVerticle])

  import scala.concurrent.ExecutionContext.Implicits.global

  var vertx = Vertx.vertx
  vertx.deployVerticleFuture(ScalaVerticle.nameForVerticle[JobVerticle])
    .onComplete {
      case Success(result) =>
        logger.info("Success, verticle ID is {}", result)
        vertx.setTimer(1000, l => vertx.close)
      case Failure(t) =>
        logger.error("Failure {}", t.getMessage)
        vertx.close()
    }
}