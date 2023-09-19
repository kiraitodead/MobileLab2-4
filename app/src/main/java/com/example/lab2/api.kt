import com.example.lab2.DataItem
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class CategoryResponse(val categories: List<Category>)

data class Category(val strCategory: String, val strCategoryThumb: String)

interface MealDbApi {
    @GET("list.php?c=list")
    fun getCategories(): Call<CategoryResponse>
}

data class MealCategory(val name: String, val imageUrl: String)

fun getMealCategories(): List<MealCategory> {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://www.themealdb.com/api/json/v1/1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(MealDbApi::class.java)

    val call = api.getCategories()
    val response = call.execute()

    if (response.isSuccessful) {
        val categoryResponse = response.body()
        val categories = categoryResponse?.categories ?: emptyList()

        return categories.map { MealCategory(it.strCategory, it.strCategoryThumb) }
    } else {
        println("Ошибка при получении категорий: ${response.message()}")
        return emptyList()
    }
}