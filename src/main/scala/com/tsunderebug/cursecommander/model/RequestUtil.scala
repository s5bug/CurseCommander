package com.tsunderebug.cursecommander.model

import java.io.{BufferedReader, File, FileOutputStream, InputStreamReader}
import java.net.{HttpURLConnection, URI}
import java.util.stream.Collectors

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object RequestUtil {

  val userAgent = "Mozilla/5.0 CurseCommander/0.1"

  def request(uri: URI): Document = {
    println(s"Requesting ${uri.toString}")
    val urlConn = uri.toURL.openConnection().asInstanceOf[HttpURLConnection]
    urlConn.addRequestProperty("User-Agent", userAgent)
    val content = new BufferedReader(new InputStreamReader(urlConn.getInputStream)).lines.collect(Collectors.joining("\n"))
    println("got content")
    val parsed = Jsoup.parse(content, uri.toString)
    println("parsed")
    parsed
  }

  def downloadFileTo(link: URI, cmf: File): Unit = {
    println(link.toString)
    val urlConn = link.toURL.openConnection().asInstanceOf[HttpURLConnection]
    urlConn.addRequestProperty("User-Agent", userAgent)
    val input = urlConn.getInputStream
    val output = new FileOutputStream(cmf)
    val buffer = new Array[Byte](1024)
    var bytesRead = 0
    bytesRead = input.read(buffer)
    while (bytesRead != -1) {
      output.write(buffer, 0, bytesRead)
      bytesRead = input.read(buffer)
    }
    input.close()
    output.close()
  }

}
