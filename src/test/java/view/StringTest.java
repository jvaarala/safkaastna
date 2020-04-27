package view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Test MainViewController formatString method for addresses
 * Expected output: Street name 1 City
 */
public class StringTest {

    /**
     * Test string with all lower case characters
     */
    @DisplayName("string_format_all_lower_case")
    @Test
    void string_format_all_lower_case() {
        String s = MainViewController.formatString("testitie 5 helsinki");
        assertEquals("Testitie 5 Helsinki", s, "String formatting works wrong");
        System.out.println("string_format_all_lower_case done");
    }

    /**
     * Test string with all upper case characters
     */
    @DisplayName("string_format_all_upper_case")
    @Test
    void string_format_all_upper_case() {
        String s = MainViewController.formatString("TESTITIE 5 HELSINKI");
        assertEquals("Testitie 5 Helsinki", s, "String formatting works wrong");
        System.out.println("string_format_all_upper_case done");
    }

    /**
     * Test string with upper and lower case characters mixed up
     */
    @DisplayName("string_format_all_messed_up")
    @Test
    void string_format_all_messed_up() {

        String s = MainViewController.formatString("tesTItie 5 helsINKI");
        assertEquals("Testitie 5 Helsinki", s, "String formatting works wrong");
        System.out.println("string_format_all_messed_up done");
    }
}
