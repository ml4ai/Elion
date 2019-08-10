package org.ml4ai.elion

import scala.io.Source
import java.io.File

object CMUParser {

  val path = "PMC5879957-cmu-out.tsv"

  def parseFile(file:File):Seq[CMURow] = parseFile(file.getAbsolutePath)

  def parseFile(path:String):Seq[CMURow] = {
    val reader = Source.fromFile(path)

    try {
      val lines = reader.getLines().toList


      val rows = lines match {
        case header :: Nil =>
          Seq.empty
        case header :: entries =>
          val headers = header.split("\t")
          entries map (e => parseRow(headers, e))
        case Nil =>
          Seq.empty
      }

      reader.close()

      rows
    }
      catch {
        case e =>
          Seq.empty
      }
    finally {
      reader.close()
    }
  }

  private def parseRow(headers:Seq[String], raw:String):CMURow = {
    val elements = (headers zip raw.split("\t")).toMap

    val (controlled, controlledId) =
      if(elements("PosReg Name").nonEmpty){
        (elements("PosReg Name"), GroundingID("", elements("PosReg ID")))
      }
      else{
        (elements("NegReg Name"), GroundingID("", elements("NegReg ID")))
      }

    CMURow(elements("Element Name"), GroundingID(elements("Database Name"), elements("Element Identifier")), controlled, controlledId)
  }

}
