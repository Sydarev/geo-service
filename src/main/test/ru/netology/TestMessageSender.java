package ru.netology;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationServiceImpl;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Map;

import static ru.netology.entity.Country.RUSSIA;

public class TestMessageSender {
    @ParameterizedTest
    @CsvSource(value = {"172.123.12.19, Добро пожаловать", "96.44.183.149, Welcome"})
    public void test_MessageSender_valid_language(String ip, String expression) {
        GeoServiceImpl geoService = Mockito.mock(GeoServiceImpl.class);
        Mockito.when(geoService.byIp(ip))
                .thenReturn(ip.startsWith("172") ? new Location("Moscow", RUSSIA, "Lenina", 15) :
                        new Location("New York", Country.USA, " 10th Avenue", 32)
                );

        LocalizationServiceImpl localizationService = Mockito.mock(LocalizationServiceImpl.class);
        Mockito.when(localizationService.locale(geoService.byIp(ip).getCountry()))
                .thenReturn(ip.startsWith("172") ? "Добро пожаловать" : "Welcome");

        MessageSenderImpl messageSender = new MessageSenderImpl(geoService, localizationService);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, ip);
        String actual = messageSender.send(headers);
        Assertions.assertEquals(actual, expression);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "172.123.32.11",
            "96.44.183.149",
            "127.0.0.1",
            "172.942.93.12"})
    public void test_GeoService_byIp(String ip) {
        Location ans = null;
        GeoServiceImpl geoService = new GeoServiceImpl();
        if ("127.0.0.1".equals(ip)) {
            ans = new Location(null, null, null, 0);
        } else if ("172.123.12.19".equals(ip)) {
            ans = new Location("Moscow", RUSSIA, "Lenina", 15);
        } else if ("96.44.183.149".equals(ip)) {
            ans = new Location("New York", Country.USA, " 10th Avenue", 32);
        } else if (ip.startsWith("172.")) {
            ans = new Location("Moscow", RUSSIA, null, 0);
        } else if (ip.startsWith("96.")) {
            ans = new Location("New York", Country.USA, null, 0);
        }
        Assertions.assertEquals(ans.toString(), geoService.byIp(ip).toString());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "RUSSIA",
            "USA"
    })
    public void test_LocalizationService_locale(Country cnt) {
        String ans;
        LocalizationServiceImpl localizationService = new LocalizationServiceImpl();
        switch (cnt) {
            case RUSSIA:
                ans = "Добро пожаловать";
                break;
            default:
                ans = "Welcome";
        }
        Assertions.assertEquals(ans, localizationService.locale(cnt));
    }

}
