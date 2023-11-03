package ru.netology.geo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.netology.entity.Country;
import ru.netology.i18n.LocalizationServiceImpl;

public class TestLocalizationService {
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
