package com.tsunderebug.cursecommander.model

object Search {

  def apply(gameVersionID: GameVersionID = GameVersionID.latestStable, sort: SortType = SortType.Name): Stream[Traversable[ModSearchResult]] = ???

}

trait SortType {

  def name: String

}

object SortType {

  object Name extends SortType {

    override def name: String = "name"

  }

}
