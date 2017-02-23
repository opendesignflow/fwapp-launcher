package test

import org.odfi.wsb.fwapp.FWappApp
import org.odfi.wsb.fwapp.Site
import org.odfi.wsb.fwapp.assets.AssetsResolver

object TestApp extends Site("/test") {
  
  "/index" is {
    
    view (classOf[TestView])
    
    
  }
  
  "/assets" uses new AssetsResolver
}