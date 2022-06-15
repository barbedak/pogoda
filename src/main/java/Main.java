import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws Exception {
        Document page = getPage();


        Elements days = page.select("div[class=weather-short]");

        System.out.println("Дата    Температура    На небе");
        for (Element day : days) {
            System.out.println(getDateFromCard(day));
            Element partNight = day.select("tr[class=night]").first();
            if (partNight != null) printDayPartWeather(partNight);
            Element partMorning = day.select("tr[class=morning]").first();
            if (partMorning != null) printDayPartWeather(partMorning);
            Element partDay = day.select("tr[class=day]").first();
            if (partDay != null) printDayPartWeather(partDay);
            Element partEvening = day.select("tr[class=evening]").first();
            if (partEvening != null) printDayPartWeather(partEvening);
        }
    }

    private static void printDayPartWeather(Element dayPart) {
        String namePart = dayPart.select("td[class=weather-day]").text().toLowerCase(Locale.ROOT);
        String temp = dayPart.select("td[class=weather-temperature] > span").text();
        String weather = dayPart.select("div[class^=wi wi-v]").attr("title");
        String separatorDateTemp = "\t\t";//"    ";
        if (namePart.equals("вечер")) {
            separatorDateTemp = "\t";
        }
        String separatorTempWeather = "\t\t\t";
        if (temp.length() > 3) {
            separatorTempWeather = "\t\t";
        }
        System.out.println("   " + namePart + separatorDateTemp + temp + separatorTempWeather + weather);
    }

    //    , 15 июня
    private static String parseDateFromString(String stringDate) {
        Pattern pattern = Pattern.compile("\\d{2}");
        Matcher matcher = pattern.matcher(stringDate);
        if (matcher.find()) {
            return matcher.group();
        }
        return "No date";
    }

    private static String getDateFromCard(Element card) {
        String stringDate = card.select("div[class^=dates short-d]").text();
        return parseDateFromString(stringDate);
    }

    private static Document getPage() throws IOException {
        String pageURL = "https://world-weather.ru/pogoda/russia/okulovka/7days/";
        return Jsoup.parse(new URL(pageURL), 6000);
    }
}
