package com.example.tejasphatakwala.advjavaassign_note;

import java.util.Date;

public class Note {

    String NoteId;
    String Subject;
    String Title;
    String Description;
    String CreatedDate;

    Note() {
        NoteId = "";
        Subject = "";
        Title = "";
        Description = "";
    }

    Note(String id, String sub, String title, String des , String curDate) {
        NoteId = id;
        Subject = sub;
        Title = title;
        Description = des;
        CreatedDate = curDate;
    }
}
