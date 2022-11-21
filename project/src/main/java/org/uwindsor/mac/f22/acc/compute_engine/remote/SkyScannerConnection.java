package org.uwindsor.mac.f22.acc.compute_engine.remote;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * @author Vivek
 * @since 18/11/22
 */
public class SkyScannerConnection {

    public static void main(String[] args) throws IOException {
        URL url = new URL("https://www.skyscanner.ca/transport/flights/yqg/yul/221123/?adults=1&adultsv2=1&cabinclass=economy&children=0&childrenv2=&destinationentityid=27536613&inboundaltsenabled=false&infants=0&originentityid=27537339&outboundaltsenabled=false&preferdirects=false&ref=home&rtn=0");
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
        httpsURLConnection.setDoOutput(true);
        httpsURLConnection.setDoInput(true);
        httpsURLConnection.connect();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(line);
        }
        bufferedReader.close();
    }
}