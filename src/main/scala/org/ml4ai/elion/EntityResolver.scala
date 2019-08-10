package org.ml4ai.elion

import org.clulab.reach.ReachSystem
import org.clulab.reach.grounding.KBResolution

object EntityResolver extends App {
  val reachSystem = new ReachSystem()

  def groundingID(entity:String): Seq[KBResolution] = {
    val doc = reachSystem.procAnnotator.annotate(entity)
    val entities = reachSystem.extractEntitiesFrom(doc)

    entities map  (
      e => e.grounding()
    ) collect { case Some(gr) => gr }
  }

//  val groundings = groundingID("human liver")
//
//  groundings foreach {
//    gr =>
//      println(gr.nsId)
//  }

}
