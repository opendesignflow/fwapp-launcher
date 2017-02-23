package org.odfi.wsb.fwapp.launcher

import org.odfi.indesign.core.module.IndesignModule
import org.odfi.indesign.core.harvest.Harvest
import org.odfi.indesign.core.config.ooxoo.OOXOOConfigModule
import org.odfi.wsb.fwapp.Site
import org.odfi.wsb.fwapp.launcher.maven.RepositoryIntermediary
import org.odfi.wsb.fwapp.assets.AssetsResolver
import org.odfi.indesign.core.brain.Brain
import org.odfi.indesign.ide.core.ui.IDEGUI

trait LauncherModule {

}
object LauncherModule extends Site("/launcher") with LauncherModule {

  tlogEnableFull[LauncherModule]
  tlogEnableFull[SiteApplication]
  tlogEnableFull[SiteApplicationHarvester]

  // Site
  //-------------------
  "/info" is {
    view(classOf[LauncherOverview])
  }

  val appsBase = "/apps" is {

  }

  "/assets" uses new AssetsResolver

  "/maven/" uses new RepositoryIntermediary("/")

  this.onLoad {
    requireModule(OOXOOConfigModule)
  }
  this.onStart {
    Harvest.addHarvester(SiteApplicationHarvester)

    //-- Try to deploy in IDE
    Brain.getResource[IDEGUI] match {
      case Some(idegui) =>
        logInfo[LauncherModule]("Deploying Launcher to IDE")
        //IDEGUI <= this
        IDEGUI.prepend(this)
      case None =>
    }

  }

  this.onStop {
    SiteApplicationHarvester.clean
  }

}