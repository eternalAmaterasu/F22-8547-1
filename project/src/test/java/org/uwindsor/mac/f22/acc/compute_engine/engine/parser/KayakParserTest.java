package org.uwindsor.mac.f22.acc.compute_engine.engine.parser;

import org.junit.jupiter.api.Test;
import org.uwindsor.mac.f22.acc.compute_engine.model.SearchResponse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * @author Vivek
 * @since 23/11/22
 */
class KayakParserTest {

    @Test
    void testParseKayakData() {
        StringBuilder data = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("src/test/resources/kayak-data.html"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim().toLowerCase();
                data.append(line).append("\n");
            }
        } catch (IOException ignored) {
        }


        /*
        1 :: 10:00 a.m. – 3:08 p.m. yqg windsor ‐ yul pierre elliott trudeau intl 1 stop ytz 5h 08m porter airlines ad 0 c$ 171 / person c$ 342 total porter airlines view deal
2 :: best 6:00 p.m. – 9:50 p.m. yqg windsor ‐ yul pierre elliott trudeau intl 1 stop yyz 3h 50m operated by air canada express - jazz air canada 1 0 c$ 172 / person c$ 344 total basic economy air canada view deal book on kayak c$ 172 air canada more booking options c$ 172 air canada c$ 201 standard c$ 257 flex c$ 172 basic economy carry-on hand baggage $ first checked bag $ advance seat selection $ premium seat changeable ticket refundable ticket c$ 201 standard carry-on hand baggage $ second checked bag $ advance seat selection $ changeable ticket $ premium seat $ change anytime c$ 257 flex advance seat selection basic seat carry-on hand baggage change anytime $ first checked bag $ changeable ticket
3 :: cheapest 10:00 a.m. – 3:08 p.m. yqg windsor ‐ yul pierre elliott trudeau intl 1 stop ytz 5h 08m porter airlines 1 0 c$ 159 / person c$ 318 total budgetair view deal book on kayak c$ 159 budgetair more booking options c$ 162 justfly c$ 162 flighthub c$ 166 travelup c$ 167 kiwi.com c$ 171 booking.com c$ 171 porter airlines c$ 171 travelocity c$ 171 expedia c$ 171 cheapoair c$ 171 gotogate c$ 171 mytrip.com c$ 172 edreams c$ 185 flightnetwork 10 more from c$ 167
4 :: 7:00 a.m. – 10:43 a.m. yqg windsor ‐ yul pierre elliott trudeau intl 1 stop ytz 3h 43m porter airlines 1 0 c$ 171 / person c$ 342 total travelocity view deal book on kayak c$ 218 budgetair more booking options c$ 171 travelocity c$ 171 expedia c$ 171 cheapoair c$ 171 gotogate c$ 171 mytrip.com c$ 172 edreams c$ 178 booking.com c$ 185 flightnetwork c$ 221 justfly c$ 221 flighthub c$ 230 travelup c$ 236 porter airlines c$ 272 kiwi.com 10 more from c$ 171
5 :: 6:00 p.m. – 10:50 p.m. yqg windsor ‐ yul pierre elliott trudeau intl 1 stop yyz 4h 50m operated by air canada express - jazz air canada 1 0 c$ 172 / person c$ 344 total basic economy air canada view deal book on kayak c$ 172 air canada more booking options c$ 172 air canada c$ 201 standard c$ 257 flex c$ 172 basic economy carry-on hand baggage $ first checked bag $ advance seat selection $ premium seat changeable ticket refundable ticket c$ 201 standard carry-on hand baggage $ second checked bag $ advance seat selection $ changeable ticket $ premium seat $ change anytime c$ 257 flex advance seat selection basic seat carry-on hand baggage change anytime $ first checked bag $ changeable ticket
6 :: 7:00 a.m. – 11:58 a.m. yqg windsor ‐ yul pierre elliott trudeau intl 1 stop ytz 4h 58m porter airlines 1 0 c$ 171 / person c$ 342 total travelocity view deal book on kayak c$ 218 budgetair more booking options c$ 171 travelocity c$ 171 expedia c$ 171 cheapoair c$ 171 gotogate c$ 171 mytrip.com c$ 172 edreams c$ 178 booking.com c$ 185 flightnetwork c$ 221 justfly c$ 221 flighthub c$ 230 travelup c$ 236 porter airlines c$ 272 kiwi.com 10 more from c$ 171
7 :: 10:00 a.m. – 4:38 p.m. yqg windsor ‐ yul pierre elliott trudeau intl 1 stop ytz 6h 38m porter airlines 0 0 c$ 172 / person c$ 343 total edreams view deal book on kayak c$ 201 budgetair more booking options c$ 172 edreams c$ 205 justfly c$ 205 flighthub c$ 207 travelup c$ 209 booking.com c$ 211 gotogate c$ 211 mytrip.com c$ 211 kiwi.com c$ 212 porter airlines c$ 212 cheapoair c$ 212 travelocity c$ 212 expedia c$ 218 flightnetwork 10 more from c$ 207
8 :: 9:20 a.m. – 1:20 p.m. yqg windsor ‐ yul pierre elliott trudeau intl 1 stop yyz 4h 00m operated by air canada express - jazz air canada 1 0 c$ 405 / person c$ 810 total standard air canada view deal book on kayak c$ 405 air canada more booking options c$ 405 air canada c$ 473 flex c$ 541 comfort c$ 405 standard carry-on hand baggage $ second checked bag $ advance seat selection $ changeable ticket $ premium seat $ change anytime c$ 473 flex advance seat selection basic seat carry-on hand baggage change anytime $ first checked bag $ changeable ticket c$ 541 comfort advance seat selection changeable ticket refundable ticket premium seat carry-on hand baggage change anytime
9 :: 10:00 a.m. – 4:58 p.m. yqg windsor ‐ yul pierre elliott trudeau intl 1 stop ytz-ykf 6h 58m porter airlines, flair airlines 1 0 c$ 198 / person c$ 396 total mytrip.com view deal
10 :: 6:00 p.m. – 12:30 a.m. +1 yqg windsor ‐ yul pierre elliott trudeau intl 1 stop yyz 6h 30m operated by air canada express - jazz air canada 1 0 c$ 324 / person c$ 647 total basic economy air canada view deal book on kayak c$ 324 air canada more booking options c$ 324 air canada c$ 352 standard c$ 409 flex c$ 324 basic economy carry-on hand baggage $ first checked bag $ advance seat selection $ premium seat changeable ticket refundable ticket c$ 352 standard carry-on hand baggage $ second checked bag $ advance seat selection $ changeable ticket $ premium seat $ change anytime c$ 409 flex advance seat selection basic seat carry-on hand baggage change anytime $ first checked bag $ changeable ticket
11 :: 10:00 a.m. – 5:48 p.m. yqg windsor ‐ yul pierre elliott trudeau intl 1 stop ytz 7h 48m porter airlines 1 0 c$ 269 / person c$ 538 total mytrip.com view deal
12 :: 9:20 a.m. – 3:20 p.m. yqg windsor ‐ yul pierre elliott trudeau intl 1 stop yyz 6h 00m operated by air canada express - jazz air canada 1 0 c$ 426 / person c$ 851 total standard air canada view deal book on kayak c$ 426 air canada more booking options c$ 426 air canada c$ 493 flex c$ 561 comfort c$ 426 standard carry-on hand baggage $ second checked bag $ advance seat selection $ changeable ticket $ premium seat $ change anytime c$ 493 flex advance seat selection basic seat carry-on hand baggage change anytime $ first checked bag $ changeable ticket c$ 561 comfort advance seat selection changeable ticket refundable ticket premium seat carry-on hand baggage change anytime
13 :: 10:00 a.m. – 6:48 p.m. yqg windsor ‐ yul pierre elliott trudeau intl 1 stop ytz 8h 48m porter airlines 1 0 c$ 269 / person c$ 538 total mytrip.com view deal
14 :: 7:00 a.m. – 4:58 p.m. yqg windsor ‐ yul pierre elliott trudeau intl 1 stop ytz-ykf 9h 58m porter airlines, flair airlines 1 0 c$ 198 / person c$ 396 total mytrip.com view deal
15 :: 7:00 a.m. – 4:38 p.m. yqg windsor ‐ yul pierre elliott trudeau intl 1 stop ytz 9h 38m porter airlines 1 0 c$ 269 / person c$ 538 total mytrip.com view deal
16 :: 10:00 a.m. – 8:28 p.m. yqg windsor ‐ yul pierre elliott trudeau intl 1 stop ytz 10h 28m porter airlines 1 0 c$ 269 / person c$ 538 total mytrip.com view deal
         */

        List<SearchResponse> searchResponses = KayakParser.parseKayakData(data.toString());
        System.out.println(searchResponses);
    }
}