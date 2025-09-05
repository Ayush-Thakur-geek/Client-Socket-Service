package com.uber.ClientSocketService.configurations;

import com.netflix.discovery.EurekaClient;
import com.uber.ClientSocketService.apis.BookingServiceApi;
import com.uber.ClientSocketService.dtos.UpdateBookingResponseDTO;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class RetrofitConfig {

    private final EurekaClient eurekaClient;

    public RetrofitConfig(EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
    }

    private String getServiceUrl(String serviceName) {
        return eurekaClient.getNextServerFromEureka(serviceName, false).getHomePageUrl();
    }

    @Bean
    public BookingServiceApi updateBooking() {
        return new Retrofit.Builder()
                .baseUrl("http://localhost:8090/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().build())
                .build()
                .create(BookingServiceApi.class);
    }
}
