package com.collabnet.ccf.ccfmaster.controller;

import com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "landscapeconfigs", formBackingObject = LandscapeConfig.class)
@RequestMapping("/landscapeconfigs")
@Controller
public class LandscapeConfigController {
}
