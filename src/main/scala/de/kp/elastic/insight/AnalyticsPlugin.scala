package de.kp.elastic.insight
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

import java.util.Collection

import org.elasticsearch.common.collect.Lists
import org.elasticsearch.common.component.LifecycleComponent
import org.elasticsearch.common.inject.Module
import org.elasticsearch.common.settings.Settings

import org.elasticsearch.plugins.AbstractPlugin
import org.elasticsearch.rest.RestModule
import org.elasticsearch.river.RiversModule

import de.kp.elastic.insight.module.{AnalyticsModule,AnalyticsRiverModule}
import de.kp.elastic.insight.rest.{GetAction,StatusAction,TrackAction}
import de.kp.elastic.insight.service.AnalyticsService

class AnalyticsPlugin(val settings:Settings) extends AbstractPlugin {
    
  override def name():String = {
    "elastic-insight"
  }

  override def description():String = {
    "Plugin that brings predictive analytics to Elasticsearch";
  }
  /**
   * REST API
   */
  def onModule(module:RestModule) {
    	
    if (settings.getAsBoolean("analytics.enabled", true)) {
      /*
       * Collect events or features
       */
   	  module.addRestAction(classOf[TrackAction])
      /*
       * Retrieve result information from remote analytics service
       */
      module.addRestAction(classOf[GetAction])
      /*
       * Retrieve status information from remote analytics service
       */
   	  module.addRestAction(classOf[StatusAction])
    }
    
  }

  /**
   * River API
   */
  def onModule(module:RiversModule) {
    	
    if (settings.getAsBoolean("analytics.enabled", true)) {
      /* 
       * The module is bound to AnalyticsRiver; the type of the
       * river is 'analytics' and must be part of the JSON request,
       * i.e. {"type":"analytics",...}  
       */
      module.registerRiver("analytics", classOf[AnalyticsRiverModule])
    
    }
    
  }

  /**
   * Module API
   */
  override def modules():Collection[Class[_ <: Module]] = {

    val modules:Collection[Class[_ <: Module]] = Lists.newArrayList()
    if (settings.getAsBoolean("analytics.enabled", true)) {
    	/* The module is bound to the AnalyticsService */
    	modules.add(classOf[AnalyticsModule])
    }
        
    modules
    
  }

  /**
   * Service API
   */
  override def services():Collection[Class[_ <: LifecycleComponent[_]]] = {
    	
    val services:Collection[Class[_ <: LifecycleComponent[_]]] = Lists.newArrayList()
    if (settings.getAsBoolean("arules.enabled", true)) {     
      services.add(classOf[AnalyticsService])        
    }
      
    services
    
  }
	
}