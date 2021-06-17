package com.sonu.unittesting.models;

import com.sonu.unittesting.TestUtil;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NoteTest {

    public static final String TIME_STAMP_1 = "05-19990";
    public static final String TIME_STAMP_2 = "06-19990";

    /*
        Compare two notes equal
     */
    @Test
    void isNotesEqual_identicalProperties_returnTrue() throws Exception {
        //Arrange
        Note note1 = new Note("Note #1", "this is note #1", TIME_STAMP_1);
        note1.setId(1);
        Note note2 = new Note("Note #1", "this is note #1", TIME_STAMP_1);
        note2.setId(1);

        //Act

        //Assert
        assertEquals(note1, note2);
        System.out.println("Notes are equal");
    }


    /*
        Compare two notes with two different ids
     */

    @Test
    void isNotesEqual_differentIds_returnFalse() throws Exception {
        //Arrange
        Note note1 = new Note("Note #1", "this is note #1", TIME_STAMP_1);
        note1.setId(1);
        Note note2 = new Note("Note #1", "this is note #1", TIME_STAMP_1);
        note2.setId(2);

        //Act

        //Assert
        assertNotEquals(note1, note2);
        System.out.println("Notes are not equal");


    }
    /*
        compare two notes with two different timestamps
     */

    @Test
    void isNotesEqual_different_timestamps_returnTrue() throws Exception {
        //Arrange
        Note note1 = new Note("Note #1", "this is note #1", TIME_STAMP_1);
        note1.setId(1);
        Note note2 = new Note("Note #1", "this is note #1", TIME_STAMP_2);
        note2.setId(1);
        //Act

        //Assert
        assertEquals(note1, note2);
        System.out.println("Notes are  equal with different timestamps");

    }
    
    /*
        compare two notes with different titles
     */

    @Test
    void isNotesEqual_different_titles_returnFalse() throws Exception {
        //Arrange
        Note note1 = new Note("Note #1", "this is note #1", TIME_STAMP_1);
        note1.setId(1);
        Note note2 = new Note("Note #2", "this is note #1", TIME_STAMP_2);
        note2.setId(1);
        //Act

        //Assert
        assertNotEquals(note1, note2);
        System.out.println("Notes are not equal with different titles  ");

    }
    /*
        compare notes with different content
     */

    @Test
    void isnotesEqual_different_contents_returnFalse() throws Exception {
        //Arrange
        Note note1 = new Note("Note #1", "this is note #1", TIME_STAMP_1);
        note1.setId(1);
        Note note2 = new Note("Note #1", "this is note #2", TIME_STAMP_2);
        note2.setId(1);
        //Act

        //Assert
        assertNotEquals(note1, note2);
        System.out.println("Notes are not equal");

    }
}
