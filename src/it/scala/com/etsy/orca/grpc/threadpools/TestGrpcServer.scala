package com.etsy.orca.grpc.threadpools

import com.etsy.core.grpc.threadpools.chat.{
  ChatMessage,
  ChatServiceGrpc,
  GreetRequest
}
import io.grpc._

import java.util.logging.Logger
import scala.concurrent.{ExecutionContext, Future}

object TestGrpcServer {
  private val logger = Logger.getLogger(classOf[TestGrpcServer].getName)

  def main(args: Array[String]): Unit = {
    val server = new TestGrpcServer(ExecutionContext.global)
    server.start()
    server.blockUntilShutdown()
  }
}

class TestGrpcServer(executionContext: ExecutionContext) {
  var server: Server = _

  def start(): Unit = {
    server = ServerBuilder
      .forPort(50051)
      .addService(
        ChatServiceGrpc.bindService(new GreeterImpl, executionContext)
      )
      .build
      .start
    TestGrpcServer.logger.info("Server started, listening on " + 50051)
    sys.addShutdownHook {
      stop()
    }
  }

  private def stop(): Unit = {
    if (server != null) {
      server.shutdown()
    }
  }

  private def blockUntilShutdown(): Unit = {
    if (server != null) {
      server.awaitTermination()
    }
  }

  private class GreeterImpl extends ChatServiceGrpc.ChatService {
    override def greet(req: GreetRequest): Future[ChatMessage] = {
      val reply = ChatMessage(
        username = req.username,
        data = req.name,
        time = System.currentTimeMillis()
      )
      Future.successful(reply)
    }
  }
}
