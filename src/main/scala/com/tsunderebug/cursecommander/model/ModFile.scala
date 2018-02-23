package com.tsunderebug.cursecommander.model

import java.io.File

case class ModFile(name: String, link: String, mcVersion: String, children: Traversable[ModFile]) {

  def downloadTo(f: File): Unit = ???

}
