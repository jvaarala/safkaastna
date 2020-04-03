package view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class StringTest {

    @DisplayName("string_format_all_lower_case")
    @Test
    void string_format_all_lower_case() {
        String s = MainViewController.formatString("testitie 5 helsinki");
        assertEquals("Testitie 5 Helsinki", s, "String formatting works wrong");
        System.out.println("string_format_all_lower_case done");
    }

    @DisplayName("string_format_all_upper_case")
    @Test
    void string_format_all_upper_case() {
        String s = MainViewController.formatString("TESTITIE 5 HELSINKI");
        assertEquals("Testitie 5 Helsinki", s, "String formatting works wrong");
        System.out.println("string_format_all_upper_case done");
    }

    @DisplayName("string_format_all_messed_up")
    @Test
    void string_format_all_messed_up() {

        String s = MainViewController.formatString("tesTItie 5 helsINKI");
        assertEquals("Testitie 5 Helsinki", s, "String formatting works wrong");
        System.out.println("string_format_all_messed_up done");
    }
}
