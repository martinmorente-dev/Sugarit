package com.martin_dev.sugarit.backend.viewmodels.recipe.instructions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.instructions.RecipeInstructionResponse
import com.martin_dev.sugarit.backend.utilites.retrofit.Retrofit
import com.martin_dev.sugarit.backend.utilites.traductions.TranslaterEnToSp
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import translateSuspend

class RecipeInstructionsViewModel: ViewModel()
{
    private val _recipeInstructions = MutableLiveData<RecipeInstructionResponse>()
    val recipeInstructions: LiveData<RecipeInstructionResponse> = _recipeInstructions
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchRecipeInstructions(recipeId: Int)
    {
        viewModelScope.launch {
            try
            {
                val response = Retrofit.api.getRecipeInstruction(recipeId = recipeId)
                val recipes = response.body()
                if(recipes != null)
                {
                    val translatedRecipe = translateRecipe(recipes)
                    _recipeInstructions.postValue(translatedRecipe)
                }
                else
                    _errorMessage.postValue("No Data")
            }
            catch (eHttp: HttpException)
            {
                when (eHttp.code())
                {
                    402 -> _errorMessage.postValue("Quota exceeded")
                    else -> _errorMessage.postValue("Error Call: ${eHttp.code()}")
                }
            }
            catch (eRed: IOException)
            {
                _errorMessage.postValue("Error Network: ${eRed.message}")
            }
            catch (e: Exception)
            {
                _errorMessage.postValue("Unknown Error: ${e.message}")
            }
        }
    }

    private suspend fun translateRecipe(recipe: RecipeInstructionResponse): RecipeInstructionResponse
    {
        val translater = TranslaterEnToSp()

        val translatedAnalyzedInstructions = recipe.analyzedInstructions.map { analyzedInstruction ->
            val translatedSteps = analyzedInstruction.steps.map { step ->
                step.copy(step = translater.translateSuspend(step.step))
            }
            analyzedInstruction.copy(steps = translatedSteps)
        }

        val translatedIngredients = recipe.extendedIngredients.map { ingredient ->
            ingredient.copy(nameClean = translater.translateSuspend(ingredient.nameClean))
        }

        return recipe.copy(
            analyzedInstructions = translatedAnalyzedInstructions,
            extendedIngredients = translatedIngredients
        )
    }
}