package com.kriuchkov;

import org.openqa.selenium.WebDriver;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<MusicAlbum> musicAlbums = Utilities.getMusicAlbumList();
        System.out.println("File parsed successfully!");

        WebDriver driver = DriverSingleton.getDriver(Browser.FIREFOX);

        Utilities.setTracks(driver, musicAlbums);
        driver.quit();
        System.out.println("URLs parsed successfully!");

        Utilities.download(musicAlbums);
        System.out.println("Music downloaded successfully!");
    }
}
