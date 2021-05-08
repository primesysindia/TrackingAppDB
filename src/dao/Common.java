package dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.ws.rs.core.UriBuilder;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gcm.server.InvalidRequestException;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.mongodb.Mongo;

import dto.MessageObject;
import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
public class Common {

	
	
	// LIve URL	

/*	public static String SQL_Connection="jdbc:sqlserver://114.143.99.170:1433;database=TrackingApp;user=sa;password=Pr1m3sys@MKT$2019;authenticationScheme=JavaKerberos;integratedSecurity=false;";
//	public static final String SQL_Connection = "jdbc:sqlserver://localhost:1466;database=TrackingAppDB;user=sa;password=Pr1m3sys@MKT$2020;authenticationScheme=JavaKerberos;integratedSecurity=false;";

	public static final String SQL_Connection_Sun_Mssql = "jdbc:sqlserver://localhost:1466;database=TrackingApp;user=sa;password=Pr1m3sys@MKT$2020;authenticationScheme=JavaKerberos;integratedSecurity=false;";

	//public static String SQL_Connection="jdbc:sqlserver://157.230.228.152:1433;database=TrackingApp;user=sa;password=Pr1m3sys;authenticationScheme=JavaKerberos;integratedSecurity=false;";
	public static String mongo_Connection="127.0.0.1";

	public static String socketURL_Connection="127.0.0.1";

	
	//for Live 
	public static String key = "a6doMg";// "gtKFFx";//put your key
	public static String salt = "XpRAicke";// "eCwWELxi";//put your salt
	public static String Log_path="/home/prime/MyKiddyLog/";
	//old key
	public static final String GOOGLE_SERVER_KEY = "AIzaSyDjNYg08r6MY0am4EbCXpBNcB0S9ibAWX4";
		
	//new key generated in dir demo project
	//public static final String GOOGLE_SERVER_KEY = "AIzaSyDdfUs3Tawl80gkZRyBSlb0138Du9GTKsQ";

	public static final String MESSAGE_KEY = "message";	
	final static String GCM_API_KEY = "AIzaSyCSdcZQ3plIXs3zAazsskOaId6Wkq-hA7o";
	final static String HSPSMS_API_KEY = "03b668ab-2626-4190-a03f-153697503b9a";
	public static final String HSPSMS_CALLER_API_KEY = "8e759ab1-1572-4f3f-a9d3-eddc7581ccfc";	
 
 
 // for live 
	public static	String  ServerFileUplod_Path="/C:/inetpub/wwwroot/Kalakhoj/Images/ShopImages/";
	public static	String  ServerProductFileUplod_Path="/C:/inetpub/wwwroot/Kalakhoj/Images/ProductImage/";
	public static	String  ServerInvoiceUplod_Path="/C:/inetpub/wwwroot/Kalakhoj/InvoiceFiles/";
	public static	String  ServerDevice_ONOFF_FileUplod_Path="/home/prime/PrimesysTrackReport/Phulera/DeviceOnOffReport/";
	public static	String  ServerDevice_Trip_FileUplod_Path="/home/prime/PrimesysTrackReport/Phulera/DeviceTripReport/";
//	public static final String ServerDevice_Exception_Trip_FileUplod_Path_PETROLMAN = "/home/prime/PrimesysTrackReport/Phulera/ExceptionReport_Pertolman/";;
	public static final String ServerDevice_Exception_Trip_FileUplod_Path_PETROLMAN = "/home/prime/PrimesysTrackReport/Phulera/ExceptionReport_Keyman/";;
	public static final String ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN = "/home/prime/PrimesysTrackReport/Phulera/ExceptionReport_Keyman/";;
	public static final String ServerIssueFileUploadPath = "/home/prime/PrimesysTrackReport/PrimesysAdminIssueAttachedFile/";
		public static final String ServerDevice_PaymentSub_Trip_FileUplod_Path_PAYSUBSCROPTION = "/home/prime/PaymentSubNotifi/";;

	//Production
	public static final   String dbDumpfileName = "/home/prime/MongoToSqlDumpData/export.csv";
    //  Paytm Live Data
   
    public static String MID = "PRTECH60810536349319";
    public static String MercahntKey = "T!qQMu4Y#WqEylSF";
    public static String INDUSTRY_TYPE_ID = "Retail109";
    public static String CHANNLE_ID = "WAP";
    public static String WEBSITE = "PRTECHWEB";
    public static String CALLBACK_URL = "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=";
*/
	
	//Local URL
	//Moon Server MSSQL COnnection	
	public static String SQL_Connection="jdbc:sqlserver://114.143.99.170:1433;database=TrackingApp;user=sa;password=Pr1m3sys@MKT$2019;authenticationScheme=JavaKerberos;integratedSecurity=false;";
//	public static final String SQL_Connection = "jdbc:sqlserver://114.143.99.170:1466;database=TrackingAppDB;user=sa;password=Pr1m3sys@MKT$2020;authenticationScheme=JavaKerberos;integratedSecurity=false;";

	public static String mongo_Connection="114.143.99.170";
	
	//SUN server MSSQL COnnection
	public static final String SQL_Connection_Sun_Mssql = "jdbc:sqlserver://114.143.99.170:1466;database=TrackingApp;user=sa;password=Pr1m3sys@MKT$2020;authenticationScheme=JavaKerberos;integratedSecurity=false;";
//
//	
//	public static String SQL_Connection="jdbc:sqlserver://123.252.246.214:1433;database=TrackingApp;user=sa;password=Prime@MKT;authenticationScheme=JavaKerberos;integratedSecurity=false;";
//	public static String mongo_Connection="127.0.0.1";
	//public static String mongo_Connection="192.168.1.101";
//	public static String mongo_Connection="157.230.228.152"; // Mongo Db on Digital Ocean Cloud

	//public static String SQL_Connection="jdbc:sqlserver://192.168.1.110:1433;database=TrackingApp;user=sa;password=Prime@MKT;authenticationScheme=JavaKerberos;integratedSecurity=false;";

	public static String socketURL_Connection="127.0.0.1";

	public static String key = "40747T";// "gtKFFx";//put your key
	public static  String salt = "cJHb2BC9";// "eCwWELxi";//put your salt
	public static String Log_path="/home/pt002/Log/";
	//old key
	//public static final String GOOGLE_SERVER_KEY = "AIzaSyDjNYg08r6MY0am4EbCXpBNcB0S9ibAWX4";
	
	//new key generated in dir demo project
	public static final String GOOGLE_SERVER_KEY = "AIzaSyDdfUs3Tawl80gkZRyBSlb0138Du9GTKsQ";

	final static String GCM_API_KEY = "AIzaSyCSdcZQ3plIXs3zAazsskOaId6Wkq-hA7o";
	final static String HSPSMS_API_KEY = "03b668ab-2626-4190-a03f-153697503b9a";
	public static final String HSPSMS_CALLER_API_KEY = "8e759ab1-1572-4f3f-a9d3-eddc7581ccfc";	
	//  Paytm Local Data
    public static String MID = "Primes84973633105435";
    public static String MercahntKey = "wJ!YZIYs3DRvMOvh";
    public static String INDUSTRY_TYPE_ID = "Retail";
    public static String CHANNLE_ID = "WAP";
    public static String WEBSITE = "APP_STAGING";
    public static String CALLBACK_URL = "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=";
   

	//Development test path 
	
	public static	String  ServerDevice_ONOFF_FileUplod_Path="/home/pt002/PrimesysTrackReport/Phulera/DeviceOnOffReport/";
	public static	String  ServerDevice_Trip_FileUplod_Path="/home/pt002/PrimesysTrackReport/Phulera/DeviceTripReport/";
	public static final String ServerDevice_Exception_Trip_FileUplod_Path_PETROLMAN = "/home/pt002/PrimesysTrackReport/Phulera/DeviceTripReport/";;
	public static final String ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN = "/home/pt002/PrimesysTrackReport/Phulera/DeviceTripReport/";;
	public static final String ServerDevice_PaymentSub_Trip_FileUplod_Path_PAYSUBSCROPTION = "/home/pt002/PaymentSubNotifi/";;
	public static final String ServerIssueFileUploadPath = "/home/pt002/PrimesysAdminIssueAttachedFile/";
	  //local
	public static final  String dbDumpfileName = "/home/pt002/MongoToSqlDumpData/export.csv";
    //Production
//  public static final   String fileName = "/home/prime/MongoToSqlDumpData/export.csv";
	// final static String GCM_API_KEY = "AIzaSyBxP5hdk1anGTuwBRKuYWWDLmxeti1eWg0";
	public static final String MESSAGE_KEY = "message";
	
   
  //Local URL End---------------------------------
   

 
	
 /*  //  paytm live
  *   public static String MID = "PRTECH60810536349319";
    public static String MercahntKey = "T!qQMu4Y#WqEylSF";
    public static String INDUSTRY_TYPE_ID = "Retail109";
    public static String CHANNLE_ID = "WAP";
    public static String WEBSITE = "PRTECHWEB";
   	public static String CALLBACK_URL = "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=";
     */
	
    //	String mongoClientURI = "mongodb://" + "ram" + ":" + "123" + "@" + "192.168.1.101" + ":" + "27017" + "/" + "tracking";
	//public static  String mongo_Connection = "mongodb://ram:123@192.168.1.101:27017/tracking";

    //authenticationScheme=JavaKerberos;
	public static String Photo="iVBORw0KGgoAAAANSUhEUgAAAHgAAAB4CAMAAAAOusbgAAADAFBMVEX////HggHBgADFggDEgQDGgwH+/f2+fQD9/fzDgQD5693GgQG4egH///7//P+/fgH36tr9//////vAfwD//v/+///CfQD6///DfwH8/f/35tL24Mj/+v+7eQC9egD///3569v26df569+5ewD7+/v7/vn35tX5+fm2fQH8//3//fr///f027/y1rn5/P/3486ocAH149G6dgD++/bw1LXOuqXAgQH03cKFSy6CRyqLTC5uPCX7+f+PTzFzPyZpNx75//v/+vv47t/248vy2Lv8///DgwbLggR4QSi+fAOzdgO7fgH7/fPz3cf23cZ/Qya7fAD99/T32rnIfQH9/vn++O7v6Nzw2sLSvqe6eQrBeQL36NTJt6H2/////vP88/H56Nd0QyqhdAbm2sNwORzHhQT3/ffq0bb88er2585nMBnBfQz08e377eLVwqz37Nvv4M/u1r/x0LCUUTHdzcHg0rzFqn18Ri2leym9gQW/cwWwcQP29/KsdgPy7eXqzq+qgjXFeAbFhQO1eQHz+/v19vj+8uTy5tbp3tPm07763bz21bbj0Lbbx7F4Ph9dMBvm2c3f08vx3b2reRP+8t2wloe5gwP99fn+69b65dP638rp3cqwikGaVTJvRTGjdRjPgQS/hgLt49bTwbRkOiT//uzo0seVaVGyehLHfgv47+P77d/p5M364cPYwKDNtJq9p5HOtYKTcWCDVT52Tjiofx+1fArEiAKyfQL35+PEsp7Cr5aKRyjIhgu4bwLy7dzz5Nv+58ngyrfqyKq2oI1aJxDBhgz+9P7WyrjIvKvXr4vIoYSng2vVsGqjd1u0lErDlD3+9+fq39vgxqntz6Dfv4bbtHi3ml7DolqPX0bKigSjagT0/+/t5+n648zd0K7YtpvIqo+2gx+bbxLRiATl19Xkv6GdiXTMp2Guklp9XEu0ii3DkiaKRSDFdRL19+ray5y9nICkjHy7pXG/l1LNnEOeXDjsx5C3jXTVpFaYYwTq27LRvY2fhEfAgyTDixOjj8e4AAAWv0lEQVRo3qzYS0gbeRwH8DkNQ2EkMySEJIXEMmbI5FFCsuRlja9ADpsMCcYsuFaloWrQbRKVXKS51LdNaqissZhDTRswtS0ml+4KrWuFWkXY9uShrj3UgiClPRSP+5sYXbsXIZkvmgcqH7////z/kxnkoqBY6QVGUSpVfT3ZMjxsYORCkVAobEAZxmNQq9UkKadHRlCExwCMnr5C0QYKRVRYfX29WCfR6WprJTiOkmRLDYWKCIwQ8QxD0CJedbnhMjUycqe+vb0dXAkXnVh3h6ZUBKHTETzL4FIU1AZYUlUlGRmhVecBkVinE0vEDngUEzxXpmia4mYa6qnkcsbQ8eLT4JvFxcU3gzPjrT0dDfATCUYQMPwI73Kxsri9/mZf68PHi7Mr+S57IpFJH64cf1z8dNvDoDQmEt8BmG+6mJqff24FtGCz2eKBQJw1my3OukB+e/buK4PBwGCloeZ5oml6pEP/6+dCJmMHNm6xWNh4nLWsrprN0d3ju7nrDE1j/MMozDMymM2nM3Z7ANrG66JmsM3WVfboyGouHH5b7EN0vMJyNUkQiBDxPJ49tNvttrquujpn1OmMWsxms9VqXV21Dlxjnebs7OB1+HVoTRA4OoxVDLd8UaugSsfgdmYDXIC7ohCWZS0cfA2ysDCwuhrPbw91IA30CO1Q1dzkAab6aIr6vWNxJQMslzpoDH0tJXcAYJAHLNH89mBDA06PECp6mMSRiqMapoWcmwg4T2Ggz9wBq3VgYGHhOwx39NsgHNyIUFxfQ8ordkUOCvWEDhO2AMt2ddWVEjVDwAX4GrADC98Xrlks+W+P+z0oLSbIGrRiWAL70afDdHCDZZ1dpRSHugSD+f37AmTAuuosrLQyHpRQ1bTIK4fvIC9eJ2zOQDD+I8zJZ/AABJZWIP1bD4OIqBEe5lhEDW+uZAJsNHg2wwA7WfZ0liHgw7fVehR3rgxyf0LzACPYzOe0LRB32jKJYBBWchEH+EQGGpYyPHBzfnTEZmY7JHBuZuSVw4bNw4TdHrRnYLOEZ8A5GTqfrigYYyu48PboKJ5YWaQQjDZUDpP3Z/N2e10mYctns9ndQiaRANlmi8IezVqsVqg5YLVYYKRZCxu1OPOzHkRSi1Y+1MNvVtI2OAcWtt8exCLr+8e76QQkaOdKw+HFjTQbd0LjOAtwvPD5NimSNFQO9/10mLEFNwrb65PLGvf8vDv8/utKOrEBCRYD5ww7l0D8mhlg28oMjVQZUB7gQgKGeufp8rK7t8nlapqb0//102z2kDstd0Xzu9nszs729k42H2VhDOL2w80+BOcBvv08nbAHdrcm17zNIalUH9ILcqOj+r9++fM55OOz/adP33eHw90wB3XQmrUX/gCYh6P63XY6GHDu7K8ta/wPxhSKsbExQWPj6N/3748+evQInorJjc5Fvu46AwFLoPD6A4KTlcOfVtLBYPR4HSa4Sa/Xh0IhQa5x7OXSy9bWl5DGxvs5wVgopMyl1reyLMDp2XdVpJoPuGsjmN+Kmfwmf/jq3JxSD5Wrx169WFpaegn6w4evrvT0LAlyc3vTX3cDLAc3tPABZ+0bwd2tWNLUJHPtpVJX9crOztCrFz0dPf0v+nu4gNs6PqaYk+3vxNkgwJ7hcmFhFYrBlZJKQotmsnXBwO6+N+nyu/RXpVKpslogqBZc6e/vv3IpB4/QvLFRMDZW3dkWOzY7bYXZ8Zb6O+LyYLyhAatRicViDu4KBrIHy0m/ew/mWNkGruCW4pLiEpcr8NXYeEkB/0u1slrzdrcumP72kGwvH0ZRlUrsICQIB9t21tdME/MpgKWn8InLpRHkW0V47WDHuZH51iq/KRaVB8sbUPjo4SAQHLkHB1fdcWQtOS27CFauTW3lE+nZlyTA5TYmcZEDw+QMMr6d3ohuTS0np/3zF8Fza+tZe+H5S3WNo1yYAVhEUaTa8+BreiN/MKVJhsOui+DetcmdrsLrN624ECkTJjmYppjrjRMf8107ES+sJrf7IjjUtPd1Z+fZ5jhSVaaMq1swEVJLMb7Q3Wfb21tTAO/Nyy6CnzzZO3j79v1Qa9mwXF2DiYS1tczo0C9P9w/WOdjlPptjkEHlcgoLOLjtyRN/JLL+/sYSebncxmQLhiCXLzM9M7+9j0xNTWlMJr97XgrrWAmwQnGKnsBXTmBlWyisnYo9vbGElgsjaGkHG/kwc7AeMzaDOu+eT0mlsF8qFLkS2l8KbCOdnZ1KZUrmbzbG/nnQg0sqhFG6b+ZpTKvRhkE+g6Hw+ZHmYEURbnK5Nd3/TFf7UB1RLszdBAAdGxnfj2lMxu4wwDIp5EcZ6FzjJdgyOVfp9+/5p+/eGFWTAFdw34OrTbT+Mh3WGLu1GplbdvUqyABDYKK5nOzZ8BZcqdQf3gtPDwl8JF7+nSeM5mBI39DQRLdW26zR+F1XQeYqd4J7KoN9izu02qQcLLsxMe6DtYgjFcAYwsXw6t7QtHZysrnZL2tqArmtrbMT2P/kU7dXakq6bozfVsuxGrJsGaa4ONUI6Xv4uFvr9Wq1piZIb68U4Orq6jNZcAt2jzYO7jUl/aHbHhzBKrhaxFAUXIoCeYlbnRFjs6wEgwzwGQ0uFAa2CAs8QsJBYDexcmFwORjGm/TkZMZILJIEuFS5rQiX9HNwMukaZQid2FHzpQapNMJalPHpJ7qN3sllt1sm46YZSitP05ZKKZTViram+Xmt1tXpQ+FzC0EayMphuHPqEfg5eNIEMqypXpCVZ0ml5pRKRZtMNq8Nhy55ijAPH29BFqKefn0YZK/JVKwMnc9Fn+qEU4dU5nY3u5SjBtwhFqtIhh/Y4MuFQIa1DPL/ab0+BYPQCx9C3al+H4MTYh3FAMyDjLUwvmoYbKOGk93ccv4xTRBT2D+f8xkYXASwgRdYhWHk9VF9sXKp83laBjEtL/snwikfQ8pxhCjdWK8wuBzT6RykT+EyGmHnPOl8LnDl6jJpJpeTExMKw9nmw+CVw6RK195OqP9ONQMMsslk+kF2ufwmr3eyeXpolCmeG+TQmoegLap2uDAgfXOaIgyy+/+wpggPjraIHSKc5AnGMbiU0ekwte/exBS4EBhu138xmTTaSGTy7pt7XzBCJMLlOM4LLHJICAShKBx/NxjRejXaUu1i/BBT0huJRJp/3Wylz0D+YJrC5R8GD2JT3qILU30WU9I45fXGnm/eJhGeYYkIBRjvm9k/iBm1sHV6jcZScS7NxsnJ2LPniz/zD0v+rd18Y9qqogD+KNBC2r7a0kIb33uNlkppHWIr+KUfWnUltNW0o52Ao01o2mGKiATISimDljFQSMDOMoWEwQIBRTY+8GeQ4cQFpoaMbYBkTOdA45Ytc7oti1On5xbd0GDipP4SKH/a/t6597z7zjsXklHEKa2Xvj5XtuPNHW8989ZbzwDbdgBQITz7IXgnWhUpcf+HGHK18dKRob07dpR9UvbmjjIQQ72NKoQXPtx95OTkqZ08VYTFXK4IlgSxGNtZkrErd/cL23aXvbXtmQdseyb36MnDrTWwTkZWLBDw6YCcXeQ4vGsit/nlJ7fthfIP3LsRe88NTU5+Wn62xsxjR1osgOUoLoVX3Np26tOJjOk3ntz2Apy9L7wB2ubmoSNHJ3PL284Wm1URjpgPYlQFyWvaGhvLP53Y1dL0xhtNzc3T0y0tJSUtR344mVHR2nbWLjcnY2wEDA9/62I+TuB8+CRSq848UtrR4aj4dPLkyXd+/C7Mjz++c/TTU46O1sZj9tnZZGmYlJQUNb71tbpdrlBI1Wo+ZimCdmLPoKPicMbE0ZPvvPPOSeAoTG9bh6Opr6eIx0sNw0uNyOa51q6y9RqlsFO6r6bnxIkTjaWlHRWQ3hOTkxNARkl5W2uHw+EYrDFquJAOMNQCdmrc1vOMaA+LU7n85OITx46daGzcX+poKcndtWtXRkZGbklLeVufIyyu1ogAPiYQxKVEYIsPn9UqjEZXGh/X2h9/DYl7Sh3TG8XlTU0Vjo7GL/eJ1O1qNWpCxLkiIib4mup9aq39iXcbGsDc09jR1NyCxMhc0lLR11QB4tazX7arVFqCwHE+7HiJIyEW1e8Tsa0fVn528OCBD47t7IHMbnkQcXOTw9FR+nRTy7mXGmw2OY7D3lwkxHwcT6tP49s+Xhrp3Pv2wQOvQ6O4Y4N4GsJtLX3qzcXL8z8tnOm18XD1vn0uKTsCG9fSpO2YtmN4fvxq5cXnD+7v6dnfcQrmeGIC3GioO1rb+t5YuLyaHj13atAKL0lL0zyUmC9SiyAlt2ukUguB86QuV0ESOMW9B8sgnNXRUH/tXNnBoi+/bau4mJuRsR7x4cPl35Y++tadn+55vfmrV2/P3dlx0IzzRXQFTwBXFwLABXA9T3FpAPbmYkKNQ5/JWF3tssTFpUrhiaozDa8231i6dS9Pr2dJ9N75pbL3zn7bVg4nMjKjoS6Z7ju3OHf7mjubyk8U0mau/bT0yfMNVpyP8+NcrjicIOh0nlxqlEo1Lhf3H8SEymyzWSGFRTC8Aj5u/Xhh8fLpq6v9eiWVn51tCMHbLu7tA6ZzIWZEbu7Q4uXOkeDojJNJffNNFqXvX52fW7zz4mtWMV3qqndppAqFSsXjybXa5BT6PyRwu73YalOIoaWXxJWb3390YWm+P92HrIDbrfMbQtduzy19Bly6dOlzoLLy8+Vrq8F0rynG6YzPOnQoJzGf4ZmZ+e324sIrRVq8vv7CBaNCIefRte3tcGL+Y+cUg+uBCGdr24u+WpibD654p5ReZWJOTr5Sqdfn1eWTo+O+wsLCruBIV1fhOum+gEkYIxQync6onJysMDk5oRAMzrN2Lc6F2jgNWt98AtTszfP3gkuTJILz33bm1YWl011r6bJuHYcilUolyfGExn3eqSq30CALdY+dP6/L9BgkLJbMYGBwOImJSOh0+v1+sGdB4IfgUAOha3N3vrITOMZNKoA85VoUm4tTL7gK0ghcdeDnX5b7+0dH8/JMoe7ubrfb7YHBm+kOdUed/+Z8PC3aGU3lX79+vSpMXY6TmQNkRcVwdGNjx48fH3DnV01dP3ToG6dndPX20sJTRQr29oL6AtjA31zMrdfwbGCFdArqAywKZpUTCoUSdPBmOo+newzwM6Ojsg4N5NdlDeTU1dUhIZPGzAKYiQyDzqnLzAbc7oG6Ooh7INvk+e3a8mcLH/cq4CRJ+Yfk4quJ938ePj0S9E5NhWQsMh+icOflMTg6vzMWRcXs9mTqsrIGQBjlj44Hop3nz/sz/cePQ7yZ3WPOrKyoKGZULPwUnoqOJyrBIMu7Nz+38GovD64hG5OLJ+bj0CFKTYbl+OmhL0YK071UXf7oqMSUGAUSt5sGcxjtRAmbM6aD6Acgw3OiM2mZCZkJ8X7ncTSvoIv2j53PgvEF4GD8MEf+2CimMDHBk6jU988vPdOgxdNEdDGPx4ONltRUrNcCiwv8LVzxPvObc7V30/UkKZEZEsPEx8TEREetw2RG/RuYwJ9fxQhptMzMzHt5lDBRd/uT15LqofUOF3ejAKYcM8KlDIesS5U/fbm2a6U/OxvEsrBXiMTMqP8EE7EuntGh97p2eW8DXw1Lqc1mwSDPMb7ZrkItIsUrw2t373rdA4nUH2IhiKOjty4eG3NfHxCGQpd3E3wBNzmOB7PL52P8FGuvAlbzMzc6795NzxMmUqTB41kXgzcC4u5uEB/v9l1dOijHuDDPPJWcDic33daroOPtu2+trcAilU+RJEenC3thgmNjty72eDjugawxyLA7B8S4WsTlVZu1BJbGVtgUGP21yq67XjKbglWKYnAywYsCBnHsVsUAh6Ti/aaA+/bPvTxCBJ3OsDhVTGdj1p87V9K9LJIVMDEYJMUJe5mREet0HBbLxOEwKN3iq7aw2Ahio0akxnmvDI+kT+llsgArU8dQUqANi2MjIWYKGRyPgcWhUcrln88QeKrrgpGHY71S9SyhePt0cKrKEArJDDMgFmYhLxI7wbtVcSJJzvw2IzNkUt7OGwfWxZBcCkWciGi4Uziel8cCJCaDSWJiMGO3CHOdGLhgchgMDgw0QOl/+phI0ki1uMWIyeU8EfH0XLperw+LJRKTSWKIjYqIOAYBzj+Zuv0JvUBjIZAYSiL17PQtn8+7QcyIjo3eIn+KhcINYuW9YavLxcOJZBDjfO0Hw3tAHNgQcfSWue/dKKb6b318BrpDanUKhhN06wu31vT6QKTFMcj7N7H36sIBhVggUhMgVh1YvLoyRRlkkRQjK/LCErhRHFide1UhSOLiBMbXFn24HEyvcns2imMigRC8fxMzPPPbbAKNJo7AMPrrN66me6+7R8cjKxYi70YxgpO9eqdB4DJaCMwi3z88sjJ1/brex5L8TRwdvTXvuniDNzFf2f/ZCQHU2gRW8P3F5SAUrlWkjBWGBLJRCRBPS0igwcN/QxgfT0NiVPxCsZQoRBdcmUlZ9VOZGZYuG1YwOHwVKlmvhGECax4AYooWr9PRGBTFgYeH4K/uTBoHAbcDVVVgTiSV46H8qntLjxUkKXqx6ublQq8yTz+q16OhRl4Y7QQaGDmQDPHx/9EL0JA4O1unM7ACLBJqKtKjy5nKW754IYmrxax7b62seAM+/ahEArGyEDIJI0Gni0dZkUD7b4AXZgp5s2UIg2cG7gyycw7VyeZvfFQvIrCi3TDFhm6Ph1KSoEZa2biM4oTH+r+bwQsFMPJmG2TrsAIBuAujVpdvfA+XYozdM1xbWDhucObUgVUiQ4wHSEa8zg8hQzXyMLqE+4S/QWKKgkBJlpI0Zfr9NCrQ3zk8aITuDFZQ7ajsXLu7oq+rC/x5aLKAkgG1ui6BQZGMh/ciHnipKkitfDiFodxMLxy59Vmfnc222LD6AnPj0M1f17qCXrhTGx+Hk4pEB0hzgpiCBH8YMSdhI5x18dRUFQXj7fMFg2u1Ny/ut0OTgKfAilNwwlx64+aVX/cEg0EQS0gqn9SD+DiMDKkkH0p8n3Ux8qL6kTXu6+ra8+uVHzJaq7cnJScTOI7Z5LOzUHkNXvzlZu3ICAQdYCmrqsJiZzyHWo9YKEQfGwd00wHm/EXMQWKShDJ9NBgcqb1SmTt9jBDA7plLjBME+i+mOCA5mVtdXjLxAwS+p6srmEdScLeN7sEODQB16+TdB6IAKASJZgah18MRQ9uCygZleJE2yMZ96el7fv31ys3J3IrBj77f2GikIzOCm1xcs3Ow6cYvlV+c7uwCgj6fT6+H0jO8fptIilyvyRASZRgSPiQA6w8CgQC8wgeAcGWta09t5+lbw0NDF5sGP7KqLGJso/i+Obm+3qWynnn9eUfzueHhypvLcAC1tXsKH5C+KSv3vypcW4P+SNcI0Hnlyhc3K4eHzjU3Pfbc+0VWs1bMxnHwbRJzchpqVHDF0B2yPvfc/kHHxZJdk5OVXyBOA51wGIg9f6M2TCeAbKCrHP7ll9zDpxyNO2uK7WZoNYnDTR0A29zM5W5Pgo4zdOSgwy6QplQX19Ts7GkF+srD+w8l0N06cmRy8tLRygdcOgKghhc0205Nl/e1nj12rKiopsauxYDtSWmofY6BmY2zsc3NcjmdzYdDUwNpaajn/Qc4MdtutxfbZ9vb9xUXfwnsfEANUFxst5vN7Xb0CD1rgiDEsJsrpnNF0GuCe+BULnQCLBbLpmY+3WxWITUO7gfw4XWwyWE0VluNSevAvsN27n3gV1KERpOqsCEUUniCRYEaeiqpRSyG1qQUgOcZN2TX79PNS88lmDgVAAAAAElFTkSuQmCC";
	private static String formatted_address;
	public static String TABLE_SOS="sos";
	public static String TABLE_LOCATION="location";
	public static String TABLE_TRIPREPORT="trip_report";
	public static String TABLE_DEVICE = "devices";
	public static String TABLE_USER_ANALYSIS = "use_analysis";
	public static final String TABLE_EMP_TRIPREPORT = "emp_trip_report";
	public static final String TABLE_EMPGCMKEY = "emp_gcm_key";
	public static final String TABLE_GEOFENCE = "geo_fence";	
	
	public static final String TABLE_TODAYLOCATION = "todays_location";
	public static final String TABLE_TODAYLOCATION1 = "todays_location1";

	public static final String TABLE_RAILADDRESS = "rail_address";
	public static final String TABLE_NEARBYFEATURE_RAILADDRESS = "near_by_signal_railaddress";	
	public static final String TABLE_ALERT_MSG = "alert_msg";
	public static final String TABLE_AllSectioWorkStatusReport = "allsection_work_status_report";
	public static final String TABLE_COMMAND_HISTORY = "commad_history";
	public static final String TABLE_GATEMITRAREPORT = "gate_mitra_report";
	public static final String TABLE_COMMANDTOBESEND = "command_resend_config";
	public static long DeviceStatusTimeDiff=300;
	public static String RED="#e50000";
	public static String GREEN="#007300";
	public static String BLUE="#0000ff";

	public static String GetAddress(String lat,String lang)
	{
	// making url request
			try {
				URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?latlng="+lat+","+lang+"&key="+GOOGLE_SERVER_KEY);
				//URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?latlng="+lat+","+lang);
				// making connection

				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");
			/*	if (conn.getResponseCode() != 200||conn.getResponseCode() != 500) {
					throw new RuntimeException("Failed : HTTP error code : "
							+ conn.getResponseCode());
				}*/

				// Reading data's from url
			   BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

				String output;
				String out="";
				while ((output = br.readLine()) != null) {
					//System.out.println(output);
					out+=output;
				}
				//System.out.println("Output from Server ....----"+out);
				try {
					// Converting Json formatted string into JSON object
					//JSONObject json = (JSONObject) JSONSerializer.toJSON(out);
					
				    JSONObject json = new JSONObject(out);

					JSONArray results=json.getJSONArray("results");
					if(results.length()>0)
					{
					JSONObject rec = results.getJSONObject(0);
					JSONArray address_components=rec.getJSONArray("address_components");
					for(int i=0;i<(address_components).length();i++){
					JSONObject rec1 = address_components.getJSONObject(i);
					//trace(rec1.getString("long_name"));
					JSONArray types=rec1.getJSONArray("types");
					String comp=types.getString(0);
/*
					if(comp.equals("locality")){
						System.out.println("city ————-"+rec1.getString("long_name"));
					}
					else if(comp.equals("country")){
						System.out.println("country ———-"+rec1.getString("long_name"));
					}*/
					}
					 formatted_address = rec.getString("formatted_address");
					 
					}else {
						 formatted_address ="http://maps.google.com/maps?q="+lat+","+lang;
					}
					System.out.println("formatted_address————–"+formatted_address);
					conn.disconnect();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return formatted_address+"";
			
	}	
	public static String getDateCurrentTimeZone(long timestamp) {
		long dv = Long.valueOf(timestamp)*1000;
		Date df = new Date(dv);
		String vv = new SimpleDateFormat("dd-MMM-yy HH:mm").format(df);
		return vv;
	}
	
	
	public static String getTimeCurrentTimeZone_HH_mm(long timestamp) {
		long dv = Long.valueOf(timestamp)*1000;
		Date df = new Date(dv);
		String vv = new SimpleDateFormat("HH:mm").format(df);
		return vv;
	}
	public static String getDateFromLong(long timestamp) {
		long dv = Long.valueOf(timestamp)*1000;
		Date df = new Date(dv);
		String vv = new SimpleDateFormat("dd-MMM-yy").format(df);
		return vv;
	}
	public static String getDateFromLong_in_dd_mm_yyyy(long timestamp) {
		long dv = Long.valueOf(timestamp)*1000;
		Date df = new Date(dv);
		String vv = new SimpleDateFormat("dd-MM-yyyy").format(df);
		return vv;
	}
	public static long getGMTTimeStampFromDate(String datetime) {
		long timeStamp = 0;
		Date localTime = new Date();

		String format = "dd-MM-yyyy hh:mm aa";
		SimpleDateFormat sdfLocalFormat = new SimpleDateFormat(format);
		sdfLocalFormat.setTimeZone(TimeZone.getDefault());

		try {

			localTime = (Date) sdfLocalFormat.parse(datetime);

			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"),
					Locale.getDefault());
			
			@SuppressWarnings("unused")
			TimeZone tz = cal.getTimeZone();

			cal.setTime(localTime);

			timeStamp = (localTime.getTime()/1000L);
			System.out.println("GMT TimeStamp: "+ " Date TimegmtTime: " + datetime
					+ ", GMT TimeStamp : " + localTime.getTime());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return timeStamp;

	}
	public static long getUs_GMTTimeStampFromDate(String datetime) {
		long timeStamp = 0;

		try{
			

			String format = "dd-MM-yyyy hh:mm aa";
			SimpleDateFormat sdfLocalFormat = new SimpleDateFormat(format);
			  DateFormat estFormat = new SimpleDateFormat();
			  DateFormat istFormat = new SimpleDateFormat();
			  TimeZone istTime = TimeZone.getTimeZone("IST");
			  TimeZone estTime = TimeZone.getTimeZone("EST");
			  estFormat.setTimeZone(istTime);
			  
			sdfLocalFormat.setTimeZone(estTime);

				Date localTime = (Date) sdfLocalFormat.parse(datetime);
				
		
				istFormat.setTimeZone(estTime);
		  System.out.println("EST Time: " + estFormat.format(localTime));
		  System.out.println("IST Time: " + istFormat.format(localTime));
		  
		  
			timeStamp = (localTime.getTime()/1000L);

			System.out.println("GMT TimeStamp US: "+ " Date TimegmtTime: " + datetime
					+ ", GMT TimeStamp US: " + localTime.getTime());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return timeStamp;

	}
	public static String GetDateTimeWithOffset( Long time_long,final String offset) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time_long*1000);
        System.out.println("get for cal offset----"+offset);

        TimeZone.setDefault(TimeZone.getTimeZone(offset.trim()));
       
        
        System.out.println("Time TimeZone----"+calendar.getTimeZone());


    	String format = "dd-MM-yyyy hh:mm aa";
		SimpleDateFormat sdfLocalFormat = new SimpleDateFormat(format);
		
		String Converted_time=sdfLocalFormat.format(calendar.getTime());
        System.err.println("Time Offset----"+calendar.getTime()+"------"+Converted_time);

		return Converted_time;
        

		
	}
	public static String GetDateWithOffset( Long time_long,final String offset) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time_long*1000);
        System.out.println("get for cal offset----"+offset);

        TimeZone.setDefault(TimeZone.getTimeZone(offset.trim()));
       
        
        System.out.println("Time TimeZone----"+calendar.getTimeZone());


    	String format = "dd-MM-yyyy ";
		SimpleDateFormat sdfLocalFormat = new SimpleDateFormat(format);
		
		String Converted_time=sdfLocalFormat.format(calendar.getTime());
        System.err.println("Time Offset----"+calendar.getTime()+"------"+Converted_time);

		return Converted_time;
        

		
	}
	public static void SendGCMPushNotofication(String regId ,String userMessage) {
		
		try {
		    final int retries = 3;

			Sender sender = new Sender(GCM_API_KEY);
			Message message = new Message.Builder().timeToLive(30)
					.delayWhileIdle(true).addData(MESSAGE_KEY, userMessage).build();
		

			System.out.println("regId---2--: " + regId);
			Result result = sender.send(message, regId, retries);
			System.out.println("regId---result--: " + result.getMessageId()+"----"+result.getErrorCodeName()+"----"+result.getCanonicalRegistrationId());

		} catch (IOException ioe) {
			ioe.printStackTrace();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static String getDateFromLong_in_mm_dd_yyyy_hh_MM(long endTime, String time) {
		String vv ="";
		long dv = Long.valueOf(endTime)*1000;
		Date df = new Date(dv);
		
		if (time.equalsIgnoreCase("end")) 
		 vv = new SimpleDateFormat("MM/dd/yyyy 23:59").format(df);
		else
			 vv = new SimpleDateFormat("MM/dd/yyyy 00:00").format(df);

		System.err.println("mm-dd-yyyy------"+vv);
		return vv;
	}
	
	/*
	public static boolean SendGCMPushNotofication1(String userMessage) {

	
	    final int retries = 3;
	    final String notificationToken = "APA91bGlsj0i5R67hVQlOH-EGsXM4Y9p8LlzH2zi0HUXRu8o5KT4aIvMGC_bocPcUZ-KCCV4CP_mKNRMW0lR6Mvv-gYnorTXV7fUlRZrgES1seGr0YFk-_Uts_4rJx-IxunQGUqCOaUN";
	    Sender sender = new Sender(GCM_API_KEY);
	    Message msg = new Message.Builder().build();

	    try {
	                Result result = sender.send(msg, notificationToken, retries);
	    			System.out.println("11111111111---result--: " + result.getMessageId()+"----"+result.getErrorCodeName()+"----"+result.getCanonicalRegistrationId());

	                if (StringUtils.isEmpty(result.getErrorCodeName())) {
	                    return true;
	                }

	    } catch (InvalidRequestException e) {
	    	e.printStackTrace();
	    } catch (IOException e) {

	    	e.printStackTrace();
	    }
	    return false;
}*/
	 public static List<Integer> findWord(String textString, String word) {
	        List<Integer> indexes = new ArrayList<Integer>();
	        String lowerCaseTextString = textString.toLowerCase();
	        String lowerCaseWord = word.toLowerCase();
	 
	        int index = 0;
	        while(index != -1){
	            index = lowerCaseTextString.indexOf(lowerCaseWord, index);
	            if (index != -1) {
	                indexes.add(index);
	                index++;
	            }
	        }
	        return indexes;
	    }

	
}