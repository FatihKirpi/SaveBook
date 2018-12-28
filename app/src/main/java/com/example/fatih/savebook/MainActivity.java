package com.example.fatih.savebook;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static  ArrayList<Bitmap> artimage;
  //üst menüyü oncreateoptionsMenu metodu ile tanımladık
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.add_save,menu);

        return super.onCreateOptionsMenu(menu);
    }
     // menuden seçilen item on optionsItemSelected metodu ile tanımladık
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.add_save)
        {
            Intent intent =new Intent(getApplicationContext(),Main2Activity.class);
            //listViewda şeçilen görselde save buttonu çıkmamasını sağlıyor
            intent.putExtra("info","new");
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.listView);
        final ArrayList<String> artname = new ArrayList<String>();
        artimage =new ArrayList<Bitmap>();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,artname);
        listView.setAdapter(arrayAdapter);
        try
        {
            Main2Activity.database=this.openOrCreateDatabase("Arts",MODE_PRIVATE,null);
            Main2Activity.database.execSQL("CREATE TABLE IF NOT EXISTS arts (name VARCHAR , image BLOB)");
            Cursor cursor=Main2Activity.database.rawQuery("SELECT * FROM arts",null);

            int nameIx=cursor.getColumnIndex("name");
            int imageIx=cursor.getColumnIndex("image");
            cursor.moveToFirst();
            //cursor boş değilse için while içinde konrol edip array adapter dataset changed yapıyoruz
            while(cursor != null)
            {
                artname.add(cursor.getString(nameIx));
                byte[] byteArray=cursor.getBlob(imageIx);
                Bitmap image = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
                artimage.add(image);

                cursor.moveToNext();
                arrayAdapter.notifyDataSetChanged();

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =new Intent(getApplicationContext(),Main2Activity.class);
                intent.putExtra("info","old");
                intent.putExtra("name",artname.get(position));
                intent.putExtra("position",position);
                startActivity(intent);

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                toastMessage("deneme ");


                return false;
            }
        });

    }
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }


}
