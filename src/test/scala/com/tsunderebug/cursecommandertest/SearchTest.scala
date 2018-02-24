package com.tsunderebug.cursecommandertest

import com.tsunderebug.cursecommander.model.{ModSearchResult, Search}

object SearchTest {

  def main(args: Array[String]): Unit = {
    var i = 0
    println(Search().foldLeft(Seq[ModSearchResult]())((acc, t) => {
      i = i + 1
      println(s"$i/107")
      Thread.sleep(200)
      acc ++ t
    }))
  }

}
