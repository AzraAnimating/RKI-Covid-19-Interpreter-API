package de.azraanimating.rkicoronainterpreterapi.handling.filter;

import de.azraanimating.jsonbuilder.JSONBuilder;
import de.azraanimating.rkicoronainterpreterapi.handling.requests.RegionRequestHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class InformationSorter {

    private final RegionRequestHandler regionRequestHandler;

    public InformationSorter() {
        this.regionRequestHandler = new RegionRequestHandler();
    }


    public String getDataForRegion(final String state, final String regionType, final String regionName) throws IOException, JSONException {
        String response = this.regionRequestHandler.getRegionData(state, regionType, regionName);
        if(!response.equals("nodata")) {
            JSONObject rawObject = new JSONObject(response);
            if (rawObject.get("features") != null) {

                AtomicInteger casesTotal = new AtomicInteger();
                AtomicInteger deathCases = new AtomicInteger();
                AtomicInteger casesM = new AtomicInteger();
                AtomicInteger casesW = new AtomicInteger();
                AtomicInteger c0_4 = new AtomicInteger();
                AtomicInteger c5_14 = new AtomicInteger();
                AtomicInteger c15_34 = new AtomicInteger();
                AtomicInteger c35_59 = new AtomicInteger();
                AtomicInteger c60_79 = new AtomicInteger();
                AtomicInteger c80 = new AtomicInteger();
                String dateOfPublication = "";

                JSONArray informationArray = rawObject.getJSONArray("features");

                ArrayList<JSONObject> information = new ArrayList<>();
                IntStream.range(0, informationArray.length()).forEach(value -> {
                    try {
                        information.add(informationArray.getJSONObject(value).getJSONObject("attributes"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });


                if(information.isEmpty()) {
                    return "{ \"data\" : \"none\" }";
                }

                information.forEach(jsonObject -> {
                    try {
                        casesTotal.set(casesTotal.get() + jsonObject.getInt("AnzahlFall"));
                        deathCases.set(deathCases.get() + jsonObject.getInt("AnzahlTodesfall"));

                        //Gender
                        if(jsonObject.getString("Geschlecht").equals("M")) {
                            casesM.set(casesM.get() + jsonObject.getInt("AnzahlFall"));
                        } else {
                            casesW.set(casesW.get() + jsonObject.getInt("AnzahlFall"));
                        }

                        //Age
                        String agegroup = jsonObject.getString("Altersgruppe");
                        if(agegroup.equals("A0-A4")) {
                            c0_4.set(c0_4.get() + jsonObject.getInt("AnzahlFall"));
                        } else if(agegroup.equals("A5-A14")) {
                            c5_14.set(c5_14.get() + jsonObject.getInt("AnzahlFall"));
                        } else if (agegroup.equals("A15-A34")) {
                            c15_34.set(c15_34.get() + jsonObject.getInt("AnzahlFall"));
                        } else if (agegroup.equals("A35-A59")) {
                            c35_59.set(c35_59.get() + jsonObject.getInt("AnzahlFall"));
                        } else if (agegroup.equals("A60-A79")) {
                            c60_79.set(c60_79.get() + jsonObject.getInt("AnzahlFall"));
                        } else if (agegroup.equals("A80+")) {
                            c80.set(c80.get() + jsonObject.getInt("AnzahlFall"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
                dateOfPublication = information.get(0).getString("Datenstand");

                JSONBuilder jsonBuilder = new JSONBuilder();

                jsonBuilder.createObject("datenstand", dateOfPublication);
                jsonBuilder.createObject("alleFälle", casesTotal.get());
                jsonBuilder.createObject("todesFälle", deathCases.get());
                jsonBuilder.createObject("männlicheFälle", casesM.get());
                jsonBuilder.createObject("weiblicheFälle", casesW.get());

                JSONBuilder agegroupBuilder = new JSONBuilder();

                agegroupBuilder.createObject("0-4", c0_4.get());
                agegroupBuilder.createObject("5-14", c5_14.get());
                agegroupBuilder.createObject("15-34", c15_34.get());
                agegroupBuilder.createObject("35-59", c35_59.get());
                agegroupBuilder.createObject("60-79", c60_79.get());
                agegroupBuilder.createObject("80+", c80.get());

                jsonBuilder.createObject("altersGruppen", agegroupBuilder.build());

                return jsonBuilder.build();
            }
        }
        return "{ \"data\" : \"none\" }";
    }

}
