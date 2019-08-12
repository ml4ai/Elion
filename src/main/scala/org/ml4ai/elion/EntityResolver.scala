package org.ml4ai.elion

import org.clulab.reach.ReachSystem
import org.clulab.reach.grounding.KBResolution
import org.ml4ai.elion.utils.IndexLoader

object EntityResolver extends App {
  val reachSystem = new ReachSystem()
  val index = IndexLoader.loadFromConfig

  def groundingID(entity:String): Seq[KBResolution] = {
    val doc = reachSystem.procAnnotator.annotate(entity)
    val entities = reachSystem.extractEntitiesFrom(doc)

    entities map  (
      e => e.grounding()
    ) collect { case Some(gr) => gr }
  }

  def findFilesByGrounding(id:String): Seq[String] = index.getOrElse(id, Nil)

  def findFilesByText(text:String):Map[String, Seq[String]] = {
    val krs = groundingID(text)
    krs map {
      kr =>
        kr.id -> findFilesByGrounding(kr.id)
    } toMap
  }

//  val groundings = groundingID("human liver")
//
//  groundings foreach {
//    gr =>
//      println(gr.nsId)
//  }

//  println(findFilesByText("MAPK"))

}
