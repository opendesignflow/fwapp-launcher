package org.odfi.wsb.fwapp.launcher

import org.odfi.indesign.core.harvest.Harvester
import org.odfi.indesign.core.brain.Brain
import org.odfi.indesign.core.brain.ExternalBrainRegion
import org.odfi.wsb.fwapp.FWappApp
import org.odfi.indesign.core.harvest.HarvestedResource
import org.odfi.indesign.core.brain.external.FolderOutputBrainRegion
import org.odfi.indesign.core.brain.artifact.ArtifactExternalRegion
import org.odfi.indesign.core.brain.RegionClassName
import org.odfi.wsb.fwapp.Site

trait SiteApplicationHarvester extends Harvester {

}
object SiteApplicationHarvester extends SiteApplicationHarvester {

  override def doHarvest = {

    logInfo[SiteApplicationHarvester]("Site harvest")
    Brain.getResourcesOfType[ExternalBrainRegion].foreach {
      case region =>
        logInfo[SiteApplicationHarvester]("Region: " + region)
        logInfo[SiteApplicationHarvester]("RegionClasses: " + region.getResourcesOfType[RegionClassName])

        region.discoverRegions.foreach {
          regionClassName =>

            logInfo[SiteApplicationHarvester]("Module: " + regionClassName)

            region.loadRegionClass(regionClassName) match {
              case ESome(appModule) if (appModule != LauncherModule && classOf[FWappApp].isInstance(appModule)) =>
                logInfo[SiteApplicationHarvester]("Found App: " + region)
                gather(new SiteApplication(region))
              case other =>
                logInfo[SiteApplicationHarvester]("Not an app " + regionClassName)
            }
        }
    }

  }

}

class SiteApplication(val region: ExternalBrainRegion) extends HarvestedResource {

  def getId = "app:" + region.getId

  this.deriveFrom(region)

  def getType = region match {
    case f: FolderOutputBrainRegion => "Folder"
    case f: ArtifactExternalRegion => "Artifact"
    case other => "External Undefined"
  }

  def processRegion(site: Site) = {

    logInfo[SiteApplication]("Site Module: " + site)

    logInfo[SiteApplication]("Path will be:" + site.basePath)

    LauncherModule.appsBase.fwappIntermediary <= site
    site.onClean {
      LauncherModule.appsBase.fwappIntermediary.intermediaries -= site
    }
    // Add to Intermediary tree
    // Derive site from appsBase to make sure functions using resources are working
    /* site.deriveFrom(FWAppLauncher.appsBase.fwappIntermediary)
    FWAppLauncher.appsBase.fwappIntermediary <= site

    site.onClean {
      FWAppLauncher.appsBase.fwappIntermediary.intermediaries -= site
      this.cleanDerivedResources
    }*/

    // Save site to these derived sources here
    // The site parent resource must be the parent intermediary
    this.addDerivedResource(site)

  }

  // Gathered
  this.onGathered {
    case h if (h == SiteApplicationHarvester) =>

      logInfo[SiteApplication]("Waiting for gathered resources for " + region)
      region.getResourcesOfType[RegionClassName].collect { case r: RegionClassName if (r.isType[Site]) => r.load[Site].get }.foreach {
        regionClass =>
          processRegion(regionClass)
      }
      region.onGatheredResources {
        resources =>
          resources.collect { case r: RegionClassName if (r.isType[Site]) => r.load[Site].get }.foreach {
            regionClassName =>
              processRegion(regionClassName)

          }
      }

    /*region.getR
      
      region.discoverRegions.foreach {
          regionClassName => 
            
            logInfo[SiteApplication]("Module: "+regionClassName)
            
            region.loadRegionClass(regionClassName) match {
              case ESome(appModule) if(classOf[FWappApp].isInstance(appModule)) => 
                logInfo[SiteApplication]("Found App: "+region)
                
                // Create resource
                
                //gather(new SiteApplication(region))
              case other => 
                println("Not an app "+regionClassName)
            }
        }*/

  }

}