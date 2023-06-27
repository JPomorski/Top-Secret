package com.example.revision;

import javafx.scene.paint.Color;

import java.util.Arrays;

public record Dot(double x, double y, double radius, Color color) {
    public static String toMessage(Dot dot) {
        return Arrays.toString(("DT" + dot.x + dot.y + dot.radius + dot.color).split(", "));
    }

    public static Dot fromMessage(String message) {
        String[] messageArr = message.substring(2).split(", ");
        double x = Integer.parseInt(messageArr[0]);
        double y = Integer.parseInt(messageArr[1]);
        double radius = Double.parseDouble(messageArr[2]);
        Color color = Color.valueOf(messageArr[3]);

        return new Dot(x, y, radius, color);
    }
}
