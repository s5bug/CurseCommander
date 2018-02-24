package com.tsunderebug.cursecommander.model

import java.io.File
import java.net.URI

case class ModFile(name: String, link: URI, mcVersion: String) {

  def downloadTo(f: File): Unit = {
    val cmf = new File(f, name.replaceAll("""["'|\\]""", "").replaceAll(" +", "_") + ".jar")
    RequestUtil.downloadFileTo(new URI(link.toString + "/download"), cmf)
  }
  def children: Traversable[ModFile] = ???

}
