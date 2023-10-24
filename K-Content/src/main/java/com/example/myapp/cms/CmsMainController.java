package com.example.myapp.cms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.myapp.cms.content.service.IContentService;
import com.example.myapp.cms.inqry.model.CmsInqry;
import com.example.myapp.cms.inqry.service.ICmsInqryService;
import com.example.myapp.user.commu.model.Commu;
import com.example.myapp.user.commu.service.ICommuService;
import com.example.myapp.user.mber.service.IMberService;

@PreAuthorize("hasRole('ADMIN')")
@Controller
@RequestMapping("/cs")
public class CmsMainController {

    @Autowired
    ICommuService commuService;

    @Autowired
    ICmsInqryService cmsInqryService;

    @Autowired
    IMberService mberService;

    @Autowired
    IContentService contentService;

    @GetMapping("")
    public String csMain(Model model) {
        List<CmsInqry> inqryList = cmsInqryService.selectRecentInqry();
        int activeMberCnt = mberService.getActiveMberCount();
        int contentTotalCnt = contentService.contentTotalCnt();

        model.addAttribute("contentTotalCnt", contentTotalCnt);
        model.addAttribute("activeMberCnt", activeMberCnt);
        model.addAttribute("inqryList", inqryList);
        return "cms/index";
    }

    @GetMapping("/recent-notice")
    @ResponseBody
    public List<Commu> commuList() {
        List<Commu> commu = commuService.selectRecentNotice();

        return commu;
    }

    @GetMapping("/recent-inqry")
    @ResponseBody
    public List<CmsInqry> inqryList() {
        List<CmsInqry> inqry = cmsInqryService.selectRecentInqry();

        return inqry;
    }
}
