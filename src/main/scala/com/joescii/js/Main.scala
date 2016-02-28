package com.joescii.js

import java.io.{InputStreamReader, BufferedReader}
import java.net.URL

import com.gargoylesoftware.htmlunit.html.HtmlPage
import com.gargoylesoftware.htmlunit.{BrowserVersion, WebClient}
import net.sourceforge.htmlunit.corejs.javascript.{ScriptableObject, Function => JsFunction}

object Main extends App {
  def urlFor(resource:String):URL = this.getClass.getClassLoader.getResource(resource)
  def readJs(path:String):String = {
    val r = new BufferedReader(new InputStreamReader(urlFor(path).openStream()))
    Stream.continually(r.readLine()).takeWhile(_ != null).mkString("\n")
  }

  val jasmine = readJs("js/jasmine.js")

  val client = new WebClient(BrowserVersion.CHROME)
  val options = client.getOptions()
  options.setHomePage(WebClient.URL_ABOUT_BLANK.toString())
  options.setJavaScriptEnabled(true)

  client.getPage("file://C:/code/htmlunit-test-engine-poc/src/main/resources/blank.html")

  val window = client.getCurrentWindow().getTopWindow
  val page:HtmlPage = window.getEnclosedPage().asInstanceOf[HtmlPage] // asInstanceOf because ... java...

  def run(js:String):Unit = {
    val toRun = "function() {"+js+"\n};"
    val result = page.executeJavaScript(js)
    val func:JsFunction = result.getJavaScriptResult().asInstanceOf[JsFunction]

    page.executeJavaScriptFunctionIfPossible(
      func,
      window.getScriptObject().asInstanceOf[ScriptableObject],
      Array.empty,
      page.getDocumentElement())
  }

  run(jasmine)
}

