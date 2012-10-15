package com.ae.apps.pnrstatus.utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.util.Log;

/**
 * utility class for perform Http related functions
 */
public class HttpUtils {

  /**
   * perform an Http POST to the supplied urlString with the supplied
   * requestHeaders and formParameters
   * @return String the response contents
   * @param urlString the URL to post to
   * @param requestHeaders a Map of the request headernames and values to
   * be placed into the request
   * @param formParameters a Map of form parameters and values to be placed
   * into the request
   * @throws MalformedURLException reports problems with the urlString
   * @throws ProtocolException reports problems performing Http POST
   * @throws IOException reports I/O sending and/or retrieving data over Http
   */
  public static String postForm( String urlString,
                                 Map requestHeaders,
                                 Map formParameters )
      throws MalformedURLException, ProtocolException, IOException {
    return post( urlString, requestHeaders, formParameters, null );
  }

  /**
   * perform an Http POST to the supplied urlString with the supplied
   * requestHeaders and contents
   * @return String the response contents
   * @param urlString the URL to post to
   * @param requestHeaders a Map of the request headernames and values to
   * be placed into the request
   * @param contents the contents of the HTTP request
   * @throws MalformedURLException reports problems with the urlString
   * @throws ProtocolException reports problems performing Http POST
   * @throws IOException reports I/O sending and/or retrieving data over Http
   */
  public static String postContents( String urlString,
                                     Map requestHeaders,
                                     String contents )
      throws MalformedURLException, ProtocolException, IOException {
    return post( urlString, requestHeaders, null, contents );
  }

  /**
   * perform an Http POST to the supplied urlString with the supplied
   * requestHeaders and formParameters
   * @return String the response contents
   * @param urlString the URL to post to
   * @param requestHeaders a Map of the request headernames and values to
   * be placed into the request
   * @param formParameters a Map of form parameters and values to be placed
   * into the request
   * @param contents the contents of the HTTP request
   * @throws MalformedURLException reports problems with the urlString
   * @throws ProtocolException reports problems performing Http POST
   * @throws IOException reports I/O sending and/or retrieving data over Http
   */
  public static String post( String urlString,
                             Map requestHeaders,
                             Map formParameters,
                             String requestContents )
      throws MalformedURLException, ProtocolException, IOException {
    // open url connection
    URL url = new URL( urlString );
    HttpURLConnection con = (HttpURLConnection) url.openConnection();

    // set up url connection to post information and
    // retrieve information back
    con.setRequestMethod( "POST" );
    con.setDoInput( true );
    con.setDoOutput( true );

    // add all the request headers
    if( requestHeaders != null ) {
      Set headers = requestHeaders.keySet();
      for( Iterator it = headers.iterator(); it.hasNext(); ) {
        String headerName = (String) it.next();
        String headerValue = (String) requestHeaders.get( headerName );
        con.setRequestProperty( headerName, headerValue );
      }
    }

    // add url form parameters
    DataOutputStream ostream = null;
    try {
      ostream = new DataOutputStream( con.getOutputStream() );
      if( formParameters != null ) {
        Set parameters = formParameters.keySet();
        Iterator it = parameters.iterator();
        StringBuffer buf = new StringBuffer();

        for( int i = 0, paramCount = 0; it.hasNext(); i++ ) {
          String parameterName = (String) it.next();
          String parameterValue = (String) formParameters.get( parameterName );

          if( parameterValue != null ) {
            //parameterValue = URLEncoder.encode( parameterValue );
            if( paramCount > 0 ) {
              buf.append( "&" );
            }
            buf.append( parameterName );
            buf.append( "=" );
            buf.append( parameterValue );
            ++paramCount;
          }
        }
        int length = (buf.length() > 1024) ? 1024 : buf.length();
        Log.d(AppConstants.TAG, "Post Params : " + buf.substring(0, length));
        
        System.out.println( "Post Params : " + buf.toString() );
        ostream.writeBytes( buf.toString() );
      }

      if( requestContents != null ) {
        ostream.writeBytes( requestContents );
      }

    }
    catch (Exception e) {
    	String msg = e.getMessage();
    	Log.e(AppConstants.TAG, "err " + msg);
    	Log.e(AppConstants.TAG, "err " + e.getStackTrace()[0].toString());
		e.printStackTrace();
	}
    finally {
      if( ostream != null ) {
        ostream.flush();
        ostream.close();
      }
    }

    Object contents = con.getContent();
    InputStream is = (InputStream) contents;
    StringBuffer buf = new StringBuffer();
    int c;
    while( ( c = is.read() ) != -1 ) {
      buf.append( (char) c );
    }
    con.disconnect();
    return buf.toString();
  }
}