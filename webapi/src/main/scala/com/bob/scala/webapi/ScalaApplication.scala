package com.bob.scala.webapi

import java.time.LocalDateTime

import com.bob.java.webapi.handler.MdcPropagatingOnScheduleAction
import com.bob.scala.webapi.controller.User
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration
import org.springframework.boot.{CommandLineRunner, SpringApplication}
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.{GetMapping, ResponseBody}
import rx.plugins.RxJavaHooks
import springfox.documentation.spring.web.json.JsonSerializer

/**
  * Created by bob on 16/2/16.
  */
object ScalaApplication extends App {

  RxJavaHooks.setOnScheduleAction(new MdcPropagatingOnScheduleAction)

  /**
    * args: _ *:此标注告诉编译器把args中的每个元素当作参数，而不是当作一个当一的参数传递
    */
  SpringApplication.run(classOf[SampleConfig], args: _ *)
}

class ScalaApplication {

}

@SpringBootApplication(exclude = Array(classOf[ThymeleafAutoConfiguration]))
@Controller
@ComponentScan(value = Array(
  "com.bob.scala.*", "com.bob.java.webapi.*"
))
@EnableAsync
class SampleConfig extends CommandLineRunner {

  @Autowired
  var objectMapper: ObjectMapper = _

  /**
    * 只有使用swagger的基础上才能导入此实例
    */
  @Autowired
  val jsonSerializer: JsonSerializer = null

  @GetMapping(value = Array("/"))
  def index(model: Model): String = {
    model.addAttribute("now", LocalDateTime.now())
    "index"
  }

  @GetMapping(value = Array("properties"))
  @ResponseBody
  def properties(): java.util.Properties = {
    System.getProperties()
  }

  override def run(args: String*): Unit = {
    val aUser = new User("c", 4, "a44", 4)
    println(objectMapper.writeValueAsString(aUser))
    println(jsonSerializer.toJson(aUser).value())
    val map = Map("message" -> "fucktest")
    println(objectMapper.writeValueAsString(map))
  }
}