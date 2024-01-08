package com.kriuchkov;

import com.opencsv.bean.CsvBindByName;

import java.net.URL;
import java.util.List;

public class MusicAlbum {
    @CsvBindByName
    private String artist;

    @CsvBindByName
    private String album;

    private List<Track> tracks;

    public MusicAlbum() {
    }

    public MusicAlbum(String artist, String album) {
        this.artist = artist;
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    @Override
    public String toString() {
        return "com.kriuchkov.MusicAlbum{" +
                "artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", tracks=" + tracks +
                '}';
    }

    static class Track {
        private String name;
        private URL url;

        public Track(String name, URL url) {
            this.name = name;
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public URL getUrl() {
            return url;
        }

        public void setUrl(URL url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "Track{" +
                    "trackName='" + name + '\'' +
                    ", trackURL=" + url +
                    '}';
        }
    }
}
