package zw.co.vokers.vinceg.boutiquor.utils;

/**
 * Created by Vince G on 23/2/2019
 */

public class AppConfig {

    //public static final String BASE_URL = "http://192.168.137.172/boutiquor/";
    //public static final String BASE_URL = "http://192.168.1.24/boutiquor/";
    public static final String BASE_URL = "http://192.168.10.104/boutiquor/";
    // Server user login url
    public static String URL_LOGIN = BASE_URL + "mobileconnect/login.php";

    public static String URL_REGISTER = BASE_URL + "mobileconnect/register.php";

    public static final String URL_PLACE_ORDER = BASE_URL + "mobileconnect/order_request.php";

    public static final String URL_SEARCH = BASE_URL + "mobileconnect/search.php?ordernumber=";

    public static final String URL_EVERYTHING = BASE_URL + "api.php?everything";

    public static final String URL_BEERS = BASE_URL + "api.php?beers";

    public static final String URL_SPIRITS = BASE_URL + "api.php?spirits";

    public static final String URL_VERIFY_PAYMENT = BASE_URL + "/v1/verifyPayment";

    public final static String PUBNUB_PUBLISH_KEY = "pub-c-34dcae8b-3a1c-4408-9cf4-76008f1167ed";

    public final static String PUBNUB_SUBSCRIBE_KEY = "sub-c-1a4f1a42-435c-11e9-bd6d-163ac0efd868";

    public final static String PUBNUB_CHANNEL_NAME = "boutiquor_tracker";

}
