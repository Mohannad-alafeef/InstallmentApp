package dev.kkarot.installmentapp.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.kkarot.installmentapp.database.dao.CustomerDao
import dev.kkarot.installmentapp.database.CustomerDatabase
import dev.kkarot.installmentapp.database.dao.InstallmentDao
import dev.kkarot.installmentapp.database.dao.PaymentDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideDatabase(app: Application):CustomerDatabase {
       return Room.databaseBuilder(
            app.applicationContext,
            CustomerDatabase::class.java,
            "customer_database.db"
        ).build()
    }
    @Provides
    @Singleton
    fun provideCustomerDao(db:CustomerDatabase): CustomerDao {
       return db.customerDao()
    }
    @Provides
    @Singleton
    fun provideInstallmentDao(db:CustomerDatabase): InstallmentDao {
        return db.installmentDao()
    }
    @Provides
    @Singleton
    fun providePaymentDao(db:CustomerDatabase): PaymentDao {
        return db.paymentDao()
    }
}