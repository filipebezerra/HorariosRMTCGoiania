package mx.x10.filipebezerra.horariosrmtcgoiania.model.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import mx.x10.filipebezerra.horariosrmtcgoiania.model.FavoriteBusStop;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table FAVORITE_BUS_STOP.
*/
public class FavoriteBusStopDao extends AbstractDao<FavoriteBusStop, Long> {

    public static final String TABLENAME = "FAVORITE_BUS_STOP";

    /**
     * Properties of entity FavoriteBusStop.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property StopCode = new Property(1, int.class, "stopCode", false, "STOP_CODE");
        public final static Property Address = new Property(2, String.class, "address", false, "ADDRESS");
        public final static Property StopReference = new Property(3, String.class, "stopReference", false, "STOP_REFERENCE");
        public final static Property LinesAvailable = new Property(4, String.class, "linesAvailable", false, "LINES_AVAILABLE");
    };


    public FavoriteBusStopDao(DaoConfig config) {
        super(config);
    }
    
    public FavoriteBusStopDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'FAVORITE_BUS_STOP' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE ," + // 0: id
                "'STOP_CODE' INTEGER NOT NULL UNIQUE ," + // 1: stopCode
                "'ADDRESS' TEXT NOT NULL ," + // 2: address
                "'STOP_REFERENCE' TEXT," + // 3: stopReference
                "'LINES_AVAILABLE' TEXT);"); // 4: linesAvailable
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_FAVORITE_BUS_STOP_STOP_CODE ON FAVORITE_BUS_STOP" +
                " (STOP_CODE);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'FAVORITE_BUS_STOP'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, FavoriteBusStop entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getStopCode());
        stmt.bindString(3, entity.getAddress());
 
        String stopReference = entity.getStopReference();
        if (stopReference != null) {
            stmt.bindString(4, stopReference);
        }
 
        String linesAvailable = entity.getLinesAvailable();
        if (linesAvailable != null) {
            stmt.bindString(5, linesAvailable);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public FavoriteBusStop readEntity(Cursor cursor, int offset) {
        FavoriteBusStop entity = new FavoriteBusStop( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getInt(offset + 1), // stopCode
            cursor.getString(offset + 2), // address
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // stopReference
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) // linesAvailable
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, FavoriteBusStop entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setStopCode(cursor.getInt(offset + 1));
        entity.setAddress(cursor.getString(offset + 2));
        entity.setStopReference(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setLinesAvailable(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(FavoriteBusStop entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(FavoriteBusStop entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
