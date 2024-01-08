package com.kriuchkov;

import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.io.FilenameUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Utilities {
    private static String formatTrackName(String trackName) {
        return trackName.replaceAll("%20", " ");
    }

    public static List<MusicAlbum> getMusicAlbumList() {
        try (FileReader reader = new FileReader(FilePaths.MUSIC_SOURCES_PATH)) {
            return (List<MusicAlbum>) new CsvToBeanBuilder(reader)
                    .withType(MusicAlbum.class)
                    .build()
                    .parse();
        } catch (IOException e) {
            System.out.println("Cannot open file!");
            return Collections.emptyList();
        }
    }

    public static void setTracks(WebDriver driver, List<MusicAlbum> musicAlbums) {
        musicAlbums.forEach(x -> x.setTracks(getTracks(driver, x)));
    }

    private static List<MusicAlbum.Track> getTracks(WebDriver driver, MusicAlbum musicAlbum) {
        String bandName = musicAlbum.getArtist();
        String albumName = musicAlbum.getAlbum();

        driver.get("http://rocknation.su/mp3/");

        //Find artist and album
        driver.findElement(By.xpath("//input[@id=\"search-band\"]")).sendKeys(bandName);
        driver.findElement(By.xpath("//input[@class=\"searchbutton\"]")).click();

        try {
            driver.findElement(By.xpath("//*[@id=\"list-bands\"]/table/tbody/tr[2]/td[1]/a")).click();
        } catch (NoSuchElementException e) {
            System.out.println("Exception: Incorrect band name \"" + bandName + "\", or band doesn't exists!");
            return Collections.emptyList();
        }

        try {
            driver.findElement(By.xpath("//a[contains(text(), '" + albumName + "')]")).click();
        } catch (NoSuchElementException e) {
            System.out.println("Exception: Incorrect album name \"" + albumName + "\", or album doesn't exists!");
            return Collections.emptyList();
        }

        //Get all buttons with which music downloads
        List<WebElement> songElement = driver.findElements(By.xpath("//div[@class=\"jp-playlist\"]//span[@class=\"jp-free-media\"]/a"));
        List<MusicAlbum.Track> tracks = new ArrayList<>();

        //Get url to each song place
        for (WebElement i : songElement) {
            String url = i.getAttribute("onclick");
            url = url.substring(11, url.length() - 2);

            try {
                URL trackUrl = new URL(url);
                String trackName = formatTrackName(FilenameUtils.getBaseName(trackUrl.getPath()));

                HttpURLConnection huc = (HttpURLConnection) trackUrl.openConnection();
                int statusCode = huc.getResponseCode();
                if (statusCode == HttpURLConnection.HTTP_MOVED_TEMP
                        || statusCode == HttpURLConnection.HTTP_MOVED_PERM) {
                    trackUrl = new URL(huc.getHeaderField("Location"));
                }

                tracks.add(0, new MusicAlbum.Track(trackName + ".mp3", trackUrl));
            } catch (IOException e) {
                System.out.println("URL is not valid: " + url);
            }
        }

        return tracks;
    }

    public static void download(List<MusicAlbum> musicAlbums) {
        for (MusicAlbum album : musicAlbums) {
            String directoryPath = FilePaths.DOWNLOADS_PATH + "/" + album.getArtist() + "/" + album.getAlbum();

            try {
                // Create the directory if it doesn't exist
                Path directory = Paths.get(directoryPath);
                Files.createDirectories(directory);

                for (MusicAlbum.Track track : album.getTracks()) {
                    System.out.println(track);
                    try (FileOutputStream fileOutputStream = new FileOutputStream(directory.resolve(track.getName()).toFile())) {
                        ReadableByteChannel readableByteChannel = Channels.newChannel(track.getUrl().openStream());
                        FileChannel fileChannel = fileOutputStream.getChannel();

                        fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
                    } catch (IOException e) {
                        System.out.println("Cannot get or write file!");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
