package mx.x10.filipebezerra.horariosrmtcgoiania.model.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import mx.x10.filipebezerra.horariosrmtcgoiania.model.FavoriteBusStop;

import mx.x10.filipebezerra.horariosrmtcgoiania.model.dao.FavoriteBusStopDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig favoriteBusStopDaoConfig;

    private final FavoriteBusStopDao favoriteBusStopDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        favoriteBusStopDaoConfig = daoConfigMap.get(FavoriteBusStopDao.class).clone();
        favoriteBusStopDaoConfig.initIdentityScope(type);

        favoriteBusStopDao = new FavoriteBusStopDao(favoriteBusStopDaoConfig, this);

        registerDao(FavoriteBusStop.class, favoriteBusStopDao);
    }
    
    public void clear() {
        favoriteBusStopDaoConfig.getIdentityScope().clear();
    }

    public FavoriteBusStopDao getFavoriteBusStopDao() {
        return favoriteBusStopDao;
    }

}