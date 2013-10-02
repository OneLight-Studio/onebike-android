package com.onelightstudio.velibnroses.ws;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.onelightstudio.velibnroses.Log;

import javax.net.ssl.HttpsURLConnection;

public class WSRequest extends AsyncTask<Void, Void, JSONObject> {

    private static final int TIMEOUT = 5;

    private Context context;
    private String resource;
    private List<NameValuePair> params;
    private WSHandler handler;
    private Exception exception;

    public WSRequest(Context context, String resource) {
        this.context = context;
        this.resource = resource;
        params = new ArrayList<NameValuePair>();
        handler = new WSDefaultHandler(true) {
            @Override
            public void onResult(Context context, JSONObject result) {
            }

            @Override
            public void onError(Context context, int errorCode) {
            }

            @Override
            public void onException(Context context, Exception e) {
            }
        };
    }

    public void call() {
        execute();
    }

    public WSRequest handleWith(WSHandler handler) {
        if (handler != null) {
            this.handler = handler;
        }
        return this;
    }

    public WSRequest withParam(String name, Object value) {
        if (value != null) {
            params.add(new BasicNameValuePair(name, String.valueOf(value)));
        }
        return this;
    }

    /**
     * Do the request
     * @return JSONObject, if the result is an array, the object has only one key named "list"
     */
    @Override
    protected JSONObject doInBackground(Void... nothing) {
        return wsRequestGet();
    }


    public JSONObject wsRequestGet() {
        JSONObject result = null;
        String uri = getURI();

        Log.d("URI: " + uri);

        InputStream in = null;
        Reader reader = null;
        HttpURLConnection urlConnection = null;

        if (uri != null) {
            try {

                URL url = new URL(uri);

                if (uri.startsWith("https")) {
                    urlConnection = (HttpsURLConnection) url.openConnection();
                } else {
                    urlConnection = (HttpURLConnection) url.openConnection();
                }

                urlConnection.setConnectTimeout(TIMEOUT * 1000);
                urlConnection.setReadTimeout(TIMEOUT * 1000);

                in = urlConnection.getInputStream();
                reader = new InputStreamReader(in, "UTF_8");
                int read;
                final char[] buffer = new char[0x10000];
                StringBuilder out = new StringBuilder();
                do {
                    read = reader.read(buffer, 0, buffer.length);
                    if (read > 0) {
                        out.append(buffer, 0, read);
                    }
                } while (read >= 0);
                String resultStr = out.toString();

                if (resultStr != null && !"".equals(resultStr.trim())) {
                    if (resultStr.startsWith("[")) {
                        //The json is an Array
                        result = new JSONObject();
                        result.put("list", new JSONArray(resultStr));
                    } else {
                        result = new JSONObject(resultStr);
                    }
                }

            } catch (Exception e) {
                exception = e;
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    Log.e("Error while closing reader", e);
                }

                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    Log.e("Error while closing inpustream", e);
                }

                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }
        return result;
    }


    protected String getURI() {
        StringBuilder uriBuilder = new StringBuilder(resource);
        if (!params.isEmpty()) {
            uriBuilder.append("?");
            Iterator<NameValuePair> it = params.iterator();
            while (it.hasNext()) {
                NameValuePair param = it.next();
                try {
                    uriBuilder
                            .append(URLEncoder.encode(param.getName(), "UTF8"))
                            .append("=")
                            .append(URLEncoder.encode(param.getValue(), "UTF8"));
                } catch (UnsupportedEncodingException e) {
                }
                if (it.hasNext()) {
                    uriBuilder.append("&");
                }
            }
        }
        return uriBuilder.toString();
    }

    @Override
    protected void onCancelled() {
        handler.onCancelled(context);
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        handler.doAfter(context);
        if (exception != null) {
            Log.d("WS Request error", exception);

            if (exception instanceof HttpResponseException) {
                handler.onError(context,
                        ((HttpResponseException) exception).getStatusCode());
            } else {
                handler.onException(context, exception);
            }
        } else {
            handler.onResult(context, result);
        }
    }

    @Override
    protected void onPreExecute() {
        handler.doBefore(context);
    }
}
