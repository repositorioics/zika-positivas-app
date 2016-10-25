package ni.org.ics.zip.appmovil.database;

/**
 * Adaptador de la base de datos Cohorte
 * 
 * @author William Aviles
 */

import java.util.ArrayList;
import java.util.List;

import ni.org.ics.zip.appmovil.domain.*;
import ni.org.ics.zip.appmovil.domain.users.Authority;
import ni.org.ics.zip.appmovil.domain.users.UserSistema;
import ni.org.ics.zip.appmovil.helpers.*;
import ni.org.ics.zip.appmovil.utils.*;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteQueryBuilder;

public class ZipAdapter {

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	private final Context mContext;
	private final String mPassword;
	private final boolean mFromServer;
	

	public ZipAdapter(Context context, String password, boolean fromServer) {
		mContext = context;
		mPassword = password;
		mFromServer = fromServer;
	}
	
	private static class DatabaseHelper extends ZipSQLiteOpenHelper {

		DatabaseHelper(Context context, String password, boolean fromServer) {
			super(FileUtils.DATABASE_PATH, MainDBConstants.DATABASE_NAME, MainDBConstants.DATABASE_VERSION, context,
					password, fromServer);
			createStorage();
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(MainDBConstants.CREATE_USER_TABLE);
			db.execSQL(MainDBConstants.CREATE_ROLE_TABLE);
			db.execSQL(MainDBConstants.CREATE_SCREENING_TABLE);
			db.execSQL(Zp01DBConstants.CREATE_STUDYENTRY_AD_TABLE);
            db.execSQL(Zp01DBConstants.CREATE_STUDYENTRY_E_TABLE);
            db.execSQL(Zp01DBConstants.CREATE_STUDYENTRY_FK_TABLE);
            db.execSQL(Zp02DBConstants.CREATE_BIOCOLLECTION_TABLE);
            db.execSQL(Zp03DBConstants.CREATE_MONTHLYVISIT_TABLE);
            db.execSQL(Zp04DBConstants.CREATE_TRIMESTERVISIT_AD_TABLE);
            db.execSQL(Zp04DBConstants.CREATE_TRIMESTERVISIT_E_TABLE);
            db.execSQL(Zp04DBConstants.CREATE_TRIMESTERVISIT_FH_TABLE);
            db.execSQL(Zp05DBConstants.CREATE_ULTRASOUNDEXAM_TABLE);
            db.execSQL(Zp06DBConstants.CREATE_DELIVERY6WVISIT_TABLE);
            db.execSQL(Zp08DBConstants.CREATE_STUDYEXIT_TABLE);
		}

		@Override
		// upgrading will destroy all old data
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + MainDBConstants.USER_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + MainDBConstants.ROLE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + MainDBConstants.SCREENING_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + Zp01DBConstants.CREATE_STUDYENTRY_AD_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + Zp01DBConstants.CREATE_STUDYENTRY_E_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + Zp01DBConstants.CREATE_STUDYENTRY_FK_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + Zp02DBConstants.CREATE_BIOCOLLECTION_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + Zp03DBConstants.CREATE_MONTHLYVISIT_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + Zp04DBConstants.CREATE_TRIMESTERVISIT_AD_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + Zp04DBConstants.CREATE_TRIMESTERVISIT_E_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + Zp04DBConstants.CREATE_TRIMESTERVISIT_FH_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + Zp05DBConstants.CREATE_ULTRASOUNDEXAM_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + Zp06DBConstants.CREATE_DELIVERY6WVISIT_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + Zp08DBConstants.CREATE_STUDYEXIT_TABLE);
			onCreate(db);
		}
	}

	public ZipAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mContext,mPassword,mFromServer);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}
	
	/**
	 * Crea un cursor desde la base de datos
	 * 
	 * @return cursor
	 */
	public Cursor crearCursor(String tabla, String whereString, String projection[], String ordenString) throws SQLException {
		Cursor c = null;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(tabla);
		c = qb.query(mDb,projection,whereString,null,null,null,ordenString);
		return c;
	}

	public static boolean createStorage() {
		return FileUtils.createFolder(FileUtils.DATABASE_PATH);
	}
	
	/**
	 * Metodos para usuarios en la base de datos
	 * 
	 * @param user
	 *            Objeto Usuario que contiene la informacion
	 *
	 */
	//Crear nuevo usuario en la base de datos
	public void crearUsuario(UserSistema user) {
		ContentValues cv = UserSistemaHelper.crearUserSistemaContentValues(user);
		mDb.insert(MainDBConstants.USER_TABLE, null, cv);
	}
	//Editar usuario existente en la base de datos
	public boolean editarUsuario(UserSistema user) {
		ContentValues cv = UserSistemaHelper.crearUserSistemaContentValues(user);
		return mDb.update(MainDBConstants.USER_TABLE, cv, MainDBConstants.username + "='" 
				+ user.getUsername()+"'", null) > 0;
	}
	//Limpiar la tabla de usuarios de la base de datos
	public boolean borrarUsuarios() {
		return mDb.delete(MainDBConstants.USER_TABLE, null, null) > 0;
	}
	//Obtener un usuario de la base de datos
	public UserSistema getUsuario(String filtro, String orden) throws SQLException {
		UserSistema mUser = null;
		Cursor cursorUser = crearCursor(MainDBConstants.USER_TABLE, filtro, null, orden);
		if (cursorUser != null && cursorUser.getCount() > 0) {
			cursorUser.moveToFirst();
			mUser=UserSistemaHelper.crearUserSistema(cursorUser);
		}
		if (!cursorUser.isClosed()) cursorUser.close();
		return mUser;
	}
	//Obtener una lista de usuarios de la base de datos
	public List<UserSistema> getUsuarios(String filtro, String orden) throws SQLException {
		List<UserSistema> mUsuarios = new ArrayList<UserSistema>();
		Cursor cursorUsuarios = crearCursor(MainDBConstants.USER_TABLE, filtro, null, orden);
		if (cursorUsuarios != null && cursorUsuarios.getCount() > 0) {
			cursorUsuarios.moveToFirst();
			mUsuarios.clear();
			do{
				UserSistema mUser = null;
				mUser = UserSistemaHelper.crearUserSistema(cursorUsuarios);
				mUsuarios.add(mUser);
			} while (cursorUsuarios.moveToNext());
		}
		if (!cursorUsuarios.isClosed()) cursorUsuarios.close();
		return mUsuarios;
	}
	
	/**
	 * Metodos para roles en la base de datos
	 * 
	 * @param rol
	 *            Objeto Authority que contiene la informacion
	 *
	 */
	//Crear nuevo rol en la base de datos
	public void crearRol(Authority rol) {
		ContentValues cv = UserSistemaHelper.crearRolValues(rol);
		mDb.insert(MainDBConstants.ROLE_TABLE, null, cv);
	}
	//Limpiar la tabla de roles de la base de datos
	public boolean borrarRoles() {
		return mDb.delete(MainDBConstants.ROLE_TABLE, null, null) > 0;
	}
	//Verificar un rol de usuario
	public Boolean buscarRol(String username, String Rol) throws SQLException {
		Cursor c = mDb.query(true, MainDBConstants.ROLE_TABLE, null,
				MainDBConstants.username + "='" + username + "' and " + MainDBConstants.role + "='" + Rol + "'" , null, null, null, null, null);
		boolean result = c != null && c.getCount()>0; 
		c.close();
		return result;
	}
	
	
	/**
	 * Metodos para Zp00Screening en la base de datos
	 * 
	 * @param screening
	 *            Objeto Zp00Screening que contiene la informacion
	 *
	 */
	//Crear nuevo Zp00Screening en la base de datos
	public void crearZp00Screening(Zp00Screening screening) {
		ContentValues cv = Zp00ScreeningHelper.crearZp00ScreeningValues(screening);
		mDb.insert(MainDBConstants.SCREENING_TABLE, null, cv);
	}
	//Editar Zp00Screening existente en la base de datos
	public boolean editarZp00Screening(Zp00Screening screening) {
		ContentValues cv = Zp00ScreeningHelper.crearZp00ScreeningValues(screening);
		return mDb.update(MainDBConstants.SCREENING_TABLE, cv, MainDBConstants.recordId + "='" 
				+ screening.getRecordId()+"'", null) > 0;
	}
	//Limpiar la tabla de Zp00Screening de la base de datos
	public boolean borrarZp00Screening() {
		return mDb.delete(MainDBConstants.SCREENING_TABLE, null, null) > 0;
	}
	//Obtener un Zp00Screening de la base de datos
	public Zp00Screening getZp00Screening(String filtro, String orden) throws SQLException {
		Zp00Screening mScreening = null;
		Cursor cursorScreening = crearCursor(MainDBConstants.SCREENING_TABLE, filtro, null, orden);
		if (cursorScreening != null && cursorScreening.getCount() > 0) {
			cursorScreening.moveToFirst();
			mScreening=Zp00ScreeningHelper.crearZp00Screening(cursorScreening);
		}
		if (!cursorScreening.isClosed()) cursorScreening.close();
		return mScreening;
	}
	//Obtener una lista de Zp00Screening de la base de datos
	public List<Zp00Screening> getZp00Screenings(String filtro, String orden) throws SQLException {
		List<Zp00Screening> mScreenings = new ArrayList<Zp00Screening>();
		Cursor cursorScreenings = crearCursor(MainDBConstants.SCREENING_TABLE, filtro, null, orden);
		if (cursorScreenings != null && cursorScreenings.getCount() > 0) {
			cursorScreenings.moveToFirst();
			mScreenings.clear();
			do{
				Zp00Screening mScreening = null;
				mScreening = Zp00ScreeningHelper.crearZp00Screening(cursorScreenings);
				mScreenings.add(mScreening);
			} while (cursorScreenings.moveToNext());
		}
		if (!cursorScreenings.isClosed()) cursorScreenings.close();
		return mScreenings;
	}

    /**
     * Metodos para Zp01StudyEntrySectionAtoD en la base de datos
     *
     */
    //Crear nuevo Zp01StudyEntrySectionAtoD en la base de datos
    public void crearZp01StudyEntrySectionAtoD(Zp01StudyEntrySectionAtoD studyEntrySectionAtoD) {
        ContentValues cv = Zp01StudyEntryHelper.crearZp01StudyEntrySectionAtoDValues(studyEntrySectionAtoD);
        mDb.insert(Zp01DBConstants.STUDYENTRY_AD_TABLE, null, cv);
    }
    //Editar Zp01StudyEntrySectionAtoD existente en la base de datos
    public boolean editarZp01StudyEntrySectionAtoD(Zp01StudyEntrySectionAtoD studyEntrySectionAtoD) {
        ContentValues cv = Zp01StudyEntryHelper.crearZp01StudyEntrySectionAtoDValues(studyEntrySectionAtoD);
        return mDb.update(Zp01DBConstants.STUDYENTRY_AD_TABLE, cv, Zp01DBConstants.recordId + "='"
                + studyEntrySectionAtoD.getRecordId()+"'", null) > 0;
    }
    //Limpiar la tabla de Zp01StudyEntrySectionAtoD de la base de datos
    public boolean borrarZp01StudyEntrySectionAtoD() {
        return mDb.delete(Zp01DBConstants.STUDYENTRY_AD_TABLE, null, null) > 0;
    }
    //Obtener un Zp01StudyEntrySectionAtoD de la base de datos
    public Zp01StudyEntrySectionAtoD getZp01StudyEntrySectionAtoD(String filtro, String orden) throws SQLException {
        Zp01StudyEntrySectionAtoD studyEntrySectionAtoD = null;
        Cursor cursorAtoD = crearCursor(Zp01DBConstants.STUDYENTRY_AD_TABLE, filtro, null, orden);
        if (cursorAtoD != null && cursorAtoD.getCount() > 0) {
            cursorAtoD.moveToFirst();
            studyEntrySectionAtoD=Zp01StudyEntryHelper.crearZp01StudyEntrySectionAtoD(cursorAtoD);
        }
        if (!cursorAtoD.isClosed()) cursorAtoD.close();
        return studyEntrySectionAtoD;
    }
    //Obtener una lista de Zp01StudyEntrySectionAtoD de la base de datos
    public List<Zp01StudyEntrySectionAtoD> getZp01StudyEntrySectionAtoDs(String filtro, String orden) throws SQLException {
        List<Zp01StudyEntrySectionAtoD> studyEntrySectionAtoDs = new ArrayList<Zp01StudyEntrySectionAtoD>();
        Cursor cursorAtoD = crearCursor(Zp01DBConstants.STUDYENTRY_AD_TABLE, filtro, null, orden);
        if (cursorAtoD != null && cursorAtoD.getCount() > 0) {
            cursorAtoD.moveToFirst();
            studyEntrySectionAtoDs.clear();
            do{
                Zp01StudyEntrySectionAtoD studyEntrySectionAtoD = null;
                studyEntrySectionAtoD = Zp01StudyEntryHelper.crearZp01StudyEntrySectionAtoD(cursorAtoD);
                studyEntrySectionAtoDs.add(studyEntrySectionAtoD);
            } while (cursorAtoD.moveToNext());
        }
        if (!cursorAtoD.isClosed()) cursorAtoD.close();
        return studyEntrySectionAtoDs;
    }

    /**
     * Metodos para Zp01StudyEntrySectionE en la base de datos
     *
     */
    //Crear nuevo Zp01StudyEntrySectionE en la base de datos
    public void crearZp01StudyEntrySectionE(Zp01StudyEntrySectionE studyEntrySectionE) {
        ContentValues cv = Zp01StudyEntryHelper.crearZp01StudyEntrySectionEValues(studyEntrySectionE);
        mDb.insert(Zp01DBConstants.STUDYENTRY_E_TABLE, null, cv);
    }
    //Editar Zp01StudyEntrySectionE existente en la base de datos
    public boolean editarZp01StudyEntrySectionE(Zp01StudyEntrySectionE studyEntrySectionE) {
        ContentValues cv = Zp01StudyEntryHelper.crearZp01StudyEntrySectionEValues(studyEntrySectionE);
        return mDb.update(Zp01DBConstants.STUDYENTRY_E_TABLE, cv, Zp01DBConstants.recordId + "='"
                + studyEntrySectionE.getRecordId() + "'", null) > 0;
    }
    //Limpiar la tabla de Zp01StudyEntrySectionE de la base de datos
    public boolean borrarZp01StudyEntrySectionE() {
        return mDb.delete(Zp01DBConstants.STUDYENTRY_E_TABLE, null, null) > 0;
    }
    //Obtener un Zp01StudyEntrySectionE de la base de datos
    public Zp01StudyEntrySectionE getZp01StudyEntrySectionE(String filtro, String orden) throws SQLException {
        Zp01StudyEntrySectionE studyEntrySectionE = null;
        Cursor cursorE = crearCursor(Zp01DBConstants.STUDYENTRY_E_TABLE, filtro, null, orden);
        if (cursorE != null && cursorE.getCount() > 0) {
            cursorE.moveToFirst();
            studyEntrySectionE=Zp01StudyEntryHelper.crearZp01StudyEntrySectionE(cursorE);
        }
        if (!cursorE.isClosed()) cursorE.close();
        return studyEntrySectionE;
    }
    //Obtener una lista de Zp01StudyEntrySectionE de la base de datos
    public List<Zp01StudyEntrySectionE> getZp01StudyEntrySectionEs(String filtro, String orden) throws SQLException {
        List<Zp01StudyEntrySectionE> studyEntrySectionEs = new ArrayList<Zp01StudyEntrySectionE>();
        Cursor cursorE = crearCursor(Zp01DBConstants.STUDYENTRY_E_TABLE, filtro, null, orden);
        if (cursorE != null && cursorE.getCount() > 0) {
            cursorE.moveToFirst();
            studyEntrySectionEs.clear();
            do{
                Zp01StudyEntrySectionE studyEntrySectionE = null;
                studyEntrySectionE = Zp01StudyEntryHelper.crearZp01StudyEntrySectionE(cursorE);
                studyEntrySectionEs.add(studyEntrySectionE);
            } while (cursorE.moveToNext());
        }
        if (!cursorE.isClosed()) cursorE.close();
        return studyEntrySectionEs;
    }

    /**
     * Metodos para Zp01StudyEntrySectionFtoK en la base de datos
     *
     */
    //Crear nuevo Zp01StudyEntrySectionFtoK en la base de datos
    public void crearZp01StudyEntrySectionFtoK(Zp01StudyEntrySectionFtoK studyEntrySectionFtoK) {
        ContentValues cv = Zp01StudyEntryHelper.crearZp01StudyEntrySectionFtoKValues(studyEntrySectionFtoK);
        mDb.insert(Zp01DBConstants.STUDYENTRY_FK_TABLE, null, cv);
    }
    //Editar Zp01StudyEntrySectionFtoK existente en la base de datos
    public boolean editarZp01StudyEntrySectionFtoK(Zp01StudyEntrySectionFtoK studyEntrySectionFtoK) {
        ContentValues cv = Zp01StudyEntryHelper.crearZp01StudyEntrySectionFtoKValues(studyEntrySectionFtoK);
        return mDb.update(Zp01DBConstants.STUDYENTRY_FK_TABLE, cv, Zp01DBConstants.recordId + "='"
                + studyEntrySectionFtoK.getRecordId() + "'", null) > 0;
    }
    //Limpiar la tabla de Zp01StudyEntrySectionFtoK de la base de datos
    public boolean borrarZp01StudyEntrySectionFtoK() {
        return mDb.delete(Zp01DBConstants.STUDYENTRY_FK_TABLE, null, null) > 0;
    }
    //Obtener un Zp01StudyEntrySectionFtoK de la base de datos
    public Zp01StudyEntrySectionFtoK getZp01StudyEntrySectionFtoK(String filtro, String orden) throws SQLException {
        Zp01StudyEntrySectionFtoK studyEntrySectionFtoK = null;
        Cursor cursorFtoK = crearCursor(Zp01DBConstants.STUDYENTRY_FK_TABLE, filtro, null, orden);
        if (cursorFtoK != null && cursorFtoK.getCount() > 0) {
            cursorFtoK.moveToFirst();
            studyEntrySectionFtoK=Zp01StudyEntryHelper.crearZp01StudyEntrySectionFtoK(cursorFtoK);
        }
        if (!cursorFtoK.isClosed()) cursorFtoK.close();
        return studyEntrySectionFtoK;
    }
    //Obtener una lista de Zp01StudyEntrySectionFtoK de la base de datos
    public List<Zp01StudyEntrySectionFtoK> getZp01StudyEntrySectionFtoKs(String filtro, String orden) throws SQLException {
        List<Zp01StudyEntrySectionFtoK> studyEntrySectionFtoKs = new ArrayList<Zp01StudyEntrySectionFtoK>();
        Cursor cursorFtoK = crearCursor(Zp01DBConstants.STUDYENTRY_FK_TABLE, filtro, null, orden);
        if (cursorFtoK != null && cursorFtoK.getCount() > 0) {
            cursorFtoK.moveToFirst();
            studyEntrySectionFtoKs.clear();
            do{
                Zp01StudyEntrySectionFtoK studyEntrySectionFtoK = null;
                studyEntrySectionFtoK = Zp01StudyEntryHelper.crearZp01StudyEntrySectionFtoK(cursorFtoK);
                studyEntrySectionFtoKs.add(studyEntrySectionFtoK);
            } while (cursorFtoK.moveToNext());
        }
        if (!cursorFtoK.isClosed()) cursorFtoK.close();
        return studyEntrySectionFtoKs;
    }

    /**
     * Metodos para Zp02BiospecimenCollection en la base de datos
     *
     */
    //Crear nuevo Zp02BiospecimenCollection en la base de datos
    public void crearZp02BiospecimenCollection(Zp02BiospecimenCollection biospecimenCollection) {
        ContentValues cv = Zp02BiospecimenCollectionHelper.crearZp02BiospecimenCollection(biospecimenCollection);
        mDb.insert(Zp02DBConstants.BIOCOLLECTION_TABLE, null, cv);
    }
    //Editar Zp02BiospecimenCollection existente en la base de datos
    public boolean editarZp02BiospecimenCollection(Zp02BiospecimenCollection biospecimenCollection) {
        ContentValues cv = Zp02BiospecimenCollectionHelper.crearZp02BiospecimenCollection(biospecimenCollection);
        return mDb.update(Zp02DBConstants.BIOCOLLECTION_TABLE, cv, Zp02DBConstants.recordId + "='"
                + biospecimenCollection.getRecordId() + "'", null) > 0;
    }
    //Limpiar la tabla de Zp02BiospecimenCollection de la base de datos
    public boolean borrarZp02BiospecimenCollection() {
        return mDb.delete(Zp02DBConstants.BIOCOLLECTION_TABLE, null, null) > 0;
    }
    //Obtener un Zp02BiospecimenCollection de la base de datos
    public Zp02BiospecimenCollection getZp02BiospecimenCollection(String filtro, String orden) throws SQLException {
        Zp02BiospecimenCollection biospecimenCollection = null;
        Cursor cursorBC = crearCursor(Zp02DBConstants.BIOCOLLECTION_TABLE, filtro, null, orden);
        if (cursorBC != null && cursorBC.getCount() > 0) {
            cursorBC.moveToFirst();
            biospecimenCollection=Zp02BiospecimenCollectionHelper.crearZp02BiospecimenCollection(cursorBC);
        }
        if (!cursorBC.isClosed()) cursorBC.close();
        return biospecimenCollection;
    }
    //Obtener una lista de Zp02BiospecimenCollection de la base de datos
    public List<Zp02BiospecimenCollection> getZp02BiospecimenCollections(String filtro, String orden) throws SQLException {
        List<Zp02BiospecimenCollection> biospecimenCollections = new ArrayList<Zp02BiospecimenCollection>();
        Cursor cursorBC = crearCursor(Zp02DBConstants.BIOCOLLECTION_TABLE, filtro, null, orden);
        if (cursorBC != null && cursorBC.getCount() > 0) {
            cursorBC.moveToFirst();
            biospecimenCollections.clear();
            do{
                Zp02BiospecimenCollection biospecimenCollection = null;
                biospecimenCollection = Zp02BiospecimenCollectionHelper.crearZp02BiospecimenCollection(cursorBC);
                biospecimenCollections.add(biospecimenCollection);
            } while (cursorBC.moveToNext());
        }
        if (!cursorBC.isClosed()) cursorBC.close();
        return biospecimenCollections;
    }

    /**
     * Metodos para Zp03MonthlyVisit en la base de datos
     *
     */
    //Crear nuevo Zp03MonthlyVisit en la base de datos
    public void crearZp03MonthlyVisit(Zp03MonthlyVisit monthlyVisit) {
        ContentValues cv = Zp03MonthlyVisitHelper.crearZp03MonthlyVisit(monthlyVisit);
        mDb.insert(Zp03DBConstants.MONTHLYVISIT_TABLE, null, cv);
    }
    //Editar Zp03MonthlyVisit existente en la base de datos
    public boolean editarZp03MonthlyVisit(Zp03MonthlyVisit monthlyVisit) {
        ContentValues cv = Zp03MonthlyVisitHelper.crearZp03MonthlyVisit(monthlyVisit);
        return mDb.update(Zp03DBConstants.MONTHLYVISIT_TABLE, cv, Zp03DBConstants.recordId + "='"
                + monthlyVisit.getRecordId() + "'", null) > 0;
    }
    //Limpiar la tabla de Zp03MonthlyVisit de la base de datos
    public boolean borrarZp03MonthlyVisit() {
        return mDb.delete(Zp03DBConstants.MONTHLYVISIT_TABLE, null, null) > 0;
    }
    //Obtener un Zp03MonthlyVisit de la base de datos
    public Zp03MonthlyVisit getZp03MonthlyVisit(String filtro, String orden) throws SQLException {
        Zp03MonthlyVisit monthlyVisit = null;
        Cursor cursorMV = crearCursor(Zp03DBConstants.MONTHLYVISIT_TABLE, filtro, null, orden);
        if (cursorMV != null && cursorMV.getCount() > 0) {
            cursorMV.moveToFirst();
            monthlyVisit=Zp03MonthlyVisitHelper.crearZp03MonthlyVisit(cursorMV);
        }
        if (!cursorMV.isClosed()) cursorMV.close();
        return monthlyVisit;
    }
    //Obtener una lista de Zp03MonthlyVisit de la base de datos
    public List<Zp03MonthlyVisit> getZp03MonthlyVisits(String filtro, String orden) throws SQLException {
        List<Zp03MonthlyVisit> monthlyVisits = new ArrayList<Zp03MonthlyVisit>();
        Cursor cursorMV = crearCursor(Zp03DBConstants.MONTHLYVISIT_TABLE, filtro, null, orden);
        if (cursorMV != null && cursorMV.getCount() > 0) {
            cursorMV.moveToFirst();
            monthlyVisits.clear();
            do{
                Zp03MonthlyVisit monthlyVisit = null;
                monthlyVisit = Zp03MonthlyVisitHelper.crearZp03MonthlyVisit(cursorMV);
                monthlyVisits.add(monthlyVisit);
            } while (cursorMV.moveToNext());
        }
        if (!cursorMV.isClosed()) cursorMV.close();
        return monthlyVisits;
    }

    /**
     * Metodos para Zp04TrimesterVisitSectionAtoD en la base de datos
     *
     */
    //Crear nuevo Zp04TrimesterVisitSectionAtoD en la base de datos
    public void crearZp04TrimesterVisitSectionAtoD(Zp04TrimesterVisitSectionAtoD trimesterVisitSectionAtoD) {
        ContentValues cv = Zp04TrimesterVisitHelper.crearZp04TrimesterVisitSectionAtoD(trimesterVisitSectionAtoD);
        mDb.insert(Zp04DBConstants.TRIMESTERVISIT_AD_TABLE, null, cv);
    }
    //Editar Zp04TrimesterVisitSectionAtoD existente en la base de datos
    public boolean editarZp04TrimesterVisitSectionAtoD(Zp04TrimesterVisitSectionAtoD trimesterVisitSectionAtoD) {
        ContentValues cv = Zp04TrimesterVisitHelper.crearZp04TrimesterVisitSectionAtoD(trimesterVisitSectionAtoD);
        return mDb.update(Zp04DBConstants.TRIMESTERVISIT_AD_TABLE, cv, Zp04DBConstants.recordId + "='"
                + trimesterVisitSectionAtoD.getRecordId() + "'", null) > 0;
    }
    //Limpiar la tabla de Zp04TrimesterVisitSectionAtoD de la base de datos
    public boolean borrarZp04TrimesterVisitSectionAtoD() {
        return mDb.delete(Zp04DBConstants.TRIMESTERVISIT_AD_TABLE, null, null) > 0;
    }
    //Obtener un Zp04TrimesterVisitSectionAtoD de la base de datos
    public Zp04TrimesterVisitSectionAtoD getZp04TrimesterVisitSectionAtoD(String filtro, String orden) throws SQLException {
        Zp04TrimesterVisitSectionAtoD trimesterVisitSectionAtoD = null;
        Cursor cursorAD = crearCursor(Zp04DBConstants.TRIMESTERVISIT_AD_TABLE, filtro, null, orden);
        if (cursorAD != null && cursorAD.getCount() > 0) {
            cursorAD.moveToFirst();
            trimesterVisitSectionAtoD=Zp04TrimesterVisitHelper.crearZp04TrimesterVisitSectionAtoD(cursorAD);
        }
        if (!cursorAD.isClosed()) cursorAD.close();
        return trimesterVisitSectionAtoD;
    }
    //Obtener una lista de Zp04TrimesterVisitSectionAtoD de la base de datos
    public List<Zp04TrimesterVisitSectionAtoD> getZp04TrimesterVisitSectionAtoDs(String filtro, String orden) throws SQLException {
        List<Zp04TrimesterVisitSectionAtoD> trimesterVisitSectionAtoDs = new ArrayList<Zp04TrimesterVisitSectionAtoD>();
        Cursor cursorAD = crearCursor(Zp04DBConstants.TRIMESTERVISIT_AD_TABLE, filtro, null, orden);
        if (cursorAD != null && cursorAD.getCount() > 0) {
            cursorAD.moveToFirst();
            trimesterVisitSectionAtoDs.clear();
            do{
                Zp04TrimesterVisitSectionAtoD trimesterVisitSectionAtoD = null;
                trimesterVisitSectionAtoD = Zp04TrimesterVisitHelper.crearZp04TrimesterVisitSectionAtoD(cursorAD);
                trimesterVisitSectionAtoDs.add(trimesterVisitSectionAtoD);
            } while (cursorAD.moveToNext());
        }
        if (!cursorAD.isClosed()) cursorAD.close();
        return trimesterVisitSectionAtoDs;
    }

    /**
     * Metodos para Zp04TrimesterVisitSectionE en la base de datos
     *
     */
    //Crear nuevo Zp04TrimesterVisitSectionE en la base de datos
    public void crearZp04TrimesterVisitSectionE(Zp04TrimesterVisitSectionE trimesterVisitSectionE) {
        ContentValues cv = Zp04TrimesterVisitHelper.crearZp04TrimesterVisitSectionE(trimesterVisitSectionE);
        mDb.insert(Zp04DBConstants.TRIMESTERVISIT_E_TABLE, null, cv);
    }
    //Editar Zp04TrimesterVisitSectionE existente en la base de datos
    public boolean editarZp04TrimesterVisitSectionE(Zp04TrimesterVisitSectionE trimesterVisitSectionE) {
        ContentValues cv = Zp04TrimesterVisitHelper.crearZp04TrimesterVisitSectionE(trimesterVisitSectionE);
        return mDb.update(Zp04DBConstants.TRIMESTERVISIT_E_TABLE, cv, Zp04DBConstants.recordId + "='"
                + trimesterVisitSectionE.getRecordId() + "'", null) > 0;
    }
    //Limpiar la tabla de Zp04TrimesterVisitSectionE de la base de datos
    public boolean borrarZp04TrimesterVisitSectionE() {
        return mDb.delete(Zp04DBConstants.TRIMESTERVISIT_E_TABLE, null, null) > 0;
    }
    //Obtener un Zp04TrimesterVisitSectionE de la base de datos
    public Zp04TrimesterVisitSectionE getZp04TrimesterVisitSectionE(String filtro, String orden) throws SQLException {
        Zp04TrimesterVisitSectionE trimesterVisitSectionE = null;
        Cursor cursorE = crearCursor(Zp04DBConstants.TRIMESTERVISIT_E_TABLE, filtro, null, orden);
        if (cursorE != null && cursorE.getCount() > 0) {
            cursorE.moveToFirst();
            trimesterVisitSectionE=Zp04TrimesterVisitHelper.crearZp04TrimesterVisitSectionE(cursorE);
        }
        if (!cursorE.isClosed()) cursorE.close();
        return trimesterVisitSectionE;
    }
    //Obtener una lista de Zp04TrimesterVisitSectionE de la base de datos
    public List<Zp04TrimesterVisitSectionE> getZp04TrimesterVisitSectionEs(String filtro, String orden) throws SQLException {
        List<Zp04TrimesterVisitSectionE> trimesterVisitSectionEs = new ArrayList<Zp04TrimesterVisitSectionE>();
        Cursor cursorE = crearCursor(Zp04DBConstants.TRIMESTERVISIT_E_TABLE, filtro, null, orden);
        if (cursorE != null && cursorE.getCount() > 0) {
            cursorE.moveToFirst();
            trimesterVisitSectionEs.clear();
            do{
                Zp04TrimesterVisitSectionE trimesterVisitSectionE = null;
                trimesterVisitSectionE = Zp04TrimesterVisitHelper.crearZp04TrimesterVisitSectionE(cursorE);
                trimesterVisitSectionEs.add(trimesterVisitSectionE);
            } while (cursorE.moveToNext());
        }
        if (!cursorE.isClosed()) cursorE.close();
        return trimesterVisitSectionEs;
    }

    /**
     * Metodos para Zp04TrimesterVisitSectionFtoH en la base de datos
     *
     */
    //Crear nuevo Zp04TrimesterVisitSectionFtoH en la base de datos
    public void crearZp04TrimesterVisitSectionFtoH(Zp04TrimesterVisitSectionFtoH trimesterVisitSectionFtoH) {
        ContentValues cv = Zp04TrimesterVisitHelper.crearZp04TrimesterVisitSectionFtoH(trimesterVisitSectionFtoH);
        mDb.insert(Zp04DBConstants.TRIMESTERVISIT_FH_TABLE, null, cv);
    }
    //Editar Zp04TrimesterVisitSectionFtoH existente en la base de datos
    public boolean editarZp04TrimesterVisitSectionFtoH(Zp04TrimesterVisitSectionFtoH trimesterVisitSectionFtoH) {
        ContentValues cv = Zp04TrimesterVisitHelper.crearZp04TrimesterVisitSectionFtoH(trimesterVisitSectionFtoH);
        return mDb.update(Zp04DBConstants.TRIMESTERVISIT_FH_TABLE, cv, Zp04DBConstants.recordId + "='"
                + trimesterVisitSectionFtoH.getRecordId() + "'", null) > 0;
    }
    //Limpiar la tabla de Zp04TrimesterVisitSectionFtoH de la base de datos
    public boolean borrarZp04TrimesterVisitSectionFtoH() {
        return mDb.delete(Zp04DBConstants.TRIMESTERVISIT_FH_TABLE, null, null) > 0;
    }
    //Obtener un Zp04TrimesterVisitSectionFtoH de la base de datos
    public Zp04TrimesterVisitSectionFtoH getZp04TrimesterVisitSectionFtoH(String filtro, String orden) throws SQLException {
        Zp04TrimesterVisitSectionFtoH trimesterVisitSectionFtoH = null;
        Cursor cursorFH = crearCursor(Zp04DBConstants.TRIMESTERVISIT_FH_TABLE, filtro, null, orden);
        if (cursorFH != null && cursorFH.getCount() > 0) {
            cursorFH.moveToFirst();
            trimesterVisitSectionFtoH=Zp04TrimesterVisitHelper.crearZp04TrimesterVisitSectionFtoH(cursorFH);
        }
        if (!cursorFH.isClosed()) cursorFH.close();
        return trimesterVisitSectionFtoH;
    }
    //Obtener una lista de Zp04TrimesterVisitSectionFtoH de la base de datos
    public List<Zp04TrimesterVisitSectionFtoH> getZp04TrimesterVisitSectionFtoHs(String filtro, String orden) throws SQLException {
        List<Zp04TrimesterVisitSectionFtoH> trimesterVisitSectionFtoHs = new ArrayList<Zp04TrimesterVisitSectionFtoH>();
        Cursor cursorFH = crearCursor(Zp04DBConstants.TRIMESTERVISIT_FH_TABLE, filtro, null, orden);
        if (cursorFH != null && cursorFH.getCount() > 0) {
            cursorFH.moveToFirst();
            trimesterVisitSectionFtoHs.clear();
            do{
                Zp04TrimesterVisitSectionFtoH trimesterVisitSectionFtoH = null;
                trimesterVisitSectionFtoH = Zp04TrimesterVisitHelper.crearZp04TrimesterVisitSectionFtoH(cursorFH);
                trimesterVisitSectionFtoHs.add(trimesterVisitSectionFtoH);
            } while (cursorFH.moveToNext());
        }
        if (!cursorFH.isClosed()) cursorFH.close();
        return trimesterVisitSectionFtoHs;
    }

    /**
     * Metodos para Zp05UltrasoundExam en la base de datos
     *
     */
    //Crear nuevo Zp05UltrasoundExam en la base de datos
    public void crearZp05UltrasoundExam(Zp05UltrasoundExam ultrasoundExam) {
        ContentValues cv = Zp05UltrasoundExamHelper.crearZp05UltrasoundExam(ultrasoundExam);
        mDb.insert(Zp05DBConstants.ULTRASOUNDEXAM_TABLE, null, cv);
    }
    //Editar Zp05UltrasoundExam existente en la base de datos
    public boolean editarZp05UltrasoundExam(Zp05UltrasoundExam ultrasoundExam) {
        ContentValues cv = Zp05UltrasoundExamHelper.crearZp05UltrasoundExam(ultrasoundExam);
        return mDb.update(Zp05DBConstants.ULTRASOUNDEXAM_TABLE, cv, Zp05DBConstants.recordId + "='"
                + ultrasoundExam.getRecordId() + "'", null) > 0;
    }
    //Limpiar la tabla de Zp05UltrasoundExam de la base de datos
    public boolean borrarZp05UltrasoundExam() {
        return mDb.delete(Zp05DBConstants.ULTRASOUNDEXAM_TABLE, null, null) > 0;
    }
    //Obtener un Zp05UltrasoundExam de la base de datos
    public Zp05UltrasoundExam getZp05UltrasoundExam(String filtro, String orden) throws SQLException {
        Zp05UltrasoundExam ultrasoundExam = null;
        Cursor cursor = crearCursor(Zp05DBConstants.ULTRASOUNDEXAM_TABLE, filtro, null, orden);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            ultrasoundExam=Zp05UltrasoundExamHelper.crearZp05UltrasoundExam(cursor);
        }
        if (!cursor.isClosed()) cursor.close();
        return ultrasoundExam;
    }
    //Obtener una lista de Zp05UltrasoundExam de la base de datos
    public List<Zp05UltrasoundExam> getZp05UltrasoundExams(String filtro, String orden) throws SQLException {
        List<Zp05UltrasoundExam> ultrasoundExams = new ArrayList<Zp05UltrasoundExam>();
        Cursor cursor = crearCursor(Zp05DBConstants.ULTRASOUNDEXAM_TABLE, filtro, null, orden);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            ultrasoundExams.clear();
            do{
                Zp05UltrasoundExam ultrasoundExam = null;
                ultrasoundExam = Zp05UltrasoundExamHelper.crearZp05UltrasoundExam(cursor);
                ultrasoundExams.add(ultrasoundExam);
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) cursor.close();
        return ultrasoundExams;
    }

    /**
     * Metodos para Zp06DeliveryAnd6weekVisit en la base de datos
     *
     */
    //Crear nuevo Zp06DeliveryAnd6weekVisit en la base de datos
    public void crearZp06DeliveryAnd6weekVisit(Zp06DeliveryAnd6weekVisit deliveryAnd6weekVisit) {
        ContentValues cv = Zp06DeliveryAnd6weekVisitHelper.crearZp06DeliveryAnd6weekVisit(deliveryAnd6weekVisit);
        mDb.insert(Zp06DBConstants.DELIVERY6WVISIT_TABLE, null, cv);
    }
    //Editar Zp06DeliveryAnd6weekVisit existente en la base de datos
    public boolean editarZp06DeliveryAnd6weekVisit(Zp06DeliveryAnd6weekVisit deliveryAnd6weekVisit) {
        ContentValues cv = Zp06DeliveryAnd6weekVisitHelper.crearZp06DeliveryAnd6weekVisit(deliveryAnd6weekVisit);
        return mDb.update(Zp06DBConstants.DELIVERY6WVISIT_TABLE, cv, Zp06DBConstants.recordId + "='"
                + deliveryAnd6weekVisit.getRecordId() + "'", null) > 0;
    }
    //Limpiar la tabla de Zp06DeliveryAnd6weekVisit de la base de datos
    public boolean borrarZp06DeliveryAnd6weekVisit() {
        return mDb.delete(Zp06DBConstants.DELIVERY6WVISIT_TABLE, null, null) > 0;
    }
    //Obtener un Zp06DeliveryAnd6weekVisit de la base de datos
    public Zp06DeliveryAnd6weekVisit getZp06DeliveryAnd6weekVisit(String filtro, String orden) throws SQLException {
        Zp06DeliveryAnd6weekVisit deliveryAnd6weekVisit = null;
        Cursor cursor = crearCursor(Zp06DBConstants.DELIVERY6WVISIT_TABLE, filtro, null, orden);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            deliveryAnd6weekVisit=Zp06DeliveryAnd6weekVisitHelper.crearZp06DeliveryAnd6weekVisit(cursor);
        }
        if (!cursor.isClosed()) cursor.close();
        return deliveryAnd6weekVisit;
    }
    //Obtener una lista de Zp06DeliveryAnd6weekVisit de la base de datos
    public List<Zp06DeliveryAnd6weekVisit> getZp06DeliveryAnd6weekVisits(String filtro, String orden) throws SQLException {
        List<Zp06DeliveryAnd6weekVisit> deliveryAnd6weekVisits = new ArrayList<Zp06DeliveryAnd6weekVisit>();
        Cursor cursor = crearCursor(Zp06DBConstants.DELIVERY6WVISIT_TABLE, filtro, null, orden);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            deliveryAnd6weekVisits.clear();
            do{
                Zp06DeliveryAnd6weekVisit deliveryAnd6weekVisit = null;
                deliveryAnd6weekVisit = Zp06DeliveryAnd6weekVisitHelper.crearZp06DeliveryAnd6weekVisit(cursor);
                deliveryAnd6weekVisits.add(deliveryAnd6weekVisit);
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) cursor.close();
        return deliveryAnd6weekVisits;
    }

    /**
     * Metodos para Zp08StudyExit en la base de datos
     *
     */
    //Crear nuevo Zp08StudyExit en la base de datos
    public void crearZp08StudyExit(Zp08StudyExit studyExit) {
        ContentValues cv = Zp08StudyExitHelper.crearZp08StudyExit(studyExit);
        mDb.insert(Zp08DBConstants.STUDYEXIT_TABLE, null, cv);
    }
    //Editar Zp08StudyExit existente en la base de datos
    public boolean editarZp08StudyExit(Zp08StudyExit studyExit) {
        ContentValues cv = Zp08StudyExitHelper.crearZp08StudyExit(studyExit);
        return mDb.update(Zp08DBConstants.STUDYEXIT_TABLE, cv, Zp08DBConstants.recordId + "='"
                + studyExit.getRecordId() + "'", null) > 0;
    }
    //Limpiar la tabla de Zp08StudyExit de la base de datos
    public boolean borrarZp08StudyExit() {
        return mDb.delete(Zp08DBConstants.STUDYEXIT_TABLE, null, null) > 0;
    }
    //Obtener un Zp08StudyExit de la base de datos
    public Zp08StudyExit getZp08StudyExit(String filtro, String orden) throws SQLException {
        Zp08StudyExit studyExit = null;
        Cursor cursor = crearCursor(Zp08DBConstants.STUDYEXIT_TABLE, filtro, null, orden);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            studyExit=Zp08StudyExitHelper.crearZp08StudyExit(cursor);
        }
        if (!cursor.isClosed()) cursor.close();
        return studyExit;
    }
    //Obtener una lista de Zp08StudyExit de la base de datos
    public List<Zp08StudyExit> getZp08StudyExits(String filtro, String orden) throws SQLException {
        List<Zp08StudyExit> zp08StudyExits = new ArrayList<Zp08StudyExit>();
        Cursor cursor = crearCursor(Zp08DBConstants.STUDYEXIT_TABLE, filtro, null, orden);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            zp08StudyExits.clear();
            do{
                Zp08StudyExit studyExit = null;
                studyExit = Zp08StudyExitHelper.crearZp08StudyExit(cursor);
                zp08StudyExits.add(studyExit);
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) cursor.close();
        return zp08StudyExits;
    }
}
