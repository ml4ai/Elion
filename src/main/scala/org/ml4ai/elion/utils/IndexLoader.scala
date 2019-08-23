package org.ml4ai.elion.utils

import com.typesafe.config.ConfigFactory
import org.clulab.utils.Serializer

/**
 * De-serializes an index built by the IndexBuilder singleton
 */
object IndexLoader {

  def load(path:String):Map[String, List[String]] = {
    Serializer.load[Map[String, List[String]]](path)
  }

  def loadFromConfig: Map[String, List[String]] = {
    val conf = ConfigFactory.load().getConfig("indexLoader")
    val path = conf.getString("path")
    load(path)
  }

  def cleanIndex(index:Map[String,  List[String]]): Map[String, List[String]] = {
    index.toSeq.withFilter{
      case (key, _) =>
        !key.startsWith("UAZ")
    }.flatMap{
      case (key, vals) =>
        val newKeys = key.replaceAll("""[{}]""", "").replaceAll("""\.\w+$""", "").split(",")
        for(newKey <- newKeys) yield newKey.trim -> vals
    }.groupBy{
      case (key, _) => key
    }.mapValues{
      elems =>
        elems.flatMap{
          case (_, vals) => vals
        }.toSet.toList
    }
  }
}
