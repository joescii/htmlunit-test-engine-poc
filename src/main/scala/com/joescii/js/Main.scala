package com.joescii.js

import java.io.{InputStreamReader, BufferedReader}
import java.net.URL

import com.gargoylesoftware.htmlunit.html.HtmlPage
import com.gargoylesoftware.htmlunit.{ScriptException, BrowserVersion, WebClient}
import net.sourceforge.htmlunit.corejs.javascript.{ScriptableObject, Function => JsFunction}

object Main extends App {
  type JavaScript = (String, String) // Name, script contents

  def urlFor(resource:String):URL = this.getClass.getClassLoader.getResource(resource)
  def readJs(path:String):JavaScript = {
    val r = new BufferedReader(new InputStreamReader(urlFor(path).openStream()))
    (path, Stream.continually(r.readLine()).takeWhile(_ != null).mkString("\n"))
  }

  val jasmine = readJs("js/jasmine.js")
  val boot = readJs("js/boot.js")
  val htmlUnitBoot = readJs("js/htmlunit_boot.js")
  val console = readJs("js/console.js")
  val tests = readJs("js/tests.js")

  val client = new WebClient(BrowserVersion.CHROME)
  val options = client.getOptions()
  options.setHomePage(WebClient.URL_ABOUT_BLANK.toString())
  options.setJavaScriptEnabled(true)

  client.getPage("file://C:/code/htmlunit-test-engine-poc/src/main/resources/blank.html")

  val window = client.getCurrentWindow().getTopWindow
  val page:HtmlPage = window.getEnclosedPage().asInstanceOf[HtmlPage] // asInstanceOf because ... java...

  def run(js:JavaScript):Unit = {
    val (name, script) = js
    println(s"Running $name...")
    val toRun = "function() {\n"+script+"\n};"
    val result = page.executeJavaScript(toRun)
    val func:JsFunction = result.getJavaScriptResult().asInstanceOf[JsFunction]

//    try {
      page.executeJavaScriptFunctionIfPossible(
        func,
        window.getScriptObject().asInstanceOf[ScriptableObject],
        Array.empty,
        page.getDocumentElement())
//    } catch {
//      case e:ScriptException => println(e.getScriptSourceCode)
//    }
  }

  run(jasmine)
  run(console)
  run(htmlUnitBoot)
  run(tests)
  run(("runner", "jasmine.getEnv().execute();"))
}

