The program, built using Gradle, compiles the project into a JAR archive with command:

```bash
./gradlew jar
```

When the program is executed in the form of an archive, it searches for a file named `music_albums.csv` in the current
directory. This CSV file contains the names of music groups and their albums like:

| artist     | album   |
|------------|---------|
| Pink Floyd | Animals |
| Pink Floyd | Meddle  |

As a result of its execution, a directory named `downloads` will be created or updated, containing organized downloaded
files like:

* downloads
  * Pink Floyd
    * Animals
      * ...
    * Meddle
      * ...

You can run program with:

```bash
java -jar music-scrapper.jar
```