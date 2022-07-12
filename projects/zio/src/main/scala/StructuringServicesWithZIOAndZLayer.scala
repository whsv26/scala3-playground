package org.whsv26.playground.zio

import StructuringServicesWithZIOAndZLayer.UserDb.UserDbEnv
import StructuringServicesWithZIOAndZLayer.UserEmailer.UserEmailerEnv
import StructuringServicesWithZIOAndZLayer.UserSubscription.UserSubscriptionEnv
import zio.clock.Clock
import zio.console.*
import zio.duration.*
import zio.{Exit, ExitCode, Has, RIO, Task, UIO, ULayer, URIO, URLayer, ZIO, ZLayer}

object StructuringServicesWithZIOAndZLayer extends zio.App {

  // ZIO[-R, +E, +A]
  // R => Either[E, A]

  val meaningOfLife = ZIO.succeed(42)
  val aFailure = ZIO.fail("Something went wrong")

  val greeting = for {
    _ <- putStrLn("Hi, what's your name?")
    name <- getStrLn
    _ <- putStrLn(s"Hello and welcome, $name")
  } yield ()

  case class User(name: String, email: String)

  object UserEmailer {
    type UserEmailerEnv = Has[UserEmailer.Service]

    // Service definition
    trait Service {
      def notify(user: User, message: String): Task[Unit]
    }

    // Service implementation
    val live: ULayer[UserEmailerEnv] =
      ZLayer.succeed(new Service {
        override def notify(user: User, message: String): Task[Unit] = Task {
          println(s"[User emailer] Sending '$message' to ${user.email}")
        }
      })

    // Front-facing API
    def notifyUser(user: User, message: String): RIO[UserEmailerEnv, Unit] = {
      ZIO.accessM { hasService =>
        val service: Service = hasService.get
        service.notify(user, message)
      }
    }
  }

  object UserDb {
    type UserDbEnv = Has[UserDb.Service]

    trait Service {
      def insert(user: User): Task[Unit]
    }

    val live = ZLayer.succeed(new Service {
      override def insert(user: User): Task[Unit] = Task {
        println(s"[Database] Insert into public.user values ('${user.email}')")
      }
    })

    def insert(user: User): RIO[UserDbEnv, Unit] = {
      ZIO.accessM(_.get.insert(user))
    }
  }

  // HORIZONTAL composition
  // ZLayer[In1, E1, Out1] ++ ZLayer[In2, E2, Out2] => ZLayer[In1 with In2, super(E1, E2), Out1 with Out2]

  val userBackendLayer: ULayer[UserDbEnv with UserEmailerEnv] =
    UserDb.live ++ UserEmailer.live

  // VERTICAL composition
  // Kinda like functional composition
  // ZLayer[In1, E1, Out1] >>> ZLayer[Out1, E2, Out2] => ZLayer[In1, super(E1, E2), Out2]

  object UserSubscription {
    type UserSubscriptionEnv = Has[UserSubscription.Service]

    class Service(notifier: UserEmailer.Service, userDb: UserDb.Service) {
      def subscribe(user: User): Task[User] = for {
        _ <- userDb.insert(user)
        _ <- notifier.notify(user, s"Welcome ${user.name}! We have nice ZIO content for you")
      } yield user
    }

    val live: URLayer[UserEmailerEnv with UserDbEnv, UserSubscriptionEnv] =
      ZLayer.fromServices[
        UserEmailer.Service,
        UserDb.Service,
        UserSubscription.Service
      ] { (userEmailer, userDb) =>
        new Service(userEmailer, userDb)
      }

    def subscribe(user: User): RIO[UserSubscriptionEnv, User] = {
      ZIO.accessM(_.get.subscribe(user))
    }
  }

  val userSubscriptionLayer: ULayer[UserSubscriptionEnv] =
    userBackendLayer >>> UserSubscription.live

  val whsv26 = User("Alex", "whsv26@gmai.com")
  val message = "Welcome!"

  def notifyWhsv26() = {
    UserEmailer.notifyUser(whsv26, message) // effect
      .provideLayer(userBackendLayer) // provide input for effect
      .exitCode
  }

  def run(args: List[String]) = {
    UserSubscription.subscribe(whsv26)
      .provideLayer(userSubscriptionLayer)
      .exitCode
  }
}
