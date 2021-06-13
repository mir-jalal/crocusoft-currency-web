package com.crocusoft.currencyconverterweb.component;

import com.crocusoft.currencyconverterweb.model.Currency;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;

@Component
public class CurrencyService {

    ArrayList<Currency> currencies;

    CurrencyService() throws IOException, ParserConfigurationException, SAXException {
        currencies = new ArrayList<>();

        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        saxParser.parse(getXmlFile(), new CurrencyHandler());
    }

    File getXmlFile() throws IOException{
        LocalDate date = LocalDate.now();
        File file = new File("currency.xml");

        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        String url = "https://www.cbar.az/currencies/" +
                (day < 10 ? "0" : "") + day + "." +
                (month < 10 ? "0" : "") + month + "." +
                year + ".xml";

        InputStream input = new URL(url).openStream();
        try(OutputStream output = new FileOutputStream(file)){
            IOUtils.copy(input, output);
        }

        return file;
    }

    public Integer getNumber() {
        return currencies.size();
    }

    public ArrayList<Currency> getCurrencies() {
        return currencies;
    }

    public class CurrencyHandler extends DefaultHandler {

        private static final String VAL_CURS = "ValCurs";
        private static final String VAL_TYPE = "ValType";
        private static final String VALUTE   = "Valute";
        private static final String NOMINAL  = "Nominal";
        private static final String NAME     = "Name";
        private static final String VALUE    = "Value";

        private String valType;
        private Currency currency;

        private boolean bValCurs = false;
        private boolean bValType = false;
        private boolean bValute  = false;
        private boolean bNominal = false;
        private boolean bName    = false;
        private boolean bValue   = false;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            switch (qName){
                case VAL_CURS:
                    bValCurs = true;
                    break;
                case VAL_TYPE:
                    valType = attributes.getValue("Type");
                    bValType = true;
                    break;
                case VALUTE:
                    currency = new Currency(valType, attributes.getValue("Code"));
                    bValute = true;
                    break;
                case NOMINAL:
                    bNominal = true;
                    break;
                case NAME:
                    bName = true;
                    break;
                case VALUE:
                    bValue = true;
                    break;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            switch (qName){
                case VAL_CURS:
                    bValCurs = false;
                    break;
                case VAL_TYPE:
                    bValType = false;
                    break;
                case VALUTE:
                    currencies.add(currency);
                    bValute = false;
                    break;
                case NOMINAL:
                    bNominal = false;
                    break;
                case NAME:
                    bName = false;
                    break;
                case VALUE:
                    bValue = false;
                    break;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length){
            if(bNominal) currency.setNominal(String.valueOf(ch, start, length));
            if(bName) currency.setName(String.valueOf(ch, start, length));
            if(bValue) currency.setChangeRate(new BigDecimal(String.valueOf(ch, start, length)));
        }
    }
}
