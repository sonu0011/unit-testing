package com.sonu.unittesting.di;

import com.sonu.unittesting.ui.note.NoteActivity;
import com.sonu.unittesting.ui.notelist.NoteListActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @ContributesAndroidInjector
    abstract NoteListActivity contributeNoteListActivity();

    @ContributesAndroidInjector
    abstract NoteActivity contributeNoteActivity();
}
