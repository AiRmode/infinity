package my.infinity.controller;

import my.infinity.InfinityWalker;
import my.infinity.concurrent.TaskExecutorManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by alshevchuk on 07.01.2016.
 */
@Controller
public class WalkerController {
    private static volatile AtomicBoolean isStarted = new AtomicBoolean(false);
    @Resource(name = "vga256Walker")
    private InfinityWalker infinityVGA256Walker;
    @Resource(name = "vga65kWalker")
    private InfinityWalker infinityVGA65KWalker;
    @Resource(name = "taskExecutorManager")
    private TaskExecutorManager taskExecutorManager;

    @PostConstruct
    public synchronized void init() {
        if (!isStarted.get()) {
            isStarted.set(true);
            taskExecutorManager.execute(infinityVGA256Walker);
            taskExecutorManager.execute(infinityVGA65KWalker);
        }
    }

    @RequestMapping("/")
    public String welcome() {
        return "welcome";
    }

    @RequestMapping("/hello")
    public String hello(@RequestParam(value = "name", required = false, defaultValue = "Test1") String name, Model model) {
        model.addAttribute("name", name);
        return "index";
    }

    @RequestMapping(value = "/error")
    public String handleErrorPage() {
        return "error";
    }
}
