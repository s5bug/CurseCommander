package com.tsunderebug.cursecommander.model

import java.net.URI

import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer

case class ModSearchResult(name: String, id: String, smallDesc: String) extends Comparable[ModSearchResult] {

  def link: URI = new URI(s"https://minecraft.curseforge.com/projects/$id")
  def files: Traversable[ModFile] = {
    val totalFiles: ListBuffer[ModFile] = ListBuffer()
    val pdoc = RequestUtil.request(new URI(s"https://minecraft.curseforge.com/projects/$id/files"))
    val totalPages = Math.max(1, pdoc.select(".listing-header .b-pagination-list > .b-pagination-item").size() - 1)
    (1 to totalPages).foreach((pageNum) => {
      Thread.sleep(200) // No DoS here folks
      val doc = RequestUtil.request(new URI(s"https://minecraft.curseforge.com/projects/$id/files?page=$pageNum"))
      val listContainer = doc.select(".listing > tbody").first()
      totalFiles ++= listContainer.children().asScala.map {
        e =>
          val nameContainer = e.children().select(".project-file-name .project-file-name-container").first()
          val nameDiv = nameContainer.children().first()
          val name = nameDiv.html
          val link = nameDiv.attr("abs:href")
          val versionDiv = e.children().select(".project-file-game-version .version-label").first()
          val version = versionDiv.html
          ModFile(name, new URI(link), version)
      }
    })
    totalFiles
  }
  def latest: ModFile = files.head
  def latest(mcVersion: String): ModFile = files.filter(_.mcVersion == mcVersion).head

  def dependencies: Traversable[ModSearchResult] = {
    val depencyDoc = RequestUtil.request(new URI(s"https://minecraft.curseforge.com/projects/$id/relations/dependencies?filter-related-dependencies=3"))
    val listing = depencyDoc.select(".listing-body .listing").first()
    listing.children().asScala.filter(_.hasClass("project-list-item")).map {
      l =>
        val namelink = l.select(".details .name .name-wrapper a").first()
        val id = namelink.attr("abs:href").drop(42)
        val name = namelink.html
        val descp = l.select(".details .description p").first()
        val desc = descp.html
        ModSearchResult(name, id, desc)
    }
  }

  def dependents: Traversable[ModSearchResult] = {
    val depencyDoc = RequestUtil.request(new URI(s"https://minecraft.curseforge.com/projects/$id/relations/dependents?filter-related-dependents=3"))
    val listing = depencyDoc.select(".listing-body .listing").first()
    listing.children().asScala.filter(_.hasClass("project-list-item")).map {
      l =>
        val namelink = l.select(".details .name .name-wrapper a").first()
        val id = namelink.attr("abs:href").drop(42)
        val name = namelink.html
        val descp = l.select(".details .description p").first()
        val desc = descp.html
        ModSearchResult(name, id, desc)
    }
  }

  override def equals(obj: scala.Any): Boolean = {
    obj match {
      case msr: ModSearchResult => msr.id == this.id
      case _ => false
    }
  }

  override def compareTo(o: ModSearchResult): Int = {
    this.name.compare(o.name)
  }

}
