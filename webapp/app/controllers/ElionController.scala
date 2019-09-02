package controllers

import javax.inject._

import play.api.mvc._

import play.api.libs.json._

import org.ml4ai.elion.EntityResolver

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class ElionController @Inject()(cc: ControllerComponents) (implicit assetsFinder: AssetsFinder)
  extends AbstractController(cc) {

  val resolver = new EntityResolver

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    Ok("Welcome to ElionWeb")
  }

  def entity(name:String) = Action {
    val ret = resolver.findFilesByText(name)
    Ok(Json.toJson(ret))
  }

  def groundingId(id:String) = Action {
    val ret = Map("PMCIDS" -> resolver.findFilesByGrounding(id))
    Ok(Json.toJson(ret))
  }
}
