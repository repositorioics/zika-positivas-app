package ni.org.ics.zip.appmovil.adapters.eventosembarazo;

import ni.org.ics.zip.appmovil.R;
import ni.org.ics.zip.appmovil.domain.Zp01StudyEntrySectionAtoD;
import ni.org.ics.zip.appmovil.domain.Zp02BiospecimenCollection;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class IngresoAdapter extends ArrayAdapter<String> {

	private final Context context;
	private final String[] values;
	private final Zp01StudyEntrySectionAtoD mZp01a;
	private final Zp02BiospecimenCollection mZp02;
	
	public IngresoAdapter(Context context, int textViewResourceId,
			String[] values, Zp01StudyEntrySectionAtoD zp01a, Zp02BiospecimenCollection zp02) {
		super(context, textViewResourceId, values);
		this.context = context;
		this.values = values;
		this.mZp01a =zp01a;
		this.mZp02 = zp02;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.menu_item_2, null);
		}
		TextView textView = (TextView) v.findViewById(R.id.label);
		textView.setTypeface(null, Typeface.BOLD);
		textView.setTextColor(Color.BLACK);
		textView.setText(values[position]);
		
		// Change icon based on position
		Drawable img = null;
		switch (position){
		case 0: 
			if(mZp01a!=null){
				textView.setText(textView.getText()+"\n"+ context.getResources().getString(R.string.done));
			}
			else{
				textView.setText(textView.getText()+"\n"+ context.getResources().getString(R.string.pending));
			}
			img=getContext().getResources().getDrawable( R.drawable.ic_demog);
			textView.setCompoundDrawablesWithIntrinsicBounds(null, img, null, null);
			break;
		case 1: 
			img=getContext().getResources().getDrawable( R.drawable.ic_health);
			textView.setCompoundDrawablesWithIntrinsicBounds(null, img, null, null);
			break;
		case 2: 
			img=getContext().getResources().getDrawable( R.drawable.ic_pregnancy);
			textView.setCompoundDrawablesWithIntrinsicBounds(null, img, null, null);
			break;
		case 3: 
			if(mZp02!=null){
				textView.setText(textView.getText()+"\n"+ context.getResources().getString(R.string.done));
			}
			else{
				textView.setText(textView.getText()+"\n"+ context.getResources().getString(R.string.pending));
			}
			img=getContext().getResources().getDrawable( R.drawable.ic_sample);
			textView.setCompoundDrawablesWithIntrinsicBounds(null, img, null, null);
			break;
		case 4: 
			img=getContext().getResources().getDrawable( R.drawable.ic_briefcase);
			textView.setCompoundDrawablesWithIntrinsicBounds(null, img, null, null);
			break;
		case 5: 
			img=getContext().getResources().getDrawable( R.drawable.ic_spray);
			textView.setCompoundDrawablesWithIntrinsicBounds(null, img, null, null);
			break;
		case 6: 
			img=getContext().getResources().getDrawable( R.drawable.ic_pest);
			textView.setCompoundDrawablesWithIntrinsicBounds(null, img, null, null);
			break;
		case 7: 
			img=getContext().getResources().getDrawable( R.drawable.ic_us);
			textView.setCompoundDrawablesWithIntrinsicBounds(null, img, null, null);
			break;
		default:
			img=getContext().getResources().getDrawable( R.drawable.ic_launcher);
			textView.setCompoundDrawablesWithIntrinsicBounds(null, img, null, null);
			break;
		}

		return v;
	}
}
