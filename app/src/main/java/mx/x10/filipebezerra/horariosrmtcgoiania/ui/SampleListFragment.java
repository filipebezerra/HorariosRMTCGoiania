package mx.x10.filipebezerra.horariosrmtcgoiania.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;

public class SampleListFragment extends SherlockListFragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		SampleAdapter adapter = new SampleAdapter(getActivity());

        String[] servicosRmtc = getResources().getStringArray(R.array.servicos_rmtc);
        String[] descricaoServicos = getResources().getStringArray(R.array.servicos_rmtc_descricao);
        TypedArray imagensServicos = getResources().obtainTypedArray(R.array.servicos_rmtc_imagens);

        imagensServicos.recycle();

        for (int i = 0; i < servicosRmtc.length; i++) {
			adapter.add(new SampleItem(servicosRmtc[i], descricaoServicos[i],
                    imagensServicos.getResourceId(i, -1)));
		}

		setListAdapter(adapter);
	}

	private class SampleItem {
		public String title;
        public String description;
		public int iconRes;

		public SampleItem(String tag, String description, int iconRes) {
			this.title = tag;
            this.description = description;
			this.iconRes = iconRes;
		}
	}

	public class SampleAdapter extends ArrayAdapter<SampleItem> {

		public SampleAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, null);
			}

            ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
			icon.setImageResource(getItem(position).iconRes);

            TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(getItem(position).title);

            TextView description = (TextView) convertView.findViewById(R.id.row_description);
            description.setText(getItem(position).description);

			return convertView;
		}

	}
}
