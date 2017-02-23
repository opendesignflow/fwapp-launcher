package org.odfi.wsb.fwapp.launcher

import org.odfi.wsb.fwapp.framework.FWAppFrameworkView
import org.odfi.wsb.fwapp.module.semantic.SemanticView
import org.odfi.wsb.fwapp.module.jquery.JQueryTreetable
import org.odfi.wsb.fwapp.Site
import org.odfi.indesign.core.brain.Brain
import org.odfi.indesign.core.brain.ExternalBrainRegion
import org.odfi.indesign.core.harvest.Harvest
import org.odfi.indesign.core.brain.external.FolderOutputBrainRegion
import org.odfi.indesign.core.brain.artifact.ArtifactExternalRegion

class LauncherOverview extends FWAppFrameworkView with SemanticView with JQueryTreetable {

  var localViewPath : Option[String] = None
  
  this.viewContent {
    html {

      head {
        placeLibraries
      }

      body {

        ribbonHeaderDiv("blue", "Application Sources") {

          "ui info message" :: "This table shows the sources from which the Launcher will look for Applications"

          "ui table" :: table {

            thead("Name", "Type", "ID")

            Brain.getResourcesOfType[ExternalBrainRegion].foreach {
              region =>
                tr {
                  td(region.getDisplayName) {

                  }
                  td("") {
                    region match {
                      case f: FolderOutputBrainRegion => text("Folder")
                      case f: ArtifactExternalRegion => text("Artifact")
                    }
                  }
                  td(region.getId) {

                  }
                }
            }

          }

        }
        ribbonHeaderDiv("blue", "Discovered Applications") {

          "ui info message" :: "This table shows the sources from which the Launcher will look for Applications"

          "ui button" :: button("Stop") {
            onClickReload {

              Brain.getResourcesOfType[ExternalBrainRegion].foreach {
                region =>
                  println("Stopping: " + region)
                  region.moveToShutdown
                  region.resetState
              }
              Harvest.run

            }
          }

          "ui button" :: button("Start") {
            onClickReload {

              Brain.getResourcesOfType[ExternalBrainRegion].foreach {
                region =>
                  println("Starting: " + region)
                  region.moveToStart
              }
              Harvest.run

            }
          }

          "ui button" :: button("Discover") {
            onClickReload {
              Harvest.run

            }
          }

          SiteApplicationHarvester.getResources.size match {
            case 0 =>
              "ui info message" :: "No Application configured yet"
            case other =>
              "ui table treetable" :: table {

                thead("Name", "Location", "Type", "Action")
                tbody {

                  SiteApplicationHarvester.getResourcesOfType[SiteApplication].foreach {
                    app =>

                      // Main
                      tr {
                        treeTableLineId(app.getId)
                        td(app.getDisplayName) {

                        }

                        td("") {

                        }

                        td(app.getType) {

                        }
                        // Action
                        td("") {

                          "ui button" :: button("Restart") {
                            onClickReload {
                              app.region.reload
                              SiteApplicationHarvester.harvest
                            }
                          }

                        }
                      }

                      // Site Apps
                      //--------------
                      app.getDerivedResources[Site].foreach {
                        site =>
                          tr {
                            treeTableLineId(site.getId)
                            treeTableParent(app.getId)
                            td(site.getDisplayName) {

                            }

                            var loc = LauncherModule.appsBase.fwappIntermediary.fullURLPath+site.basePath
                            td("") {
                              
                              a(loc)(text(loc))
                            }

                            td("") {

                            }

                            td("") {
                              
                              // View locally
                              "ui button" :: button("View Locally") {
                                onClickReload {
                                  localViewPath = Some(loc)
                                }
                              }
                              
                            }
                          }
                      }

                  }

                }

              }
          }

        }
        // EOF Discovered app
        
        // View Locally
        //--------------------------
        localViewPath match {
          case None => 
          case Some(localPath) => 
            
        }

      }
    }

  }

}