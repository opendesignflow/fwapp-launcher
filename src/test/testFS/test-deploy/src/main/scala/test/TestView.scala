package test

import org.odfi.wsb.fwapp.framework.FWAppFrameworkView
import org.odfi.wsb.fwapp.module.semantic.SemanticView

class TestView extends FWAppFrameworkView with SemanticView {
  
  
  this.viewContent {
    html {
    
      head {
        
        placeLibraries
        
      }
      
      body {
        h1("Hello World 2") {
          
        }
      }
      
    }
    
    
  }
}