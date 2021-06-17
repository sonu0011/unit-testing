package com.sonu.unittesting.ui.note;

import com.sonu.unittesting.LiveDataTestUtil;
import com.sonu.unittesting.TestUtil;
import com.sonu.unittesting.models.Note;
import com.sonu.unittesting.repository.NoteRepository;
import com.sonu.unittesting.ui.Resource;
import com.sonu.unittesting.util.InstantExecutorExtension;
import com.sonu.unittesting.viewmodels.NoteViewModel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Flowable;
import io.reactivex.internal.operators.single.SingleToFlowable;

import static com.sonu.unittesting.repository.NoteRepository.INSERT_SUCCESS;
import static com.sonu.unittesting.repository.NoteRepository.NOTE_TITLE_NULL;
import static com.sonu.unittesting.repository.NoteRepository.UPDATE_SUCCESS;
import static com.sonu.unittesting.viewmodels.NoteViewModel.NO_CONTENT_ERROR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(InstantExecutorExtension.class)
public class NoteViewModelTest {

    //system under test
    private NoteViewModel noteViewModel;

    @Mock
    private NoteRepository noteRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        noteViewModel = new NoteViewModel(noteRepository);
    }
    /*
        can't observe a note that has not been set
     */

    @Test
    void observeEmptyNoteWhenNoteSet() throws Exception {
        //Arrange
        LiveDataTestUtil<Note> liveDataTestUtil = new LiveDataTestUtil<>();

        //Act
        Note note = liveDataTestUtil.getValue(noteViewModel.observeNote());

        //Assert
        assertNull(note);
    }

    /*
         observe a note that has been set  and onChange will gets trigger in activity
      */
    @Test
    void observeNote_whenSet() throws Exception {
        //Arrange
        LiveDataTestUtil<Note> liveDataTestUtil = new LiveDataTestUtil<>();
        Note myNote = new Note(TestUtil.TEST_NOTE_1);
        //Act
        noteViewModel.setNote(myNote);
        Note note = liveDataTestUtil.getValue(noteViewModel.observeNote());
        //Assert
        assertEquals(myNote, note);
    }

    /*
        insert a new note and observe the return row
     */

    @Test
    void insertNote_ObserveRow() throws Exception {
        //Arrange
        Note note = new Note(TestUtil.TEST_NOTE_1);
        LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        final int insertedRow = 1;
        Flowable<Resource<Integer>> returnedData = SingleToFlowable.just(Resource.success(insertedRow, INSERT_SUCCESS));
        when(noteRepository.insertNote(any(Note.class))).thenReturn(returnedData);

        //Act
        noteViewModel.setNote(note);
        noteViewModel.setIsNewNote(true);
        Resource<Integer> result = liveDataTestUtil.getValue(noteViewModel.saveNote());
        //Assert

        assertEquals(Resource.success(insertedRow, INSERT_SUCCESS), result);
    }
        
    /*
        insert don't return a new row without observer
     */

    @Test
    void dontReturnInsertRowWithoutObserver() throws Exception {
        //Arrange
        final Note note = new Note(TestUtil.TEST_NOTE_1);
        //Act
        noteViewModel.setNote(note);
        //Assert

        verify(noteRepository, never()).insertNote(any(Note.class));
    }
    
    /*
        set note null title throws exception
     */

    @Test
    void setNoteNull_throws_exception() throws Exception {
        //Arrange
        final Note note = new Note(TestUtil.TEST_NOTE_1);

        //Act
        note.setTitle(null);
        Exception exception = assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                noteViewModel.setNote(note);
            }
        });
        //Assert
        assertEquals(NOTE_TITLE_NULL, exception.getMessage());
    }

      /*
        update a new note and observe the return row
     */

    @Test
    void updateNote_ObserveRow() throws Exception {
        //Arrange
        Note note = new Note(TestUtil.TEST_NOTE_1);
        LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        final int updatedRow = 1;
        Flowable<Resource<Integer>> returnedData = SingleToFlowable.just(Resource.success(updatedRow, UPDATE_SUCCESS));
        when(noteRepository.updateNote(any(Note.class))).thenReturn(returnedData);

        //Act
        noteViewModel.setNote(note);
        noteViewModel.setIsNewNote(false);
        Resource<Integer> result = liveDataTestUtil.getValue(noteViewModel.saveNote());
        //Assert

        assertEquals(Resource.success(updatedRow, UPDATE_SUCCESS), result);
    }

     /*
        update don't return a new row without observer
     */

    @Test
    void dontReturnUpdateRowWithoutObserver() throws Exception {
        //Arrange
        final Note note = new Note(TestUtil.TEST_NOTE_1);
        //Act
        noteViewModel.setNote(note);
        //Assert

        verify(noteRepository, never()).updateNote(any(Note.class));
    }

    /*
        save note content null
        return exception
     */

    @Test
    void saveNote_contentNull_returnError() throws Exception {
        //Arrange
        final Note note = new Note(TestUtil.TEST_NOTE_1);

        //Act
        note.setContent(null);
        noteViewModel.setNote(note);
        noteViewModel.setIsNewNote(true);
        Exception exception = assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                noteViewModel.saveNote();
            }
        });
        //Assert
        assertEquals(NO_CONTENT_ERROR, exception.getMessage());
    }
}
