package de.kp.elastic.insight.io
/* Copyright (c) 2014 Dr. Krusche & Partner PartG
* 
* This file is part of the Elastic-Insight project
* (https://github.com/skrusche63/elastic-insight).
* 
* Elastic-Insight is free software: you can redistribute it and/or modify it under the
* terms of the GNU General Public License as published by the Free Software
* Foundation, either version 3 of the License, or (at your option) any later
* version.
* 
* Elastic-Insight is distributed in the hope that it will be useful, but WITHOUT ANY
* WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
* A PARTICULAR PURPOSE. See the GNU General Public License for more details.
* You should have received a copy of the GNU General Public License along with
* Elastic-Insight. 
* 
* If not, see <http://www.gnu.org/licenses/>.
*/

import de.kp.spark.core.Names
import de.kp.spark.core.model._

import de.kp.elastic.insight.model._
import de.kp.elastic.insight.exception.AnalyticsException

import scala.collection.mutable.HashMap
import scala.collection.JavaConversions._

class StatusRequestBuilder extends RequestBuilder {

  override def build(params:Map[String,Any]):ServiceRequest = {
        
    val service = params("service").asInstanceOf[String]
    if (Services.isService(service) == false) {
      throw new AnalyticsException("No <service> found.")
      
    }
    
    /* Build request data */
    val data = HashMap.empty[String,String]   

    data += Names.REQ_UID -> params(Names.REQ_UID).asInstanceOf[String]
    data += Names.REQ_SITE -> params(Names.REQ_SITE).asInstanceOf[String]
 
    val topics = List("latest","all")

    val subject = params("subject").asInstanceOf[String]
    if (topics.contains(subject) == false) {
      throw new AnalyticsException("No <subject> found.")
      
    }
    val task = "status:" + subject
    new ServiceRequest(service,task,data.toMap)
    
  }

}