package de.azraanimating.rkicoronainterpreterapi.handling.requests;

import de.azraanimating.jsonbuilder.JSONBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

public class RegionRequestHandler {

    public String getRegionData(final String state, final String regionType, final String regionName) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://services7.arcgis.com/mOBPykOjAyBO2ZKk/arcgis/rest/services/RKI_COVID19/FeatureServer/0/query?where=Bundesland%20%3D%20%27" + state + "%27%20AND%20Landkreis%20%3D%20%27" + regionType + "%20" + regionName + "%27&outFields=Landkreis,AnzahlFall,AnzahlTodesfall,Datenstand,Altersgruppe,Geschlecht&outSR=4326&f=json")
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        if(response.body() != null) {
            return Objects.requireNonNull(response.body()).string();
        } else {
            return "nodata";
        }
    }
}
