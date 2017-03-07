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
import org.odfi.indesign.core.harvest.Harvest
import org.odfi.indesign.ide.core.project.ProjectsHarvester
import org.odfi.indesign.ide.core.project.jvm.JavaOutputProject
import org.odfi.indesign.ide.core.project.ProjectHarvesterTrait
import org.apache.maven.project.MavenProject
import org.odfi.indesign.core.harvest.fs.FileSystemHarvester
import java.lang.reflect.Modifier

trait SiteApplicationHarvester extends Harvester {

}
object SiteApplicationHarvester extends SiteApplicationHarvester {

  //tlogEnableFull[Harvester]

  override def doHarvest = {

    logInfo[SiteApplicationHarvester]("Site harvest on: " + hashCode())

    /*Harvest.collectOnHarvesters[ProjectHarvesterTrait,List[Site]] {
      case pht => 
        
        List[Site]()
    }*/ 

    Harvest.collectResourcesOnHarvesters[ProjectHarvesterTrait, JavaOutputProject, List[Class[Site]]] {
      case javaProject =>

        logInfo[SiteApplicationHarvester](s"Discovering on region: " + javaProject)

        var sites = javaProject.discoverType[Site].filterNot { cl => Modifier.isAbstract(cl.getModifiers)}

        sites.foreach {
          cl =>
            logInfo[SiteApplicationHarvester](s"Found Site canditate -> " + cl)
            gather(new SiteApplication(javaProject, cl))
        }
        sites
      //List[Site]()
    }
    logInfo[SiteApplicationHarvester]("Finished Harvest")

    super.doHarvest
  }

}

class SiteApplication(val project: JavaOutputProject, val siteClass: Class[Site]) extends HarvestedResource {
 

  def getId = "site:" + project.getId + ":" + siteClass.getCanonicalName

  var site: Option[Site] = None

  //-- Added 
  this.onAdded {
    case h if (h == SiteApplicationHarvester) =>

      
      site = Some(Brain.createRegion(siteClass.getClassLoader, siteClass.getCanonicalName).asInstanceOf[Site])
      
      //-- Site derives from its project to get cleaned flawlessly
      site.get.deriveFrom(project)
      site.get.onClean {
        this.clean
      }
      
      site.get.moveToStart
      
     /*.onBuildOutputChanged {
        site.get.clean
      }*/
      LauncherModule.appsBase.fwappIntermediary <= site.get
  }

  //-- Clean
  this.onClean {
    println(s"Cleaning")
   //site.get.clean
    site = None
  }

  /*
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
*/
}