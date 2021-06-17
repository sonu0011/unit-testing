package com.sonu.unittesting.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.sonu.unittesting.models.Note;
import com.sonu.unittesting.persistence.NoteDao;
import com.sonu.unittesting.ui.Resource;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class NoteRepository {
    public static final String NOTE_TITLE_NULL = "Note title cannot be null";
    public static final String INVALID_NOTE_ID = "Invalid id. Can't delete note";
    public static final String DELETE_SUCCESS = "Delete success";
    public static final String DELETE_FAILURE = "Delete failure";
    public static final String UPDATE_SUCCESS = "Update success";
    public static final String UPDATE_FAILURE = "Update failure";
    public static final String INSERT_SUCCESS = "Insert success";
    public static final String INSERT_FAILURE = "Insert failure";

    private int timeDelay = 0;
    private TimeUnit timeUnit = TimeUnit.SECONDS;


    //inject
    private NoteDao noteDao;

    @Inject
    public NoteRepository(NoteDao noteDao) {
        this.noteDao = noteDao;
    }


    public Flowable<Resource<Integer>> insertNote(final Note note) throws Exception {
        checkTitle(note);
        return noteDao.insertNote(note)
                .delaySubscription(timeDelay, timeUnit)
                .map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(@NonNull Long aLong) throws Exception {
                        long l = aLong;
                        return (int) l;
                    }
                })
                .onErrorReturn(new Function<Throwable, Integer>() {
                    @Override
                    public Integer apply(@NonNull Throwable throwable) throws Exception {
                        return -1;
                    }
                })
                .map(new Function<Integer, Resource<Integer>>() {
                    @Override
                    public Resource<Integer> apply(@NonNull Integer integer) throws Exception {
                        if (integer > 0) {
                            return Resource.success(integer, INSERT_SUCCESS);
                        } else {
                            return Resource.error(null, INSERT_FAILURE);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .toFlowable();
    }

    public Flowable<Resource<Integer>> updateNote(final Note note) throws Exception {
        checkTitle(note);
        return noteDao.updateNote(note)
                .delaySubscription(timeDelay, timeUnit)

                .onErrorReturn(new Function<Throwable, Integer>() {
                    @Override
                    public Integer apply(@NonNull Throwable throwable) throws Exception {
                        return -1;
                    }
                })
                .map(new Function<Integer, Resource<Integer>>() {
                    @Override
                    public Resource<Integer> apply(@NonNull Integer integer) throws Exception {
                        if (integer > 0) {
                            return Resource.success(integer, UPDATE_SUCCESS);
                        } else {
                            return Resource.error(null, UPDATE_FAILURE);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .toFlowable();
    }


    private void checkTitle(Note note) throws Exception {
        if (note.getTitle() == null) {
            throw new Exception(NOTE_TITLE_NULL);
        }
    }



    public LiveData<Resource<Integer>> deleteNote(final Note note) throws Exception{

        checkId(note);

        return LiveDataReactiveStreams.fromPublisher(
                noteDao.deleteNote(note)
                        .onErrorReturn(new Function<Throwable, Integer>() {
                            @Override
                            public Integer apply(Throwable throwable) throws Exception {
                                return -1;
                            }
                        })
                        .map(new Function<Integer, Resource<Integer>>() {
                            @Override
                            public Resource<Integer> apply(Integer integer) throws Exception {
                                if(integer > 0){
                                    return Resource.success(integer, DELETE_SUCCESS);
                                }
                                return Resource.error(null, DELETE_FAILURE);
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .toFlowable()
        );
    }

    public LiveData<List<Note>> getNotes(){
        return noteDao.getNotes();
    }

    private void checkId(Note note) throws Exception{
        if(note.getId() < 0){
            throw new Exception(INVALID_NOTE_ID);
        }
    }
}
