package org.ml4ai.elion.utils

import java.io.{File, FilenameFilter}

import com.typesafe.scalalogging.LazyLogging
import org.clulab.utils.Serializer
import org.ml4ai.elion.{CMUParser, CMURow}

import scala.util.{Success, Try}

object IndexBuilder extends App with LazyLogging {

  val path = "/Volumes/My Passport/cmu"

  val dir = new File(path)

  if(dir.isDirectory){
    val files = dir.listFiles(new FilenameFilter {
      override def accept(dir: File, name: String): Boolean = {
        val n = name.toLowerCase
        n.endsWith("tsv") && n.contains("pmc")
      }
    })

    logger.info(s"Building Elion index from directory $path containing ${files.length} tsv files")

    val PMCID = """PMC\d+""".r

    val rowsPerFile =
      files.zipWithIndex map {
        case (file, ix) =>
          {
            if(ix %1000 == 0)
              logger.info(s"Processed ${ix} files")

            val pmcid = PMCID findFirstIn file.getName match {
              case Some(id) => id
              case None =>
                throw new UnsupportedOperationException("All files should have their PMCID in their name")
            }

            val rows = CMUParser.parseFile(file)

            val tuples = rows map rowToTuples

            (pmcid, tuples)
          }
      }

//    val inverseIndex:Map[String, Set[String]] =
//      rowsPerFile groupBy(_._1) mapValues {
//          _.flatMap{
//            case (_, entityPairs) =>
//              entityPairs flatMap {
//                case (controller, controlled) =>
//                  Set(controller, controlled)
//              }
//          }.toSet
//      }

//    val entitiesPerPaper = rowsPerFile map {
//      case ()
//    }


    val entity_pmcid = rowsPerFile flatMap {
      case (pmcid, entityPairs) =>
        entityPairs flatMap {
          case (controller, controlled) =>
            Seq(controller -> pmcid, controlled -> pmcid)
        }
    }

    val index:Map[String, List[String]] = entity_pmcid.groupBy{ case (entity, _) => entity}.mapValues(_.map(_._2).toList).map(identity)

    Serializer.save(index, "index.ser")

  }
  else
    logger.error(s"$path is not a directory")


  def rowToTuples(row:CMURow):(String, String) = (row.controllerId.id, row.controlledId.id)


}
