import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martin_dev.sugarit.backend.model.api.Spoonacular.food.autocomplete.IngredientSuggestion
import com.martin_dev.sugarit.backend.utilites.retrofit.Retrofit
import com.martin_dev.sugarit.backend.utilites.traductions.TranslaterEnToSp
import com.martin_dev.sugarit.backend.utilites.traductions.TranslaterSpToEn
import kotlinx.coroutines.launch

class IngredientAutoCompleteViewModel: ViewModel() {

    private val _suggestions = MutableLiveData<List<IngredientSuggestion>>()
    val suggestions: LiveData<List<IngredientSuggestion>> = _suggestions

    fun fetchSuggestions(query: String) {
        TranslaterSpToEn().translate(query) { translatedQuery ->
            if (translatedQuery.isNullOrBlank()) {
                _suggestions.postValue(emptyList())
                return@translate
            }
            viewModelScope.launch {
                try {
                    val response = Retrofit.api.autocompleteIngredient(translatedQuery)
                    if (response.isSuccessful) {
                        val englishSuggestions = response.body() ?: emptyList()
                        val translatedSuggestions = mutableListOf<IngredientSuggestion>()
                        val translater = TranslaterEnToSp()
                        var translatedCount = 0

                        if (englishSuggestions.isEmpty()) {
                            _suggestions.postValue(emptyList())
                            return@launch
                        }

                        englishSuggestions.forEach { suggestion ->
                            translater.translate(suggestion.name) { translatedName ->
                                translatedSuggestions.add(
                                    suggestion.copy(name = translatedName ?: suggestion.name)
                                )
                                translatedCount++
                                if (translatedCount == englishSuggestions.size) {
                                    _suggestions.postValue(translatedSuggestions)
                                }
                            }
                        }
                    } else {
                        _suggestions.postValue(emptyList())
                    }
                } catch (e: Exception) {
                    _suggestions.postValue(emptyList())
                }
            }
        }
    }
}
