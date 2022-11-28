package org.uwindsor.mac.f22.acc.compute_engine.engine.parser;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.uwindsor.mac.f22.acc.compute_engine.model.SearchResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * @author Vivek
 * @since 23/11/22
 */
@Slf4j
public class KayakParser {

    public static List<SearchResponse> parseKayakData(String data) {

        List<SearchResponse> list = new ArrayList<>();
        Document document = Jsoup.parse(data);
        Elements elementsByClass = document.getElementsByClass("resultInner");
        AtomicInteger index = new AtomicInteger(1);
        elementsByClass.forEach(element -> {
            //System.out.println(index.get() + " :: " + element.text());

            SearchResponse itemSearchResponse = new SearchResponse();

            String airlineInString = null;
            Integer  takeoffTimeInInteger = null;
            Integer durationInInteger = null;
            Integer  travelTimeInInteger = null;
            int stopsInInt = 0;
            String destinationAirportCodeInString = null;
            String sourceAirportCodeInString = null;
            double priceInDouble = 0;

            try {

                airlineInString = element.select("[class~=codeshares-airline-names]").get(0).text();

                String takeoffTimeInString = element.select("[class~=depart-time base-time]").get(0).text();
                takeoffTimeInInteger = convertTimeInSearchResponseFormat(takeoffTimeInString);

                String durationTimeInString = element.select("div[class=section duration allow-multi-modal-icons]").get(0).select("[class~=top]").text();
                durationInInteger = convertDurationInMinutes(durationTimeInString);

                String travelTimeInString = element.select("[class~=arrival-time base-time]").get(0).text();
                travelTimeInInteger = convertTimeInSearchResponseFormat(travelTimeInString);

                String stopsInString = element.select("[class~=stops-text]").get(0).text();

                if(stopsInString.equalsIgnoreCase("direct")){
                    stopsInInt = 0;
                }
                else{
                    String[] stopUnits = stopsInString.split(" ");
                    stopsInInt = Integer.parseInt(stopUnits[0]);
                }

                String destinationAirportInString = element.getElementsByAttributeValueMatching("id", Pattern.compile("[a-zA-Z]*-info-leg-.-destination-airport")).text();
                String[] destinationAirportParts = destinationAirportInString.split(" ");
                destinationAirportCodeInString = destinationAirportParts[0];

                String sourceAirportInString = element.getElementsByAttributeValueMatching("id", Pattern.compile("[a-zA-Z]*-info-leg-.-origin-airport")).text();
                String[] sourceAirportParts = sourceAirportInString.split(" ");
                sourceAirportCodeInString = sourceAirportParts[0];

                String priceInString = element.select("[class~=price option-text]").get(0).text();
                String[] priceParts = priceInString.split(" ");
                priceInDouble = Double.parseDouble(priceParts[1].replaceAll(",", ""));

            } catch (NumberFormatException e) {
                log.error("Encountered error while generating SearchResponse in parsing: ", e);
            }

            itemSearchResponse.setAirline(airlineInString);
            itemSearchResponse.setLaunchTime(takeoffTimeInInteger);
            itemSearchResponse.setLandTime(travelTimeInInteger);
            itemSearchResponse.setTravelTime(durationInInteger);
            itemSearchResponse.setStops(stopsInInt);
            itemSearchResponse.setDestinationAirCode(destinationAirportCodeInString);
            itemSearchResponse.setSourceAirCode(sourceAirportCodeInString);
            itemSearchResponse.setBestDealPrice(priceInDouble);
            
            list.add(itemSearchResponse);
            index.incrementAndGet();
        });
        return list;

    }

    private static Integer convertTimeInSearchResponseFormat(String inputTimeInString) {

        Integer outputFinalTimeInInteger = null;

        String[] inputTimeUnits = inputTimeInString.split(":"); //will break the string (in hh:mm) up into an array
        int inputHours = Integer.parseInt(inputTimeUnits[0]); //first element

        String[] inputTimeMinutesUnits = inputTimeUnits[1].split(" "); //will break the string (in mm aa) up into an array
        int takeoffMinutes = Integer.parseInt(inputTimeMinutesUnits[0]); //first element

        if(inputTimeMinutesUnits[1].equals("a.m."))
        {
            if(inputHours == 12)
                inputHours = 0;
        }
        else if(inputTimeMinutesUnits[1].equals("p.m."))
        {
            if(inputHours != 12)
                inputHours += 12;
        }

        String outputTimeHourFormat = inputHours >= 10 ? "%2d" : "0%d";
        String outputTimeHourInString = String.format(outputTimeHourFormat, inputHours);


        String outputTimeMinutesFormat = takeoffMinutes >= 10 ? "%2d" : "0%d";
        String outputTimeMinutesInString = String.format(outputTimeMinutesFormat, takeoffMinutes);

        String outputFinalTimeInString = outputTimeHourInString + outputTimeMinutesInString;
        outputFinalTimeInInteger = Integer.parseInt(outputFinalTimeInString);

        return outputFinalTimeInInteger;
    }

    private static Integer convertDurationInMinutes(String inputDurationInString) {

        Integer outputDurationInMinutes = null;

        String[] durationTimeUnits = inputDurationInString.split(" "); //will break the string (in hh mm) up into an array
        StringBuffer durationHoursSB= new StringBuffer(durationTimeUnits[0]);

        //invoking the method
        durationHoursSB.deleteCharAt(durationHoursSB.length()-1);
        StringBuffer durationMinutesSB= new StringBuffer(durationTimeUnits[1]);

        //invoking the method
        durationMinutesSB.deleteCharAt(durationMinutesSB.length()-1);
        outputDurationInMinutes = Integer.parseInt(durationHoursSB.toString()) * 60 + Integer.parseInt(durationMinutesSB.toString());

        return outputDurationInMinutes;
    }
}