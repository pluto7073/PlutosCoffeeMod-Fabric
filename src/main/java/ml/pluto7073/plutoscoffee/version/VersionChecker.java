package ml.pluto7073.plutoscoffee.version;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import net.minecraft.client.MinecraftClient;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class VersionChecker {

    private static final String VERSION_LINK = "https://pluto7073.github.io/files/coffee-version-number.txt";

    private static boolean outdated;
    private static boolean screenShown = false;

    public static void checkOutdated() {
        try (BufferedInputStream bis = new BufferedInputStream(new URL(VERSION_LINK).openStream())) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(bis));
            int recentVersion = Integer.parseInt(reader.readLine());
            reader.close();
            bis.close();
            outdated = recentVersion > PlutosCoffee.MOD_VERSION;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isOutdated() {
        return outdated;
    }

    public static void showWarningScreen() {
        if (screenShown) return;
        screenShown = true;
        if (!isOutdated()) return;
        MinecraftClient.getInstance().setScreen(new VersionWarningScreen());
    }

}
