package org.ml4ai.elion

import com.typesafe.scalalogging.LazyLogging
import org.clulab.reach.ReachSystem
import org.clulab.reach.grounding.KBResolution
import org.ml4ai.elion.utils.IndexLoader

class EntityResolver extends LazyLogging{
  private val reachSystem = new ReachSystem()
  private val index = IndexLoader.cleanIndex(IndexLoader.loadFromConfig)

  def groundingID(entity:String): Seq[KBResolution] = {
    val doc = reachSystem.procAnnotator.annotate(entity)
    val entities = reachSystem.extractEntitiesFrom(doc)

    entities map  (
      e => e.grounding()
    ) collect { case Some(gr) => gr }
  }

  def findFilesByGrounding(id:String): Seq[String] = index.getOrElse(id, Nil) map { PMCID => s"$PMCID-cmu-out.tsv" }

  def findFilesByText(text:String):Map[String, Seq[String]] = {
    val krs = groundingID(text)
    krs map {
      kr =>
        kr.id -> findFilesByGrounding(kr.id)
    } toMap
  }

}
