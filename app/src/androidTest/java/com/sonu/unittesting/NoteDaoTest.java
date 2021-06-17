package com.sonu.unittesting;

import android.database.sqlite.SQLiteConstraintException;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.room.Insert;

import com.sonu.unittesting.models.Note;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class NoteDaoTest extends NoteDatabaseTest {
    public static final String NOTE_CONTENT = "This  is  updated content";
    public static final String NOTE_TITLE = "This is updated title";
    public static final String NOTE_TIMESTAMP = "06-2022";


    //junit4 requires this rule to run on main thread
    @Rule
    public InstantTaskExecutorRule taskExecutorRule = new InstantTaskExecutorRule();
    /*
        Insert , Read  , Delete
     */

    @Test
    public void insertReadDeleted() throws Exception {
        Note note = TestUtil.TEST_NOTE_1;
        //insert
        getNoteDao().insertNote(note).blockingGet();

        //read
        LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
        List<Note> noteList = liveDataTestUtil.getValue(getNoteDao().getNotes());
        assertNotNull(noteList);
        assertEquals(note.getTimestamp(), noteList.get(0).getTimestamp());
        assertEquals(note.getContent(), noteList.get(0).getContent());
        assertEquals(note.getTitle(), noteList.get(0).getTitle());

        note.setId(noteList.get(0).getId());
        assertEquals(note, noteList.get(0));

        //confirm database is empty
        getNoteDao().deleteNote(note).blockingGet();
        noteList = liveDataTestUtil.getValue(getNoteDao().getNotes());
        assertEquals(0, noteList.size());
    }

    /*
           Insert, Read, Update, Read, Delete,

     */
    @Test
    public void insertReadUpdateReadDelete() throws Exception {
        Note note = new Note(TestUtil.TEST_NOTE_1);
        System.out.println(note.toString());
        note.setTitle("new title");
        //insert
        getNoteDao().insertNote(note).blockingGet();

        //read
        LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
        List<Note> insertedNotes = liveDataTestUtil.getValue(getNoteDao().getNotes());
        assertNotNull(insertedNotes);

        assertEquals(note.getContent(), insertedNotes.get(0).getContent());
        assertEquals(note.getTimestamp(), insertedNotes.get(0).getTimestamp());
        assertEquals(note.getTitle(), insertedNotes.get(0).getTitle());

        note.setId(insertedNotes.get(0).getId());
        assertEquals(note, insertedNotes.get(0));


        //update
        note.setTitle(NOTE_TITLE);
        note.setContent(NOTE_CONTENT);
        note.setTimestamp(NOTE_TIMESTAMP);
        getNoteDao().updateNote(note).blockingGet();

        //read

        insertedNotes = liveDataTestUtil.getValue(getNoteDao().getNotes());
        assertNotNull(insertedNotes);

        assertEquals(insertedNotes.get(0).getTitle(), NOTE_TITLE);
        assertEquals(insertedNotes.get(0).getContent(), NOTE_CONTENT);
        assertEquals(insertedNotes.get(0).getTimestamp(), NOTE_TIMESTAMP);

        note.setId(insertedNotes.get(0).getId());
        assertEquals(note, insertedNotes.get(0));
        // delete
        getNoteDao().deleteNote(note).blockingGet();

        // confirm the database is empty
        insertedNotes = liveDataTestUtil.getValue(getNoteDao().getNotes());
        assertEquals(0, insertedNotes.size());


    }

    /*
          Insert Note with null title throws exception
     */

    @Test(expected = SQLiteConstraintException.class)
    public void insertWithNullTitle() throws Exception {

        final Note note = new Note(TestUtil.TEST_NOTE_1);
        note.setTitle(null);
        //insert
        getNoteDao().insertNote(note).blockingGet();
    }

    /*
        Insert Update with null title throws exception
     */

    @Test(expected = SQLiteConstraintException.class)
    public void insertUpdateWithNullTitle() throws Exception {
        Note note = TestUtil.TEST_NOTE_1;
        //insert
        getNoteDao().insertNote(note).blockingGet();

        //read
        LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
        List<Note> noteList = liveDataTestUtil.getValue(getNoteDao().getNotes());
        assertNotNull(noteList);


        note.setTitle(null);
        getNoteDao().updateNote(note).blockingGet();

    }
}
