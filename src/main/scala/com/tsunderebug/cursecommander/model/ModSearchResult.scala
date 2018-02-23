package com.tsunderebug.cursecommander.model

import java.net.URI

case class ModSearchResult(name: String, id: String, smallDesc: String) {

  def link: URI = ???
  def files: Traversable[ModFile] = ???
  def latest: ModFile = files.head
  def latest(mcVersion: String): ModFile = files.filter(_.mcVersion == mcVersion).head

}
