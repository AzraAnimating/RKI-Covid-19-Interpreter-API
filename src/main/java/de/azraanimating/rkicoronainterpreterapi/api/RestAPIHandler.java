package de.azraanimating.rkicoronainterpreterapi.api;

import de.azraanimating.rkicoronainterpreterapi.handling.filter.InformationSorter;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class RestAPIHandler {

    private final InformationSorter informationSorter;

    public RestAPIHandler() {
        this.informationSorter = new InformationSorter();
    }

    @GetMapping("/rki-covid-19/region")
    @ResponseBody
    public ResponseEntity<String> getRegionStats(@RequestParam final String bundesland, @RequestParam final String regionName) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(this.informationSorter.getDataForRegion(bundesland.toUpperCase(), regionName.toUpperCase()));
        } catch (IOException | JSONException exception) {
            exception.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{ \"data\" : \"none\" }");
    }

}
