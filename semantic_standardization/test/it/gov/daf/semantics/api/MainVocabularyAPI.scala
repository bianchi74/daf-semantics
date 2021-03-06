package it.gov.daf.semantics.api

import org.slf4j.LoggerFactory
import play.Logger
import com.typesafe.config.ConfigFactory
import it.almawave.linkeddata.kb.repo.utils.ConfigHelper
import it.almawave.linkeddata.kb.utils.JSONHelper

object MainVocabularyAPI extends App {

  val params = Map("lang" -> "it")

  val logger = Logger.underlying()

  val ontofactory = new VocabularyAPIFactory()

  val TEST_CONFIG = ConfigHelper.injectParameter(VocabularyAPIFactory.DEFAULT_CONFIG, "data_dir", "./dist/data")

  ontofactory.config(TEST_CONFIG)

  ontofactory.start()

  println("ITEMS: " + ontofactory.items)

  val ontoapi = ontofactory.items("Istat-Classificazione-08-Territorio")

  val json_tree = ontoapi.extract_data(params)
  //  val json = JSONHelper.writeToString(json_tree)
  //  logger.debug(json)

  val results = json_tree.map {
    _.toList.map { item =>
      ("key" -> item._1, "value" -> item._2.toString())
    }.toSeq
  }.toSeq.slice(0, 2)

  val json_results = JSONHelper.writeToString(results)
  logger.debug(json_results)

  val keys = ontoapi.extract_keys(params)
  logger.debug("\n\nKEYS: {}", keys.mkString(" | "))

  ontofactory.stop()

}

