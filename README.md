# RKI-Covid-19-Interpreter-API
## Eine API um schnell aktuelle Covid-19 Zahlen zu sehen!

Die API benutzt die Daten des Robert-Koch-Institut welches diese auch in visueller Form [hier](https://experience.arcgis.com/experience/478220a4c454480e823b17327b2bf1d4/page/page_1/) zur Verfügung stellt!

# Wie benutzt man die API?

Es gibt derzeit einen Endpoint

GET api.zyonicsoftware.com/rki-covid-19/region

### Parameter
- bundesland (Text)
- regionsTyp (Text)
- regionName (Text)

Das Bundesland muss mit vollem Namen angegeben werden: "Nordrhein-Westfalen"
Der Regionstyp ist entweder LK für Landkreis oder SK für Stadtkreis (Primär bei größeren Städten). Genaueres können sie auch auf der [RKI-Karte](https://experience.arcgis.com/experience/478220a4c454480e823b17327b2bf1d4/page/page_1/) nachschauen.
Der Regionsname ist der Name der Region wie sie in der RKI-Karte aufgeführt ist

## Beispiel:

https://api.zyonicsoftware.com/rki-covid-19/region?bundesland=Nordrhein-Westfalen&regionsTyp=sk&regionName=Bochum
