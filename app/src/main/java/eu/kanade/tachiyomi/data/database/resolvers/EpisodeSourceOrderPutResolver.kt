package eu.kanade.tachiyomi.data.database.resolvers

import androidx.core.content.contentValuesOf
import com.pushtorefresh.storio.sqlite.StorIOSQLite
import com.pushtorefresh.storio.sqlite.operations.put.PutResolver
import com.pushtorefresh.storio.sqlite.operations.put.PutResult
import com.pushtorefresh.storio.sqlite.queries.UpdateQuery
import eu.kanade.tachiyomi.data.database.inTransactionReturn
import eu.kanade.tachiyomi.data.database.models.Episode
import eu.kanade.tachiyomi.data.database.tables.EpisodeTable

class EpisodeSourceOrderPutResolver : PutResolver<Episode>() {

    override fun performPut(db: StorIOSQLite, episode: Episode) = db.inTransactionReturn {
        val updateQuery = mapToUpdateQuery(episode)
        val contentValues = mapToContentValues(episode)

        val numberOfRowsUpdated = db.lowLevel().update(updateQuery, contentValues)
        PutResult.newUpdateResult(numberOfRowsUpdated, updateQuery.table())
    }

    fun mapToUpdateQuery(episode: Episode) = UpdateQuery.builder()
        .table(EpisodeTable.TABLE)
        .where("${EpisodeTable.COL_URL} = ? AND ${EpisodeTable.COL_ANIME_ID} = ?")
        .whereArgs(episode.url, episode.anime_id)
        .build()

    fun mapToContentValues(episode: Episode) =
        contentValuesOf(
            EpisodeTable.COL_SOURCE_ORDER to episode.source_order
        )
}
