import akka.actor.ActorSystem
import com.twitchconlistservice.app._
import com.twitchconlistservice.app.Application
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  val system = ActorSystem()

  override def init(context: ServletContext) {
    context.mount(new Application(system), "/*")
  }

  override def destroy(context: ServletContext): Unit = {
    system.terminate()
  }
}
