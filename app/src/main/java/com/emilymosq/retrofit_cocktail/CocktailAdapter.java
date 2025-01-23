package com.emilymosq.retrofit_cocktail;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CocktailAdapter extends RecyclerView.Adapter<CocktailAdapter.CocktailViewHolder> {

    View view;
    List<Drinks.Cocktail> mListaCockstails;
    Context context;
    ViewGroup parent;

    public CocktailAdapter (List<Drinks.Cocktail> mListaCockstails, Context context) {
        this.mListaCockstails = mListaCockstails;
        this.context = context;
    }

    @NonNull
    @Override
    public CocktailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.parent = parent;
        this.view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cocktail_cardview, parent, false);
        return new CocktailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CocktailViewHolder holder, int position) {
        holder.bind(mListaCockstails.get(position).getCocktailName(), mListaCockstails.get(position).getCocktailImageUrl());

        holder.itemView.setOnClickListener(view -> {
            View detailsCocktailView = LayoutInflater.from(context)
                    .inflate(R.layout.cocktail_detail, parent, false);

            ImageView detailsImageIV = detailsCocktailView.findViewById(R.id.imageDetailCocktail);
            TextView idTextView = detailsCocktailView.findViewById(R.id.idDetailCocktail);
            TextView nameTextView = detailsCocktailView.findViewById(R.id.nameDetailCocktail);
            TextView instructionsTextView = detailsCocktailView.findViewById(R.id.instructionsDetailCocktail);

            Glide.with(view)
                    .load(mListaCockstails.get(holder.getAdapterPosition()).getCocktailImageUrl())
                    .transition(DrawableTransitionOptions.withCrossFade(500))
                    .into(detailsImageIV);

            nameTextView.setText(mListaCockstails.get(holder.getAdapterPosition()).getCocktailName());
            idTextView.setText(mListaCockstails.get(holder.getAdapterPosition()).getCocktailId());

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<DrinkDetails> detailCall = apiInterface.getDrinkDetails(mListaCockstails.get(holder.getAdapterPosition()).getCocktailId());

            detailCall.enqueue(new Callback<DrinkDetails>() {
                @Override
                public void onResponse(Call<DrinkDetails> call, Response<DrinkDetails> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String instructions = response.body().getInstructions();
                        instructionsTextView.setText(instructions);
                    } else {
                        instructionsTextView.setText("No hay detalles.");
                    }
                }

                @Override
                public void onFailure(Call<DrinkDetails> call, Throwable t) {
                    instructionsTextView.setText("Failed to fetch details.");
                }
            });

            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(context)
                    .setView(detailsCocktailView)
                    .setNegativeButton("Close", (dialogInterface, i) -> {
                    });
            materialAlertDialogBuilder.show();
        });
    }

    @Override
    public int getItemCount() {
        return mListaCockstails.size();
    }


    public static class CocktailViewHolder extends RecyclerView.ViewHolder {
        ImageView cocktailIV;
        TextView cocktailTV;

        public CocktailViewHolder(@NonNull View itemView) {
            super(itemView);
            cocktailTV = itemView.findViewById(R.id.cocktailTextVIew);
            cocktailIV = itemView.findViewById(R.id.cocktailImageView);
        }

        public void bind(String name, String imageUrl) {
            cocktailTV.setText(name);

            Glide.with(itemView)
                    .load(imageUrl)
                    .transition(DrawableTransitionOptions.withCrossFade(500))
                    .into(cocktailIV);
        }
    }
}