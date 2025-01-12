package com.MwandoJrTechnologies.the_smart_parent.BabyProducts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.MwandoJrTechnologies.the_smart_parent.NewsFeed.MainActivity;
import com.MwandoJrTechnologies.the_smart_parent.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ViewProductsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference productsReference;
    String currentUserID;
    private String productCategory;

    private RecyclerView allProductsRecyclerView;


    private TextView viewCategoryTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_products);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);  //for the back button
        getSupportActionBar().setTitle("View Product rating");


        allProductsRecyclerView = findViewById(R.id.all_baby_products_layout);

        viewCategoryTv = findViewById(R.id.tv_view_product_cat);


        /**
         * Receiving data inside onCreate() method of Second Activity
         *
         * Get extra method
         */

        productCategory = getIntent().getExtras().get("product_category").toString();
        viewCategoryTv.setText(productCategory);

        productsReference = FirebaseDatabase.getInstance().getReference().child("Products");
        Query categoryQuery = productsReference.orderByChild("category").equalTo(productCategory);

        DisplayAllProductsLayouts();
    }

    private void DisplayAllProductsLayouts() {

        LinearLayoutManager linearLayoutManager = new
                LinearLayoutManager(ViewProductsActivity.this);

        allProductsRecyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        final FirebaseRecyclerOptions<Products> options = new
                FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productsReference, Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductsViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Products, ProductsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductsViewHolder productsViewHolder,
                                                    int position, @NonNull Products products) {

                        //gets the products unique key
                        final String productKey = products.getProductKey();

                        Picasso.get()
                                .load(products.getProductImage())
                                .placeholder(R.mipmap.project_logo)
                                .into(productsViewHolder.productRatingViewImage);
                        productsViewHolder
                                .productRatingName
                                .setText(products.getProductName());
                        productsViewHolder
                                .productRatingManufacturer
                                .setText(products.getProductManufactureCompany());
                        productsViewHolder
                                .productRatingDescription
                                .setText(products.getProductDescription());
                        productsViewHolder
                                .productRatedRatingBar
                                .setRating(products.getProductRating());


                        //working on the click event to view more details on the products
                        productsViewHolder.productsMoreDetailsAndComments.setOnClickListener(v -> {

                            Intent individualProductIntent = new
                                    Intent(ViewProductsActivity.this,
                                    IndividualProductActivity.class);
                            individualProductIntent
                                    .putExtra
                                            ("product_key", productKey);
                            individualProductIntent
                                    .putExtra("product_category", productCategory);
                            finish();
                            startActivity(individualProductIntent);

                        });
                    }

                    @NonNull
                    @Override
                    public ProductsViewHolder onCreateViewHolder
                            (@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater
                                .from(parent.getContext())
                                .inflate(R.layout.all_baby_products_display_layout,
                                        parent,
                                        false);
                        ProductsViewHolder viewHolder = new ProductsViewHolder(view);

                        return viewHolder;
                    }
                };
        allProductsRecyclerView.setLayoutManager(linearLayoutManager);
        allProductsRecyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();
        firebaseRecyclerAdapter.startListening();
    }


    private static class ProductsViewHolder extends RecyclerView.ViewHolder {

        private ImageView productRatingViewImage;
        private TextView productRatingName;
        private TextView productRatingManufacturer;
        private TextView productRatingDescription;
        private RatingBar productRatedRatingBar;
        private TextView productsMoreDetailsAndComments;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);

            productRatingViewImage = itemView.findViewById(R.id.view_product_rating_image);
            productRatingName = itemView.findViewById(R.id.view_product_rating_product_name);
            productRatingManufacturer = itemView.findViewById(R.id.view_product_rating_manufacturer);
            productRatingDescription = itemView.findViewById(R.id.view_product_rating_description);
            productRatedRatingBar = itemView.findViewById(R.id.view_product_rating_bar);
            productsMoreDetailsAndComments = itemView.findViewById(R.id.view_product_more_details);
        }
    }

    //activate back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            SendUserToMainActivity();
        }
        return super.onOptionsItemSelected(item);
    }


    //open main activity
    private void SendUserToMainActivity() {
        Intent mainActivityIntent = new
                Intent(ViewProductsActivity.this, MainActivity.class);
        finish();
        startActivity(mainActivityIntent);
    }

}