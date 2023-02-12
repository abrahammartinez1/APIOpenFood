import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class Main {
    private static final String API_BASE_URL = "https://world.openfoodfacts.org/api/v0/";
    private static final String API_KEY = "your_api_key_here";

    public static void main(String[] args) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenFoodFactsService service = retrofit.create(OpenFoodFactsService.class);

        String barcode = "7501055320622";

        Call<Product> call = service.getProductByBarcode(barcode, API_KEY);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    Product product = response.body();
                    System.out.println("Product name: " + product.getProductName());
                    System.out.println("Ingredients: " + product.getIngredients());
                    System.out.println("Nutrition score: " + product.getNutritionScore());
                    // Show the product information in a JavaFX application
                } else {
                    System.out.println("Request failed with code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                System.out.println("Request failed with error: " + t.getMessage());
            }
        });
    }

    interface OpenFoodFactsService {
        @GET("product/{barcode}.json")
        Call<Product> getProductByBarcode(@Path("barcode") String barcode, @Query("token") String token);
    }

    static class Product {
        private String productName;
        private String ingredients;
        private int nutritionScore;

        public String getProductName() {
            return productName;
        }

        public String getIngredients() {
            return ingredients;
        }

        public int getNutritionScore() {
            return nutritionScore;
        }
    }
}

