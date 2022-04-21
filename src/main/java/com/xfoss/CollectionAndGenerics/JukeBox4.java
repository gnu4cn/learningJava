package com.xfoss.CollectionAndGenerics;

import java.util.*;
import java.io.*;
import com.xfoss.Utils.XPlatformThings;

public class JukeBox4 {

    // 这里将这个 ArrayList 从 String 修改为了 Song 对象。
    ArrayList<Song> songList = new ArrayList<Song> ();
    String wDir = XPlatformThings.getWorkingDir("learningJava");

    public JukeBox4 () {
        getSongs();
        System.out.println(songList);

        Collections.sort(songList);
        System.out.println(songList);

        ArtistCompare artistCompare = new ArtistCompare();
        Collections.sort(songList, artistCompare);
        System.out.println(songList);
    }

    public static void main(String[] args){
        new JukeBox4();
    }

    class ArtistCompare implements Comparator<Song> {
        public int compare(Song one, Song two) {
            return one.getArtist().compareTo(two.getArtist());
        }
    }

    void getSongs() {
        try {
            File file = new File(String.format("%s/SongListMore.txt", wDir));
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null) {
                addSong(line);
            }
        } catch (IOException ex) {ex.printStackTrace();}
    }

    void addSong(String lineToParse) {

        // 这里使用四个令牌（也就是歌曲文件中这一行的四个信息片段）创建
        // 出一个新的 Song 对象，并将该 Song 对象添加到那个清单。
        String [] tokens = lineToParse.split("/");

        Song nextSong = new Song(tokens[0], tokens[1], tokens[2], tokens[3]);
        songList.add(nextSong);
    }
}
