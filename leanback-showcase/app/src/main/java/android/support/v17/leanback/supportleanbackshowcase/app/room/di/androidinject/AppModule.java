package android.support.v17.leanback.supportleanbackshowcase.app.room.di.androidinject;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.support.v17.leanback.supportleanbackshowcase.app.room.db.AppDatabase;
import android.support.v17.leanback.supportleanbackshowcase.app.room.db.dao.CategoryDao;
import android.support.v17.leanback.supportleanbackshowcase.app.room.db.dao.VideoDao;
import android.support.v17.leanback.supportleanbackshowcase.app.room.di.androidinjectorannotation.LiveDataOverviewActivitySubcomponent;
import android.support.v17.leanback.supportleanbackshowcase.app.room.di.viewmodel.ViewModelModule;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;


@Module(includes= ViewModelModule.class, subcomponents = LiveDataOverviewActivitySubcomponent.class)
public class AppModule {

  @Singleton
  @Provides
  AppDatabase provideAppDatabase(Application app) {
    return Room.databaseBuilder(app, AppDatabase.class, AppDatabase.DATABASE_NAME).build();
  }

  @Singleton
  @Provides
  CategoryDao provideCategoryDao(AppDatabase db) {
    return db.categoryDao();
  }

  @Singleton
  @Provides
  VideoDao provideVideoDao(AppDatabase db) {
    return db.videoDao();
  }
}
