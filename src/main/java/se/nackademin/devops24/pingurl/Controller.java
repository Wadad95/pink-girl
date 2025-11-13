package se.nackademin.devops24.pingurl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@org.springframework.stereotype.Controller
public class Controller {

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);
    private final PingUrlService pingUrlService;

    public Controller(PingUrlService pingUrlService) {
        this.pingUrlService = pingUrlService;
    }
    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("urls", pingUrlService.getPingUrls());
        return modelAndView;
    }

    @PostMapping("/pingurl")
    public RedirectView addUrl(@RequestParam String url,
                            @RequestParam String name,
                            RedirectAttributes redirectAttributes) {

        try {
            pingUrlService.addPingUrl(url, name);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return new RedirectView("/");
    }

    @PostMapping("/delete")
    public RedirectView deleteUrl(@RequestParam String name) {
        pingUrlService.deleteUrlFromPing(name);
        return new RedirectView("/");
    }


    @PostMapping("/ping-now")
    public RedirectView pingNow(@RequestParam String name) {
        pingUrlService.pingOneUrl(name);
        return new RedirectView("/");
    }




    


    

}
