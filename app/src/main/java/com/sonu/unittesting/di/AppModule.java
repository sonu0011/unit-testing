package com.sonu.unittesting.di;

import android.app.Application;

import androidx.room.Room;

import com.sonu.unittesting.persistence.NoteDao;
import com.sonu.unittesting.persistence.NoteDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static com.sonu.unittesting.persistence.NoteDatabase.DATABASE_NAME;

@Module
public class AppModule {

    @Singleton
    @Provides
    static NoteDatabase provideNoteDatabase(Application application) {
        return Room.databaseBuilder
                (application, NoteDatabase.class, DATABASE_NAME).build();
    }

    @Singleton
    @Provides
    static NoteDao provideNoteDao(NoteDatabase database) {
        return database.getNoteDao();
    }
}
