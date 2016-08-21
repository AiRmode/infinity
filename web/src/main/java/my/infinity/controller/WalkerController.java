package my.infinity.controller;

import my.infinity.DataSnapshotStorage;
import my.infinity.InfinityWalker;
import my.infinity.concurrent.TaskExecutorManager;
import my.infinity.controller.model.DataState;
import my.infinity.dataConfig.DataConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Arrays;
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

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String welcome() {
        return "welcome";
    }

    @RequestMapping(value = "/vga256", method = RequestMethod.GET)
    public String vga256(Model model) {
        DataConfig dataConfig = infinityVGA256Walker.getDataConfig();
        Byte[] bytes = DataSnapshotStorage.getMap().get(dataConfig);
        model.addAttribute("name", Arrays.toString(bytes));
        return "arrayPrint";
    }

    @RequestMapping(value = "/vga256StateJSON", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    DataState vga256StateJSON() {
        DataState dataState = new DataState();
        DataConfig dataConfig = infinityVGA256Walker.getDataConfig();
        Byte[] bytes = DataSnapshotStorage.getMap().get(dataConfig);
        dataState.setArray(bytes);
        return dataState;
    }

    @RequestMapping(value = "/vga65k", method = RequestMethod.GET)
    public String vga65k(Model model) {
        DataConfig dataConfig = infinityVGA65KWalker.getDataConfig();
        Byte[] bytes = DataSnapshotStorage.getMap().get(dataConfig);
        model.addAttribute("name", Arrays.toString(bytes));
        return "arrayPrint";
    }

    @RequestMapping(value = "/vga65KStateJSON", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    DataState vga65kStateJSON() {
        DataState dataState = new DataState();
        DataConfig dataConfig = infinityVGA65KWalker.getDataConfig();
        Byte[] bytes = DataSnapshotStorage.getMap().get(dataConfig);
        dataState.setArray(bytes);
        return dataState;
    }

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleErrorPage() {
        ModelAndView model = new ModelAndView("error");
        model.addObject("errMsg", "this is Exception.class");
        return model;
    }
}
