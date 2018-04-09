package com.tsunderebug.cursecommandertest

import java.io.File
import java.net.URI

import com.tsunderebug.cursecommander.model.RequestUtil

object SearchTest {

  def main(args: Array[String]): Unit = {
    RequestUtil.downloadFileTo(new URI("https://minecraft.curseforge.com/projects/pams-harvestcraft/files/2533991/download"), new File("dab.jar"))
  }

}
