package com.ocado.basket;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BasketSplitterTest {
    private final String config = """
                {
                "Carrots (1kg)": ["Express Delivery", "Click&Collect"], "Cold Beer (330ml)": ["Express Delivery"],
                "Steak (300g)": ["Express Delivery", "Click&Collect"], "AA Battery (4 Pcs.)": ["Express Delivery", "Courier"], "Espresso Machine": ["Courier", "Click&Collect"], "Garden Chair": ["Courier"]
                }""";
    @Test
    void split_ValidConfig_ValidItems_shouldReturnValidMap(@TempDir Path tempDir) throws IOException {
        //Arrange
        List<String> items = Arrays.asList("Steak (300g)", "Carrots (1kg)",
                                            "Cold Beer (330ml)", "AA Battery (4 Pcs.)",
                                            "Espresso Machine", "Garden Chair");

        Path configFile = tempDir.resolve("config.json");
        Files.writeString(configFile, config);

        Map<String, List<String>> validOutput = new HashMap<>();
        validOutput.put("Courier", Arrays.asList("Garden Chair", "Espresso Machine"));
        validOutput.put("Express Delivery", Arrays.asList("AA Battery (4 Pcs.)", "Steak (300g)", "Cold Beer (330ml)", "Carrots (1kg)"));
        //Act
        BasketSplitter _sut = new BasketSplitter(configFile.toString());
        Map<String, List<String>> output = _sut.split(items);

        //Assert
        assertEquals(validOutput, output);
    }

    @Test
    void split_ValidConfig_InvalidItemsNoItems_shouldReturnEmptyMap(@TempDir Path tempDir) throws IOException {
        //Arrange
        List<String> items = List.of();
        Path configFile = tempDir.resolve("config.json");
        Files.writeString(configFile, config);
        Map<String, List<String>> validOutput = new HashMap<>();

        //Act
        BasketSplitter _sut = new BasketSplitter(configFile.toString());
        Map<String, List<String>> output = _sut.split(items);

        //Assert
        assertEquals(validOutput, output);
    }

    @Test
    void split_InvalidConfigUnexpectedCharacter_ThrowsIOException(@TempDir Path tempDir) throws IOException {
        //Arrange
        String localConfig = """
                {
                "Carrots (1kg)": , ["Express Delivery", "Click&Collect"], "Cold Beer (330ml)": ["Express Delivery"]
                }""";

        Path configFile = tempDir.resolve("config.json");
        Files.writeString(configFile, localConfig);

        //Act && Assert
        Assertions.assertThrows(IOException.class, () -> new BasketSplitter(configFile.toString()));
    }
}