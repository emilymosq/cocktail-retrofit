package com.emilymosq.retrofit_cocktail;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DrinkDetails {
    @SerializedName("drinks")
    private List<DrinkDetail> drinkDetails;

    public static class DrinkDetail {
        @SerializedName("strInstructions")
        private String instructions;

        public String getInstructions() {
            return instructions;
        }
    }

    public String getInstructions() {
        if (drinkDetails != null && !drinkDetails.isEmpty()) {
            return drinkDetails.get(0).getInstructions();
        }
        return "No instructions available.";
    }
}