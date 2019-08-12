package org.ml4ai.elion.utils

import com.typesafe.config.ConfigFactory
import org.clulab.utils.Serializer

/**
 * De-serializes an index built by the IndexBuilder singleton
 */
object IndexLoader {

  def loadIndex(path:String):Map[String, List[String]] = {
    Serializer.load[Map[String, List[String]]](path)
  }

  def loadIndexFromConfig: Map[String, List[String]] = {
    val conf = ConfigFactory.load().getConfig("indexLoader")
    val path = conf.getString("path")
    loadIndex(path)
  }
}