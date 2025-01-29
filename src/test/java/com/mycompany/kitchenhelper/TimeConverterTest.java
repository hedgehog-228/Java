/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.kitchenhelper;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TimeConverterTest {

    private final TimeConverter timeConverter = new TimeConverter();

    @Test
    public void testConvertToBaseUnit() {
        Time time = new Time(1.5, "hours");
        Map.Entry<String, Double> result = timeConverter.convertToBaseUnit(time);

        assertEquals("minutes", result.getKey());
        assertEquals(90.0, result.getValue());    
    }

    @Test
    public void testConvertToOptimalUnitMinutesToHours() {
        Time time = new Time(90, "minutes");
        Map.Entry<String, Double> result = timeConverter.convertToOptimalUnit(time);

        assertEquals("hours", result.getKey());   
        assertEquals(1.5, result.getValue());     
    }

    @Test
    public void testConvertToOptimalUnitSecondsToMinutes() {
        Time time = new Time(30, "seconds");
        Map.Entry<String, Double> result = timeConverter.convertToOptimalUnit(time);

        assertEquals("seconds", result.getKey()); 
        assertEquals(30.0, result.getValue());    
    }

    @Test
    public void testConvertToOptimalUnitHoursToHours() {
        Time time = new Time(1, "hours");
        Map.Entry<String, Double> result = timeConverter.convertToOptimalUnit(time);

        assertEquals("hours", result.getKey());  
        assertEquals(1.0, result.getValue());   
    }

    @Test
    public void testAddQuantitiesSameUnits() {
        Time time1 = new Time(30, "minutes");
        Time time2 = new Time(29, "minutes");

        Map<String, Double> result = timeConverter.addQuantities(time1, time2);

        assertTrue(result.containsKey("minutes"));
        assertEquals(59.0, result.get("minutes")); 
    }

    @Test
    public void testAddQuantitiesDifferentUnits() {
        Time time1 = new Time(1.5, "hours");
        Time time2 = new Time(30, "minutes");

        Map<String, Double> result = timeConverter.addQuantities(time1, time2);

        assertTrue(result.containsKey("hours"));
        assertEquals(2.0, result.get("hours")); 
    }

    @Test
    public void testValidateUnitInvalidUnit() {
        Time invalidTime = new Time(10, "days");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            timeConverter.convertToBaseUnit(invalidTime);
        });

        assertEquals("Unit 'days' is not in the conversion table.", exception.getMessage());
    }
}
