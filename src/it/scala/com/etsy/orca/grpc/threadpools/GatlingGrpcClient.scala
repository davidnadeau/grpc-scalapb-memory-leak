package com.etsy.orca.grpc.threadpools

import com.etsy.core.grpc.threadpools.chat.{ChatServiceGrpc, GreetRequest}
import com.github.phisgr.gatling.grpc.Predef._
import io.gatling.core.Predef._
import io.gatling.core.session.{Expression, expressionSeq2SeqExpression}
import io.grpc.ManagedChannelBuilder

import java.util.concurrent.{Executor, Executors}
import scala.concurrent.ExecutionContext

class GatlingGrpcClient extends Simulation {
  new TestGrpcServer(ExecutionContext.global).start()

  val executor = Executors.newSingleThreadExecutor()

  ExecutionContext.global
  val channel = ManagedChannelBuilder
    .forAddress("localhost", 50051)
    .executor(executor)
//    .directExecutor()
    .usePlaintext()

  val grpcConf =
    grpc(channel)
      .warmUpCall(ChatServiceGrpc.METHOD_GREET, GreetRequest.defaultInstance)

  val greetPayload: Expression[GreetRequest] =
    GreetRequest(name = "World", username = "a" * 100000)

  val successfulCall = grpc("Success")
    .rpc(ChatServiceGrpc.METHOD_GREET)
    .payload(greetPayload)
    .extract(_.data.split(' ').headOption)(_ saveAs "s")

  val s = scenario("test_grpc")
    .repeat(3000) {
      exec(successfulCall)
    }

  setUp(
    s.inject(atOnceUsers(54))
  ).protocols(grpcConf)
}
