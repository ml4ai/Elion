package org.ml4ai.elion

case class GroundingID(database:String, id:String)

case class CMURow(controller:String, controllerId:GroundingID, controlled:String, controlledId:GroundingID)
