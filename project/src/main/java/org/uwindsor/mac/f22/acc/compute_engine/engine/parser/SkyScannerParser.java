package org.uwindsor.mac.f22.acc.compute_engine.engine.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.uwindsor.mac.f22.acc.compute_engine.model.SearchResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sila
 * @since 23/11/22
 */
public class SkyScannerParser {

    public static List<SearchResponse> parseSkyScannerData(String pageSource) {

        List<SearchResponse> list = new ArrayList<>();
        Document document = Jsoup.parse(pageSource);
        Elements elementsByClass = document.getElementsByAttributeValueMatching("class", "[f|F]lights[t|T]icket_container*");

        elementsByClass.forEach(element -> {

            SearchResponse itemSearchResponse = new SearchResponse();

            String airlineInString = null;
            Integer  takeoffTimeInInteger = null;
            Integer durationInInteger = 0;
            Integer  travelTimeInInteger = null;
            int stopsInInt = 0;
            String destinationAirportCodeInString = null;
            String sourceAirportCodeInString = null;
            double priceInDouble = 0;

            String stopsInString = null;

            Element divForAirline = element.getElementsByClass("LogoImage_container__MDU0Z UpperTicketBody_ticketLogo__Nzc1Z").first();
            airlineInString = divForAirline.select("span").first().text();

            Element divForTakeOffTime = element.getElementsByClass("LegInfo_routePartialDepart__NzEwY").first();
            String takeoffTimeInString = divForTakeOffTime.select("span").first().text();
            takeoffTimeInInteger = convertTimeInSearchResponseFormat(takeoffTimeInString);

            Element divForTravelTime = element.getElementsByClass("LegInfo_routePartialArrive__Y2U1N").first();
            String travelTimeInString = divForTravelTime.getElementsByClass("bpktext_bpk-text__zwizz bpktext_bpk-text--label-1__mzvmn").first().text();
            travelTimeInInteger = convertTimeInSearchResponseFormat(travelTimeInString);

            Elements elementsForStops = element.getElementsByClass("BpkText_bpk-text__ZWIzZ BpkText_bpk-text--xs__MTAxY LegInfo_stopsLabelRed__NTY2Y");
            if (elementsForStops.size() > 0){
                stopsInString = elementsForStops.select("span").first().text();
                String[] stopUnits = stopsInString.split(" ");
                stopsInInt = Integer.parseInt(stopUnits[0]);
            }

            String sourceAirportInString = element.getElementsByClass("BpkText_bpk-text__ZWIzZ BpkText_bpk-text--body-default__MzkyN LegInfo_routePartialCityTooltip__NTE4Z").get(0).text();
            String destinationAirportInString = element.getElementsByClass("BpkText_bpk-text__ZWIzZ BpkText_bpk-text--body-default__MzkyN LegInfo_routePartialCityTooltip__NTE4Z").get(1).text();

            Element divForPrice = element.getElementsByClass("Price_mainPriceContainer__MDM3O").first();
            String priceInString = divForPrice.select("span").first().text();
            priceInDouble = Double.parseDouble(priceInString.substring(2).replaceAll(",", ""));

            Elements elementsForTravelTime = element.getElementsByClass("BpkText_bpk-text__ZWIzZ BpkText_bpk-text--xs__MTAxY Duration_duration__NmUyM");
            String durationTimeInString = elementsForTravelTime.first().text();
            durationInInteger = convertDurationInMinutes(durationTimeInString);

            itemSearchResponse.setAirline(airlineInString);
            itemSearchResponse.setLaunchTime(takeoffTimeInInteger);
            itemSearchResponse.setLandTime(travelTimeInInteger);
            itemSearchResponse.setTravelTime(durationInInteger);
            itemSearchResponse.setStops(stopsInInt);
            itemSearchResponse.setDestinationAirCode(destinationAirportInString);
            itemSearchResponse.setSourceAirCode(sourceAirportInString);
            itemSearchResponse.setBestDealPrice(priceInDouble);

            list.add(itemSearchResponse);


        });

        return list;
    }

    private static Integer convertTimeInSearchResponseFormat(String inputTimeInString) {

        Integer outputFinalTimeInInteger = null;

        String[] inputTimeUnits = inputTimeInString.split(":"); //will break the string (in hh:mm) up into an array
        int inputHours = Integer.parseInt(inputTimeUnits[0]); //first element

        String[] inputTimeMinutesUnits = inputTimeUnits[1].split(" "); //will break the string (in mm aa) up into an array
        int takeoffMinutes = Integer.parseInt(inputTimeMinutesUnits[0]); //first element

        if(inputTimeMinutesUnits[1].equals("a.m.") || inputTimeMinutesUnits[1].equals("AM"))
        {
            if(inputHours == 12)
                inputHours = 0;
        }
        else if(inputTimeMinutesUnits[1].equals("p.m.") || inputTimeMinutesUnits[1].equals("PM"))
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
        Integer durationMinutesInInteger = 0;

        String[] durationTimeUnits = inputDurationInString.split(" "); //will break the string (in hh mm) up into an array
        StringBuffer durationHoursSB= new StringBuffer(durationTimeUnits[0]);

        //invoking the method
        durationHoursSB.deleteCharAt(durationHoursSB.length()-1);

        if (durationTimeUnits.length > 1) {

            StringBuffer durationMinutesSB= new StringBuffer(durationTimeUnits[1]);

            //invoking the method
            durationMinutesSB.deleteCharAt(durationMinutesSB.length()-1);

            durationMinutesInInteger = Integer.parseInt(durationMinutesSB.toString());

        }

        outputDurationInMinutes = Integer.parseInt(durationHoursSB.toString()) * 60 + durationMinutesInInteger;

        return outputDurationInMinutes;
    }
}