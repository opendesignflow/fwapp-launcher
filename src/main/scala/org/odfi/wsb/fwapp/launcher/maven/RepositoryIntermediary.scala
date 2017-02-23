package org.odfi.wsb.fwapp.launcher.maven

import org.odfi.wsb.fwapp.FWappIntermediary
import com.idyria.osi.wsb.webapp.http.message.HTTPIntermediary
import com.idyria.osi.wsb.webapp.http.message.HTTPPathIntermediary
import com.idyria.osi.wsb.webapp.http.message.HTTPResponse

class RepositoryIntermediary(path:String) extends FWappIntermediary(path) {
  
  this <= new HTTPPathIntermediary("/deploy") {
    
    this.onDownMessage {
      
      req => 
        
        println(s"Req for: "+req.path+" type "+req.operation)
        println(s"Req for: "+req.path+" type "+req.operation)
        println(s"Multipart: "+req.isMultipart)
        println(s"Req content: "+req.urlParameters)
         println(s"Req params: "+req.parameters)
         
         println(s"bytes: "+req.bytes.length)
         
        //-- Res
        var resp = HTTPResponse()
        resp.code = 200
        
        response(resp,req)
    }
    
    
    
  }
  
}