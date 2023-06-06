package school.calc4.calcWeb.controller;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import school.calc4.calcWeb.calcModel.CalculatorModel;
import school.calc4.calcWeb.service.ServiceUtil;

import java.util.*;

@Controller
public class MainController {
    private final ServiceUtil serviceUtil;

    public MainController(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @GetMapping("/")
    public String index(@NotNull Model model) {
        model.addAttribute("title", "SmartCalc_4.0");
        List<String> allHistory = serviceUtil.getAllHistory();
        allHistory.removeIf(Objects::isNull);
        allHistory.removeIf(String::isEmpty);
        model.addAttribute("allHistory", allHistory);

        return "home";
    }

    @GetMapping("/clean")
    public String clean(@NotNull Model model) {
        serviceUtil.cleanHistory();
        return "home";
    }

    @GetMapping("/guide")
    public String guide(@NotNull Model model) {
        return "guide";
    }

    @GetMapping("/calculate")
    public String calculate(@RequestParam String calcStr, @NotNull Model model) {
        CalculatorModel calc = new CalculatorModel(calcStr);
        model.addAttribute("result", calc.calculateResult());
        serviceUtil.saveFileHistory(calcStr);
        return "home";
    }

    @GetMapping("/favicon.ico")
    public String faviconAbort(@NotNull Model model) {
        return "home";
    }

    @GetMapping("/graph")
    public String graph(@RequestParam String calcStr, @NotNull Model model) {
        model.addAttribute("calcStr", calcStr);
        return "graph";
    }

    @PostMapping("/build")
    public String createGraph(@RequestParam(name="outStr") String calcStr,
                              @RequestParam(defaultValue = "-30", required = false, name="minX") Double minX,
                              @RequestParam(defaultValue = "30", required = false, name="maxX") Double maxX,
                              @RequestParam(defaultValue = "0.05", required = false, name="step") Double step,
                              @NotNull Model model) {
        Map<Double,Double> result = new LinkedHashMap<>();
        List<List<Double>> chartData = new ArrayList<>();
        for (double i = minX; i < maxX; i += step) {
            CalculatorModel calc = new CalculatorModel(calcStr.replaceAll("x", String.valueOf(i)));
            result.put(i, Double.valueOf(calc.calculateResult()));
            chartData.add(List.of(i, Double.valueOf(calc.calculateResult())));
        }
        Double minY = Collections.min(result.values());
        Double maxY = Collections.max(result.values());
        model.addAttribute("result", result);
        model.addAttribute("chartData", chartData);
        model.addAttribute("minX", minX);
        model.addAttribute("maxX", maxX);
        model.addAttribute("minY", minY);
        model.addAttribute("maxY", maxY);
        return "build";
    }
}
