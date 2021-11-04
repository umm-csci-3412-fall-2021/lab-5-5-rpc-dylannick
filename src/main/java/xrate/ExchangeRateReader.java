package xrate;
import java.io.InputStream;
import java.net.*;
import java.io.IOException;
import org.json.*;

/**
 * Provide access to basic currency exchange rate services.
 */
public class ExchangeRateReader {
    private String accessKey;
    private String baseURL;

    /**
     * Construct an exchange rate reader using the given base URL. All requests will
     * then be relative to that URL. If, for example, your source is Xavier Finance,
     * the base URL is http://api.finance.xaviermedia.com/api/ Rates for specific
     * days will be constructed from that URL by appending the year, month, and day;
     * the URL for 25 June 2010, for example, would be
     * http://api.finance.xaviermedia.com/api/2010/06/25.xml
     * 
     * @param baseURL the base URL for requests
     */
    public ExchangeRateReader(String baseURL) {
        this.baseURL=baseURL;
        readAccessKey();
    }

    /**
     * This reads the `fixer_io` access key from from the system environment and
     * assigns it to the field `accessKey`.
     * 
     * You don't have to change anything here.
     */
    private void readAccessKey() {
        // Read the desired environment variable.
        accessKey = System.getenv("FIXER_IO_ACCESS_KEY");
        // If that environment variable isn't defined, then
        // `getenv()` returns `null`. We'll throw a (custom)
        // exception if that happens since the program can't
        // really run if we don't have an access key.
        if (accessKey == null) {
            throw new MissingAccessKeyException();
        }
    }

    /**
     * Get the exchange rate for the specified currency against the base currency
     * (the Euro) on the specified date.
     * 
     * @param currencyCode the currency code for the desired currency
     * @param year         the year as a four digit integer
     * @param month        the month as an integer (1=Jan, 12=Dec)
     * @param day          the day of the month as an integer
     * @return the desired exchange rate
     * @throws IOException if there are problems reading from the server
     */
    public float getExchangeRate(String currencyCode, int year, int month, int day) throws IOException {
        String monthFinal = ""+month;
        String dayFinal = ""+day;
        if(month<10){
            monthFinal = "0"+month; }
        if(day<10){
            dayFinal = "0"+day; }
        String URLstr = baseURL+year+"-"+monthFinal+"-"+dayFinal+"?access_key="+accessKey;
        URL url = new URL(URLstr);
        InputStream stream = url.openStream();
        JSONTokener tokener = new JSONTokener(stream);
        JSONObject object = new JSONObject(tokener);
        return object.getJSONObject("rates").getFloat(currencyCode);
    }

    /**
     * Get the exchange rate of the first specified currency against the second on
     * the specified date.
     * 
     * @param fromCurrency the currency code we're exchanging *from*
     * @param toCurrency   the currency code we're exchanging *to*
     * @param year         the year as a four digit integer
     * @param month        the month as an integer (1=Jan, 12=Dec)
     * @param day          the day of the month as an integer
     * @return the desired exchange rate
     * @throws IOException if there are problems reading from the server
     */
    public float getExchangeRate(String fromCurrency, String toCurrency, int year, int month, int day)
            throws IOException {
        String monthFinal = ""+month;
        String dayFinal = ""+day;
        if(month<10){
            monthFinal = "0"+month; }
        if(day<10){
            dayFinal = "0"+day; }
        String URLstr = baseURL+year+"-"+monthFinal+"-"+dayFinal+"?access_key="+accessKey;
        URL url = new URL(URLstr);
        InputStream stream = url.openStream();
        JSONTokener tokener = new JSONTokener(stream);
        JSONObject object = new JSONObject(tokener);
        float From = object.getJSONObject("rates").getFloat(fromCurrency);
        float To = object.getJSONObject("rates").getFloat(toCurrency);
        return From/To;
    }
}