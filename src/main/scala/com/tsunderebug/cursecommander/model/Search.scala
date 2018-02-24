package com.tsunderebug.cursecommander.model

import java.net.URI

import scala.collection.JavaConverters._

object Search {

  def apply(gameVersionID: GameVersionID = GameVersionID.latestStable, sort: SortType = SortType.Name): Stream[Traversable[ModSearchResult]] = {
    val firstPage = RequestUtil.request(new URI(s"https://minecraft.curseforge.com/mc-mods?filter-game-version=${gameVersionID.key}&filter-sort=${sort.name}"))
    val numPages = firstPage.select("li.b-pagination-item > a.b-pagination-item").last().html.toInt
    Stream.range(1, numPages + 1).map {
      p =>
        val page = RequestUtil.request(new URI(s"https://minecraft.curseforge.com/mc-mods?filter-game-version=${gameVersionID.key}&filter-sort=${sort.name}&page=$p"))
        val listing = page.select(".project-list .listing-body .listing").first()
        listing.children().asScala.map {
          l =>
            val namelink = l.select(".details .name .name-wrapper a").first()
            val id = namelink.attr("href").drop(10)
            val name = namelink.html
            val descp = l.select(".details .description p").first()
            val desc = descp.html
            ModSearchResult(name, id, desc)
        }
    }
  }

  def getAllSafeJ(gameVersionID: GameVersionID = GameVersionID.latestStable, sortType: SortType = SortType.Name, onUpdate: java.util.function.Consumer[java.util.List[ModSearchResult]]): java.util.List[ModSearchResult] = {
    apply(gameVersionID, sortType).foldLeft(Seq[ModSearchResult]())((acc, p) => {
      Thread.sleep(200) // No DoS here folks
      val t = acc ++ p
      try {
        onUpdate.accept(t.asJava)
      } catch {
        case e: Exception => e.printStackTrace()
      }
      t
    }).asJava
  }

}

trait SortType {

  def name: String

}

object SortType {

  object Name extends SortType {

    override def name: String = "name"

  }

}
