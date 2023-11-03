package ru.netology.i18n;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoServiceImpl;

import static ru.netology.entity.Country.RUSSIA;

public class TestGeoService {
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
}
