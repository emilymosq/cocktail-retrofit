package com.emilymosq.retrofit_cocktail;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private CocktailAdapter adapter;
    private RecyclerView cocktailRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText ingredientEditText = findViewById(R.id.ingredientEditText);
        Button searchButton = findViewById(R.id.searchButton);
        cocktailRecyclerView = findViewById(R.id.cocktailRecyclerView);

        cocktailRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        searchButton.setOnClickListener(view -> {
            String ingredient = ingredientEditText.getText().toString().trim();
            if (!ingredient.isEmpty()) {
                fetchCocktailsByIngredient(ingredient);
            } else {
                Toast.makeText(this, "Please enter an ingredient", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchCocktailsByIngredient(String ingredient) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Drinks> call = apiInterface.getDrinksByLicour(ingredient);
        call.enqueue(new Callback<Drinks>() {
            @Override
            public void onResponse(Call<Drinks> call, Response<Drinks> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Drinks.Cocktail> cocktails = response.body().getDrinks();
                    adapter = new CocktailAdapter(cocktails, MainActivity.this);
                    cocktailRecyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(MainActivity.this, "No cocktails found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Drinks> call, Throwable t) {
                Log.e("Error", t.getMessage());
                Toast.makeText(MainActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}