package com.example.mesutgungor.androidlistview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by mesutgungor on 5/24/17.
 */
public class HaberAdapter extends ArrayAdapter<Haber> {

    private LayoutInflater li;
    private ArrayList<Haber> haberler;
    Context myContext;

    public HaberAdapter(Context context, int resource, ArrayList<Haber> objects) {

        super(context, resource, objects);

        li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        haberler = objects;
        myContext = context;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if(convertView == null){
            convertView = li.inflate(R.layout.list_view_row_model, parent, false);
        }

        //Her satırdaki view referanslarını ilgili değişkenlere atıyoruz.

        TextView haberbasligi = (TextView) convertView.findViewById(R.id.haberbasligi);
        TextView habertarihi = (TextView) convertView.findViewById(R.id.habertarihi);
        TextView haberkategorisi = (TextView) convertView.findViewById(R.id.haberkategorisi);
        TextView habericerigi = (TextView) convertView.findViewById(R.id.habericerigi);
        ImageView haberresmi = (ImageView) convertView.findViewById(R.id.haberresmi);

        //Haberresmini indirecek olan async task ı çağırıyoruz.
        haberler.get(position).getHaberresmi();
        new HaberResmiIndir(haberresmi).execute(haberler.get(position).getHaberresmi());

        // Haberle ilgili textviewlere ilgili textleri atıyoruz.

        haberbasligi.setText(haberler.get(position).getHaberbasligi());
        habertarihi.setText(haberler.get(position).getHabertarihi());
        haberkategorisi.setText(haberler.get(position).getHaberinkategorisi().trim().toUpperCase());
        habericerigi.setText(haberler.get(position).getHaberinicerigi());

        return convertView;
    }

    //Haber Resimlerini Asynch Task ile getiriyoruz ve ilgili haberin imageview ine atıyoruz.
    //Eğer Haberresmi yoksa varsayılan olarak son_dakika.png atanıyor.

    private class HaberResmiIndir extends AsyncTask<String, Void, Bitmap> {

        ImageView hbrResmi;

        public HaberResmiIndir(ImageView hbrResmi) {
            this.hbrResmi = hbrResmi;
        }

        protected Bitmap doInBackground(String... urls) {

            String haberResmiUrl = urls[0];
            Bitmap haberresmi = null;
            try {
                InputStream in = new java.net.URL(haberResmiUrl).openStream();
                haberresmi = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return haberresmi;
        }

        protected void onPostExecute(Bitmap result) {

            if (result != null){
            hbrResmi.setImageBitmap(result);
            }else {
                hbrResmi.setImageResource(R.drawable.son_dakika);
            }
        }
    }
}
