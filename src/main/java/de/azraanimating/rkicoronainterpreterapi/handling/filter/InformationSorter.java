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

    public String getDataForCounty(final String countyName) throws IOException, JSONException {
        System.out.println("Started Lookup for '" + countyName + "'");
        String response = this.regionRequestHandler.getCountyData("lk", countyName);
        if(response != null && !response.equals("nodata")) {
            JSONObject rawObject = new JSONObject(response);
            if(rawObject.get("features") != null) {
                JSONArray rawFeaturesArray = rawObject.getJSONArray("features");

                //falls der Prefix (Countyprefix) Falsch war, Stadtkreis nutzen
                if(rawFeaturesArray.length() < 1) {
                    response = this.regionRequestHandler.getCountyData("sk", countyName);
                    if(response != null && !response.equals("nodata")) {
                        rawObject = new JSONObject(response);
                        if (rawObject.get("features") != null) {
                            rawFeaturesArray = rawObject.getJSONArray("features");
                        }
                    }
                }

                if(rawFeaturesArray.length() < 1) {
                    return "{ \"data\" : \"none\" }";
                }

                JSONObject attributes = rawFeaturesArray.getJSONObject(0).getJSONObject("attributes");
                JSONBuilder jsonBuilder = new JSONBuilder();

                jsonBuilder
                        .createObject("region", new JSONBuilder().createObject("bezeichnung", attributes.getString("BEZ")).createObject(("name"), attributes.getString("GEN")).build())
                        .createObject("datenstand", attributes.getString("last_update"))
                        .createObject("fälleGesamt", attributes.getString("cases"))
                        .createObject("todesfälle", attributes.getString("deaths"))
                        .createObject("todesrate", attributes.getString("death_rate"))
                        .createObject("fällePro100KEinwohner", attributes.getString("cases7_per_100k"));

                System.out.println("Sent Data for '" + countyName + "'");

                return jsonBuilder.build();

            }
        }
        return "{ \"data\" : \"none\" }";
    }

    public String getDataForRegion(final String state, final String regionName) throws IOException, JSONException {
        System.out.println("Lookup for '" + regionName + "' in '" + state + "'");
        String response = this.regionRequestHandler.getRegionData(state, "lk", regionName);
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

                if(informationArray.length() < 1) {
                    rawObject = new JSONObject(this.regionRequestHandler.getRegionData(state, "sk", regionName));
                    informationArray = rawObject.getJSONArray("features");
                }


                ArrayList<JSONObject> information = new ArrayList<>();
                JSONArray finalInformationArray = informationArray;
                IntStream.range(0, informationArray.length()).forEach(value -> {
                    try {
                        information.add(finalInformationArray.getJSONObject(value).getJSONObject("attributes"));
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

                System.out.println("Sent Data for '" + regionName + "' in '" + state + "'");
                return jsonBuilder.build();
            }
        }
        return "{ \"data\" : \"none\" }";
    }

}
