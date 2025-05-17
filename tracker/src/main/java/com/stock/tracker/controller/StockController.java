package com.stock.tracker.controller;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class StockController {

    @Value("${alpha.vantage.api.key}")
    private String apiKey;

    @GetMapping("/")
    public String home() {
        return "index"; // shows index.html
    }

    @GetMapping("/stock")
    public String getStockPrice(@RequestParam String symbol, Model model) {
        String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" +
                symbol + "&apikey=" + apiKey;

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        String price = "N/A";
        if (response != null && response.contains("\"05. price\":")) {
            int index = response.indexOf("\"05. price\":");
            int start = response.indexOf("\"", index + 13) + 1;
            int end = response.indexOf("\"", start);
            price = response.substring(start, end);
        }

        model.addAttribute("symbol", symbol);
        model.addAttribute("price", price);
        model.addAttribute("response",response);

        return "index"; // will load index.html again with price
    }
}