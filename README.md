![Maintainer](https://img.shields.io/badge/Maintainer-AzraAnimating-blue?style=for-the-badge)
![GitHub top language](https://img.shields.io/github/languages/top/AzraAnimating/RKI-Covid-19-Interpreter-API?color=orange&style=for-the-badge)
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/AzraAnimating/RKI-Covid-19-Interpreter-API?style=for-the-badge)
![GitHub release (latest by date)](https://img.shields.io/github/v/release/AzraAnimating/RKI-Covid-19-Interpreter-API?style=for-the-badge)
# RKI-Covid-19-Interpreter-API
## Eine API um schnell aktuelle Covid-19 Zahlen zu sehen!

Die API benutzt die Daten des Robert-Koch-Institut welches diese auch in visueller Form [hier](https://experience.arcgis.com/experience/478220a4c454480e823b17327b2bf1d4/page/page_1/) zur Verfügung stellt!

# Wie benutzt man die API?

Es gibt derzeit zwei Endpoints

GET api.zyonicsoftware.com/rki-covid-19/region (Limitation auf die letzten 1000 Meldungen aufgrund RKI-API)

### Parameter
- bundesland (Text)
- regionName (Text)

GET api.zyonicsoftware.com/rki-covid-19/specific

### Parameter
-regionName (Text)

Das Bundesland muss mit vollem Namen angegeben werden: "Nordrhein-Westfalen"
Der Regionsname ist der Name der Region wie sie in der [RKI-Karte](https://experience.arcgis.com/experience/478220a4c454480e823b17327b2bf1d4/page/page_1/) aufgeführt ist.

## Beispiele:

- https://api.zyonicsoftware.com/rki-covid-19/region?bundesland=Nordrhein-Westfalen&regionName=Bochum
- https://api.zyonicsoftware.com/rki-covid-19/region?bundesland=Bayern&regionName=Nürnberg
- https://api.zyonicsoftware.com/rki-covid-19/region?bundesland=Nordrhein-Westfalen&regionName=Ennepe-Ruhr-Kreis

- https://api.zyonicsoftware.com/rki-covid-19/specific?regionName=Ennepe-Ruhr-Kreis




## Quellen:

RKI-Daten-API:
- https://services7.arcgis.com/mOBPykOjAyBO2ZKk/arcgis/rest/services/RKI_COVID19/FeatureServer/0/query?where=Bundesland%20%3D%20%27NORDRHEIN-WESTFALEN%27%20AND%20Landkreis%20%3D%20%27SK%20BOCHUM%27&outFields=Landkreis,AnzahlFall,AnzahlTodesfall,Datenstand,Altersgruppe,Geschlecht&outSR=4326&f=json


- https://services7.arcgis.com/mOBPykOjAyBO2ZKk/arcgis/rest/services/RKI_Landkreisdaten/FeatureServer/0/query?where=county%20%3D%20'SK%20M%C3%9CNCHEN'&outFields=death_rate,cases,deaths,cases_per_100k,cases_per_population,last_update,cases7_per_100k,county,BEZ,GEN,KFL,EWZ&outSR=4326&f=json

