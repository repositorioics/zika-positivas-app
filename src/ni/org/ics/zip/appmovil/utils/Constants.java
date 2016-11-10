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
	
	//nombres de objetos
	public static final String TITLE = "titulo";
	public static final String OBJECTO_ZP00 = "zp00";
	public static final String OBJECTO_ZP01A = "zp01a";
    public static final String OBJECTO_ZP01E = "zp01e";
    public static final String OBJECTO_ZP01F = "zp01f";
    public static final String OBJECTO_ZP02 = "zp02";
    public static final String OBJECTO_ZP03 = "zp03";
    public static final String OBJECTO_ZP04A = "zp04a";
    public static final String OBJECTO_ZP04E = "zp04e";
    public static final String OBJECTO_ZP04F = "zp04f";
    public static final String OBJECTO_ZP05 = "zp05";
    public static final String OBJECTO_ZP06 = "zp06";
    public static final String OBJECTO_ZP08 = "zp08";

	public static final String OBJECTO_ZPEST = "zpestado";
	public static final String OBJECTO_ZPDATA = "zpdatos";
	public static final String DONE = "hecho";
	
	//Eventos
	public static final String EVENT = "event";
	public static final String SCREENING = "screening_arm_1";
	public static final String ENTRY = "entry_arm_1";
	public static final String WEEK4 = "4_week_post_entry_arm_1";
	public static final String EXIT = "study_exit_arm_1";

    public static final String RECORDID = "recordId";
    
    //Form wizard
    public static final String FORM_NAME = "form";
	public static final String FORM_DATOS_EMB = "datos_embarazada";
	public static final String ROJO = "#db0000";
	public static final String WIZARD = "#ff0099cc";


}
