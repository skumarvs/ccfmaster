// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.collabnet.ccf.ccfmaster.controller;

import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import java.lang.String;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

privileged aspect ExternalAppController_Roo_Controller_Finder {
    
    @RequestMapping(params = { "find=ByLandscape", "form" }, method = RequestMethod.GET)
    public String ExternalAppController.findExternalAppsByLandscapeForm(Model uiModel) {
        uiModel.addAttribute("landscapes", Landscape.findAllLandscapes());
        return "externalapps/findExternalAppsByLandscape";
    }
    
    @RequestMapping(params = { "find=ByLinkIdEquals", "form" }, method = RequestMethod.GET)
    public String ExternalAppController.findExternalAppsByLinkIdEqualsForm(Model uiModel) {
        return "externalapps/findExternalAppsByLinkIdEquals";
    }
    
}
