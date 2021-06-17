package com.sonu.unittesting.ui.noteslist;

import androidx.lifecycle.MutableLiveData;

import com.sonu.unittesting.LiveDataTestUtil;
import com.sonu.unittesting.TestUtil;
import com.sonu.unittesting.models.Note;
import com.sonu.unittesting.repository.NoteRepository;
import com.sonu.unittesting.ui.Resource;
import com.sonu.unittesting.ui.notelist.NotesListViewModel;
import com.sonu.unittesting.util.InstantExecutorExtension;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static com.sonu.unittesting.repository.NoteRepository.DELETE_FAILURE;
import static com.sonu.unittesting.repository.NoteRepository.DELETE_SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(InstantExecutorExtension.class)
public class NotesListViewModelTest {

    // system under test
    private NotesListViewModel viewModel;

    @Mock
    private NoteRepository noteRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        viewModel = new NotesListViewModel(noteRepository);
    }

    /*
        Retrieve list of notes
        observe list
        return list
     */

    @Test
    void retrieveNotes_returnNotesList() throws Exception {
        // Arrange
        List<Note> returnedData = TestUtil.TEST_NOTES_LIST;
        LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<List<Note>> returnedValue = new MutableLiveData<>();
        returnedValue.setValue(returnedData);
        when(noteRepository.getNotes()).thenReturn(returnedValue);

        // Act
        viewModel.getNotes();
        List<Note> observedData = liveDataTestUtil.getValue(viewModel.observeNotes());

        // Assert
        assertEquals(returnedData, observedData);
    }
    /*
        retrieve list of notes
        observe the list
        return empty list
     */

    @Test
    void retrieveNotes_returnEmptyNotesList() throws Exception {
        // Arrange
        List<Note> returnedData = new ArrayList<>();
        LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<List<Note>> returnedValue = new MutableLiveData<>();
        returnedValue.setValue(returnedData);
        when(noteRepository.getNotes()).thenReturn(returnedValue);

        // Act
        viewModel.getNotes();
        List<Note> observedData = liveDataTestUtil.getValue(viewModel.observeNotes());

        // Assert
        assertEquals(returnedData, observedData);
    }


    /*
        delete note
        observe Resource.success
        return Resource.success
     */

    @Test
    void deleteNote_observeResourceSuccess() throws Exception {
        // Arrange
        Note deletedNote = new Note(TestUtil.TEST_NOTE_1);
        Resource<Integer> returnedData = Resource.success(1, DELETE_SUCCESS);
        LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<Resource<Integer>> returnedValue = new MutableLiveData<>();
        returnedValue.setValue(returnedData);
        when(noteRepository.deleteNote(any(Note.class))).thenReturn(returnedValue);

        // Act
        Resource<Integer> observedValue = liveDataTestUtil.getValue(viewModel.deleteNote(deletedNote));


        // Assert
        assertEquals(returnedData, observedValue);
    }

    /*
        delete note
        observe Resource.error
        return Resource.error
     */
    @Test
    void deleteNote_observeResourceError() throws Exception {
        // Arrange
        Note deletedNote = new Note(TestUtil.TEST_NOTE_1);
        Resource<Integer> returnedData = Resource.error(null, DELETE_FAILURE);
        LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<Resource<Integer>> returnedValue = new MutableLiveData<>();
        returnedValue.setValue(returnedData);
        when(noteRepository.deleteNote(any(Note.class))).thenReturn(returnedValue);

        // Act
        Resource<Integer> observedValue = liveDataTestUtil.getValue(viewModel.deleteNote(deletedNote));


        // Assert
        assertEquals(returnedData, observedValue);
    }

}

