package com.joescii.js

import java.io.{InputStreamReader, BufferedReader}
import java.net.URL

import com.gargoylesoftware.htmlunit.html.HtmlPage
import com.gargoylesoftware.htmlunit.{WebConsole, ScriptException, BrowserVersion, WebClient}
import net.sourceforge.htmlunit.corejs.javascript.{ScriptableObject, Function => JsFunction}

object Main extends App {
  type JavaScript = (String, String) // Name, script contents

  def urlFor(resource:String):URL = this.getClass.getClassLoader.getResource(resource)
  def readJs(path:String):JavaScript = {
    val r = new BufferedReader(new InputStreamReader(urlFor(path).openStream()))
    (path, Iterator.continually(r.readLine()).takeWhile(_ != null).mkString("\n"))
  }

  val jasmine = readJs("js/jasmine.js")
  val boot = readJs("js/boot.js")
  val htmlUnitBoot = readJs("js/htmlunit_boot.js")
  val console = readJs("js/console.js")

  val angular = readJs("angular/angular.js")
  val angularMocks = readJs("angular/angular-mocks.js")


  val app = readJs("angular/sample-app.js")
  val appTests = readJs("angular/sample-test.js")
  val tests = readJs("js/tests.js")

  val client = new WebClient(BrowserVersion.CHROME)
  val options = client.getOptions()
  options.setHomePage(WebClient.URL_ABOUT_BLANK.toString())
  options.setJavaScriptEnabled(true)

  val sep = System.getProperty("file.separator")
  val cd = new java.io.File(".").getCanonicalPath.replaceAllLiterally(sep, "/")
  val blankHtml = s"file://$cd/src/main/resources/blank.html"
  client.getPage(blankHtml)

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

  val c = new WebConsole()
  c.warn("BLAH")

  run(jasmine)
  run(console)
  run(htmlUnitBoot)
  run(angular)
  run(angularMocks)

  run(app)
  run(appTests)

  run(tests)
  run(("runner", "jasmine.getEnv().execute();"))
}

