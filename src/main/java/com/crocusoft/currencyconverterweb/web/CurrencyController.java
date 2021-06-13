package com.crocusoft.currencyconverterweb.web;

import com.crocusoft.currencyconverterweb.component.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CurrencyController {
    CurrencyService currencyService;

    @Autowired
    CurrencyController(CurrencyService currencyService){
        this.currencyService = currencyService;
    }

    @GetMapping
    public String doGetCurrencyView(Model model){
        model.addAttribute("currencies", currencyService.getCurrencies());
        return "index";
    }
}
