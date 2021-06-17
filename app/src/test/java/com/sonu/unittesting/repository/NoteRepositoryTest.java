package com.sonu.unittesting.repository;

import androidx.lifecycle.MutableLiveData;

import com.sonu.unittesting.LiveDataTestUtil;
import com.sonu.unittesting.TestUtil;
import com.sonu.unittesting.models.Note;
import com.sonu.unittesting.persistence.NoteDao;
import com.sonu.unittesting.ui.Resource;
import com.sonu.unittesting.util.InstantExecutorExtension;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Not;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

import static com.sonu.unittesting.repository.NoteRepository.DELETE_FAILURE;
import static com.sonu.unittesting.repository.NoteRepository.DELETE_SUCCESS;
import static com.sonu.unittesting.repository.NoteRepository.INSERT_FAILURE;
import static com.sonu.unittesting.repository.NoteRepository.INSERT_SUCCESS;
import static com.sonu.unittesting.repository.NoteRepository.INVALID_NOTE_ID;
import static com.sonu.unittesting.repository.NoteRepository.NOTE_TITLE_NULL;
import static com.sonu.unittesting.repository.NoteRepository.UPDATE_FAILURE;
import static com.sonu.unittesting.repository.NoteRepository.UPDATE_SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(InstantExecutorExtension.class)
public class NoteRepositoryTest {

    final Note NOTE1 = new Note(TestUtil.TEST_NOTE_1);
    //system under test
    private NoteRepository repository;

    private NoteDao noteDao;

    @BeforeEach
    public void initEach() {
        noteDao = Mockito.mock(NoteDao.class);
        repository = new NoteRepository(noteDao);
    }

    /*
        insert note
        verify correct method is called
        confirm observe is triggered
        confirm new rows is inserted
     */

    @Test
    void insertNote_return_Row() throws Exception {
        //Arrange
        final Long insertedRow = 1L;
        final Single<Long> returnedData = Single.just(insertedRow);
        when(noteDao.insertNote(any(Note.class))).thenReturn(returnedData);

        //Act
        final Resource<Integer> returnedValue = repository.insertNote(NOTE1).blockingFirst();

        //Assert
        verify(noteDao).insertNote(any(Note.class));
        verifyNoMoreInteractions(noteDao);

        System.out.println("returnValue " + returnedValue.data);

        assertEquals(Resource.success(1, INSERT_SUCCESS), returnedValue);

        // or test using RxJava
//        repository.insertNote(NOTE1)
//                .test()
//                .await()
//                .assertValue(Resource.success(1 , INSERT_SUCCESS));

    }

    /*
        insert note
        Failure( return -1)
     */

    @Test
    void insertNote_returnError() throws Exception {
        //Arrange
        final Long failedStart = -1L;
        final Single<Long> returnedFailedData = Single.just(failedStart);
        when(noteDao.insertNote(any(Note.class))).thenReturn(returnedFailedData);

        //Act
        final Resource<Integer> returnedFailedValue = repository.insertNote(NOTE1).blockingFirst();

        //Assert
        verify(noteDao).insertNote(any(Note.class));
        verifyNoMoreInteractions(noteDao);
        assertEquals(Resource.error(null, INSERT_FAILURE), returnedFailedValue);
    }
    
    /*
        insert note
        title null 
        throw exception
     */

    @Test
    void insertNote_titleNull_throwError() throws Exception {
        Exception exception = assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                final Note note = new Note(TestUtil.TEST_NOTE_1);
                note.setTitle(null);
                repository.insertNote(note);
            }
        });
        assertEquals(NOTE_TITLE_NULL, exception.getMessage());
    }

    /*
        update note
        verify correct method is called
        confirm observer is triggered
        confirm no of rows updated
     */

    @Test
    void updateNoteReturn_rows_updated() throws Exception {
        //Arrange
        final int updatedRow = 1;
        when(noteDao.updateNote(any(Note.class))).thenReturn(Single.just(updatedRow));
        //Act

        final Resource<Integer> returnedData = repository.updateNote(NOTE1).blockingFirst();
        //Assert
        verify(noteDao).updateNote(any(Note.class));
        verifyNoMoreInteractions(noteDao);
        assertEquals(Resource.success(updatedRow, UPDATE_SUCCESS), returnedData);
    }
    
    /*
        update note
        Failed (-1)
     */

    @Test
    void updateNote_returnFailure() throws Exception {
        //Arrange
        final int failedInsert = -1;
        when(noteDao.updateNote(any(Note.class))).thenReturn(Single.just(failedInsert));
        //Act

        final Resource<Integer> returnedData = repository.updateNote(NOTE1).blockingFirst();
        //Assert
        verify(noteDao).updateNote(any(Note.class));
        verifyNoMoreInteractions(noteDao);
        assertEquals(Resource.error(null, UPDATE_FAILURE), returnedData);

    }

    /*
        update note
        title null
        throws exception

     */

    @Test
    void updateNote_titleNull_throwError() throws Exception {
        Exception exception = assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                final Note note = new Note(TestUtil.TEST_NOTE_1);
                note.setTitle(null);
                repository.updateNote(note);
            }
        });
        assertEquals(NOTE_TITLE_NULL, exception.getMessage());
    }

     /*
        delete note
        null id
        throw exception
     */

    @Test
    void deleteNote_nullId_throwException() throws Exception {
        Exception exception = assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                final Note note = new Note(TestUtil.TEST_NOTE_1);
                note.setId(-1);
                repository.deleteNote(note);
            }
        });

        assertEquals(INVALID_NOTE_ID, exception.getMessage());
    }

    /*
        delete note
        delete success
        return Resource.success with deleted row
     */

    @Test
    void deleteNote_deleteSuccess_returnResourceSuccess() throws Exception {
        // Arrange
        final int deletedRow = 1;
        Resource<Integer> successResponse = Resource.success(deletedRow, DELETE_SUCCESS);
        LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        when(noteDao.deleteNote(any(Note.class))).thenReturn(Single.just(deletedRow));

        // Act
        Resource<Integer> observedResponse = liveDataTestUtil.getValue(repository.deleteNote(NOTE1));

        // Assert
        assertEquals(successResponse, observedResponse);
    }


    /*
        delete note
        delete failure
        return Resource.error
     */
    @Test
    void deleteNote_deleteFailure_returnResourceError() throws Exception {
        // Arrange
        final int deletedRow = -1;
        Resource<Integer> errorResponse = Resource.error(null, DELETE_FAILURE);
        LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        when(noteDao.deleteNote(any(Note.class))).thenReturn(Single.just(deletedRow));

        // Act
        Resource<Integer> observedResponse = liveDataTestUtil.getValue(repository.deleteNote(NOTE1));

        // Assert
        assertEquals(errorResponse, observedResponse);
    }


    /*
        retrieve notes
        return list of notes
     */

    @Test
    void getNotes_returnListWithNotes() throws Exception {
        // Arrange
        List<Note> notes = TestUtil.TEST_NOTES_LIST;
        LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<List<Note>> returnedData = new MutableLiveData<>();
        returnedData.setValue(notes);
        when(noteDao.getNotes()).thenReturn(returnedData);

        // Act
        List<Note> observedData = liveDataTestUtil.getValue(repository.getNotes());

        // Assert
        assertEquals(notes, observedData);
    }

    /*
        retrieve notes
        return empty list
     */

    @Test
    void getNotes_returnEmptyList() throws Exception {
        // Arrange
        List<Note> notes = new ArrayList<>();
        LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<List<Note>> returnedData = new MutableLiveData<>();
        returnedData.setValue(notes);
        when(noteDao.getNotes()).thenReturn(returnedData);

        // Act
        List<Note> observedData = liveDataTestUtil.getValue(repository.getNotes());

        // Assert
        assertEquals(notes, observedData);
    }
}

