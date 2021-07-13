package com.example.bigapp.sqlite;

public class DbSchema {
    public static final class NoteTable {
        public static final String NOTE_NAME = "notes";

        public static final class Cols {
            public static final String PHONE_NUMBER = "phoneNumber";
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String CONTEXT = "content";
        }
    }

    public static final class UserTable {
        public static final String USER_NAME = "user";
        public static final class Cols {
            public static final String PHONE_NUMBER = "phoneNumber";
            public static final String PASSWORD = "password";
            public static final String NAME = "name";
            public static final String SEX = "sex";
            public static final String PICTURE  = "picture";
        }
    }
    public static final class DataTable{
        public static final class Cols{
            public static final String PHONE_NUMBER = "phoneNumber"; //我自己的电话号码
            public static final String NAMEId = "nameId"; //对面还是我们
            public static final String CONTENT = "content"; //聊天内容
        }
    }
}
