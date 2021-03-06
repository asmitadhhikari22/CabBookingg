package app.rent_likeme.com.rent_likeme;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

import app.rent_likeme.com.rent_likeme.model.Address;
import app.rent_likeme.com.rent_likeme.model.Car;
import app.rent_likeme.com.rent_likeme.model.Provider;
import app.rent_likeme.com.rent_likeme.util.Utility;

/**
 * created by anto004
 * Image credits: http://cars.disney.com
 */
public class RentalDetailActivity extends AppCompatActivity {
    public static final String LOG_TAG = RentalDetailActivity.class.getSimpleName();
    public static final String PROVIDER_KEY = "provider";
    public static final String ADDRESS_KEY = "address";
    public static final String CAR_KEY = "car";
    private CollapsingToolbarLayout mCollapsingToolbar;
    private LinearLayout mParentLayout;
    private List<Car> mCars;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        Provider mProvider = getIntent().getParcelableExtra(PROVIDER_KEY);
        final Address mAddress = getIntent().getParcelableExtra(ADDRESS_KEY);
        mCars = getIntent().getParcelableArrayListExtra(CAR_KEY);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dest = mAddress.line1 + ", " + mAddress.city + ", "  + mAddress.region + ", " + mAddress.country;
                Uri intentUri = Uri.parse("google.navigation:q=" + dest);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, intentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if(mapIntent.resolveActivity(getPackageManager()) != null){
                    startActivity(mapIntent);
                }
                else {
                    Toast.makeText(RentalDetailActivity.this, "Device Has No Navigation App Installed",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mCollapsingToolbar.setTitle(mProvider.companyName);
        displayImageOnToolbar();

        TextView companyNameTv = (TextView) findViewById(R.id.company_name_detail_textView);
        companyNameTv.setText(mProvider.companyName);

        TextView addressTv = (TextView) findViewById(R.id.detail_address_textView);
        String address = mAddress.line1 + ", " + mAddress.city + ", "  + mAddress.region;
        addressTv.setText(Utility.formatAddress(address));

        mParentLayout = (LinearLayout) findViewById(R.id.car_list_parent);

        if(mCars != null) {
            createMyCarAdapter();
        }
    }

    //Dynamically adding new Child View to parent ScrollView
    public void createMyCarAdapter(){
        for(int i = 0; i < mCars.size(); i++){
            CarHolder carHolder = createCarItemView();
            addCarListItem(carHolder, i);
        }
    }

    public CarHolder createCarItemView(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.car_list_item, null);
        return new CarHolder(view);
    }

    public void addCarListItem(CarHolder holder, int position) {
        holder.mCar = mCars.get(position);
        holder.mCategory.setText(holder.mCar.vehicleInfo.category);
        holder.mType.setText(holder.mCar.vehicleInfo.type);
        String price = holder.mCar.rates.get(0).price.amount;
        holder.mPrice.setText(Utility.getFormattedPrice(this, Double.parseDouble(price)));

        mParentLayout.addView(holder.mView, mParentLayout.getChildCount() - 1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        slideActivityLeft();
    }

    public void slideActivityLeft(){
        overridePendingTransition(R.anim.come_in_from_left, R.anim.go_out_right);
    }

    public void displayImageOnToolbar(){
        Random r = new Random();
        InputStream inputStream = null;
        List<String> names = Utility.getImageNames();

        try {
            String imageName = names.get(r.nextInt(10));
            inputStream = getAssets().open(imageName);
            Bitmap b = BitmapFactory.decodeStream(inputStream);

            ImageView imageView = findViewById(R.id.detail_image);
            imageView.setImageBitmap(b);

            Palette.from(b).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(@NonNull Palette palette) {
                    int mutedColor = palette.getMutedColor(R.attr.colorPrimary);
                    mCollapsingToolbar.setContentScrimColor(mutedColor);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class CarHolder {
        private Car mCar;
        private TextView mCategory;
        private TextView mType;
        private TextView mPrice;
        private View mView;

        public CarHolder(View view) {
            mView = view;
            mCategory = (TextView) view.findViewById(R.id.category_textView);
            mType = (TextView) view.findViewById(R.id.type_textView);
            mPrice = (TextView) view.findViewById(R.id.price_textView);
        }
    }
}
