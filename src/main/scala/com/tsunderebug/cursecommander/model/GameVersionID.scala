package com.tsunderebug.cursecommander.model

trait GameVersionID {

  def name: String

}

object GameVersionID {

  def all: Traversable[GameVersionID] = ???
  def latestStable: GameVersionID = all.filter(!_.name.endsWith("-Snapshot")).head

}
