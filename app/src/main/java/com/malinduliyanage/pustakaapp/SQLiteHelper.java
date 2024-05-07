package com.malinduliyanage.pustakaapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Random;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pustakaapp.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_USERS = "CREATE TABLE users (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "lib_id TEXT UNIQUE," +
            "name TEXT," +
            "password TEXT," +
            "location TEXT," +
            "isloggedin INTEGER DEFAULT 0)";

    private static final String CREATE_TABLE_BOOKS = "CREATE TABLE books (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "book_id TEXT UNIQUE," +
            "book_title TEXT," +
            "book_author TEXT," +
            "book_desc TEXT ," +
            "book_thumb TEXT," +
            "book_isbn TEXT," +
            "book_pages TEXT," +
            "book_publisher TEXT," +
            "book_copies_count INTEGER)";

    private static final String CREATE_TABLE_USERLENDINGS = "CREATE TABLE userlendings (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "book_id TEXT," +
            "lib_id TEXT," +
            "timestamp TEXT)";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_BOOKS);
        db.execSQL(CREATE_TABLE_USERLENDINGS);
        insertDummyBookData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS books");
        db.execSQL("DROP TABLE IF EXISTS userlendings");
        onCreate(db);
    }

    private void insertDummyBookData(SQLiteDatabase db) {
        // Dummy book data to be inserted
        String[] bookTitles = {
                "Madol Doova",
                "Gamperaliya",
                "Senkottan",
                "Guru Geethaya",
                "Paliganeema",
                "An Historical Relation of the Island Ceylon",
                "Kaliyugaya",
                "Daiwayogaya",
                "Thun Man Handiya",
                "Wijayaba Kollaya"

        };

        String[] bookAuthors = {
                "Martin Wickramasinghe",
                "Martin Wickramasinghe",
                "Mahinda Prasad Masimbula",
                "Chinghiz Aitmatov",
                "G. B. Senanayake",
                "Robert Knox",
                "Martin Wickramasinghe",
                "W. A. Silva",
                "Mahagamasekara",
                "W. A. Silva"

        };

        String[] bookDescs = {
                "Madol Doova (Sinhala: මඩොල් දූව is a children's novel and coming-of-age story written by Sri Lankan writer Martin Wickramasinghe and first published in 1947. The book recounts the misadventures of Upali Giniwella and his friends on the Southern coast of Sri Lanka during the 1890s. It later describes the efforts of Upali and his friend Jinna to lead their lives in a small deserted island. The novel has been translated into several languages, and was made into a film of the same name in 1976.",
                "The trilogy consists of the following I - Gamperaliya, The Village; II - Kaliyugaya, The Village and the City; III - Yuganthaya, The City. The first novel depicts the crumbling of traditional village life under the pressure of modernisation. The story of a successful family in a Southern village is used to portray the gradual replacement of traditional economic and social structure of the village by commercial city influence. Yuganthaya and After the decay of traditional life, the story details the rise of the bourgeoisie, with its urban base and entrepreneurial drive, ending with the formation of the labour movement and socialist theology and rise of hopes for a new social order.",
                "Senkottan is a novel that reveals the old sri lankan society and how it treated towards the low castes,and how their lives went through with pain and mistreat",
                "The action takes place in the years from 1924 all the way to the early 1950s in the Kukureu village of the Kyrgyz Soviet Socialist Republic, which is now Kyrgyzstan. The Russian Civil War ended not so long ago. Young Komsomol member and a former Red Army soldier Dyuyshen comes to the village as the new teacher of the village. His enthusiasm to bring new ideas immediately faces a centuries-old tradition of life in Central Asia. The former soldier tries to improve literacy to this far Moslem area while villagers didn't allow gỉrls to attend school. He then met Altynai, a 15-year-old illiterate girl who has a burning desire to study, but her aunt sells her to a powerful and wealthy chieftain. Then school is burned down and is rebuilt using centuries old trees, being a pride to the local population.",
                "A novel by G. B. Senanayake",
                "An Historical Relation of the Island Ceylon together With somewhat Concerning Severall Remarkable passages of my life that hath hapned since my Deliverance out of Captivity is a book written by the English trader and sailor Robert Knox in 1681. It describes his experiences some years earlier in the Kingdom of Kandy, on the island today known as Sri Lanka. It provides one of the most important contemporary accounts of 17th century Sri Lankan life.",
                "Kaliyugaya (Sinhala, Age of Darkness) is a novel written by Sinhala writer Martin Wickremasinghe and first published in 1957. It is the second book of Wickremasinghe's trilogy that started with Gamperaliya - transformation of a village. The final book of the trilogy is Yuganthaya (culmination of the era).",
                "A novel by W. A. Silva",
                "This is a wonderful book that is written by one of the best ever poet, song writer and a painter ever born in Sri Lanka . This is the only novel written by this wonderful person. Most things about Siripala is of his including date of birth, school and characters their. Very touching book about a village boy.",
                "A novel based on the history of Sri Lanka by W. A. Silva"


        };

        String[] bookThumbs = {
                "https://upload.wikimedia.org/wikipedia/en/5/5c/MadolDoova.jpg",
                "https://static-01.daraz.lk/p/3031230cc9e8fe114704b0b1989c94db.jpg_750x750.jpg_.webp",
                "https://archives1.dailynews.lk/sites/default/files/news/2016/08/02/z_p30-village.jpg",
                "https://goodultimate.weebly.com/uploads/1/2/5/0/125045254/862901047.jpg",
                "https://www.keheli.lk/wp-content/uploads/2022/09/CamScanner-09-11-2022-12.41_7.jpg",
                "https://www.vijithayapa.com/Prodpics/BK%20143693.jpg",
                "https://us.lakpura.com/cdn/shop/products/LS6520FCFF-01-E.jpg?v=1634724951",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTTrZ_ZsJSOgI4cYt_lT6j-EYv6hZHnKTqp-qBT0nZyJMfDm0jEI5UmFaVqJrVtI5CUElE&usqp=CAU",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTQ5RYxZDn6zU7dGGnUfUCbTX0AWcPgkBuW5oJ9muqMzA&s",
                "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1638448741i/59740867.jpg"

        };

        String[] bookIsbn = {
                "9781234567897",
                "1231234585297",
                "9452874569852",
                "7845134852889",
                "1242345673895",
                "9125488967894",
                "9781234567893",
                "9781234567890",
                "9456734567897",
                "9781245127899"

        };

        String[] bookPages = {
                "100",
                "124",
                "300",
                "254",
                "80",
                "251",
                "124",
                "150",
                "320",
                "154"

        };

        String[] bookPublisher = {
                "M. D. Gunasena Publishers",
                "Pothpath Publishers",
                "Wijesuriya Grantha Kendraya",
                "M. D. Gunasena Publishers",
                "Grantha",
                "Author Publishers",
                "Hasu Publishers",
                "M. D. Gunasena Publishers",
                "Epa Publishers",
                "Pustaka Publishers"

        };

        String[] bookCopies = {
                "3",
                "1",
                "4",
                "5",
                "10",
                "1",
                "5",
                "5",
                "2",
                "1"

        };


        ContentValues values = new ContentValues();
        int i = 0;
        while (i < bookTitles.length) {
            values.clear();
            String bookId = generateBookId();
            values.put("book_id", bookId);
            values.put("book_title", bookTitles[i]);
            values.put("book_author", bookAuthors[i]);
            values.put("book_desc", bookDescs[i]);
            values.put("book_thumb", bookThumbs[i]);
            values.put("book_isbn", bookIsbn[i]);
            values.put("book_pages", bookPages[i]);
            values.put("book_publisher", bookPublisher[i]);
            values.put("book_copies_count", bookCopies[i]);
            db.insert("books", null, values);
            i++;
        }

    }


    private String generateBookId() {
        Random random = new Random();
        int bookId = random.nextInt(900000) + 100000;
        return String.valueOf(bookId);
    }
}
