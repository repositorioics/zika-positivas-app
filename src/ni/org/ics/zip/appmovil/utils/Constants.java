package ni.org.ics.zip.appmovil.utils;

import android.net.Uri;


/**
 * Constantes usadas en multiples clases de la aplicacion
 * 
 * @author William Aviles
 * 
 */
public class Constants {
	
	
	// status for records
    public static final String STATUS_SUBMITTED = "enviado";
    public static final String STATUS_NOT_SUBMITTED = "no enviado";
    public static final String STATUS_NOT_COMPLETED = "incompleto";
    
    
    //Providers
	public static final String AUTHORITY = "org.odk.collect.android.provider.odk.forms";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/forms");
	public static final String AUTHORITY_I = "org.odk.collect.android.provider.odk.instances";
	public static final Uri CONTENT_URI_I = Uri.parse("content://" + AUTHORITY_I + "/instances");
	
	//nombres de extras
	public static final String TITLE = "titulo";
	public static final String OBJECTO = "objeto";
	public static final String DONE = "hecho";
	
	//Eventos
	public static final String MAT_1 = "ingreso";
	
	


}
