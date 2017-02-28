package org.odfi.wsb.fwapp.launcher

import org.odfi.indesign.core.main.IndesignPlatorm
import org.odfi.wsb.fwapp.FWappApp
import org.odfi.wsb.fwapp.FWappTreeBuilder
import org.odfi.wsb.fwapp.launcher.maven.RepositoryIntermediary
import org.odfi.indesign.core.brain.Brain
import java.io.File
import org.odfi.wsb.fwapp.assets.AssetsResolver
import org.odfi.wsb.fwapp.Site
 
object FWAppLauncher extends App with FWappTreeBuilder {

  IndesignPlatorm.prepareDefault
  IndesignPlatorm.stopHarvest

  IndesignPlatorm use LauncherModule
  
  
  Brain.addExternalFolderRegion(new File("src/test/testFS/test-deploy"))
  
  
  
  // Create App
  //----------------
  //var server = new Site("/")
  //server.listen(8402)

  IndesignPlatorm use LauncherModule
  LauncherModule.listen(8402)



  // Start
  //----------------
  IndesignPlatorm.start
}