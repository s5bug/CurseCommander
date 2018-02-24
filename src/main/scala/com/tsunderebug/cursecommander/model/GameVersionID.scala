package com.tsunderebug.cursecommander.model

import java.net.URI

import org.jsoup.nodes.Element

trait GameVersionID {

  def key: String
  def name: String

}

object GameVersionID {

  def all: Traversable[GameVersionID] = {
    val mp = RequestUtil.request(new URI("https://minecraft.curseforge.com/mc-mods"))
    val vs = mp.select("#filter-game-version").first()
    val all = vs.children().toArray.map(_.asInstanceOf[Element]).toSeq.filter(_.html.startsWith("&nbsp;&nbsp;")).map {
      e =>
        val lname = e.html.drop(12)
        val lkey = e.attr("value")
        println(s"parsed version entry $lname, $lkey")
        new GameVersionID {
          override def name: String = lname
          override def key: String = lkey
        }
    }
    println("completed all")
    all
  }
  def latestStable: GameVersionID = {
    val ls = all.filter(!_.name.endsWith("-Snapshot")).head
    println(s"ls is ${ls.name}")
    ls
  }

}
