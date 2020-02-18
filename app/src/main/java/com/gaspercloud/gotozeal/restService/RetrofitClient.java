package com.gaspercloud.gotozeal.restService;

import com.gaspercloud.gotozeal.BuildConfig;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionSpec;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit;

//Define the base URL//

    //private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

//Create the Retrofit instance//

    public static Retrofit getRetrofitInstance(String BASE_URL, final String c_username, final String c_password) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();



        /*OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
                Request originalRequest = chain.request();

                Request.Builder builder = originalRequest.newBuilder().header("Authorization",
                        Credentials.basic(c_username, c_password));

                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        })
        .addInterceptor(logging)
        .build();*/
        OkHttpClient okHttpClient = getUnsafeOkHttpClient(BASE_URL, c_username, c_password, logging);
        /*OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
                Request originalRequest = chain.request();

                Request.Builder builder = originalRequest.newBuilder().header("Authorization",
                        Credentials.basic(c_username, c_password));

                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        });

            okHttpClient.connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {//only LOG REST for DEBUG
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
            okHttpClient.addInterceptor(logging);
        }
*/

        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)

                    //Add the converter//
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    //.client(okHttpClient.build())

                    //Build the Retrofit instance//
                    .build();
        }
        return retrofit;
    }


    public static OkHttpClient getUnsafeOkHttpClient(String BASE_URL, final String c_username, final String c_password, HttpLoggingInterceptor logging) {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                    .tlsVersions(TlsVersion.TLS_1_0)
                    .allEnabledCipherSuites()
                    .build();

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectionSpecs(Collections.singletonList(spec));
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            builder.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
                    Request originalRequest = chain.request();

                    Request.Builder builder = originalRequest.newBuilder().header("Authorization",
                            Credentials.basic(c_username, c_password));

                    Request newRequest = builder.build();
                    return chain.proceed(newRequest);
                }
            });
            builder.connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS);

            if (BuildConfig.DEBUG) {//only LOG REST for DEBUG
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
                builder.addInterceptor(logging);
            }

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
