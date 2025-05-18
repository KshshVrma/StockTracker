package com.stock.tracker.controller;
//


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Controller
public class StockController {

    @Value("${alpha.vantage.api.key}")
    private String apiKey;

    @GetMapping("/stocks")
    public String getMultipleStockData(Model model) {
        List<String> symbols = CSVReaderUtil.readSymbolsFromCSV("stocks.csv");
        RestTemplate restTemplate = new RestTemplate();
        List<Map<String, String>> stockDataList = new ArrayList<>();
int count=0;
        for (String symbol : symbols) {
            String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" +
                    symbol + "&apikey=" + apiKey;

            try {
                Map<String, Object> response = restTemplate.getForObject(url, Map.class);
                Map<String, String> quote = (Map<String, String>) response.get("Global Quote");
                if (quote != null && !quote.isEmpty()) {
                    double price=Double.parseDouble(quote.get("05. price"));
                    double previous_close=Double.parseDouble(quote.get("08. previous close"));
                    double diff=price-previous_close;
                    quote.put("diff",String.format("%.2f",diff));
                    quote.put("symbol", symbol);
                    stockDataList.add(quote);
                    count++;
                }
                if(count%5==0)
                Thread.sleep(10000); // avoid API rate limits (5 requests per minute for free tier)
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        model.addAttribute("stocks", stockDataList);
        return "stocklist";
    }
}

