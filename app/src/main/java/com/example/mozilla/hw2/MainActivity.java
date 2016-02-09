package com.example.mozilla.hw2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;

import com.example.mozilla.hw2.response.Weather;
import com.example.mozilla.hw2.response.Conditions;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

import android.view.View;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Button;

import org.w3c.dom.Text;


interface LucaService {
    @GET("weather/default/get_weather/")
    Call<Weather> getWeather();
}

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void getWeather(View v){

        Button button = (Button) findViewById(R.id.button);
        button.setVisibility(View.INVISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://luca-teaching.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())	//parse Gson string
                .build();

        LucaService luca = retrofit.create(LucaService.class);

        Call<Weather> call = luca.getWeather();
        call.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Response<Weather> response) {
                if (response.isSuccess()) {
                    if (response.body().getResponse().getResult().compareTo("ok") != 0) {
                        errorOut();
                        return;
                    }

                    Conditions c = response.body().getResponse().getConditions();

                    TextView placeHolder = (TextView) findViewById(R.id.place_holder);
                    placeHolder.setText(c.getObservationLocation().getFull());

                    TextView tempHolder = (TextView) findViewById(R.id.temp_holder);
                    tempHolder.setText("Temperature: " + c.getTempF() + " degrees Farenheit");

                    TextView humidityHolder = (TextView) findViewById(R.id.humidity_holder);
                    humidityHolder.setText("Humidity: " + c.getRelativeHumidity());

                    TextView windSpeedAverageHolder = (TextView) findViewById(R.id.windspeed_average_holder);
                    windSpeedAverageHolder.setText("Wind Speed (average): " + c.getWindMph() + " mph");

                    TextView windSpeedGustsHolder = (TextView) findViewById(R.id.windspeed_gusts_holder);
                    windSpeedGustsHolder.setText("Wind Speed (gusts): " + c.getWindGustMph() + " mph");

                    TextView weatherHolder = (TextView) findViewById(R.id.weather_holder);
                    weatherHolder.setText("Weather: " + c.getWeather());

                    LinearLayout layout = (LinearLayout) findViewById(R.id.viewer);
                    layout.setVisibility(View.VISIBLE);

                    TextView status = (TextView) findViewById(R.id.status);
                    status.setVisibility(View.INVISIBLE);

                    Button button = (Button) findViewById(R.id.button);
                    button.setVisibility(View.VISIBLE);

                } else {
                    errorOut();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // something went completely south (like no internet connection)
                errorOut();
            }
        });


    }

    private void errorOut(){
        LinearLayout layout = (LinearLayout) findViewById(R.id.viewer);
        layout.setVisibility(View.INVISIBLE);

        TextView status = (TextView) findViewById(R.id.status);
        status.setText("An error occurred :(");
        status.setVisibility(View.VISIBLE);

        Button button = (Button) findViewById(R.id.button);
        button.setVisibility(View.VISIBLE);
    }
}
