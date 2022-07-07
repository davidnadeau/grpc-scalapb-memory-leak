package com.testing.helloworld

import java.util.concurrent.TimeUnit
import com.testing.helloworld.helloworld.GreeterGrpc.GreeterStub
import com.testing.helloworld.helloworld.{GreeterGrpc, HelloReply, HelloRequest}
import io.grpc.{ManagedChannel, ManagedChannelBuilder}

import scala.concurrent.{ExecutionContext, Future}

object HelloWorldClient {
  def main(args: Array[String]): Unit = {
    val channel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build
    val stub = GreeterGrpc.stub(channel)
    val client = new HelloWorldClient(channel, stub)

    sys.addShutdownHook {
      client.shutdown()
    }

    while (true) {
      client.greet().recover { case t: Throwable =>
        println(t.getMessage)
      }(ExecutionContext.global)
      Thread.sleep(1)
    }
  }
}

class HelloWorldClient(channel: ManagedChannel, asyncStub: GreeterStub) {
  def shutdown(): Unit = {
    channel.shutdown.awaitTermination(5, TimeUnit.SECONDS)
  }

  def greet(): Future[HelloReply] = {
    val configuredStub = asyncStub.withDeadlineAfter(10, TimeUnit.MILLISECONDS)
    val payload = "a" * 100000
    val request = HelloRequest(name = payload)
    configuredStub.sayHello(request)
  }
}