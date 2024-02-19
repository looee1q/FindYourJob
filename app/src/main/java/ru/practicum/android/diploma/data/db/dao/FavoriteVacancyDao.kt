package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.practicum.android.diploma.data.db.entity.FavoriteVacancyEntity

@Dao
interface FavoriteVacancyDao {
    @Insert(entity = FavoriteVacancyEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacancy(vacancy: FavoriteVacancyEntity)

    @Delete(entity = FavoriteVacancyEntity::class)
    suspend fun deleteVacancy(vacancy: FavoriteVacancyEntity)

    @Query("SELECT * FROM fav_vacancy_table ORDER BY timeAddToFav DESC")
    suspend fun getAllVacancies(): List<FavoriteVacancyEntity>

    @Query("SELECT id FROM fav_vacancy_table")
    suspend fun getVacancyId(): List<String>
}
